/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cannon3;

import java.util.ArrayList;

/**
 *
 * @author tuis
 */
public class CannonRandom extends CannonSearchBase {

    public static void main(String[] args) {
        CannonRandom cr = new CannonRandom();
        cr.search(SPEED_RANGE, ANGLE_RANGE, BOMB_BLAST, ENEMY_DISTANCE);
    }

    @Override
    void search(int speedrange, int anglerange, int bombarea, int enemy_distance) {
        int complimit = 5;
        int shot = 0;
        ArrayList<Double> history = new ArrayList<Double>();
        ArrayList<Double> speedlist = new ArrayList<Double>();
        ArrayList<Double> anglelist = new ArrayList<Double>();
        double fspeed = Math.random() * speedrange;
        double fangle = Math.random() * anglerange;
        double first = compDrop(fspeed, fangle);

        // 爆弾の範囲に敵が入ればヒット
        while (shot - bombarea <= enemy_distance && enemy_distance <= shot + bombarea) {
            history.clear();
            speedlist.clear();
            anglelist.clear();

            /*
            while (speedlist.size() <= 5) {
                double s = Math.random() * (fspeed + bombarea);
                if (s >= Math.random() * (fspeed - bombarea)) {
                    speedlist.add(s);
                }
            }
            */

            /*
            while (anglelist.size() <= 5) {
                double a = Math.random() * (fangle + bombarea);
                if (a >= Math.random() * (fangle - bombarea)) {
                    anglelist.add(a);
                }
            }
                    */

            // モンテカルロ法で行き先を決定
            for (int i = 0; i < complimit; i++) {
                double u = first+(Math.random()*0.2)-0.1;
                history.add(u);
            }
        }
    }
    
    // 評価関数 評価結果は数値で返す
    public int valueDrop(int p1,int p2){
        if(p1 >= ENEMY_DISTANCE){
            
        }
        
        return 0;
    }
    
}
