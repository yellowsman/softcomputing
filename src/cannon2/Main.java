/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cannon2;

/**
 *
 * @author tuis
 */
public class Main {
    final int ENEMY_POSITION = 500;
    final static int SEARCH_RANGE = 1000;
    static int count = 0;
    
    public static void main(String[] args){
        Main m = new Main();
        //System.out.println(m.compA(45, 70));
        
        double d = m.esearch(SEARCH_RANGE);
        System.out.println("d = "+d);
    }
    
    public double esearch(int range){
        int speed = 0;
        double angle = 0;
        
        for(speed = 70;speed<=100;speed+=0.1){
            for(angle = 1;angle<=90;angle+=0.1){
                
                double distance = compA(angle,speed);
                if(distance < 0 || distance > range) continue;
                
                //System.out.println(distance);
                if(distance <= ENEMY_POSITION+0.1 && distance >= ENEMY_POSITION-0.1){
                    System.out.println("speed:"+speed+" angle:"+angle);
                    count++;
                    //return distance;
                }
            }  
        }
        System.out.println(count);
        return 0;
    }
    
    public double compA(double a,double s){
    //     System.out.println(Math.asin((9.8*d) / (s*s))/2);
         return  (s*s*Math.sin(Math.toRadians(2*a)))/9.8;
    }
    
}
