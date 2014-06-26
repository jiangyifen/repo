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

/**
 * A NewChannelEvent is triggered when a new channel is created.<p>
 * It is implemented in <code>channel.c</code>
 * 
 * @author srt
 * @version $Id: NewChannelEvent.java,v 1.1 2010/12/10 06:59:53 bruce Exp $
 */
public class NewChannelEvent extends AbstractChannelStateEvent
{
    /**
     * Serializable version identifier
     */
    static final long serialVersionUID = -4503396901506287549L;

    /**
     * @param source
     */
    public NewChannelEvent(Object source)
    {
        super(source);
    }
}
