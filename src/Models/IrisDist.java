/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

/**
 *
 * @author Yellow
 */
public class IrisDist {

    private Iris iris;
    private Double dist;

    public IrisDist(Iris iris, Double dist) {
        this.iris = iris;
        this.dist = dist;
    }

    public Iris getIris() {
        return iris;
    }

    public void setIris(Iris iris) {
        this.iris = iris;
    }

    public Double getDist() {
        return dist;
    }

    public void setDist(Double dist) {
        this.dist = dist;
    }
}
