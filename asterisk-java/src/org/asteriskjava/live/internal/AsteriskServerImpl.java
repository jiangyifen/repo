/*
 *  Copyright 2004-2006 Stefan Reuter
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.asteriskjava.live.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.AsteriskQueue;
import org.asteriskjava.live.AsteriskServer;
import org.asteriskjava.live.AsteriskServerListener;
import org.asteriskjava.live.CallerId;
import org.asteriskjava.live.ChannelState;
import org.asteriskjava.live.LiveException;
import org.asteriskjava.live.ManagerCommunicationException;
import org.asteriskjava.live.MeetMeRoom;
import org.asteriskjava.live.MeetMeUser;
import org.asteriskjava.live.NoSuchChannelException;
import org.asteriskjava.live.OriginateCallback;
import org.asteriskjava.live.Voicemailbox;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionState;
import org.asteriskjava.manager.ManagerEventListener;
import org.asteriskjava.manager.ManagerEventListenerProxy;
import org.asteriskjava.manager.ResponseEvents;
import org.asteriskjava.manager.action.CommandAction;
import org.asteriskjava.manager.action.EventGeneratingAction;
import org.asteriskjava.manager.action.GetVarAction;
import org.asteriskjava.manager.action.MailboxCountAction;
import org.asteriskjava.manager.action.ManagerAction;
import org.asteriskjava.manager.action.OriginateAction;
import org.asteriskjava.manager.action.SetVarAction;
import org.asteriskjava.manager.event.AbstractMeetMeEvent;
import org.asteriskjava.manager.event.OriginateResponseEvent;
import org.asteriskjava.manager.event.CdrEvent;
import org.asteriskjava.manager.event.ConnectEvent;
import org.asteriskjava.manager.event.DialEvent;
import org.asteriskjava.manager.event.DisconnectEvent;
import org.asteriskjava.manager.event.HangupEvent;
import org.asteriskjava.manager.event.JoinEvent;
import org.asteriskjava.manager.event.LeaveEvent;
import org.asteriskjava.manager.event.LinkEvent;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.NewCallerIdEvent;
import org.asteriskjava.manager.event.NewChannelEvent;
import org.asteriskjava.manager.event.NewExtenEvent;
import org.asteriskjava.manager.event.NewStateEvent;
import org.asteriskjava.manager.event.RenameEvent;
import org.asteriskjava.manager.event.ResponseEvent;
import org.asteriskjava.manager.event.UnlinkEvent;
import org.asteriskjava.manager.response.CommandResponse;
import org.asteriskjava.manager.response.MailboxCountResponse;
import org.asteriskjava.manager.response.ManagerError;
import org.asteriskjava.manager.response.ManagerResponse;
import org.asteriskjava.util.DateUtil;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

/**
 * Default implementation of the {@link AsteriskServer} interface.
 * 
 * @author srt
 * @version $Id: AsteriskServerImpl.java,v 1.2 2011/05/24 03:48:03 bruce Exp $
 */
public class AsteriskServerImpl implements AsteriskServer, ManagerEventListener
{
    private static final String ACTION_ID_PREFIX_ORIGINATE = "AJ_ORIGINATE_";
    private static final String SHOW_VERSION_COMMAND = "show version";
    private static final String SHOW_VERSION_FILES_COMMAND = "show version files";
    private static final Pattern SHOW_VERSION_FILES_PATTERN = Pattern.compile("^([\\S]+)\\s+Revision: ([0-9\\.]+)");
    private static final String SHOW_VOICEMAIL_USERS_COMMAND = "show voicemail users";
    private static final Pattern SHOW_VOICEMAIL_USERS_PATTERN = Pattern.compile("^(\\S+)\\s+(\\S+)\\s+(.{25})");

    private final Log logger = LogFactory.getLog(this.getClass());

    /**
     * The underlying manager connection used to receive events from Asterisk.
     */
    private ManagerConnection eventConnection;
    private ManagerEventListener eventListener = null;
    private ManagerEventListenerProxy managerEventListenerProxy = null;

    private boolean initialized = false;

    /**
     * A pool of manager connections to use for sending actions to Asterisk.
     */
    private final ManagerConnectionPool connectionPool;

