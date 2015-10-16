/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wn;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author syogo wordnetの類語辞典の関連性を調べるクラス アルゴリズムは次のようになる ・類語ファイルを読み込む
 * ・単語データをIDと結びつけてハッシュマップ化(MapA) ・さらに、IDごとにハッシュマップで登場した回数を調べて接続の多い単語を調べる(MapB)
 * ・@区切りは両方カウントする ・MapBを接続数の多い単語順に並べ替え、高い順にMapAを図にマッピングする
 * ・マッピングの座標は接続が多いものを図の中心に据え、少ないものを端に置く ・図のサイズ(マッピングの範囲)は最小を0、最大を単語の件数/2とする
 */
public class SearchComb {
    Relation[] relations;

    /*
     類似リストを順番に追う
     指定したIDに関連するIDをint型配列で返す
     */
    public ArrayList<Integer> searchHop(int sid) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for(Relation rel:relations){
            if(rel.getSid() == sid){
                list.add(rel.getEid());
            }
            if(rel.getEid() == sid){
                list.add(rel.getSid());
            }
        }

        return list;
    }

    /*
     ホップ数を計算するメソッド
     類似リストを順に追っていき、目的のidにたどり着いたら回数を返す
     */
    public int getHop(int sid, int eid) {
        return hop(sid,eid,0);
    }
    
    /*
    　無限ループの危険性あり
      ※ a <--> bの場合など(aとbで巡回し続ける)
    */
    private int hop(int sid,int eid,int count){
        count++;
        ArrayList<Integer> a = searchHop(sid);
        if(a.indexOf(eid) >= 0){
            return count;
        }else{
            for(int i:a){
                if(i == sid){ continue; }
                return hop(i,eid,count);
            }
        }
        
        return 0;
    }
}

class Relation{
    private int sid;
    private int eid;
    
    private Relation(){}
    
    public Relation(int sid, int eid) {
        this.sid = sid;
        this.eid = eid;
    }

    public int getSid() {
        return sid;
    }

    public int getEid() {
        return eid;
    }
}
