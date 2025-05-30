package com.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import javax.swing.*;
import java.util.List;
import java.util.Map;

public class ChartGenerator extends JFrame {

    public ChartGenerator(String title) {
        super(title);
    }

    /**
     * Cria e exibe um gráfico de dispersão com a curva de regressão.
     * @param chartTitle Título do gráfico
     * @param xAxisLabel Rótulo do eixo X
     * @param yAxisLabel Rótulo do eixo Y
     * @param originalData Dados originais
     * @param predictedData Dados preditos pela regressão
     * @param degree Grau do polinômio
     */
    public void createRegressionChart(String chartTitle, String xAxisLabel, String yAxisLabel,
                                      List<double[]> originalData, List<double[]> predictedData, int degree) {
        XYSeriesCollection dataset = new XYSeriesCollection();

        // Adicionar os pontos originais
        XYSeries originalSeries = new XYSeries("Dados Originais");
        for (double[] point : originalData) {
            originalSeries.add(point[0], point[1]);
        }
        dataset.addSeries(originalSeries);

        // Adicionar a curva de regressão
        XYSeries regressionSeries = new XYSeries("Regressão Polinomial (Grau " + degree + ")");
        for (double[] point : predictedData) {
            regressionSeries.add(point[0], point[1]);
        }
        dataset.addSeries(regressionSeries);

        JFreeChart chart = ChartFactory.createXYLineChart(
                chartTitle,
                xAxisLabel,
                yAxisLabel,
                dataset,
                PlotOrientation.VERTICAL,
                true, // include legend
                true, // tooltips
                false // urls
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        setContentPane(chartPanel);
        this.pack();
        RefineryUtilities.centerFrameOnScreen(this);
        this.setVisible(true);
    }

    /**
     * Cria e exibe um gráfico da evolução do R-quadrado por grau.
     * @param chartTitle Título do gráfico
     * @param xAxisLabel Rótulo do eixo X
     * @param yAxisLabel Rótulo do eixo Y
     * @param rSquaredValues Mapa do grau para o valor de R-quadrado
     */
    public void createR2Chart(String chartTitle, String xAxisLabel, String yAxisLabel,
                              Map<Integer, Double> rSquaredValues) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries r2Series = new XYSeries("Coeficiente de Determinação (R²)");

        for (Map.Entry<Integer, Double> entry : rSquaredValues.entrySet()) {
            r2Series.add(entry.getKey(), entry.getValue());
        }
        dataset.addSeries(r2Series);

        JFreeChart chart = ChartFactory.createXYLineChart(
                chartTitle,
                xAxisLabel,
                yAxisLabel,
                dataset,
                PlotOrientation.VERTICAL,
                true, // include legend
                true, // tooltips
                false // urls
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        setContentPane(chartPanel);
        this.pack();
        RefineryUtilities.centerFrameOnScreen(this);
        this.setVisible(true);
    }
}