    private final List<AsteriskServerListener> listeners;

    private final ChannelManager channelManager;
    private final MeetMeManager meetMeManager;
    private final QueueManager queueManager;

    /**
     * The exact version string of the Asterisk server we are connected to.
     * <p>
     * Contains <code>null</code> until lazily initialized.
     */
    private String version;

    /**
     * Holds the version of Asterisk's source files.
     * <p>
     * That corresponds to the output of the CLI command
     * <code>show version files</code>.
     * <p>
     * Contains <code>null</code> until lazily initialized.
     */
    private Map<String, String> versions;

    /**
     * Maps the traceId to the corresponding callback data.
     */
    private final Map<String, OriginateCallbackData> originateCallbacks;

    private final AtomicLong idCounter;

    /* config options */

    /**
     * Flag to skip initializing queues as that results in a timeout on Asterisk
     * 1.0.x.
     */
    private boolean skipQueues;

    /**
     * Set to <code>true</code> to not handle ManagerEvents in the reader
     * tread but process them asynchronously. This is a good idea :)
     */
    private boolean asyncEventHandling = true;

    /**
     * Creates a new instance.
     */
    public AsteriskServerImpl()
    {
        connectionPool = new ManagerConnectionPool(1);
        idCounter = new AtomicLong();
        listeners = new ArrayList<AsteriskServerListener>();
        originateCallbacks = new HashMap<String, OriginateCallbackData>();
        channelManager = new ChannelManager(this);
        meetMeManager = new MeetMeManager(this, channelManager);
        queueManager = new QueueManager(this, channelManager);
    }

    /**
     * Creates a new instance.
     * 
     * @param eventConnection the ManagerConnection to use for receiving events
     *            from Asterisk.
     */
    public AsteriskServerImpl(ManagerConnection eventConnection)
    {
        this();
        setManagerConnection(eventConnection);
    }

    /**
     * Determines if queue status is retrieved at startup. If you don't need
     * queue information and still run Asterisk 1.0.x you can set this to
     * <code>true</code> to circumvent the startup delay caused by the missing
     * QueueStatusComplete event.
     * <p>
     * Default is <code>false</code>.
     * 
     * @param skipQueues <code>true</code> to skip queue initialization,
     *            <code>false</code> to not skip.
     * @since 0.2
     */
    public void setSkipQueues(boolean skipQueues)
    {
        this.skipQueues = skipQueues;
    }

    public void setManagerConnection(ManagerConnection eventConnection)
    {
        if (this.eventConnection != null)
        {
            throw new IllegalStateException("ManagerConnection already set.");
        }

        this.eventConnection = eventConnection;
        this.connectionPool.clear();
        this.connectionPool.add(eventConnection);
    }

    public ManagerConnection getManagerConnection()
    {
        return eventConnection;
    }

    public void initialize() throws ManagerCommunicationException
    {
        initializeIfNeeded();
    }

    private synchronized void initializeIfNeeded() throws ManagerCommunicationException
    {
        if (initialized)
        {
            return;
        }

        if (eventConnection.getState() == ManagerConnectionState.INITIAL
                || eventConnection.getState() == ManagerConnectionState.DISCONNECTED)
        {
            try
            {
                eventConnection.login();
            }
            catch (Exception e)
            {
                throw new ManagerCommunicationException("Unable to login", e);
            }
        }

        channelManager.initialize();
        meetMeManager.initialize();
        if (!skipQueues)
        {
            queueManager.initialize();
        }

        if (asyncEventHandling && managerEventListenerProxy == null)
        {
            managerEventListenerProxy = new ManagerEventListenerProxy(this);
            eventConnection.addEventListener(managerEventListenerProxy);
        } 
        else if (!asyncEventHandling && eventListener == null)
        {
            eventListener = this;
            eventConnection.addEventListener(eventListener);
        }
        logger.info("Initializing done");
        initialized = true;
    }

    /* Implementation of the AsteriskServer interface */

    public AsteriskChannel originateToExtension(String channel, String context, String exten, int priority, long timeout)
            throws ManagerCommunicationException, NoSuchChannelException
    {
        return originateToExtension(channel, context, exten, priority, timeout, null, null);
    }

