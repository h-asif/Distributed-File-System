/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import javax.swing.JTextArea;

/**
 *
 * @author salman
 */
public class DFSClientOperations extends UnicastRemoteObject implements ClientSideRemoteOperations{

    ServerSideRemoteOperations serverStub;
    String clientStubID;
    HashMap<String,JTextArea> textAreas;
    
    public DFSClientOperations( HashMap<String,JTextArea> _textAreas ) 
                                                                            throws RemoteException{
        super();
        
        this.serverStub = null;
        this.clientStubID = "client_"+RandomDataGenerator.getRandomString();
        textAreas = _textAreas;
        
        
    }
    

    
    public int createFile(String fileName) throws RemoteException {
        if( serverStub == null ){
            return -1;
        }
        return serverStub.createFile( fileName );
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    
    public int openFile(String fileName, int mode) throws RemoteException {
        
        if(  fileName ==  null ){
            return -1;
        }
        
        return serverStub.openFile(fileName, mode, clientStubID);
    }

    
    public String readFile(int fd ) throws RemoteException  {
        if( fd <= 0){
            return null;
        }
        return serverStub.readFile(fd, clientStubID );
    }

    public int writeFile( int fd, String buffer, int mode) throws RemoteException{
        
        if( fd < 0 || buffer == null || mode < 0 || mode > 2){
            return -1;
        }
        return serverStub.writeFile(fd, buffer, mode);
    
    }
    
    public int writeFile(String fileName, String buffer ) throws RemoteException {
        
        for( String file_name: textAreas.keySet() ){
            if( file_name.split("_")[0].equals(fileName) ){
                textAreas.get( file_name ).setText(buffer);
            }
        }
        
        return 1;
    }

    
    public int closeFile(int fd) throws RemoteException {
        
        if( fd <= 0 ){
            return -1;
        }
        return serverStub.closeFile(fd, clientStubID );
    }
    
    public String connect(String hostIP ) throws RemoteException, NotBoundException, MalformedURLException{
        if( hostIP != null ){
            
            this.serverStub = (ServerSideRemoteOperations) Naming.lookup("rmi://" + hostIP + "/DFSServer");
            String _serverStubID = serverStub.connect( clientStubID, hostIP);
            serverStub.connect(clientStubID, "127.0.0.1");
        }
        return null;
    }
    
    
    
}
