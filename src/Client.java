/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.rmi.PortableRemoteObject;
import javax.swing.JTextArea;


/**
 *
 * @author salman
 */
public class Client extends DFSClientOperations{
    
    HashMap<Integer,File> openFiles;
    public Client( HashMap<String, JTextArea> textAreas ) throws RemoteException, NotBoundException, MalformedURLException, AlreadyBoundException{
        super( textAreas );
        //this.serverStub = (ServerSideRemoteOperations) Naming.lookup("rmi://" + hostIP + "/DFSServer");
        
        
        
        
    }
    
   /* public int openFile(String fileName, int mode) throws RemoteException {
        
        int fd = super.openFile(fileName, mode);
        
    }*/

    public static void main( String args[] ){

        
        Client client;
       /* if(System.getSecurityManager () == null) {
            System.setSecurityManager(new RMISecurityManager());
        }
        
       //DFSOperations service = null;
        try {
            // Call registry for PowerService
            
            client = new Client( );
            //DFSOperations service = (DFSOperations) PortableRemoteObject.narrow(object, DFSOperations.class);
            System.out.println("connect: "+client.connect( "127.0.0.1") );
            
            System.out.println("creating file: "+client.createFile("test.txt") );
            int fd = client.openFile("test.txt", 1);
            System.out.println("opening file: "+ fd);
            
            System.out.println("writing file: "+client.writeFile(fd, "my name is khan and i'm not a terrorist", 1) );
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            System.out.println("closing file: "+client.closeFile(fd ) );
            
            fd = client.openFile("test.txt", 0);
            System.out.println("opening file: "+fd );
            
            System.out.println("reading file: "+client.readFile(fd) );
            
            
            
        } catch (AlreadyBoundException ex) {
            ex.printStackTrace();
        } catch (MalformedURLException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }catch (RemoteException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        
        
    }
}