    public AsteriskChannel originateToExtension(String channel, String context, String exten, int priority, long timeout,
            CallerId callerId, Map<String, String> variables) throws ManagerCommunicationException, NoSuchChannelException
    {
        final OriginateAction originateAction;

        originateAction = new OriginateAction();
        originateAction.setChannel(channel);
        originateAction.setContext(context);
        originateAction.setExten(exten);
        originateAction.setPriority(priority);
        originateAction.setTimeout(timeout);
        if (callerId != null)
        {
            originateAction.setCallerId(callerId.toString());
        }
        originateAction.setVariables(variables);

        return originate(originateAction);
    }

    public AsteriskChannel originateToApplication(String channel, String application, String data, long timeout)
            throws ManagerCommunicationException, NoSuchChannelException
    {
        return originateToApplication(channel, application, data, timeout, null, null);
    }

    public AsteriskChannel originateToApplication(String channel, String application, String data, long timeout,
            CallerId callerId, Map<String, String> variables) throws ManagerCommunicationException, NoSuchChannelException
    {
        final OriginateAction originateAction;

        originateAction = new OriginateAction();
        originateAction.setChannel(channel);
        originateAction.setApplication(application);
        originateAction.setData(data);
        originateAction.setTimeout(timeout);
        if (callerId != null)
        {
            originateAction.setCallerId(callerId.toString());
        }
        originateAction.setVariables(variables);

        return originate(originateAction);
    }

    private AsteriskChannel originate(OriginateAction originateAction) throws ManagerCommunicationException,
            NoSuchChannelException
    {
        final ResponseEvents responseEvents;
        final Iterator<ResponseEvent> responseEventIterator;
        String uniqueId = null;
        AsteriskChannel channel = null;

        // must set async to true to receive OriginateEvents.
        originateAction.setAsync(Boolean.TRUE);

        initializeIfNeeded();

        // 2000 ms extra for the OriginateFailureEvent should be fine
        responseEvents = sendEventGeneratingAction(originateAction, originateAction.getTimeout() + 2000);

        responseEventIterator = responseEvents.getEvents().iterator();
        if (responseEventIterator.hasNext())
        {
            ResponseEvent responseEvent;

            responseEvent = responseEventIterator.next();
            if (responseEvent instanceof OriginateResponseEvent)
            {
                uniqueId = ((OriginateResponseEvent) responseEvent).getUniqueId();
                logger.debug(responseEvent.getClass().getName() + " received with uniqueId " + uniqueId);
                channel = getChannelById(uniqueId);
            }
        }

        if (channel == null)
        {
            throw new NoSuchChannelException("Channel '" + originateAction.getChannel() + "' is not available");
        }

        return channel;
    }

    public void originateToExtensionAsync(String channel, String context, String exten, int priority, long timeout,
            OriginateCallback cb) throws ManagerCommunicationException
    {
        originateToExtensionAsync(channel, context, exten, priority, timeout, null, null, cb);
    }

    public void originateToExtensionAsync(String channel, String context, String exten, int priority, long timeout,
            CallerId callerId, Map<String, String> variables, OriginateCallback cb) throws ManagerCommunicationException
    {
        final OriginateAction originateAction;

        originateAction = new OriginateAction();
        originateAction.setChannel(channel);
        originateAction.setContext(context);
        originateAction.setExten(exten);
        originateAction.setPriority(priority);
        originateAction.setTimeout(timeout);
        if (callerId != null)
        {
            originateAction.setCallerId(callerId.toString());
        }
        originateAction.setVariables(variables);

        originateAsync(originateAction, cb);
    }

    public void originateToApplicationAsync(String channel, String application, String data, long timeout,
            OriginateCallback cb) throws ManagerCommunicationException
    {
        originateToApplicationAsync(channel, application, data, timeout, null, null, cb);
    }

    public void originateToApplicationAsync(String channel, String application, String data, long timeout,
            CallerId callerId, Map<String, String> variables, OriginateCallback cb) throws ManagerCommunicationException
    {
        final OriginateAction originateAction;

        originateAction = new OriginateAction();
        originateAction.setChannel(channel);
        originateAction.setApplication(application);
        originateAction.setData(data);
        originateAction.setTimeout(timeout);
        if (callerId != null)
        {
            originateAction.setCallerId(callerId.toString());
        }
        originateAction.setVariables(variables);

        originateAsync(originateAction, cb);
    }

