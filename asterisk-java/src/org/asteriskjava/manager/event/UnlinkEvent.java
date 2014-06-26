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
 * An UnlinkEvent is triggered when a link between two voice channels is discontinued, for example,
 * just before call completion.<p>
 * It is implemented in <code>channel.c</code>
 * 
 * @author srt
 * @version $Id: UnlinkEvent.java,v 1.1 2010/12/10 06:59:39 bruce Exp $
 */
public class UnlinkEvent extends LinkageEvent
{
    /**
     * Serial version identifier
     */
    static final long serialVersionUID = -2943257621137870024L;

    /**
     * @param source
     */
    public UnlinkEvent(Object source)
    {
        super(source);
    }
}
