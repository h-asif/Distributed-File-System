/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author salman
 */
public interface ServerSideRemoteOperations extends Remote{
    
    public int createFile( String filename) throws RemoteException;
    
    public int openFile ( String filename, int mode, String stubID ) throws RemoteException;
    
    public String readFile(int fd, String stubID ) throws RemoteException;
    
    public int writeFile(int fd, String buffer, int mode) throws RemoteException;
    
    public int closeFile(int fd, String stubID ) throws RemoteException;
    
    public String connect( String stubID, String IPAddress ) throws RemoteException;
    
    public void disconnect(String stubID) throws RemoteException;

    
}
