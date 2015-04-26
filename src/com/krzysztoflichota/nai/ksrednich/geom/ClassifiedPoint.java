package com.krzysztoflichota.nai.ksrednich.geom;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Random;

/**
 * Created by Krzysztof Lichota on 2015-04-26.
 * krzysztoflichota.com
 */
public class ClassifiedPoint extends Point2D.Double {
    private RepresentatingPoint representant;

    public ClassifiedPoint(double x, double y) {
        super(x, y);
    }

    public RepresentatingPoint getRepresentant() {
        return representant;
    }

    public void setRepresentant(RepresentatingPoint representant) {
        this.representant = representant;
    }

    public boolean isClassified(){
        return !(representant == null);
    }

    public Color getColor(){
        if(isClassified()) return representant.getColor();

        return Color.GRAY;
    }

    public static ClassifiedPoint getRandomInstance(double limitX, double limitY){
        double x = Math.random()*limitX;
        double y = Math.random()*limitY;

        Random r = new Random();
        x *= r.nextInt() % 2 == 0 ? -1 : 1;
        y *=r.nextInt() % 2 == 0 ? -1 : 1;

        return new ClassifiedPoint(x, y);
    }
}
