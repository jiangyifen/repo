
package com.jiangyifen.ec.customer.olay.ws;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 2.6.1
 * 2014-09-17T20:29:58.162+08:00
 * Generated source version: 2.6.1
 */

@WebFault(name = "RemoteException", targetNamespace = "http://IVR.myolay.www.accentiv.cn")
public class RemoteException extends Exception {
    
    private com.rmi.RemoteException remoteException;

    public RemoteException() {
        super();
    }
    
    public RemoteException(String message) {
        super(message);
    }
    
    public RemoteException(String message, Throwable cause) {
        super(message, cause);
    }

    public RemoteException(String message, com.rmi.RemoteException remoteException) {
        super(message);
        this.remoteException = remoteException;
    }

    public RemoteException(String message, com.rmi.RemoteException remoteException, Throwable cause) {
        super(message, cause);
        this.remoteException = remoteException;
    }

    public com.rmi.RemoteException getFaultInfo() {
        return this.remoteException;
    }
}
