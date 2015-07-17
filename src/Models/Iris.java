/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

/**
 *
 * @author g15002se
 */
public class Iris {

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
