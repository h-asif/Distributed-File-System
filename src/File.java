
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author salman
 */
public class File{
        
        int fileDescriptor;
        String name;
        String content;
        String path;
        boolean open, read, write, modified;
        int openCount;
        
        
        public File( String fileName){ 
            this.name = fileName;
            this.fileDescriptor =  RandomDataGenerator.getRandomInt(1, 9999999);
            this.content = null;
            this.path = null;
            this.open = false;
            this.read = false;
            this.write = false;
            this.modified = false;
            openCount = 0;
        }
        
        public synchronized int open(int mode ) throws FileNotFoundException, IOException{
            
            if( mode == 1 && write ){
                return -2;
            }else if( mode == 1 && !write ){
                write = true;
            }else if( mode == 0 && !read ){
                read = true;
                try{
                    String _content = readFromHard(name);
                    if( _content == null ){
                        return -1;
                    }else{
                        this.content = _content;
                    }
                }catch( Exception ex ){
                    throw ex;
                }
                
            }else if( mode != 0 && mode != 1 ){
                return -1;
            }
            openCount++;
            open = true;
            return fileDescriptor;
        }
        
        public int close( int mode ) throws Exception{
            if( open ){
                
                if( modified ){
                    try{
                        writeFileOnHard(name, content);
                        modified = false;
                    }catch( Exception ex ){
                        throw ex;
                    }
                    
                }
                openCount--;
                if( mode == 1 ){
                    write = false;
                    return 1;
                }
                
                
                if( openCount == 0 ){
                    open = false;
                    read = false;
                    return 1;
                }
            }
            return -1;
        }
        
        public void write( String _content ){
            
            if( open && write ){
                this.content = _content;
                modified = true;
                try {
                    writeFileOnHard(name, this.content);
                    modified = false;
                }catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        public void setContent( String _content ){
            if( open ){
               this.content = _content;
               modified = true;
            }
        }
        
        public int getFileDescriptor(){
            return fileDescriptor;
        }
        
        public String getContent(){
            if( open ){
                return this.content;
            }
            return null;
        }
        
        public String getPath(){
            return this.path;
        }
        
    public void writeFileOnHard(String _name, String _content) throws IOException, Exception {


        FileWriter fw;
        BufferedWriter bw;

        fw = new FileWriter(_name);
        bw = new BufferedWriter(fw);

        if (bw != null) {
            bw.write(_content);
            bw.close();
            fw.close();
        } else {
            throw new Exception("buffered writer is null in File.close()");
        }
    }
    
    public String readFromHard(String _name) {
        FileReader fr = null;
        BufferedReader br;
        try {
            fr = new FileReader(_name);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
        
        br = new BufferedReader(fr);
        String line = null, _content = "";
        boolean readSomething=false;
        try {
            while ((line = br.readLine()) != null) {
                readSomething = true;
                _content += line+"\n";
            }
            br.close();
            fr.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        
        
        return _content;
    }
    
    public boolean isOpen(){
        return open;
    }
}
