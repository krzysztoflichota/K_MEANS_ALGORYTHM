package com.krzysztoflichota.nai.ksrednich.geom;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Random;

/**
 * Created by Krzysztof Lichota on 2015-04-26.
 * krzysztoflichota.com
 */
public class RepresentatingPoint extends Point2D.Double {
    private Color color;
    private double xAverage, yAverage;
    private int denomOfAverage;

    public RepresentatingPoint (double x, double y){
        this(x, y, getRandomColor());
    }

    public RepresentatingPoint(double x, double y, Color color){
        super(x, y);
        this.color = color;
        xAverage = x;
        yAverage = y;
        denomOfAverage = 1;
    }

    public Color getColor() {
        return color;
    }

    public static RepresentatingPoint getRandomInstance(double limitX, double limitY){
        double x = Math.random()*limitX;
        double y = Math.random()*limitY;

        Random r = new Random();
        x *= r.nextInt() % 2 == 0 ? -1 : 1;
        y *=r.nextInt() % 2 == 0 ? -1 : 1;

        Color color = getRandomColor();

        return new RepresentatingPoint(x, y, color);
    }

    public void addPointToAverage(Point2D point){
        xAverage += point.getX();
        yAverage += point.getY();
        denomOfAverage++;
    }

    public void move(){
        double newX = xAverage/denomOfAverage;
        double newY = yAverage/denomOfAverage;
        setLocation(newX, newY);

        xAverage = newX;
        yAverage = newY;
        denomOfAverage = 1;
    }

    private static Color getRandomColor(){
        return new Color((int)(Math.random()*255), (int)(Math.random()*255), (int)(Math.random()*255));
    }
}
