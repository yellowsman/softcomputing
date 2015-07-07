/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grid;

import java.util.Scanner;

/**
 *
 * @author Yellow
 */
public class Main {

    public static void main(String[] args) {

    }

    void grid() {
        Scanner scan = new Scanner(System.in); //file

        int n = scan.nextInt();
        int max = 0;
        int min = 9999;
        // 150件
        for (int i = 0; i < n; i++) {
            String name = scan.next(); // 名前
            int d = scan.nextInt(); // 萼片や花びら
            max = Math.max(d, max);
            min = Math.min(d, min);
            // 分類処理
        }

        int size = max - min;
        double term = size / 0.5;
        int[][] grid = new int[size][size];

    }
    
    Iris[] readFile(Scanner scan,int n){
        Iris[] irises = new Iris[n];
        
        return irises;
    }
}

class Iris {

    String category;
    double gaku_hei;
    double gaku_wid;
    double hana_hei;
    double hana_wid;

    public Iris(String category, double gaku_hei, double gaku_wid, double hana_hei, double hana_wid) {
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
