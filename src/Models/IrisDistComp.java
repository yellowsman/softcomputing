/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.util.Comparator;

/**
 *
 * @author Yellow
 */
public class IrisDistComp implements Comparator<IrisDist>{

    @Override
    public int compare(IrisDist o1, IrisDist o2) {
        
        if(o1.getDist() == o2.getDist()){
            return 0;
        }
        if(o1.getDist() > o2.getDist()){
            return 1;
        }else{
            return -1;
        }
    }
}
