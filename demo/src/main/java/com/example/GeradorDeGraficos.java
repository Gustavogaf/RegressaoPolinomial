package com.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import javax.swing.*;
import java.awt.Color;
import java.util.List;
import java.util.Map;

/**
 * Classe responsável por criar e exibir gráficos usando a biblioteca
 * JFreeChart.
 * Estende JFrame para exibir os gráficos em janelas separadas.
 */
public class GeradorDeGraficos extends JFrame {

    /**
     * Construtor para a classe GeradorDeGraficos.
     * 
     * @param titulo O título da janela do gráfico.
     */
    public GeradorDeGraficos(String titulo) {
        super(titulo);
    }

    /**
     * Cria e exibe um gráfico de dispersão com a curva de regressão.
     * Os dados originais são plotados como pontos e a curva de regressão como uma
     * linha.
     *
     * @param tituloGrafico  Título principal do gráfico.
     * @param rotuloEixoX    Rótulo para o eixo X.
     * @param rotuloEixoY    Rótulo para o eixo Y.
     * @param dadosOriginais Dados originais a serem plotados como pontos.
     * @param dadosPreditos  Dados preditos pela regressão para formar a curva.
     * @param grau           Polinomial do grau utilizado na regressão.
     */
    public void criarGraficoRegressao(String tituloGrafico, String rotuloEixoX, String rotuloEixoY,
            List<double[]> dadosOriginais, List<double[]> dadosPreditos, int grau) {
        XYSeriesCollection conjuntoDeDados = new XYSeriesCollection();

        // Adicionar os pontos originais
        XYSeries serieOriginal = new XYSeries("Dados Originais");
        for (double[] ponto : dadosOriginais) {
            serieOriginal.add(ponto[0], ponto[1]);
        }
        conjuntoDeDados.addSeries(serieOriginal);

        XYSeries serieRegressao = new XYSeries("Regressão Polinomial (Grau " + grau + ")");
        for (double[] ponto : dadosPreditos) {
            serieRegressao.add(ponto[0], ponto[1]);
        }
        conjuntoDeDados.addSeries(serieRegressao);

        // Cria o gráfico XY de linha
        JFreeChart grafico = ChartFactory.createXYLineChart(
                tituloGrafico,
                rotuloEixoX,
                rotuloEixoY,
                conjuntoDeDados,
                PlotOrientation.VERTICAL,
                true, // Incluir legenda
                true, // Incluir tooltips
                false // Não incluir URLs
        );

        // Personalizar o renderer para os dados originais e a linha de regressão
        XYPlot plot = grafico.getXYPlot();
        XYLineAndShapeRenderer renderizador = new XYLineAndShapeRenderer();

        // Para a série de Dados Originais (índice 0): exibir apenas formas (pontos)
        renderizador.setSeriesLinesVisible(0, false);
        renderizador.setSeriesShapesVisible(0, true);
        renderizador.setSeriesPaint(0, Color.BLUE);

        // Para a série de Regressão Polinomial (índice 1): exibir apenas linhas
        renderizador.setSeriesLinesVisible(1, true);
        renderizador.setSeriesShapesVisible(1, false);
        renderizador.setSeriesPaint(1, Color.RED);

        plot.setRenderer(renderizador);

        ChartPanel painelDoGrafico = new ChartPanel(grafico);
        painelDoGrafico.setPreferredSize(new java.awt.Dimension(800, 600));
        setContentPane(painelDoGrafico);
        this.pack();
        RefineryUtilities.centerFrameOnScreen(this);
        this.setVisible(true);
    }

    /**
     * Cria e exibe um gráfico da evolução do R-quadrado Ajustado por grau do
     * polinômio.
     *
     * @param tituloGrafico   Título principal do gráfico.
     * @param rotuloEixoX     Rótulo para o eixo X.
     * @param rotuloEixoY     Rótulo para o eixo Y.
     * @param valoresRSquared Mapa onde a chave é o grau do polinômio e o valor é o
     *                        R² Ajustado correspondente.
     */
    public void criarGraficoR2(String tituloGrafico, String rotuloEixoX, String rotuloEixoY,
            Map<Integer, Double> valoresRSquared) {
        XYSeriesCollection conjuntoDeDados = new XYSeriesCollection();
        XYSeries serieR2 = new XYSeries("Coeficiente de Determinação (R² Ajustado)");

        for (Map.Entry<Integer, Double> entrada : valoresRSquared.entrySet()) {
            serieR2.add(entrada.getKey(), entrada.getValue());
        }
        conjuntoDeDados.addSeries(serieR2);

        JFreeChart grafico = ChartFactory.createXYLineChart(
                tituloGrafico,
                rotuloEixoX,
                rotuloEixoY,
                conjuntoDeDados,
                PlotOrientation.VERTICAL,
                true, // Incluir legenda
                true, // Incluir tooltips
                false // Não incluir URLs
        );

        // Personalização do renderer para o gráfico R2
        XYPlot plot = grafico.getXYPlot();
        XYLineAndShapeRenderer renderizador = new XYLineAndShapeRenderer();
        renderizador.setSeriesLinesVisible(0, true);
        renderizador.setSeriesShapesVisible(0, true);
        renderizador.setSeriesPaint(0, Color.GREEN);
        plot.setRenderer(renderizador);

        ChartPanel painelDoGrafico = new ChartPanel(grafico);
        painelDoGrafico.setPreferredSize(new java.awt.Dimension(800, 600));
        setContentPane(painelDoGrafico);
        this.pack();
        RefineryUtilities.centerFrameOnScreen(this);
        this.setVisible(true);
    }
}