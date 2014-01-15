/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author salman
 */
public class DFSServerOperations extends UnicastRemoteObject implements ServerSideRemoteOperations{

    HashMap<Integer,ArrayList<DFSClientOperations>> readers;
    HashMap<Integer,DFSClientOperations> writers;
    ConnectedClients connectedClients;
    String serverStubID;
    FileDirectory fileDirectory;
    
    public DFSServerOperations(String _serverStubID, 
                                ConnectedClients _connectedClients,
                                FileDirectory _fileDirectory) throws RemoteException{
        super();
        this.serverStubID = _serverStubID;
        this.connectedClients = _connectedClients;
        this.fileDirectory = _fileDirectory;
        readers = new HashMap<>();
        writers = new HashMap<>();
    }

    //@Override
    public int createFile(String fileName) throws RemoteException {
        try {
            return fileDirectory.createFile(fileName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    //@Override
    public int openFile(String fileName, int mode, String stubID ) throws RemoteException {
        try {
            int fileDescriptor = fileDirectory.openFile(fileName, mode);
            if( fileDescriptor > 0 ){
                if( mode == 0 ){
                    connectedClients.addReader( fileDescriptor, fileDirectory.getFileName(fileDescriptor), stubID );
                }else if( mode == 1 ){
                    connectedClients.addWriter(fileDescriptor, fileDirectory.getFileName(fileDescriptor), stubID);
                }
            }
            return fileDescriptor;
        } catch ( Exception ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    //@Override
    public String readFile(int fd, String stubID ) throws RemoteException {
        
        if( connectedClients.isReaderOf(fd, stubID, fileDirectory.getFileName(fd) ) ){
            return fileDirectory.readFile(fd);
        }else{
            return null;
        }
    }

    //@Override
    public int writeFile(int fd, String buffer, int mode) throws RemoteException {
        System.out.println("thread entering....");
        
        if( !fileDirectory.writeFiles.containsKey(fd) ){
            return -2;
        }
        if( mode == 1 ){
            final DFSServerOperations DFSS = this;
            final int _fd = fd;
            final String _buffer = buffer;
            Thread t = new Thread(new Runnable() {
                          public void run() {
                            try {
                                DFSS.writeFile( _fd, _buffer, 0); // make sure you catch here all exceptions thrown by methodToRun(), if any
                            } catch (RemoteException ex) {
                                ex.printStackTrace();
                            }
                          }
                        });

            t.start();
            System.out.println("salamti council....");
            return 1;
            
        }
        
        int result = fileDirectory.writeFile(fd, buffer);
        
        if( result < 0 ){
            return -1;
        }
        String file_name = fileDirectory.getFileName(fd);
        ArrayList<ClientSideRemoteOperations> readersStubList = connectedClients.getReadersOf( file_name );
        
        for( ClientSideRemoteOperations readerClient: readersStubList ){
            readerClient.writeFile( file_name, buffer );
        }
        return 1;
    }

    //@Override
    public int closeFile(int fd, String stubID ) throws RemoteException {

        if( connectedClients.isWriterOf(fd, stubID, fileDirectory.getFileName(fd)) ){
            try {
                connectedClients.removeFromWriterOf(fd, stubID, fileDirectory.getFileName(fd));
                return fileDirectory.closeFile(fd, 1);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }else if( connectedClients.isReaderOf(fd, stubID, fileDirectory.getFileName(fd) ) ){
            try {
                connectedClients.removeFromReadersOf(fd, stubID);
                return fileDirectory.closeFile(fd, 0);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return -1;
    }
    
    public String connect( String stubID, String IPAddress ){
        try {
            ClientSideRemoteOperations remoteClient = (ClientSideRemoteOperations) Naming.lookup("rmi://" + IPAddress + "/"+stubID);
            connectedClients.register(stubID, remoteClient);
            return serverStubID;
        }catch (NotBoundException | MalformedURLException | RemoteException ex) {
            ex.printStackTrace();
        }
        return serverStubID;
    }
    
    public void disconnect ( String stubID ) throws RemoteException {
        
        
        if( !connectedClients.isReader( stubID ) &&  !connectedClients.isWriter( stubID )){
            connectedClients.removeClient( stubID );
        }
    }
    
}