    private void originateAsync(OriginateAction originateAction, OriginateCallback cb) throws ManagerCommunicationException
    {
        final Map<String, String> variables;
        final String traceId;

        traceId = ACTION_ID_PREFIX_ORIGINATE + idCounter.getAndIncrement();
        if (originateAction.getVariables() == null)
        {
            variables = new HashMap<String, String>();
        }
        else
        {
            variables = new HashMap<String, String>(originateAction.getVariables());
        }
        // prefix variable name by "__" to enable variable inheritence across
        // channels
        variables.put("__" + Constants.VARIABLE_TRACE_ID, traceId);
        originateAction.setVariables(variables);

        // must set async to true to receive OriginateEvents.
        originateAction.setAsync(Boolean.TRUE);
        originateAction.setActionId(traceId);

        if (cb != null)
        {
            OriginateCallbackData callbackData;

            callbackData = new OriginateCallbackData(originateAction, DateUtil.getDate(), cb);
            // register callback
            synchronized (originateCallbacks)
            {
                originateCallbacks.put(traceId, callbackData);
            }
        }

        initializeIfNeeded();
        sendActionOnEventConnection(originateAction);
    }

    public Collection<AsteriskChannel> getChannels() throws ManagerCommunicationException
    {
        initializeIfNeeded();
        return channelManager.getChannels();
    }

    public AsteriskChannel getChannelByName(String name) throws ManagerCommunicationException
    {
        initializeIfNeeded();
        return channelManager.getChannelImplByName(name);
    }

    public AsteriskChannel getChannelById(String id) throws ManagerCommunicationException
    {
        initializeIfNeeded();
        return channelManager.getChannelImplById(id);
    }

    public Collection<MeetMeRoom> getMeetMeRooms() throws ManagerCommunicationException
    {
        initializeIfNeeded();
        return meetMeManager.getMeetMeRooms();
    }

    public MeetMeRoom getMeetMeRoom(String name) throws ManagerCommunicationException
    {
        initializeIfNeeded();
        return meetMeManager.getOrCreateRoomImpl(name);
    }

    public Collection<AsteriskQueue> getQueues() throws ManagerCommunicationException
    {
        initializeIfNeeded();
        return queueManager.getQueues();
    }

    @SuppressWarnings("rawtypes")
	public synchronized String getVersion() throws ManagerCommunicationException
    {
        final ManagerResponse response;

        initializeIfNeeded();
        if (version == null)
        {
            response = sendAction(new CommandAction(SHOW_VERSION_COMMAND));
            if (response instanceof CommandResponse)
            {
                final List result;

                result = ((CommandResponse) response).getResult();
                if (result.size() > 0)
                {
                    version = (String) result.get(0);
                }
            }
            else
            {
                logger.error("Response to CommandAction(\"" + SHOW_VERSION_COMMAND + "\") was not a CommandResponse but "
                        + response);
            }
        }

        return version;
    }

    public int[] getVersion(String file) throws ManagerCommunicationException
    {
        String fileVersion = null;
        String[] parts;
        int[] intParts;

        initializeIfNeeded();
        if (versions == null)
        {
            Map<String, String> map;
            ManagerResponse response;

            map = new HashMap<String, String>();
            try
            {
                response = sendAction(new CommandAction(SHOW_VERSION_FILES_COMMAND));
                if (response instanceof CommandResponse)
                {
                    List<String> result;

                    result = ((CommandResponse) response).getResult();
                    for (int i = 2; i < result.size(); i++)
                    {
                        String line;
                        Matcher matcher;

                        line = (String) result.get(i);
                        matcher = SHOW_VERSION_FILES_PATTERN.matcher(line);
                        if (matcher.find())
                        {
                            String key = matcher.group(1);
                            String value = matcher.group(2);

                            map.put(key, value);
                        }
                    }

                    fileVersion = (String) map.get(file);
                    versions = map;
                }
                else
                {
                    logger.error("Response to CommandAction(\"" + SHOW_VERSION_FILES_COMMAND
                            + "\") was not a CommandResponse but " + response);
                }
            }
            catch (Exception e)
            {
                logger.warn("Unable to send '" + SHOW_VERSION_FILES_COMMAND + "' command.", e);
            }
        }
        else
        {
            synchronized (versions)
            {
                fileVersion = versions.get(file);
            }
        }

        if (fileVersion == null)
        {
            return null;
        }

        parts = fileVersion.split("\\.");
        intParts = new int[parts.length];

        for (int i = 0; i < parts.length; i++)
        {
            try
            {
                intParts[i] = Integer.parseInt(parts[i]);
            }
            catch (NumberFormatException e)
            {
                intParts[i] = 0;
            }
        }

        return intParts;
    }

