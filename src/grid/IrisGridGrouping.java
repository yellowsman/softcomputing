/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grid;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
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
    int swmax, slmax, plmax, pwmax;
    int[][] s_setosa, p_setosa, s_virginica, p_virginica, s_versicolor, p_versicolor;
    double[][] prob_sep_seto, prob_pet_seto, prob_sep_vir, prob_pet_vir, prob_sep_ver, prob_pet_ver;
    int s_cnt, vi_cnt, ve_cnt;
    Scanner scan;

    public static void main(String[] args) throws IOException {
        IrisGridGrouping igg = new IrisGridGrouping();
        igg.controll();
    }

    // 初期化
    // 確率表の作成
    void buildGrid() throws IOException {
        //scan = new Scanner(System.in); //file
        scan = new Scanner(new File("C:\\Users\\省吾\\Documents\\NetBeansProjects\\softcomputing\\src\\grid\\iris.txt"));

        for (int i = 0; i < 5; i++) {
            System.out.println(scan.next()); // ラベル捨て
        }

        // 件数
        n = scan.nextInt();
        swmax = 0;
        slmax = 0;
        plmax = 0;
        pwmax = 0;

        // 150件
        Iris[] irises = new Iris[n];

        for (int i = 0; i < n; i++) {
            scan.nextInt(); // 行番号データ 捨てる
            double sl = scan.nextDouble();
            double sw = scan.nextDouble();
            double pl = scan.nextDouble();
            double pw = scan.nextDouble();
            String cate = scan.next();

            slmax = Math.max((int) Math.ceil(sl), slmax);
            swmax = Math.max((int) Math.ceil(sw), swmax);
            plmax = Math.max((int) Math.ceil(pl), plmax);
            pwmax = Math.max((int) Math.ceil(pw), pwmax);

            irises[i] = new Iris(sl, sw, pl, pw, cate);
        }

        // 間隔を0.5ずつにする　よって、最大値の2倍の範囲を取る2倍
        // 入ってきた値も2倍し、四捨五入して配列に格納する
        slmax = slmax * 2 + 1;
        swmax = swmax * 2 + 1;
        plmax = plmax * 2 + 1;
        pwmax = pwmax * 2 + 1;

        // マッピングテーブル
        // 直接確率を記入した方が早いかも？
        // それとも、後で確率を計算する時のためのリソースとして扱う？
        s_setosa = new int[slmax][swmax];
        s_versicolor = new int[slmax][swmax];
        s_virginica = new int[slmax][swmax];

        p_setosa = new int[plmax][pwmax];
        p_versicolor = new int[plmax][pwmax];
        p_virginica = new int[plmax][pwmax];

        s_cnt = 0;
        ve_cnt = 0;
        vi_cnt = 0;

        for (Iris i : irises) {
            if (i.getCategory().equals("setosa")) {
                s_setosa = incrementGridSepal(i, s_setosa);
                p_setosa = incrementGridPetal(i, p_setosa);
                s_cnt++;

            } else if (i.getCategory().equals("versicolor")) {
                s_versicolor = incrementGridSepal(i, s_versicolor);
                p_versicolor = incrementGridPetal(i, p_versicolor);
                ve_cnt++;
            } else {
                // virginica
                s_virginica = incrementGridSepal(i, s_virginica);
                p_virginica = incrementGridPetal(i, p_virginica);
                vi_cnt++;
            }
        }

        prob_sep_seto = new double[slmax][swmax];
        prob_pet_seto = new double[plmax][pwmax];
        prob_sep_vir = new double[slmax][swmax];
        prob_pet_vir = new double[plmax][pwmax];
        prob_sep_ver = new double[slmax][swmax];
        prob_pet_ver = new double[plmax][pwmax];

        for (int i = 0; i < slmax; i++) {
            for (int j = 0; j < swmax; j++) {
                prob_sep_seto[i][j] = s_setosa[i][j] / s_cnt;
                prob_sep_vir[i][j] = s_virginica[i][j] / vi_cnt;
                prob_sep_ver[i][j] = s_versicolor[i][j] / ve_cnt;
            }
        }

        for (int i = 0; i < plmax; i++) {
            for (int j = 0; j < pwmax; j++) {
                prob_pet_seto[i][j] = p_setosa[i][j] / s_cnt;
                prob_pet_vir[i][j] = p_virginica[i][j] / vi_cnt;
                prob_pet_ver[i][j] = p_versicolor[i][j] / ve_cnt;
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
        String setoSepal = "setosa_Sepal.txt";
        String setoPetal = "setosa_Petal.txt";
        String verSepal = "versicolor_Sepal.txt";
        String verPetal = "versicolor_Petal.txt";
        String virSepal = "virginica_Sepal.txt";
        String virPetal = "virginica_Petal.txt";

        FileWriter fw = null;
        try {
            fw = new FileWriter(new File(setoSepal));

            fwriter(fw, 50, 0.5, 0.5, s_cnt, s_setosa);
            fw = new FileWriter(new File(setoPetal));
            fwriter(fw, 50, 0.5, 0.5, s_cnt, p_setosa);

            fw = new FileWriter(new File(verSepal));
            fwriter(fw, 50, 0.5, 0.5, ve_cnt, s_versicolor);
            fw = new FileWriter(new File(verPetal));
            fwriter(fw, 50, 0.5, 0.5, ve_cnt, p_versicolor);

            fw = new FileWriter(new File(virSepal));
            fwriter(fw, 50, 0.5, 0.5, vi_cnt, s_virginica);
            fw = new FileWriter(new File(virPetal));
            fwriter(fw, 50, 0.5, 0.5, vi_cnt, p_virginica);
        } catch (IOException ex) {
            Logger.getLogger(IrisGridGrouping.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    // 入力データを分類
    void grouping() {
        try {
            // ファイルを読み込んで、判定を出力しつづける
            // 合致していたかの判断も○×で出力

            scan = new Scanner(new File("C:\\Users\\省吾\\Documents\\NetBeansProjects\\softcomputing\\src\\grid\\testdata"));
            n = scan.nextInt();
            double cnt=0;

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
                }else{
                    System.out.println(" x");
                }

            }
            
            System.out.println("正答率:"+(cnt/n));

        } catch (FileNotFoundException ex) {
            Logger.getLogger(IrisGridGrouping.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // 入力データから、どの種類なのかを返す
    String decision(Iris iris) {
        int sl2r = (int) (Math.round(iris.getSepal_len()) * 2);
        int sw2r = (int) (Math.round(iris.getSepal_wid()) * 2);
        int pl2r = (int) (Math.round(iris.getPetal_len()) * 2);
        int pw2r = (int) (Math.round(iris.getPetal_wid()) * 2);

        double sep, pet;
        String scate, pcate;

        if (prob_sep_ver[sl2r][sw2r] < prob_sep_vir[sl2r][sw2r]) {
            if (prob_sep_seto[sl2r][sw2r] < prob_sep_vir[sl2r][sw2r]) {
                // virが一番
                sep = prob_sep_vir[sl2r][sw2r];
                scate = "virginica";
            } else {
                // setoが一番
                sep = prob_sep_seto[sl2r][sw2r];
                scate = "setosa";
            }
        } else {
            if (prob_sep_seto[sl2r][sw2r] < prob_sep_ver[sl2r][sw2r]) {
                // verが一番
                sep = prob_sep_ver[sl2r][sw2r];
                scate = "versicolor";
            } else {
                // setoが一番
                sep = prob_sep_seto[sl2r][sw2r];
                scate = "setosa";
            }

        }

        if (prob_pet_ver[pl2r][pw2r] < prob_pet_vir[pl2r][pw2r]) {
            if (prob_pet_seto[pl2r][pw2r] < prob_pet_vir[pl2r][pw2r]) {
                // virが一番
                pet = prob_pet_vir[pl2r][pw2r];
                pcate = "virginica";
            } else {
                // setoが一番
                pet = prob_pet_seto[pl2r][pw2r];
                pcate = "setosa";
            }
        } else {
            if (prob_pet_seto[pl2r][pw2r] < prob_pet_ver[pl2r][pw2r]) {
                // verが一番
                pet = prob_pet_ver[pl2r][pw2r];
                pcate = "versicolor";
            } else {
                // setoが一番
                pet = prob_pet_seto[pl2r][pw2r];
                pcate = "setosa";
            }

        }

        if (sep > pet) {
            return scate;
        } else {
            return pcate;
        }

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

    }

    int[][] incrementGridSepal(Iris i, int[][] grid) {
        int sl2r = (int) Math.round(i.getSepal_len() * 2);
        int sw2r = (int) Math.round(i.getSepal_wid() * 2);

        // 指定のグリッドへ個数を追加
        grid[sl2r][sw2r]++;
        //System.out.println(Arrays.deepToString(grid));
        return grid;
    }

    int[][] incrementGridPetal(Iris i, int[][] grid) {
        int pl2r = (int) (Math.round(i.getPetal_len()) * 2);
        int pw2r = (int) (Math.round(i.getPetal_wid()) * 2);

        // 指定のグリッドへ個数を追加
        grid[pl2r][pw2r]++;
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

    private String category;
    private double sepal_len;
    private double sepal_wid;
    private double Petal_len;
    private double Petal_wid;

    public Iris(double sl, double sw, double pl, double pw, String category) {
        this.category = category;
        this.sepal_len = sl;
        this.sepal_wid = sw;
        this.Petal_len = pl;
        this.Petal_wid = pw;
    }

    public String getCategory() {
        return category;
    }

    public double getSepal_len() {
        return sepal_len;
    }

    public double getSepal_wid() {
        return sepal_wid;
    }

    public double getPetal_len() {
        return Petal_len;
    }

    public double getPetal_wid() {
        return Petal_wid;
    }

}
