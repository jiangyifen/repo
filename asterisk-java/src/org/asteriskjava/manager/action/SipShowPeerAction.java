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
package org.asteriskjava.manager.action;

import org.asteriskjava.manager.event.PeerlistCompleteEvent;

/**
 * Retrieves a the details about a given SIP peer.<p>
 * For a PeerEntryEvent is sent by Asterisk containing the details of the peer
 * followed by a PeerlistCompleteEvent.<p>
 * Available since Asterisk 1.2
 * 
 * @see org.asteriskjava.manager.event.PeerEntryEvent
 * @see org.asteriskjava.manager.event.PeerlistCompleteEvent
 * @author srt
 * @version $Id: SipShowPeerAction.java,v 1.2 2011/05/24 03:48:00 bruce Exp $
 * @since 0.2
 */
public class SipShowPeerAction extends AbstractManagerAction
        implements
            EventGeneratingAction
{
    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = 921037572305993779L;
    private String peer;

    /**
     * Creates a new empty SipShowPeerAction.
     */
    public SipShowPeerAction()
    {

    }

    /**
     * Creates a new SipShowPeerAction that requests the details about the given
     * SIP peer.
     * <p>
     * This is just the peer name without the channel type prefix. For example
     * if your channel is called "SIP/john", the peer name is just "john".
     * 
     * @param peer the name of the SIP peer to retrieve details for.
     * @since 0.2
     */
    public SipShowPeerAction(String peer)
    {
        this.peer = peer;
    }

    @Override
   public String getAction()
    {
        return "SIPShowPeer";
    }

    /**
     * Returns the name of the peer to retrieve.<p>
     * This parameter is mandatory.
     * 
     * @return the name of the peer to retrieve without the channel type prefix.
     */
    public String getPeer()
    {
        return peer;
    }

    /**
     * Sets the name of the peer to retrieve.
     * <p>
     * This is just the peer name without the channel type prefix. For example
     * if your channel is called "SIP/john", the peer name is just "john".
     * <p>
     * This parameter is mandatory.
     * 
     * @param peer the name of the peer to retrieve without the channel type prefix.
     */
    public void setPeer(String peer)
    {
        this.peer = peer;
    }

    @SuppressWarnings("rawtypes")
    public Class getActionCompleteEventClass()
    {
        return PeerlistCompleteEvent.class;
    }
}
