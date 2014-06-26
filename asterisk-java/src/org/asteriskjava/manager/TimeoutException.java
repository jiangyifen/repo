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

/**
 * A TimeoutException is thrown if a ManagerResponse is not received within the
 * expected time period.
 * 
 * @author srt
 * @version $Id: TimeoutException.java,v 1.1 2010/12/10 07:00:36 bruce Exp $
 */
public class TimeoutException extends Exception
{
    /**
     * Serial version identifier
     */
    private static final long serialVersionUID = 7674248607624140309L;

    /**
     * Creates a new TimeoutException with the given message.
     * 
     * @param message message with details about the timeout.
     */
    public TimeoutException(final String message)
    {
        super(message);
    }
}
