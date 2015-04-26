package com.krzysztoflichota.nai.ksrednich;

import com.krzysztoflichota.nai.ksrednich.geom.ClassifiedPoint;
import com.krzysztoflichota.nai.ksrednich.geom.RepresentatingPoint;
import com.krzysztoflichota.nai.ksrednich.gui.CartesianPanelListener;
import com.krzysztoflichota.nai.ksrednich.gui.CoordinateSystemComponent;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Krzysztof Lichota on 2015-04-26.
 * krzysztoflichota.com
 */
public class KMeans extends JFrame {

    public static void main(String ...args){
        KMeans kMeans = new KMeans();
        kMeans.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private CoordinateSystemComponent cartesian;
    private List<ClassifiedPoint> cpoints;
    private List<RepresentatingPoint> rpoints;
    private KMeansWorker worker;

    private JLabel iterations, numberOfPoints, numberOfReps;

    public KMeans(){
        super("Algorytm K-Srednich");

        cpoints = Collections.synchronizedList(new LinkedList<ClassifiedPoint>());
        rpoints = Collections.synchronizedList(new LinkedList<RepresentatingPoint>());
        cartesian = new CoordinateSystemComponent(cpoints, rpoints);
        iterations = new JLabel("Liczba iteracji: 0");
        numberOfPoints = new JLabel("Liczba punktów: 0");
        numberOfReps = new JLabel("Liczba reprezentantów: 0");
        worker = new KMeansWorker(cartesian, cpoints, rpoints, iterations);
        generateRandomPoints(10000);
        generateRandomRepresentants(20);

        initGUI();
        pack();
        setVisible(true);
    }

    private void initGUI() {
        CartesianPanelListener listener = new CartesianPanelListener(cpoints, rpoints, cartesian, numberOfPoints, numberOfReps);
        cartesian.addMouseListener(listener);
        cartesian.addMouseWheelListener(listener);
        add(cartesian, BorderLayout.CENTER);
        add(createInfoPanel(), BorderLayout.NORTH);
        add(createControlPanel(), BorderLayout.SOUTH);
    }


    public void generateRandomPoints(int n){
        double limitX = cartesian.getPixelsInX(cartesian.WIDTH);
        double limitY = cartesian.getPixelsInY(cartesian.HEIGHT);
        for(int i = 0; i < n; i++) cpoints.add(ClassifiedPoint.getRandomInstance(limitX, limitY));
        numberOfPoints.setText("Liczba punktów: " + cpoints.size());
    }

    public void generateRandomRepresentants(int n){
        double limitX = cartesian.getPixelsInX(cartesian.WIDTH);
        double limitY = cartesian.getPixelsInY(cartesian.HEIGHT);
        for(int i = 0; i < n; i++) rpoints.add(RepresentatingPoint.getRandomInstance(limitX, limitY));
        numberOfReps.setText("Liczba reprezentantów: " + rpoints.size());
    }

    private JPanel createInfoPanel(){
        JPanel panel = new JPanel(new GridLayout(3, 1));
        panel.add(iterations);
        panel.add(numberOfPoints);
        panel.add(numberOfReps);
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        border = BorderFactory.createTitledBorder(border, "INFO");
        panel.setBorder(border);

        return panel;
    }

    private JPanel createControlPanel(){
        JPanel panel = new JPanel(new GridLayout(3, 1));
        JButton start = new JButton("Start");
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(worker).start();
            }
        });
        panel.add(start);

        JButton generate = new JButton("Generuj 100 punktów");
        generate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateRandomPoints(100);
                cartesian.repaint();
            }
        });
        panel.add(generate);

        JButton clear = new JButton("Wyczyść");
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });
        panel.add(clear);

        Border border = BorderFactory.createLineBorder(Color.BLACK);
        border = BorderFactory.createTitledBorder(border, "PUNKTY");
        panel.setBorder(border);

        return panel;
    }

    private void clear(){
        cpoints.clear();
        rpoints.clear();
        cartesian.repaint();
        numberOfPoints.setText("Liczba punktów: 0");
        numberOfReps.setText("Liczba reprezentantów: 0");
    }
}
