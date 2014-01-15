
import java.util.ArrayList;
import java.util.HashMap;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author salman
 */
public class ConnectedClients {
    
    HashMap<String,ArrayList<Integer>> fileNameToFds;
    HashMap<Integer,String> fdToFileName;
    HashMap<String,String> writers;
    HashMap<Integer,String> fdToWriters;
    HashMap<Integer, String> fdToReaders;
    HashMap<String, ClientSideRemoteOperations> connectedClients;
    
    public ConnectedClients(){
        
        connectedClients = new  HashMap<>();
        writers = new  HashMap<>();
        fdToFileName = new HashMap<>();
        fdToReaders = new HashMap<>();
        fileNameToFds = new HashMap<>();
        fdToWriters = new HashMap<>();
        
    }
    
    public void addReader( int fd,String fileName, String clientStubID ){
        System.out.println("test test");
        fdToReaders.put(fd, clientStubID);
        fdToFileName.put( fd, fileName );
        if( !fileNameToFds.containsKey( fileName ) ){
            fileNameToFds.put(fileName, new ArrayList<Integer>());
            
        }
        fileNameToFds.get(fileName).add(fd);
    }
    
    
    public void addWriter( int fd, String fileName, String clientStudID ){
        
        if( !writers.containsKey( fileName ) ){
            fdToFileName.put(fd, fileName );
            writers.put(fileName, clientStudID );
            fdToWriters.put(fd, clientStudID );
        }
    }
    
    public void register( String clientStubID, ClientSideRemoteOperations DFSClientOp ){
        connectedClients.put(clientStubID, DFSClientOp);
    }
    
    public HashMap<String,String> getWriters(){
        return writers;
    }
    
   /* public HashMap<String,ArrayList<String>> getReaders(){
        return readers;
    }*/
    
    
    public ArrayList<ClientSideRemoteOperations> getReadersOf( String fileName ){
        
        ArrayList<ClientSideRemoteOperations> readersStubList = new ArrayList<ClientSideRemoteOperations>();
        if( fileNameToFds.containsKey( fileName ) ){
            ArrayList<Integer> fdList = fileNameToFds.get( fileName );
            
            for( int _fd: fdList ){
                readersStubList.add( connectedClients.get( fdToReaders.get( _fd ) ) );
            }
            return readersStubList;
        }
        return null;
    }
    public boolean isReaderOf( int fd, String clientStubID, String fileName ){
        if( fileNameToFds.containsKey( fileName ) ){
            
            for( int _fd : fileNameToFds.get( fileName ) ){
                if( connectedClients.containsKey( fdToReaders.get(_fd) ) &&
                        fdToReaders.get(fd).equals( clientStubID ) ) {
                    return true;
                }
            }
        }
        return false;
    }
    
    
    public boolean isWriterOf( int fd, String clientStubID, String fileName ){
        if( writers.containsKey( fileName ) && fdToWriters.containsKey( fd ) && 
                fdToWriters.get(fd).equals(clientStubID) ){
            return true;
        }
        return false;
    }
    
    public int removeFromWriterOf( int fd, String clientStubID, String fileName ){
        if( isWriterOf( fd, clientStubID, fileName ) ){
            writers.remove( fileName);
            fdToFileName.remove( fd );
            return 1;
        }
        return -1;
    }
    
    public int removeFromReadersOf( int fd, String clientStubID ){
        if( isReaderOf(fd, clientStubID, fdToFileName.get(fd) ) ){
            fileNameToFds.get( fdToFileName.get(fd) ).remove(new Integer(fd) );
            if( fileNameToFds.get( fdToFileName.get(fd) ).size() == 0 ){
                fileNameToFds.remove( fdToFileName.get(fd) );
            }
            fdToReaders.remove( fd );
            fdToFileName.remove( fd );
            
            
            return 1;
        }
        return -1;
    }
    
    
    public void removeClient( String clientStubID ){
        
        if( clientStubID != null ){
            ArrayList<Integer> allFileDescriptorsForClient = getAllFileDescriptorsFor(clientStubID);
            
            for( int _fd: allFileDescriptorsForClient ){
                
                if( writers.containsKey( fdToFileName.get(_fd) ) ){
                    removeFromWriterOf(_fd, clientStubID, fdToFileName.get(_fd) );
                }else{
                    removeFromReadersOf(_fd, clientStubID );
                }
            }
            connectedClients.remove( clientStubID );
        }
    }
    
    public ArrayList<Integer> getAllFileDescriptorsFor( String clientStubID ){
        ArrayList<Integer> allFdsForClient = new ArrayList<Integer>();
        for( int _fd : fdToReaders.keySet() ){
            if( fdToReaders.get(_fd).equals( clientStubID ) ){
                allFdsForClient.add( _fd );
            }
        }
        
        return allFdsForClient;
    }
    
    public boolean isReader( String clientStubID ){
        
        for( String _clientStubID: fdToReaders.values() ){
            
            if( _clientStubID.equals( clientStubID ) ){
                return true;
            }
        }
        return false;
        
    }
    
    public boolean isWriter( String clientStubID ){
        
        for( String _clientStubID: writers.values() ){
            
            if( _clientStubID.equals( clientStubID ) ){
                return true;
            }
        }
        return false;
    }
}
