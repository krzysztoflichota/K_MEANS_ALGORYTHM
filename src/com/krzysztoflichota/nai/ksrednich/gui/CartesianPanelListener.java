package com.krzysztoflichota.nai.ksrednich.gui;

import com.krzysztoflichota.nai.ksrednich.geom.ClassifiedPoint;
import com.krzysztoflichota.nai.ksrednich.geom.RepresentatingPoint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.List;

/**
 * Created by Krzysztof Lichota on 2015-04-26.
 * krzysztoflichota.com
 */
public class CartesianPanelListener extends MouseAdapter {
    private List<ClassifiedPoint> cpoints;
    private List<RepresentatingPoint> rpoints;
    private CoordinateSystemComponent cartesian;
    private JLabel numberOfPoints, numberOfReps;

    public static final double ZOOM = 0.2;
    public static final double MIN_ZOOM = 5.0;
    public static final double MAX_ZOOM = 4900.0;

    public CartesianPanelListener(List<ClassifiedPoint> cpoints, List<RepresentatingPoint> rpoints, CoordinateSystemComponent cartesian, JLabel numberOfPoints, JLabel numberOfReps) {
        this.cpoints = cpoints;
        this.rpoints = rpoints;
        this.cartesian = cartesian;
        this.numberOfPoints = numberOfPoints;
        this.numberOfReps = numberOfReps;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        double x = cartesian.getPixelsInX(e.getX());
        double y = cartesian.getPixelsInY(e.getY());

        switch(e.getButton()){
            case MouseEvent.BUTTON1:
                cpoints.add(new ClassifiedPoint(x, y));
                numberOfPoints.setText("Liczba punktów: " + cpoints.size());
                break;
            case MouseEvent.BUTTON3:
                rpoints.add(new RepresentatingPoint(x, y));
                numberOfReps.setText("Liczba reprezentantów: " + rpoints.size());
        }

        cartesian.repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        cartesian.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
    }

    @Override
    public void mouseExited(MouseEvent e) {
        cartesian.setCursor(Cursor.getDefaultCursor());
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int turns = e.getWheelRotation();

        if(turns > 0 && cartesian.GAP < MAX_ZOOM){
            cartesian.zoomIn(ZOOM);
        }
        else if(turns < 0 && cartesian.GAP > MIN_ZOOM){
            cartesian.zoomOut(ZOOM);
        }

        cartesian.repaint();
    }

}
