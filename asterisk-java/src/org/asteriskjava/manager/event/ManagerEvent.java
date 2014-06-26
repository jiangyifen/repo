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
package org.asteriskjava.manager.event;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.EventObject;
import java.util.Map;

import org.asteriskjava.util.ReflectionUtil;

/**
 * Abstract base class for all Events that can be received from the Asterisk
 * server.
 * <p>
 * Events contain data pertaining to an event generated from within the Asterisk
 * core or an extension module.
 * <p>
 * There is one conrete subclass of ManagerEvent per each supported Asterisk
 * Event.
 * 
 * @author srt
 * @version $Id: ManagerEvent.java,v 1.1 2010/12/10 06:59:44 bruce Exp $
 */
public abstract class ManagerEvent extends EventObject implements Serializable
{
    /**
     * Serializable version identifier
     */
    static final long serialVersionUID = 4299374743315152040L;

    /**
     * AMI authorization class.
     */
    private String privilege;

    /**
     * The point in time this event has been received from the Asterisk server.
     */
    private Date dateReceived;

    private Double timestamp;

    /**
     * @param source
     */
    public ManagerEvent(Object source)
    {
        super(source);

    }

    /**
     * Returns the point in time this event was received from the Asterisk
     * server.
     * <p>
     * Pseudo events that are not directly received from the asterisk server
     * (for example ConnectEvent and DisconnectEvent) may return
     * <code>null</code>.
     */
    public Date getDateReceived()
    {
        return dateReceived;
    }

    /**
     * Sets the point in time this event was received from the asterisk server.
     */
    public void setDateReceived(Date dateReceived)
    {
        this.dateReceived = dateReceived;
    }

    /**
     * Returns the AMI authorization class of this event.
     * <p>
     * This is one or more of system, call, log, verbose, command, agent or
     * user. Multiple privileges are separated by comma.
     * <p>
     * Note: This property is not available from Asterisk 1.0 servers.
     * 
     * @since 0.2
     */
    public String getPrivilege()
    {
        return privilege;
    }

    /**
     * Sets the AMI authorization class of this event.
     * 
     * @since 0.2
     */
    public void setPrivilege(String privilege)
    {
        this.privilege = privilege;
    }

    /**
     * Returns the timestamp for this event.
     * <p>
     * The timestamp property is available in Asterisk since 1.4 if enabled in
     * <code>manager.conf</code> by setting <code>timestampevents = yes</code>.
     * <p>
     * In contains the time the event was generated in seconds since the epoch.
     * <p>
     * Example: 1159310429.569108
     * 
     * @return
     * @since 0.3
     */
    public final Double getTimestamp()
    {
        return timestamp;
    }

    /**
     * Sets the timestamp for this event.
     * 
     * @param timestamp the timestamp to set.
     * @since 0.3
     */
    public final void setTimestamp(Double timestamp)
    {
        this.timestamp = timestamp;
    }

    @Override
    public String toString()
    {
        StringBuffer sb;
        Map<String, Method> getters;

        sb = new StringBuffer(getClass().getName() + "[");
        sb.append("dateReceived=" + getDateReceived() + ",");
        if (getPrivilege() != null)
        {
            sb.append("privilege='" + getPrivilege() + "',");
        }
        getters = ReflectionUtil.getGetters(getClass());
        for (String attribute : getters.keySet())
        {
            if ("class".equals(attribute) || "datereceived".equals(attribute) || "privilege".equals(attribute)
                    || "source".equals(attribute))
            {
                continue;
            }

            try
            {
                Object value;
                value = getters.get(attribute).invoke(this);
                sb.append(attribute + "='" + value + "',");
            }
            catch (Exception e) // NOPMD
            {
            }
        }
        sb.append("systemHashcode=" + System.identityHashCode(this));
        sb.append("]");

        return sb.toString();
    }
}
