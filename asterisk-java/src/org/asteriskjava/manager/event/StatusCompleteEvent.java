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
 * A StatusCompleteEvent is triggered after the state of all channels has been reported in response
 * to a StatusAction.
 * 
 * @see org.asteriskjava.manager.action.StatusAction
 * @see org.asteriskjava.manager.event.StatusEvent
 * 
 * @author srt
 * @version $Id: StatusCompleteEvent.java,v 1.1 2010/12/10 06:59:53 bruce Exp $
 */
public class StatusCompleteEvent extends ResponseEvent
{
    /**
     * Serial version identifier
     */
    private static final long serialVersionUID = -1177773673509373296L;

    /**
     * @param source
     */
    public StatusCompleteEvent(Object source)
    {
        super(source);
    }
}
