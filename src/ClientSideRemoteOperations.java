
import java.rmi.Remote;
import java.rmi.RemoteException;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author salman
 */
public interface ClientSideRemoteOperations extends Remote {
    
     public int writeFile(String fileName, String buffer ) throws RemoteException;
    
}
