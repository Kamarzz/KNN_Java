package com.komarzz.KNN;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.statistics.HistogramDataset;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.NumericColumn;
import tech.tablesaw.api.Table;

import javax.swing.*;
import java.awt.*;

public class VisualizationUtility {

    private VisualizationUtility(){}


    public static void drawChart(Table table) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (int i = 0; i < table.columnCount(); i++) {
            if (table.column(i) instanceof DoubleColumn) {
                DoubleColumn column = (DoubleColumn) table.column(i);
                for (int j = 0; j < column.size(); j++) {
                    dataset.addValue(column.get(j), column.name(), Integer.toString(j));
                }
            }
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "Chart",
                "Category",
                "Score",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new Dimension(560, 367));
        JFrame frame = new JFrame();
        frame.getContentPane().add(chartPanel);
        frame.pack();
        frame.setVisible(true);
    }

    public static void printTable(Table table){
        System.out.println(table.print());
    }

    public static void showHistograms(Table data) {
        for (int i = 0; i < data.columnCount(); i++) {
            if (data.column(i) instanceof NumericColumn) {
                double[] arr = new double[data.column(i).size()];

                for (int j = 0; j < arr.length; j++) {
                    arr[j] = ((NumericColumn<?>) data.column(i)).getDouble(j);
                }

                VisualizationUtility.showHistogram(arr, data.column(i).name());
            }
        }
    }

    private static void showHistogram(double[] data, String title) {

        int binCount = (int) (1 + 3.322 * Math.log10(data.length));

        HistogramDataset dataset = new HistogramDataset();
        dataset.addSeries(title, data, binCount);

        JFreeChart histogram = ChartFactory.createHistogram(
                title,
                null,
                null,
                dataset,
                PlotOrientation.VERTICAL,
                false,
                true,
                false
        );

        JFrame frame = new JFrame();
        frame.setTitle(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        ChartPanel chartPanel = new ChartPanel(histogram);
        frame.add(chartPanel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }

}

