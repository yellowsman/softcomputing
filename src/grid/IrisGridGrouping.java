/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grid;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TODO: データの分類 データの学習(確率テーブルの再構築、おそらく速度的に問題ない) JavaPlotを利用した描画 -> 縦横の単位が違う
 * 現状は個数が横になっているので、それぞれ長さと幅になるよう修正すべき DataSetクラスや、PlotStyleクラス
 *
 *
 * @author Yellow
 *
 */
public class IrisGridGrouping {

    final double BORDER_POINT = 0.5; // 0.1~1まで

    int n;
    int maxSepWid, maxSepLen, maxPetLen, maxPetWid; // sepal,petalそれぞれの長さと幅の最大値
    int sizeSepWid, sizeSepLen, sizePetLen, sizePetWid; // 配列の大きさ
    int[][] cntSepSetosa, cntPetSetosa, cntSepVirginica, cntPetVirginica, cntSepVersicolor, cntPetVersicolor; //種類ごとのグリッドデータの件数を格納
    double[][] probSepSetosa, probPetSetosa, probSepVirginica, probPetVirginica, probSepVersicolor, probPetVersicolor; // グリッドのマス目の確率を計算
    int cntS, cntVi, cntVe; // 種類の個数
    Scanner scan;

    final String IRIS_DATA = "iris.txt";
    final String TEST_DATA = "testdata";
    final String GNUPLOT_PATH = "C:\\Program Files\\gnuplot\\bin\\gnuplot.exe";

    enum IrisNames {

        setosa,
        versicolor,
        virginica
    }

    public static void main(String[] args) throws IOException {
        IrisGridGrouping igg = new IrisGridGrouping();
        igg.controll();
    }

    // レスポンスシステムにする
    void controll() throws IOException {
//        System.out.println("-----操作を選んでください-----");
//        System.out.println("i {データファイルパス} = 確率表を構築");
//        System.out.println("g [データファイルパス] = データを分類 確率表は更新されない");
//        System.out.println("u [データファイルパス] = データを分類 確率表を更新");
//        System.out.println("v = 分類の種類を一覧表示");
        buildGrid();
        grouping();
        //plot();

        view();
    }