    public String getGlobalVariable(String variable) throws ManagerCommunicationException
    {
        ManagerResponse response;
        String value;

        initializeIfNeeded();
        response = sendAction(new GetVarAction(variable));
        if (response instanceof ManagerError)
        {
            return null;
        }
        value = response.getAttribute("Value");
        if (value == null)
        {
            value = response.getAttribute(variable); // for Asterisk 1.0.x
        }
        return value;
    }

    public void setGlobalVariable(String variable, String value) throws ManagerCommunicationException
    {
        ManagerResponse response;

        initializeIfNeeded();
        response = sendAction(new SetVarAction(variable, value));
        if (response instanceof ManagerError)
        {
            logger.error("Unable to set global variable '" + variable + "' to '" + value + "':" + response.getMessage());
        }
    }

    public Collection<Voicemailbox> getVoicemailboxes() throws ManagerCommunicationException
    {
        final Collection<Voicemailbox> voicemailboxes;
        ManagerResponse response;
        final List<String> result;

        initializeIfNeeded();
        voicemailboxes = new ArrayList<Voicemailbox>();
        response = sendAction(new CommandAction(SHOW_VOICEMAIL_USERS_COMMAND));
        if (!(response instanceof CommandResponse))
        {
            logger.error("Response to CommandAction(\"" + SHOW_VOICEMAIL_USERS_COMMAND
                    + "\") was not a CommandResponse but " + response);
            return voicemailboxes;
        }

        result = ((CommandResponse) response).getResult();
        if (result == null || result.size() < 1)
        {
            return voicemailboxes;
        }

        // remove headline
        result.remove(0);

        for (String line : result)
        {
            final Matcher matcher;
            final Voicemailbox voicemailbox;
            final String context;
            final String mailbox;
            final String user;

            matcher = SHOW_VOICEMAIL_USERS_PATTERN.matcher(line);
            if (!matcher.find())
            {
                continue;
            }

            context = matcher.group(1);
            mailbox = matcher.group(2);
            user = matcher.group(3).trim();

            voicemailbox = new Voicemailbox(mailbox, context, user);
            voicemailboxes.add(voicemailbox);
        }

        // get message count for each mailbox
        for (Voicemailbox voicemailbox : voicemailboxes)
        {
            final String fullname;

            fullname = voicemailbox.getMailbox() + "@" + voicemailbox.getContext();
            response = sendAction(new MailboxCountAction(fullname));
            if (response instanceof MailboxCountResponse)
            {
                MailboxCountResponse mailboxCountResponse;

                mailboxCountResponse = (MailboxCountResponse) response;
                voicemailbox.setNewMessages(mailboxCountResponse.getNewMessages());
                voicemailbox.setOldMessages(mailboxCountResponse.getOldMessages());
            }
            else
            {
                logger.error("Response to MailboxCountAction was not a MailboxCountResponse but " + response);
            }
        }

        return voicemailboxes;
    }

    public List<String> executeCliCommand(String command) throws ManagerCommunicationException
    {
        final ManagerResponse response;

        initializeIfNeeded();
        response = sendAction(new CommandAction(command));
        if (!(response instanceof CommandResponse))
        {
            throw new ManagerCommunicationException("Response to CommandAction(\"" + command
                    + "\") was not a CommandResponse but " + response, null);
        }

        return ((CommandResponse) response).getResult();
    }

    public void addAsteriskServerListener(AsteriskServerListener listener) throws ManagerCommunicationException
    {
        initializeIfNeeded();
        synchronized (listeners)
        {
            listeners.add(listener);
        }
    }

