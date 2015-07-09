/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grid;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TODO: - データの分類 - データの学習(確率テーブルの再構築) - プロット(ファイルへの出力)
 *
 * @author Yellow
 *
 */
public class IrisGridGrouping {

    final String SEPARATOR = System.getProperty("line.separator");
    int n;
    int gwmax, ghmax, hhmax, hwmax;
    int[][] g_setosa, h_setosa, g_virginica, h_virginica, g_versicolor, h_versicolor;
    int s_cnt, vi_cnt, ve_cnt;
    Scanner scan;
    
    

    enum CATEGORY {

        setose,
        versicolor,
        virginica
    }

    
    
    public static void main(String[] args) {
        
    }

    // 初期化
    // 確率表の作成
    void buildGrid() throws IOException {
        scan = new Scanner(System.in); //file
        

        // 件数
        n = scan.nextInt();
        gwmax = 0;
        ghmax = 0;
        hhmax = 0;
        hwmax = 0;

        // 150件
        Iris[] irises = new Iris[n];

        for (int i = 0; i < n; i++) {
            scan.nextInt(); // 行番号データ 捨てる
            double gh = scan.nextInt();
            double gw = scan.nextInt();
            double hh = scan.nextInt();
            double hw = scan.nextInt();
            String cate = scan.next();

            ghmax = Math.max((int) Math.ceil(gh), ghmax);
            gwmax = Math.max((int) Math.ceil(gw), gwmax);
            hhmax = Math.max((int) Math.ceil(hh), hhmax);
            hwmax = Math.max((int) Math.ceil(hw), hwmax);

            irises[i] = new Iris(gh, gw, hh, hw, cate);
        }

        // 間隔を0.5ずつにする　よって、最大値の2倍の範囲を取る2倍
        // 入ってきた値も2倍し、四捨五入して配列に格納する
        ghmax *= 2;
        gwmax *= 2;
        hhmax *= 2;
        hwmax *= 2;

        // マッピングテーブル
        // 直接確率を記入した方が早いかも？
        // それとも、後で確率を計算する時のためのリソースとして扱う？
        g_setosa = new int[ghmax][gwmax];
        g_versicolor = new int[ghmax][gwmax];
        g_virginica = new int[ghmax][gwmax];

        h_setosa = new int[hhmax][hwmax];
        h_versicolor = new int[hhmax][hwmax];
        h_virginica = new int[hhmax][hwmax];

        s_cnt = 0;
        ve_cnt = 0;
        vi_cnt = 0;

        for (Iris i : irises) {
            if (i.getCategory().equals(CATEGORY.setose)) {
                g_setosa = incrementGridGaku(i, g_setosa);
                h_setosa = incrementGridHana(i, h_setosa);
                s_cnt++;

            } else if (i.getCategory().equals(CATEGORY.versicolor)) {
                g_versicolor = incrementGridGaku(i, g_versicolor);
                h_versicolor = incrementGridHana(i, h_versicolor);
                ve_cnt++;
            } else {
                // virginica
                g_virginica = incrementGridGaku(i, g_virginica);
                h_virginica = incrementGridHana(i, h_virginica);
                vi_cnt++;
            }
        }

    }

    void plot() {
        // データの出力
        // 1プロット表につき、1つのファイルを出力する
        // 新しい教師データが入ってきたら、確率計算をやり直す
        // 
        // 出力ファイルフォーマット
        // n 個数
        // inc_x,inc_y x軸とy軸の値の増え方,区間
        // x区間の開始値,y区間の開始値,確率値
        //
        // 例)
        // 20 -> データの個数が20行
        // 5,3 -> xの区間幅は5,yの区間幅は3
        // 12,24,0.8 -> x区間は12~17,y区間は24~27,区間確率は0.8
        String setoGaku = "setosa_Gaku.txt";
        String setoHana = "setosa_Hana.txt";
        String verGaku = "versicolor_Gaku.txt";
        String verHana = "versicolor_Hana.txt";
        String virGaku = "virginica_Hana.txt";
        String virHana = "virginica_Hana.txt";

        FileWriter fw = null;
        try {
            fw = new FileWriter(new File(setoGaku));

            fwriter(fw, 50, 0.5, 0.5, s_cnt, g_setosa);
            fw = new FileWriter(new File(setoHana));
            fwriter(fw, 50, 0.5, 0.5, s_cnt, h_setosa);

            fw = new FileWriter(new File(verGaku));
            fwriter(fw, 50, 0.5, 0.5, ve_cnt, g_versicolor);
            fw = new FileWriter(new File(verHana));
            fwriter(fw, 50, 0.5, 0.5, ve_cnt, h_versicolor);

            fw = new FileWriter(new File(virGaku));
            fwriter(fw, 50, 0.5, 0.5, vi_cnt, g_virginica);
            fw = new FileWriter(new File(virHana));
            fwriter(fw, 50, 0.5, 0.5, vi_cnt, h_virginica);
        } catch (IOException ex) {
            Logger.getLogger(IrisGridGrouping.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    void grouping() {

    }
    
    // レスポンスシステムにする
    void controll(){
        System.out.println("-----操作を選んでください-----");
        System.out.println("i {データファイルパス} = 確率表を構築");
        System.out.println("g [データファイルパス] = データを分類 確率表は更新されない");
        System.out.println("u [データファイルパス] = データを分類 確率表を更新");
        System.out.println("v = 分類の種類を一覧表示");
    }

    int[][] incrementGridGaku(Iris i, int[][] grid) {
        int gh2r = (int) Math.round(i.getGaku_hei() * 2);
        int gw2r = (int) Math.round(i.getGaku_wid() * 2);

        // 指定のグリッドへ個数を追加
        grid[gh2r][gw2r]++;
        return grid;
    }

    int[][] incrementGridHana(Iris i, int[][] grid) {
        int hh2r = (int) Math.round(i.getHana_hei() * 2);
        int hw2r = (int) Math.round(i.getHana_wid() * 2);

        // 指定のグリッドへ個数を追加
        grid[hh2r][hw2r]++;
        return grid;
    }

    void fwriter(FileWriter w, int n, double x_inc, double y_inc, int cnt, int[][] grid) {
        try {
            w.write(n);
            w.write(SEPARATOR);
            w.write(x_inc + "," + y_inc);

            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    if (grid[i][j] != 0) {
                        fwriteLine(w, i, j, grid[i][j] / cnt);
                    }
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(IrisGridGrouping.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    void fwriteLine(FileWriter w, int xs, int ys, double prob) throws IOException {
        String c = ",";
        w.write(xs + c + ys + c + prob);
        w.write(SEPARATOR);
    }
}

class Iris {

    String category;
    double gaku_hei;
    double gaku_wid;
    double hana_hei;
    double hana_wid;

    public Iris(double gaku_hei, double gaku_wid, double hana_hei, double hana_wid, String category) {
        this.category = category;
        this.gaku_hei = gaku_hei;
        this.gaku_wid = gaku_wid;
        this.hana_hei = hana_hei;
        this.hana_wid = hana_wid;
    }

    public String getCategory() {
        return category;
    }

    public double getGaku_hei() {
        return gaku_hei;
    }

    public double getGaku_wid() {
        return gaku_wid;
    }

    public double getHana_hei() {
        return hana_hei;
    }

    public double getHana_wid() {
        return hana_wid;
    }
}