    // 初期化
    // 確率表の作成
    void buildGrid() throws IOException {
        //scan = new Scanner(System.in); //file
        scan = new Scanner(new File(IRIS_DATA));

        for (int i = 0; i < 5; i++) {
            System.out.println(scan.next()); // ラベル捨て
        }

        // 件数
        n = scan.nextInt();
        maxSepWid = maxSepLen = maxPetLen = maxPetWid = 0;

        // 150件
        Iris[] irises = new Iris[n];

        for (int i = 0; i < n; i++) {
            scan.nextInt(); // 行番号データ 捨てる
            double sl = scan.nextDouble();
            double sw = scan.nextDouble();
            double pl = scan.nextDouble();
            double pw = scan.nextDouble();
            String cate = scan.next();

            maxSepLen = Math.max((int) Math.ceil(sl), maxSepLen);
            maxSepWid = Math.max((int) Math.ceil(sw), maxSepWid);
            maxPetLen = Math.max((int) Math.ceil(pl), maxPetLen);
            maxPetWid = Math.max((int) Math.ceil(pw), maxPetWid);

            irises[i] = new Iris(sl, sw, pl, pw, cate);
        }

        // グリッドの間隔、マス目を決定
        sizeSepLen = (int) (maxSepLen / BORDER_POINT) + 1;
        sizeSepWid = (int) (maxSepWid / BORDER_POINT) + 1;
        sizePetLen = (int) (maxPetLen / BORDER_POINT) + 1;
        sizePetWid = (int) (maxPetWid / BORDER_POINT) + 1;

        // カウントテーブル
        cntSepSetosa = new int[sizeSepLen][sizeSepWid];
        cntSepVersicolor = new int[sizeSepLen][sizeSepWid];
        cntSepVirginica = new int[sizeSepLen][sizeSepWid];
        cntPetSetosa = new int[sizePetLen][sizePetWid];
        cntPetVersicolor = new int[sizePetLen][sizePetWid];
        cntPetVirginica = new int[sizePetLen][sizePetWid];

        // 確率テーブル
        probSepSetosa = new double[sizeSepLen][sizeSepWid];
        probSepVersicolor = new double[sizeSepLen][sizeSepWid];
        probSepVirginica = new double[sizeSepLen][sizeSepWid];
        probPetSetosa = new double[sizePetLen][sizePetWid];
        probPetVersicolor = new double[sizePetLen][sizePetWid];
        probPetVirginica = new double[sizePetLen][sizePetWid];

        cntS = cntVe = cntVi = 0;

        // カウントテーブルへ値を設定
        for (Iris i : irises) {
            if (i.getCategory().equals(IrisNames.setosa.toString())) {
                cntSepSetosa = incrementGridSepal(i, cntSepSetosa);
                cntPetSetosa = incrementGridPetal(i, cntPetSetosa);
                cntS++;

            } else if (i.getCategory().equals(IrisNames.versicolor.toString())) {
                cntSepVersicolor = incrementGridSepal(i, cntSepVersicolor);
                cntPetVersicolor = incrementGridPetal(i, cntPetVersicolor);
                cntVe++;
            } else {
                // virginica
                cntSepVirginica = incrementGridSepal(i, cntSepVirginica);
                cntPetVirginica = incrementGridPetal(i, cntPetVirginica);
                cntVi++;
            }
        }

        // 確率計算
        for (int i = 0; i < sizeSepLen; i++) {
            for (int j = 0; j < sizeSepWid; j++) {
                probSepSetosa[i][j] = cntSepSetosa[i][j] / (double) cntS;
                probSepVirginica[i][j] = cntSepVirginica[i][j] / (double) cntVi;
                probSepVersicolor[i][j] = cntSepVersicolor[i][j] / (double) cntVe;
            }
        }

        for (int i = 0; i < sizePetLen; i++) {
            for (int j = 0; j < sizePetWid; j++) {
                probPetSetosa[i][j] = cntPetSetosa[i][j] / (double) cntS;
                probPetVirginica[i][j] = cntPetVirginica[i][j] / (double) cntVi;
                probPetVersicolor[i][j] = cntPetVersicolor[i][j] / (double) cntVe;
            }
        }

    }

    void plot() {
        // データの出力
        // 与えられたデータをgnuplotで表示

    }

    void plotArray(int[][] array, String name) {
        System.out.println(name);

        for (int[] a : array) {
            System.out.println(Arrays.toString(a));
        }
    }

    void plotArray(double[][] array, String name) {
        System.out.println(name);

        for (double[] a : array) {
            System.out.println(Arrays.toString(a));
        }
    }

    void view() {
        System.out.println("------COUNT--------");
        System.out.println("-----Petal---------");
        plotArray(cntPetSetosa, "setosa");
        plotArray(cntPetVersicolor, "versicolor");
        plotArray(cntPetVirginica, "virginica");
        System.out.println("-------Sepal---------");
        plotArray(cntSepSetosa, "setosa");
        plotArray(cntSepVersicolor, "versicolor");
        plotArray(cntSepVirginica, "virginica");
        System.out.println();
        System.out.println("------PROB--------");
        System.out.println("------Petal--------");
        plotArray(probPetSetosa, "setosa");
        plotArray(probPetVersicolor, "versicolor");
        plotArray(probPetVirginica, "virginica");
        System.out.println("-------Sepal---------");
        plotArray(probSepSetosa, "setosa");
        plotArray(probSepVersicolor, "versicolor");
        plotArray(probSepVirginica, "virginica");

    }