    public void removeAsteriskServerListener(AsteriskServerListener listener)
    {
        synchronized (listeners)
        {
            listeners.remove(listener);
        }
    }

    void fireNewAsteriskChannel(AsteriskChannel channel)
    {
        synchronized (listeners)
        {
            for (AsteriskServerListener listener : listeners)
            {
                try
                {
                    listener.onNewAsteriskChannel(channel);
                }
                catch (Exception e)
                {
                    logger.warn("Exception in onNewAsteriskChannel()", e);
                }
            }
        }
    }

    void fireNewMeetMeUser(MeetMeUser user)
    {
        synchronized (listeners)
        {
            for (AsteriskServerListener listener : listeners)
            {
                try
                {
                    listener.onNewMeetMeUser(user);
                }
                catch (Exception e)
                {
                    logger.warn("Exception in onNewMeetMeUser()", e);
                }
            }
        }
    }

    ManagerResponse sendActionOnEventConnection(ManagerAction action) throws ManagerCommunicationException
    {
        try
        {
            return eventConnection.sendAction(action);
        }
        catch (Exception e)
        {
            throw ManagerCommunicationExceptionMapper.mapSendActionException(action.getAction(), e);
        }
    }

    ManagerResponse sendAction(ManagerAction action) throws ManagerCommunicationException
    {
        // return connectionPool.sendAction(action);
        try
        {
            return eventConnection.sendAction(action);
        }
        catch (Exception e)
        {
            throw ManagerCommunicationExceptionMapper.mapSendActionException(action.getAction(), e);
        }
    }

    ResponseEvents sendEventGeneratingAction(EventGeneratingAction action) throws ManagerCommunicationException
    {
        // return connectionPool.sendEventGeneratingAction(action);
        try
        {
            return eventConnection.sendEventGeneratingAction(action);
        }
        catch (Exception e)
        {
            throw ManagerCommunicationExceptionMapper.mapSendActionException(action.getAction(), e);
        }
    }

    ResponseEvents sendEventGeneratingAction(EventGeneratingAction action, long timeout)
            throws ManagerCommunicationException
    {
        // return connectionPool.sendEventGeneratingAction(action, timeout);
        try
        {
            return eventConnection.sendEventGeneratingAction(action, timeout);
        }
        catch (Exception e)
        {
            throw ManagerCommunicationExceptionMapper.mapSendActionException(action.getAction(), e);
        }
    }

    OriginateCallbackData getOriginateCallbackDataByTraceId(String traceId)
    {
        synchronized (originateCallbacks)
        {
            return originateCallbacks.get(traceId);
        }
    }

    /* Implementation of the ManagerEventListener interface */

    /**
     * Handles all events received from the Asterisk server.
     * <p>
     * Events are queued until channels and queues are initialized and then
     * delegated to the dispatchEvent method.
     */
    public void onManagerEvent(ManagerEvent event)
    {
        if (event instanceof ConnectEvent)
        {
            handleConnectEvent((ConnectEvent) event);
        }
        else if (event instanceof DisconnectEvent)
        {
            handleDisconnectEvent((DisconnectEvent) event);
        }
        else if (event instanceof NewChannelEvent)
        {
            channelManager.handleNewChannelEvent((NewChannelEvent) event);
        }
        else if (event instanceof NewExtenEvent)
        {
            channelManager.handleNewExtenEvent((NewExtenEvent) event);
        }
        else if (event instanceof NewStateEvent)
        {
            channelManager.handleNewStateEvent((NewStateEvent) event);
        }
        else if (event instanceof NewCallerIdEvent)
        {
            channelManager.handleNewCallerIdEvent((NewCallerIdEvent) event);
        }
        else if (event instanceof DialEvent)
        {
            channelManager.handleDialEvent((DialEvent) event);
        }
        else if (event instanceof LinkEvent)
        {
            channelManager.handleLinkEvent((LinkEvent) event);
        }
        else if (event instanceof UnlinkEvent)
        {
            channelManager.handleUnlinkEvent((UnlinkEvent) event);
        }
        else if (event instanceof RenameEvent)
        {
            channelManager.handleRenameEvent((RenameEvent) event);
        }
        else if (event instanceof HangupEvent)
        {
            channelManager.handleHangupEvent((HangupEvent) event);
        }
        else if (event instanceof CdrEvent)
        {
            channelManager.handleCdrEvent((CdrEvent) event);
        }
        else if (event instanceof JoinEvent)
        {
            queueManager.handleJoinEvent((JoinEvent) event);
        }
        else if (event instanceof LeaveEvent)
        {
            queueManager.handleLeaveEvent((LeaveEvent) event);
        }
        else if (event instanceof AbstractMeetMeEvent)
        {
            meetMeManager.handleMeetMeEvent((AbstractMeetMeEvent) event);
        }
        else if (event instanceof OriginateResponseEvent)
        {
            handleOriginateEvent((OriginateResponseEvent) event);
        }
    }

