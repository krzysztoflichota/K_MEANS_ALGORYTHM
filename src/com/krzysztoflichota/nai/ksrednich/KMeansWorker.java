package com.krzysztoflichota.nai.ksrednich;

import com.krzysztoflichota.nai.ksrednich.geom.ClassifiedPoint;
import com.krzysztoflichota.nai.ksrednich.geom.RepresentatingPoint;
import com.krzysztoflichota.nai.ksrednich.gui.CoordinateSystemComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;

/**
 * Created by Krzysztof Lichota on 2015-04-26.
 * krzysztoflichota.com
 */
public class KMeansWorker implements Runnable{
    private CoordinateSystemComponent cartesian;
    private JLabel iterations;
    private List<ClassifiedPoint> cpoints;
    private List<RepresentatingPoint> rpoints;
    public static final int SLEEP = 200;

    public KMeansWorker(CoordinateSystemComponent cartesian, List<ClassifiedPoint> cpoints, List<RepresentatingPoint> rpoints, JLabel iterations) {
        this.cartesian = cartesian;
        this.cpoints = cpoints;
        this.rpoints = rpoints;
        this.iterations = iterations;
    }

    @Override
    public void run() {

        int i = 0;
        boolean toContinue = true;
        while(toContinue){
            try {
                Thread.sleep(SLEEP);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            toContinue = classifyPoints();
            moveRepresentants();
            i++;
            final int finalI = i;
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    cartesian.repaint();
                    iterations.setText("Liczba iteracji: " + finalI);
                }
            });
        }
    }

    private boolean classifyPoints(){
        boolean moved = false;

        synchronized(cpoints) {
            for (ClassifiedPoint p : cpoints) {
                RepresentatingPoint rep = getClosestRepresentant(p);
                if (rep == null) return false;
                if (rep != p.getRepresentant()) moved = true;
                p.setRepresentant(rep);
                rep.addPointToAverage(p);
            }
        }

        return moved;
    }

    private RepresentatingPoint getClosestRepresentant(ClassifiedPoint point){
        double minDist = Double.MAX_VALUE;
        RepresentatingPoint rep = null;

        synchronized (rpoints) {
            for (RepresentatingPoint p : rpoints) {
                double dist = Point2D.distance(point.getX(), point.getY(), p.getX(), p.getY());
                if (dist < minDist) {
                    minDist = dist;
                    rep = p;
                }
            }
        }

        return rep;
    }

    private void moveRepresentants(){
        for(RepresentatingPoint p : rpoints) p.move();
    }
}
