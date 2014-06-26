/**
 * 
 */
package org.asteriskjava.live.internal;

import java.util.Date;

import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.OriginateCallback;
import org.asteriskjava.manager.action.OriginateAction;

/**
 * Wrapper class for OriginateCallbacks.
 * 
 * @author srt
 * @version $Id: OriginateCallbackData.java,v 1.1 2010/12/10 07:00:40 bruce Exp $
 */
class OriginateCallbackData
{
    private OriginateAction originateAction;
    private Date dateSent;
    private OriginateCallback callback;
    private AsteriskChannel channel;

    /**
     * Creates a new instance.
     * 
     * @param originateAction the action that has been sent to the Asterisk
     *            server
     * @param dateSent date when the the action has been sent
     * @param callback callback to notify about result
     */
    OriginateCallbackData(OriginateAction originateAction, Date dateSent, OriginateCallback callback)
    {
        super();
        this.originateAction = originateAction;
        this.dateSent = dateSent;
        this.callback = callback;
    }

    OriginateAction getOriginateAction()
    {
        return originateAction;
    }

    Date getDateSent()
    {
        return dateSent;
    }

    OriginateCallback getCallback()
    {
        return callback;
    }

    AsteriskChannel getChannel()
    {
        return channel;
    }

    void setChannel(AsteriskChannel channel)
    {
        this.channel = channel;
    }
}
