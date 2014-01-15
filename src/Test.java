

import java.io.FileReader;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author salman
 */
public class Test {
    
    public static void main( String args[] ) {
        FileReader fr = null;
        //try {
            //fr = new FileReader("test");
        //} catch (FileNotFoundException ex) {
           // ex.printStackTrace();
        //}
        //BufferedReader br = new BufferedReader(fr);
         String str ="safas";
         System.out.println(str.split("_")[0]);
       // if( br == null ){
        System.out.println("it is indeed null");
        //}
    }
    
    public static void b() throws Exception{
        in(0);
    }
    
    public static int in(int x) throws Exception{
        if(x==1){
            return 0;
        }else if( x !=1){
            return 1;
        }
        throw new Exception();
    }
    
    
}