    /*
     * Resets the internal state when the connection to the asterisk server is
     * lost.
     */
    private void handleDisconnectEvent(DisconnectEvent disconnectEvent)
    {
        // reset version information as it might have changed while Asterisk
        // restarted
        version = null;
        versions = null;

        // same for channels and queues, they are reinitialized when reconnected
        channelManager.disconnected();
        meetMeManager.disconnected();
        queueManager.disconnected();
        initialized = false;
    }

    /*
     * Requests the current state from the asterisk server after the connection
     * to the asterisk server is restored.
     */
    private void handleConnectEvent(ConnectEvent connectEvent)
    {
        try
        {
            initialize();
        }
        catch (Exception e)
        {
            logger.error("Unable to reinitialize state after reconnection", e);
        }
    }

    private void handleOriginateEvent(OriginateResponseEvent originateEvent)
    {
        final String traceId;
        final OriginateCallbackData callbackData;
        final OriginateCallback cb;
        final AsteriskChannelImpl channel;
        final AsteriskChannelImpl otherChannel; // the other side if local
        // channel

        traceId = originateEvent.getActionId();
        if (traceId == null)
        {
            return;
        }

        synchronized (originateCallbacks)
        {
            callbackData = originateCallbacks.get(traceId);
            if (callbackData == null)
            {
                return;
            }
            originateCallbacks.remove(traceId);
        }

        cb = callbackData.getCallback();
        channel = channelManager.getChannelImplById(originateEvent.getUniqueId());

        try
        {
            if (channel == null)
            {
                LiveException cause;

                cause = new NoSuchChannelException("Channel '" + callbackData.getOriginateAction().getChannel()
                        + "' is not available");
                cb.onFailure(cause);
                return;
            }

            if (channel.wasInState(ChannelState.UP))
            {
                cb.onSuccess(channel);
                return;
            }

            if (channel.wasBusy())
            {
                cb.onBusy(channel);
                return;
            }

            otherChannel = channelManager.getOtherSideOfLocalChannel(channel);
            // special treatment of local channels:
            // the interesting things happen to the other side so we have a look
            // at that
            if (otherChannel != null)
            {
                final AsteriskChannel dialedChannel;

                dialedChannel = otherChannel.getDialedChannel();

                // on busy the other channel is in state busy when we receive
                // the originate event
                if (otherChannel.wasBusy())
                {
                    cb.onBusy(channel);
                    return;
                }

                // alternative:
                // on busy the dialed channel is hung up when we receive the
                // originate event having a look at the hangup cause reveals the
                // information we are interested in
                // this alternative has the drawback that there might by
                // multiple channels that have been dialed by the local channel
                // but we only look at the last one.
                if (dialedChannel != null && dialedChannel.wasBusy())
                {
                    cb.onBusy(channel);
                    return;
                }
            }

            // if nothing else matched we asume no answer
            cb.onNoAnswer(channel);
        }
        catch (Throwable t)
        {
            logger.warn("Exception dispatching originate progress", t);
        }
    }

    public void shutdown() {
        
        if (eventConnection != null && eventConnection.getState() == ManagerConnectionState.CONNECTED) 
        {
            eventConnection.logoff();
            eventConnection = null;
        }
        if (managerEventListenerProxy != null) 
        {
            managerEventListenerProxy.shutdown();
        }
        managerEventListenerProxy = null;
        eventListener = null;
    }
}
