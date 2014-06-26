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
package org.asteriskjava.manager;

import java.util.concurrent.atomic.AtomicLong;

import org.asteriskjava.manager.action.PingAction;
import org.asteriskjava.manager.response.ManagerResponse;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

/**
 * A Thread that pings the Asterisk server at a given interval.
 * <p>
 * You can use this to prevent the connection being shut down when there is no
 * traffic.
 * 
 * @author srt
 * @version $Id: PingThread.java,v 1.1 2010/12/10 07:00:37 bruce Exp $
 */
public class PingThread extends Thread
{
    /**
     * Default value for the interval attribute.
     */
    private static final long DEFAULT_INTERVAL = 20 * 1000L;
    private static final AtomicLong idCounter = new AtomicLong(0);

    /**
     * Instance logger.
     */
    private final Log logger = LogFactory.getLog(getClass());

    private final long id;
    private long interval = DEFAULT_INTERVAL;
    private long timeout = 0;
    private boolean die;
    private final ManagerConnection connection;

    /**
     * Creates a new PingThread that uses the given ManagerConnection.
     * 
     * @param connection ManagerConnection that is pinged
     */
    public PingThread(ManagerConnection connection)
    {
        super();
        this.connection = connection;
        this.die = false;
        this.id = idCounter.getAndIncrement();
        setName("Asterisk-Java Ping-" + id);
        setDaemon(true);
    }

    /**
     * Adjusts how often a PingAction is sent.
     * <p>
     * Default is 20000ms, i.e. 20 seconds.
     * 
     * @param interval the interval in milliseconds
     */
    public void setInterval(long interval)
    {
        this.interval = interval;
    }

    /**
     * Sets the timeout to wait for the ManagerResponse before throwing an
     * excpetion.
     * <p>
     * If set to 0 the response will be ignored an no exception will be thrown
     * at all.
     * <p>
     * Default is 0.
     * 
     * @param timeout the timeout in milliseconds or 0 to indicate no timeout.
     * @since 0.3
     */
    public void setTimeout(long timeout)
    {
        this.timeout = timeout;
    }

    /**
     * Terminates this PingThread.
     */
    public void die()
    {
        this.die = true;
        interrupt();
    }

    @Override
   public void run()
    {
        while (!die)
        {
            try
            {
                sleep(interval);
            }
            catch (InterruptedException e) // NOPMD
            {
                // swallow
            }

            // exit if die is set
            if (die)
            {
                break;
            }

            // skip if not connected
            if (connection.getState() != ManagerConnectionState.CONNECTED)
            {
                continue;
            }

            ping();
        }
    }

    /**
     * Sends a ping to Asterisk and logs any errors that may occur.
     */
    protected void ping()
    {
        ManagerResponse response;
        try
        {
            if (timeout <= 0)
            {
                connection.sendAction(new PingAction(), null);
            }
            else
            {
                response = connection.sendAction(new PingAction(), timeout);
                logger.debug("Ping response: " + response);
            }
        }
        catch (Exception e)
        {
            logger.warn("Exception on sending Ping", e);
        }
    }
}
