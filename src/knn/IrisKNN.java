/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package knn;

import Models.Iris;
import Models.IrisDist;
import Models.IrisDistComp;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author g15002se TODO: - テストデータ、学習データの調整
 */
public class IrisKNN {

    int n, k;
    Scanner scan;
    ArrayList<Iris> irislist;
    ArrayList<IrisDist> distlist;
    Iris[] irises;

    final String IRIS_DATA = "iris.txt";
    final String TEST_DATA = "testdata";

    enum IrisNames {

        setosa,
        versicolor,
        virginica
    }

    public static void main(String[] args) {
        IrisKNN main = new IrisKNN();
        main.buildArea();
        main.input();
        main.knn();
    }

    public void buildArea() {
        try {
            scan = new Scanner(new File(IRIS_DATA));
//
//            for (int i = 0; i < 5; i++) {
//                System.out.println(scan.next()); // ラベル捨て
//            }

            // 件数
            n = scan.nextInt();

    
            irislist = new ArrayList<Iris>();

            for (int i = 0; i < n; i++) {
                scan.nextInt(); // 行番号データ 捨てる
                double sl = scan.nextDouble();
                double sw = scan.nextDouble();
                double pl = scan.nextDouble();
                double pw = scan.nextDouble();
                String cate = scan.next();

                irislist.add(new Iris(sl, sw, pl, pw, cate));
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(IrisKNN.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void input() {
        try {
            scan = new Scanner(new File(TEST_DATA));

            int dn = scan.nextInt();
            k = scan.nextInt();
            irises = new Iris[dn];
            for (int i = 0; i < irises.length; i++) {
                scan.nextInt();
                irises[i] = new Iris(scan.nextDouble(), scan.nextDouble(), scan.nextDouble(), scan.nextDouble(), scan.next());
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(IrisKNN.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void knn() {
        int c = 0;
        String ans = "x";
        for (Iris i : irises) {
            String result = grouping(i);

            if (result.equals(i.getCategory())) {
                ans = "o";
                c++;
            }
            System.out.println(result + ":" + ans);
        }
        System.out.println("正答率:" + (c / irises.length));
    }

    public String grouping(Iris i) {
        Iris[] rank = search(i, k, irislist);
        int cntS, cntVi, cntVe;
        cntS = cntVi = cntVe = 0;

        HashMap<String, Integer> count = new HashMap<String, Integer>();
        count.put(IrisNames.setosa.toString(), 0);
        count.put(IrisNames.virginica.toString(), 0);
        count.put(IrisNames.versicolor.toString(), 0);

        for (Iris r : rank) {
            String c = r.getCategory();
            if (c.equals(IrisNames.setosa.toString())) {
                cntS++;
            } else if (c.equals(IrisNames.versicolor.toString())) {
                cntVi++;
            } else {
                // virginica
                cntVe++;
            }
        }

        if (cntS < cntVi) {
            if (cntVe < cntVi) {
                return IrisNames.virginica.toString();
            }
        } else if (cntVe < cntS) {
            return IrisNames.setosa.toString();
        } else if (cntVe > cntS) {
            return IrisNames.versicolor.toString();
        }

        return "No Answer";
    }

    public Iris[] search(Iris i, int k, ArrayList<Iris> area) {
        // 距離計算
        // 上位k個だけ返す
        distlist = new ArrayList<IrisDist>();
        for (Iris v : area) {
            double dist = dist(i, v);
            distlist.add(new IrisDist(v, dist)); // iからみたvの距離を保存
        }

        Iris[] arr = new Iris[k];
        Collections.sort(distlist, new IrisDistComp());
        for (int j = 0; j < k; j++) {
            arr[j] = distlist.get(distlist.size() - 1 - j).getIris();
        }

        return arr;
    }

    public double dist(Iris a, Iris b) {
        double pl = b.getPetalLen() - a.getPetalLen();
        double pw = b.getPetalWid() - a.getPetalWid();
        double sl = b.getSepalLen() - a.getSepalLen();
        double sw = b.getSepalWid() - a.getSepalWid();

        return Math.sqrt(Math.pow(pl, 2) + Math.pow(pw, 2) + Math.pow(sl, 2) + Math.pow(sw, 2));
    }

}
