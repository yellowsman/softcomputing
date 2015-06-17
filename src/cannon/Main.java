/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cannon;

/**
 *
 * @author syogo
 */
public class Main {

    final int TARGET_DISTANCE = 500; // 単位はメートル
    final int BOMB_BLAST = 10;
    final static int SERCH_RANGE = 500;
    final int SPEED = 70;

    public static void main(String[] args) {
       
        Main m = new Main();
        m.bruteforce(SERCH_RANGE);
    }

    public void bruteforce(int try_field) {
        double angle = 0;
        boolean hit = false;
        
        // d = s^2 * sin(2a)/9.8  d=距離 s=speed a=angle 

        // 手前から奥に向かって爆風分だけしらみ潰しに調査
        for (int p = 0; p <= try_field; p += BOMB_BLAST) {
            angle = compA(p,SPEED);
            System.out.println(angle);
            if (p + BOMB_BLAST > TARGET_DISTANCE) {
                hit = true;
                break;
            }
        }
        
        if(hit){
            System.out.println("標的に当たりました" + "射角は"+angle+"度です。");
        }else{
            System.out.println("標的は見つかりませんでした");
        }

    }
    
    // 正しい計算式
    public double compA(double d,double s){
         System.out.println(Math.asin((9.8*d) / (s*s))/2);
         return  Math.toDegrees(Math.asin((9.8*d) / (s*s))/2);
    }
}
