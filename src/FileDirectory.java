
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author salman
 */
public class FileDirectory {
   
    String mainPath;
    HashMap<String, File> allFiles;
    HashMap<Integer, File> openFiles;
    HashMap<Integer, File> readFiles;
    HashMap<Integer, File> writeFiles;
    HashMap<Integer, Integer> fdToFdMap;
    int fileDescriptor;
    
    public FileDirectory(){
        allFiles = new HashMap<>();
        openFiles = new HashMap<>();
        readFiles =  new HashMap<>();
        writeFiles = new HashMap<>();
        fdToFdMap = new HashMap<>();
        fileDescriptor = RandomDataGenerator.getRandomInt(1, 1000);
        
    }
    
    
    public int openFile( String fileName, int mode ) throws FileNotFoundException, IOException{
        
        int origanalFD, temporaryFD;
        if( fileName == null ){
            return -1;
        }
        File file = null;
        
        if( !fileExists( fileName ) ){
            
            file = new File(fileName);
            allFiles.put(fileName, file);
            origanalFD = file.open(mode);
        }else{
            file = allFiles.get(fileName);
            origanalFD = file.open( mode );
        }
        
        if( origanalFD < 0 ){
            return origanalFD;
        }
        
        temporaryFD = fileDescriptor++;
        fdToFdMap.put( temporaryFD, origanalFD);
        openFiles.put( temporaryFD, file);
        if( mode == 0 ){
            readFiles.put( temporaryFD, file);
        }else if( mode == 1 ){
            writeFiles.put( temporaryFD, file );
        }
        
        return temporaryFD;
    }
    
    // have to look it more 
    public int closeFile( int fileDescriptor, int mode ) throws Exception{
        
        String fileName = getFileName(fileDescriptor);
        if( fileExists(fileName) && openFiles.get(fileDescriptor).open ){
            
            int fileClose = openFiles.get(fileDescriptor).close( mode );
            if( fileClose < 0 ){
                openFiles.remove(fileDescriptor);
            }
            
            if( mode == 1 && fileClose == 1 ){
                writeFiles.remove( fileDescriptor );
            }else if( mode == 0 && fileClose == 1 ){
                readFiles.remove( fileDescriptor );
            } 
            fdToFdMap.remove( fileDescriptor );
            return 1;
            
        }
        return -1;
    }
    
    public int createFile( String fileName ) throws Exception{
        
        if( fileName != null && !fileExists( fileName )  ){
            allFiles.put(fileName, new File( fileName ) );
            try{
                createFileOnHard(fileName,"");
            }catch( Exception ex ){
                throw ex;
            }
            return 1;
        }
        return -1;
    }
    
    public String readFile( int fileDescriptor){
        
        if( readFiles.containsKey( fileDescriptor ) && readFiles.get( fileDescriptor ).isOpen() ){
            return readFiles.get( fileDescriptor ).getContent();
        }
        return null;
    }
    
    public int writeFile( int fileDescriptor, String content ){
        
        if( writeFiles.containsKey( fileDescriptor ) ){
            writeFiles.get( fileDescriptor ).write( content );
            return 1;
        }
        return -1;
    }
    
    public boolean fileExists( String fileName ){
        
        for( String _fileName: allFiles.keySet() ){
            if( _fileName.equals( fileName ) ){
                return true;
            }
        }
        
        if( allFiles.containsKey( fileName ) ){
            return true;
        }
        return false;
    }
    
    public int deleteFile( String fileName ){
        
        if( fileExists( fileName ) ){
            allFiles.remove( fileName );
            return 1;
        }
        return -1;
    }
    
    public String getFileName( int fileDescriptor ){
        if( openFiles.containsKey( fileDescriptor ) ){
            return openFiles.get( fileDescriptor ).name;
        }
        return null;
    }
    
    
    public void createFileOnHard(String _name, String _content) throws IOException, Exception {

        FileWriter fw;
        BufferedWriter bw;

        fw = new FileWriter(_name);
        bw = new BufferedWriter(fw);

        if (bw != null) {
            bw.write(_content);
            bw.close();
            fw.close();
        } else {
            throw new Exception("buffered writer is null in FileDirectory.createFileOnHard()");
        }
    }
    
}
