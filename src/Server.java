/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author salman
 */
public class Server {
    
    public static void main( String args[] ){
         
        String serverStubID = "server:"+RandomDataGenerator.getRandomString();
        ConnectedClients connectedClients = new ConnectedClients();
        FileDirectory fileDirectory = new FileDirectory();
        
       // if (System.getSecurityManager() == null)
        //    System.setSecurityManager ( new RMISecurityManager() );

        // Create an instance of our power service server ...
        DFSServerOperations server = null;
        try {
            server = new DFSServerOperations(serverStubID, connectedClients, fileDirectory );
        } catch (RemoteException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

        // ... and bind it with the RMI Registry
        if( server != null )
        try {
            Naming.bind ("DFSServer", server);
        } catch (AlreadyBoundException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println ("Service bound....");
    }
    
}