    // 入力データを分類
    void grouping() {
        try {
            // ファイルを読み込んで、判定を出力しつづける
            // 合致していたかの判断も○×で出力
            scan = new Scanner(new File(TEST_DATA));
            n = scan.nextInt();
            double cnt = 0;

            for (int i = 0; i < n; i++) {
                scan.nextInt(); //番号捨て
                double sl = scan.nextDouble();
                double sw = scan.nextDouble();
                double pl = scan.nextDouble();
                double pw = scan.nextDouble();
                String cate = scan.next();

                String dec = decision(new Iris(sl, sw, pl, pw, cate));
                System.out.print(dec);
                if (cate.equals(dec)) {
                    System.out.println(" o");
                    cnt++;
                } else {
                    System.out.println(" x");
                }

            }

            System.out.println("正答率:" + (cnt / n));

        } catch (FileNotFoundException ex) {
            Logger.getLogger(IrisGridGrouping.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // 入力データから、どの種類なのかを返す
    String decision(Iris iris) {
        int slbr = (int) (Math.round(iris.getSepalLen()) / BORDER_POINT);
        int swbr = (int) (Math.round(iris.getSepalWid()) / BORDER_POINT);
        int plbr = (int) (Math.round(iris.getPetalLen()) / BORDER_POINT);
        int pwbr = (int) (Math.round(iris.getPetalWid()) / BORDER_POINT);

        double sep, pet;
        String scate, pcate; // sepalとpetalでの判断を格納

        // sepal
        if (probSepVersicolor[slbr][swbr] < probSepVirginica[slbr][swbr]) {
            if (probSepSetosa[slbr][swbr] < probSepVirginica[slbr][swbr]) {
                // virが一番
                sep = probSepVirginica[slbr][swbr];
                scate = IrisNames.virginica.toString();
            } else {
                // setoが一番
                sep = probSepSetosa[slbr][swbr];
                scate = IrisNames.setosa.toString();
            }
        } else {
            if (probSepSetosa[slbr][swbr] < probSepVersicolor[slbr][swbr]) {
                // verが一番
                sep = probSepVersicolor[slbr][swbr];
                scate = IrisNames.versicolor.toString();
            } else {
                // setoが一番
                sep = probSepSetosa[slbr][swbr];
                scate = IrisNames.setosa.toString();
            }

        }

        // petal
        if (probPetVersicolor[plbr][pwbr] < probPetVirginica[plbr][pwbr]) {
            if (probPetSetosa[plbr][pwbr] < probPetVirginica[plbr][pwbr]) {
                // virが一番
                pet = probPetVirginica[plbr][pwbr];
                pcate = IrisNames.virginica.toString();
            } else {
                // setoが一番
                pet = probPetSetosa[plbr][pwbr];
                pcate = IrisNames.setosa.toString();
            }
        } else {
            if (probPetSetosa[plbr][pwbr] < probPetVersicolor[plbr][pwbr]) {
                // verが一番
                pet = probPetVersicolor[plbr][pwbr];
                pcate = IrisNames.versicolor.toString();
            } else {
                // setoが一番
                pet = probPetSetosa[plbr][pwbr];
                pcate = IrisNames.setosa.toString();
            }

        }

        // sepalとpetalで確率が高い方を採用
        if (sep > pet) {
            return scate;
        } else {
            return pcate;
        }

    }

    // グリッドへ個数を追加
    int[][] incrementGridSepal(Iris i, int[][] grid) {
        int slbr = (int) Math.round(i.getSepalLen() / BORDER_POINT);
        int swbr = (int) Math.round(i.getSepalWid() / BORDER_POINT);

        // 指定のグリッドへ個数を追加
        grid[slbr][swbr]++;
        return grid;
    }

    int[][] incrementGridPetal(Iris i, int[][] grid) {
        int plbr = (int) (Math.round(i.getPetalLen()) / BORDER_POINT);
        int pwbr = (int) (Math.round(i.getPetalWid()) / BORDER_POINT);

        // 指定のグリッドへ個数を追加
        grid[plbr][pwbr]++;
        return grid;
    }

}

class Iris {

    private String category;
    private double sepalLen;
    private double sepalWid;
    private double PetalLen;
    private double PetalWid;

    public Iris(double sl, double sw, double pl, double pw, String category) {
        this.category = category;
        this.sepalLen = sl;
        this.sepalWid = sw;
        this.PetalLen = pl;
        this.PetalWid = pw;
    }

    public String getCategory() {
        return category;
    }

    public double getSepalLen() {
        return sepalLen;
    }

    public double getSepalWid() {
        return sepalWid;
    }

    public double getPetalLen() {
        return PetalLen;
    }

    public double getPetalWid() {
        return PetalWid;
    }

}

class IrisPlot extends Number {

    @Override
    public int intValue() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public long longValue() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public float floatValue() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double doubleValue() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
