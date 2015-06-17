/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cannon3;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author tuis
 */
public abstract class CannonSearchBase {
    final static int SPEED_RANGE = 100;
    final static int ANGLE_RANGE = 90;
    final static int ENEMY_DISTANCE = 500;
    final static int BOMB_BLAST = 10;
    
    private static int count = 0;
    private File logfile;
    private final String sep = System.getProperty("line.separator");
    
    // 探索の条件
    abstract void  search(int speedrange,int anglerange,int bombarea,int enemy_distance);
    
    private void write(String filename,int distance) throws IOException{
        logfile = new File(filename);
        FileWriter fw = new FileWriter(logfile);
        fw.write(""+count+","+distance+sep);
        if(fw != null){
            fw.close();
        }
    }
    
    public double compDrop(double d,double s){
         //System.out.println(Math.asin((9.8*d) / (s*s))/2);
         return  Math.toDegrees(Math.asin((9.8*d) / (s*s))/2); // 度数法で返す
    }
}
