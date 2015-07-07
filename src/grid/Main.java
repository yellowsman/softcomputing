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

/**
 *
 * @author Yellow
 */
public class Main {

    enum CATEGORY {

        setose,
        versicolor,
        virginica
    }

    public static void main(String[] args) {

    }

    void grid() throws IOException {
        Scanner scan = new Scanner(System.in); //file

        // 件数
        int n = scan.nextInt();
        int gwmax = 0;
        int ghmax = 0;
        int hhmax = 0;
        int hwmax = 0;

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
        int[][] g_setosa = new int[ghmax][gwmax];
        int[][] g_versicolor = new int[ghmax][gwmax];
        int[][] g_virginica = new int[ghmax][gwmax];

        int[][] h_setosa = new int[hhmax][hwmax];
        int[][] h_versicolor = new int[hhmax][hwmax];
        int[][] h_virginica = new int[hhmax][hwmax];

        int s_cnt = 0;
        int ve_cnt = 0;
        int vi_cnt = 0;

        for (Iris i : irises) {
            if (i.getCategory().equals(CATEGORY.setose)) {
                int gh2r = (int) Math.round(i.getGaku_hei() * 2);
                int gw2r = (int) Math.round(i.getGaku_wid() * 2);
                int hh2r = (int) Math.round(i.getHana_hei() * 2);
                int hw2r = (int) Math.round(i.getHana_wid() * 2);

                // 指定のグリッドへ個数を追加
                g_setosa[gh2r][gw2r]++;
                h_setosa[hh2r][hw2r]++;

                s_cnt++;

            } else if (i.getCategory().equals(CATEGORY.versicolor)) {

                int gh2r = (int) Math.round(i.getGaku_hei() * 2);
                int gw2r = (int) Math.round(i.getGaku_wid() * 2);
                int hh2r = (int) Math.round(i.getHana_hei() * 2);
                int hw2r = (int) Math.round(i.getHana_wid() * 2);

                // 指定のグリッドへ個数を追加
                g_versicolor[gh2r][gw2r]++;
                h_versicolor[hh2r][hw2r]++;

                ve_cnt++;
            } else {
                // virginica

                int gh2r = (int) Math.round(i.getGaku_hei() * 2);
                int gw2r = (int) Math.round(i.getGaku_wid() * 2);
                int hh2r = (int) Math.round(i.getHana_hei() * 2);
                int hw2r = (int) Math.round(i.getHana_wid() * 2);

                // 指定のグリッドへ個数を追加
                g_virginica[gh2r][gw2r]++;
                h_virginica[hh2r][hw2r]++;

                vi_cnt++;
            }
        }

        // データの出力
        // どういう形式で出力すべきか…
        // とりあえず各データをカンマ区切りで各行出力
        String setoGaku = "setosa_Gaku.txt";
        String setoHana = "setosa_Hana.txt";
        String verGaku = "versicolor_Gaku.txt";
        String verHana = "versicolor_Hana.txt";
        String virGaku = "virginica_Hana.txt";
        String virHana = "virginica_Hana.txt";

        FileWriter fw = new FileWriter(new File(setoGaku));

        // 現状のファイル構造は、空白がファイルに出力されてしまう
        for (int[] l : g_setosa) {
        }

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
