
import java.math.BigInteger;
import java.security.SecureRandom;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author salman
 */
public class RandomDataGenerator {

    private static SecureRandom random = new SecureRandom();
    
    public static int getRandomInt(int min, int max) {
        
        return (int) getRandomDouble( (double) min, (double) max);
    }

    public static double getRandomDouble(double min, double max) {

        return min + (Math.random() * ((max - min) + 1));
    }

    public static float getRandomFloat(double min, double max) {

        return (float)min + (float)(Math.random() * ((max - min) + 1));
    }
    
    public static String getRandomString(String dummy1, String dummy2) {

        return new BigInteger(130, random).toString(32);
    }
    
    public static String getRandomString() {

        return new BigInteger(130, random).toString(32);
    }
    
    public static boolean getRandomBoolean(){
        if( Math.random() < 0.5 ){
            return true;
        }else{
            return false;
        }
    }
    
    public static Object getRandomData( Object min, Object max, Class _class ){
        
        if( _class.equals(int.class ) || _class.equals(Integer.class )){
            return (int)getRandomDouble((double)(int)min, (double)(int) max);
        }if( _class.equals(double.class ) || _class.equals(Double.class )){
            return getRandomDouble((double)min, (double) max);
        }if( _class.equals(float.class ) || _class.equals(Float.class )){
            return getRandomFloat( (float)min, (float)max);
        }if( _class.equals(boolean.class ) || _class.equals(Boolean.class )){
            return getRandomBoolean();
        }if( _class.equals(char.class ) || _class.equals(Character.class )){
            return getRandomString(null, null).toCharArray();
        }if( _class.equals(String.class ) ){
            return getRandomString(null, null);
        }else {
            return null;
        }
    }
}
