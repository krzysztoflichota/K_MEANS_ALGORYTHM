package com.krzysztoflichota.nai.ksrednich.gui;

import com.krzysztoflichota.nai.ksrednich.geom.ClassifiedPoint;
import com.krzysztoflichota.nai.ksrednich.geom.RepresentatingPoint;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;


/**
 * Created by Krzysztof Lichota on 2015-03-20.
 * krzysztoflichota.com
 */
public class CoordinateSystemComponent extends JComponent {

    public int WIDTH = 1300;
    public int HEIGHT = 500;
    public double GAP = 20;
    public int OFFSET_X = 0;
    public int OFFSET_Y = 0;
    public final Color backgroundColor = Color.WHITE;
    public static final int POINT_SIZE = 10;

    private List<ClassifiedPoint> cpoints;
    private List<RepresentatingPoint> rpoints;

    public CoordinateSystemComponent(List<ClassifiedPoint> cpoints, List<RepresentatingPoint> rpoints){
        this.cpoints = cpoints;
        this.rpoints = rpoints;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics2D = (Graphics2D) g;

        fixSize();
        paintBackground(graphics2D);
        drawNet(graphics2D);
        drawPoints(graphics2D);
        drawLines(graphics2D);
    }

    private void drawLines(Graphics2D graphics2D) {
        Color presentColor = graphics2D.getColor();
        for(ClassifiedPoint p : cpoints){
            if(p.isClassified()){
                graphics2D.setColor(p.getColor());
                graphics2D.drawLine((getXInPixels(p.getX())), (getYInPixels(p.getY())),
                        (getXInPixels(p.getRepresentant().getX())), (getYInPixels(p.getRepresentant().getY())));
            }
        }
        graphics2D.setColor(presentColor);
    }


    private void drawPoints(Graphics2D graphics2D) {
        for(ClassifiedPoint p : cpoints) drawCirclePoint(graphics2D, p);
        for(RepresentatingPoint p : rpoints) drawRectPoint(graphics2D, p);
    }


    private void drawCirclePoint(Graphics2D graphics2D, ClassifiedPoint point) {
        Color presentColor = graphics2D.getColor();
        graphics2D.setColor(point.getColor());
        graphics2D.fillOval((int) (getXInPixels(point.getX()) - POINT_SIZE / 2) + OFFSET_X, (int) (getYInPixels(point.getY()) - POINT_SIZE / 2) + OFFSET_Y, POINT_SIZE, POINT_SIZE);
        graphics2D.setColor(presentColor);
    }

    private void drawRectPoint(Graphics2D graphics2D, RepresentatingPoint point) {
        Color presentColor = graphics2D.getColor();
        graphics2D.setColor(point.getColor());
        graphics2D.fillRect((int) (getXInPixels(point.getX()) - POINT_SIZE / 2) + OFFSET_X, (int) (getYInPixels(point.getY()) - POINT_SIZE / 2) + OFFSET_Y, POINT_SIZE, POINT_SIZE);
        graphics2D.setColor(presentColor);
    }

    private void paintBackground(Graphics2D g){
        Color presentColor = g.getColor();
        g.setColor(backgroundColor);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.setColor(presentColor);
    }

    private void drawNet(Graphics2D g){
        Color presentColor = g.getColor();
        g.setColor(Color.BLACK);

        //Od środka w lewo pionowe
        for(int i = 0; getCenterWidth() - i >= 0; i += GAP){
            g.drawLine(getCenterWidth() - i, 0, getCenterWidth() - i, HEIGHT);
        }

        //Od środka w prawo pionowe
        for(int i = 0; getCenterWidth() + i <= WIDTH; i += GAP){
            g.drawLine(getCenterWidth() + i, 0, getCenterWidth() + i, HEIGHT);
        }

        //Od środka w górę poziome
        for(int i = 0; getCenterHeight() - i >= 0; i += GAP){
            g.drawLine(0, getCenterHeight() - i, WIDTH, getCenterHeight() - i);
        }

        //Od środka w dół poziome
        for(int i = 0; getCenterHeight() + i <= HEIGHT; i += GAP){
            g.drawLine(0, getCenterHeight() + i, WIDTH, getCenterHeight() + i);
        }

        Stroke presentStroke = g.getStroke();
        g.setStroke(new BasicStroke(3));
        g.drawLine(getCenterWidth(), 0, getCenterWidth(), HEIGHT);
        g.drawLine(0, getCenterHeight(), WIDTH, getCenterHeight());

        g.setStroke(presentStroke);
        g.setColor(presentColor);
    }

    private int getCenterWidth(){
        return WIDTH/2 + OFFSET_X;
    }

    private int getCenterHeight(){
        return HEIGHT/2 + OFFSET_Y;
    }

    public int getXInPixels(double x){
        return (int)Math.round(x*GAP + WIDTH / 2);
    }

    public int getYInPixels(double y){
        return (int)Math.round(y*GAP + HEIGHT / 2);
    }

    public double getPixelsInX(double x){
        return (x - WIDTH / 2)/GAP;
    }

    public double getPixelsInY(double y){
        return (y - HEIGHT / 2)/GAP;
    }

    private void fixSize(){
        WIDTH = getWidth();
        HEIGHT = getHeight();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WIDTH, HEIGHT);
    }

    public void zoomIn(double zoom){
        GAP *= 1 + zoom;
        OFFSET_X *= 1 + zoom;
        OFFSET_Y *= 1 + zoom;
    }

    public void zoomOut(double zoom){
        GAP *= 1 - zoom;
        OFFSET_X *= 1 - zoom;
        OFFSET_Y *= 1 - zoom;
    }


    public Point2D getPointAtPixels(int xPixels, int yPixels, List<? extends Point2D> points){
        double offset = POINT_SIZE / 2;
        for(Point2D point : points){
            double x = getXInPixels(point.getX());
            double y = getYInPixels(point.getY());

            if(xPixels >= x - offset && xPixels <= x + offset && yPixels >= y - offset && yPixels <= y + offset) return point;
        }

        return null;
    }
}
