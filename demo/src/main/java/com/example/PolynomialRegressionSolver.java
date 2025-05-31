package com.example;

import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics; // Importar para estatísticas

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

public class PolynomialRegressionSolver {

    /**
     * Realiza a regressão polinomial para um dado grau.
     * Padroniza os valores de t para melhorar a estabilidade numérica.
     * @param data Lista de pontos [t, f(t)]
     * @param degree Grau do polinômio
     * @return Array de coeficientes do polinômio (do termo constante ao maior grau)
     */
    public static double[] fitPolynomial(List<double[]> data, int degree) {
        OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();

        // 1. Calcular média e desvio padrão de t para padronização
        DescriptiveStatistics statsT = new DescriptiveStatistics();
        data.stream().mapToDouble(d -> d[0]).forEach(statsT::addValue);

        double meanT = statsT.getMean();
        double stdDevT = statsT.getStandardDeviation();

        double[] y = new double[data.size()];
        double[][] x = new double[data.size()][degree + 1]; // +1 para o termo constante e potências de t

        for (int i = 0; i < data.size(); i++) {
            double tOriginal = data.get(i)[0];
            y[i] = data.get(i)[1]; // f(t)

            double tStandardized;
            if (stdDevT == 0) { // Evita divisão por zero se todos os t forem iguais
                tStandardized = 0; // Se t é constante, use 0
            } else {
                // Padronizar t: (t - média) / desvio_padrão
                tStandardized = (tOriginal - meanT) / stdDevT;
            }

            x[i][0] = 1.0; // Termo constante
            for (int j = 1; j <= degree; j++) {
                x[i][j] = Math.pow(tStandardized, j); // t_standardized, t_standardized^2, ..., t_standardized^degree
            }
        }

        regression.newSampleData(y, x);
        return regression.estimateRegressionParameters();
    }

    /**
     * Calcula o coeficiente de determinação (R-quadrado).
     * @param data Dados originais [t, f(t)]
     * @param coefficients Coeficientes do polinômio ajustado (baseados em t padronizado)
     * @return Valor de R-quadrado
     */
    public static double calculateR2(List<double[]> data, double[] coefficients) {
        double ssTotal = 0;
        double ssResidual = 0;

        double meanFt = data.stream().mapToDouble(d -> d[1]).average().orElse(0.0);

        // Calcular média e desvio padrão de t novamente para padronizar ao fazer as predições
        DescriptiveStatistics statsT = new DescriptiveStatistics();
        data.stream().mapToDouble(d -> d[0]).forEach(statsT::addValue);
        double meanT = statsT.getMean();
        double stdDevT = statsT.getStandardDeviation();


        for (double[] point : data) {
            double tOriginal = point[0];
            double actualFt = point[1];

            double tStandardized;
            if (stdDevT == 0) {
                tStandardized = 0;
            } else {
                tStandardized = (tOriginal - meanT) / stdDevT;
            }

            // Calcula o f(t) predito pelo modelo usando o t padronizado
            double predictedFt = 0;
            for (int i = 0; i < coefficients.length; i++) {
                predictedFt += coefficients[i] * Math.pow(tStandardized, i);
            }

            ssTotal += Math.pow(actualFt - meanFt, 2);
            ssResidual += Math.pow(actualFt - predictedFt, 2);
        }

        if (ssTotal == 0) {
            return 1.0;
        }

        return 1 - (ssResidual / ssTotal);
    }

    /**
     * Gera pontos f(t) preditos para um dado conjunto de t e coeficientes.
     * Os coeficientes são baseados em t padronizado, então a predição também deve usar t padronizado.
     * @param data Dados originais (usados para determinar o range, média e desvio padrão de t)
     * @param coefficients Coeficientes do polinômio
     * @return Lista de pontos [t (original), f(t) predito]
     */
    public static List<double[]> generatePredictedPoints(List<double[]> data, double[] coefficients) {
        List<double[]> predictedPoints = new ArrayList<>();
        OptionalDouble minTOpt = data.stream().mapToDouble(d -> d[0]).min();
        OptionalDouble maxTOpt = data.stream().mapToDouble(d -> d[0]).max();

        // Calcular média e desvio padrão de t
        DescriptiveStatistics statsT = new DescriptiveStatistics();
        data.stream().mapToDouble(d -> d[0]).forEach(statsT::addValue);
        double meanT = statsT.getMean();
        double stdDevT = statsT.getStandardDeviation();

        double minT = minTOpt.orElse(0);
        double maxT = maxTOpt.orElse(0);
        double step = (maxT - minT) / 99; // 100 pontos, 99 passos

        for (int i = 0; i < 100; i++) {
            double tOriginal = minT + i * step;

            double tStandardized;
            if (stdDevT == 0) {
                tStandardized = 0;
            } else {
                tStandardized = (tOriginal - meanT) / stdDevT;
            }

            double predictedFt = 0;
            for (int j = 0; j < coefficients.length; j++) {
                predictedFt += coefficients[j] * Math.pow(tStandardized, j);
            }
            predictedPoints.add(new double[]{tOriginal, predictedFt}); // Retorna tOriginal para o gráfico
        }
        return predictedPoints;
    }
}