package com.example;

import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoint;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;

import java.util.ArrayList;
import java.util.List;

public class PolynomialRegressionSolver {

    /**
     * Realiza a regressão polinomial para um dado grau.
     * @param data Lista de pontos [t, f(t)]
     * @param degree Grau do polinômio
     * @return Array de coeficientes do polinômio (do termo constante ao maior grau)
     */
    public static double[] fitPolynomial(List<double[]> data, int degree) {
        // Usando OLSMultipleLinearRegression para lidar com a natureza multi-variável dos termos polinomiais
        OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();
        
        double[] y = new double[data.size()];
        double[][] x = new double[data.size()][degree + 1]; // +1 para o termo constante e potências de t

        for (int i = 0; i < data.size(); i++) {
            double t = data.get(i)[0];
            y[i] = data.get(i)[1]; // f(t)

            x[i][0] = 1.0; // Termo constante
            for (int j = 1; j <= degree; j++) {
                x[i][j] = Math.pow(t, j); // t, t^2, ..., t^degree
            }
        }

        regression.newSampleData(y, x);
        return regression.estimateRegressionParameters();
    }

    /**
     * Calcula o coeficiente de determinação (R-quadrado).
     * @param data Dados originais [t, f(t)]
     * @param coefficients Coeficientes do polinômio ajustado
     * @return Valor de R-quadrado
     */
    public static double calculateR2(List<double[]> data, double[] coefficients) {
        double ssTotal = 0;
        double ssResidual = 0;
        
        // Média de f(t)
        double meanFt = data.stream().mapToDouble(d -> d[1]).average().orElse(0.0);

        for (double[] point : data) {
            double t = point[0];
            double actualFt = point[1];
            
            // Calcula o f(t) predito pelo modelo
            double predictedFt = 0;
            for (int i = 0; i < coefficients.length; i++) {
                predictedFt += coefficients[i] * Math.pow(t, i);
            }

            ssTotal += Math.pow(actualFt - meanFt, 2);
            ssResidual += Math.pow(actualFt - predictedFt, 2);
        }
        
        if (ssTotal == 0) { // Evita divisão por zero se todos os f(t) forem iguais
            return 1.0; 
        }

        return 1 - (ssResidual / ssTotal);
    }

    /**
     * Gera pontos f(t) preditos para um dado conjunto de t e coeficientes.
     * @param tValues Valores de t para os quais predizer f(t)
     * @param coefficients Coeficientes do polinômio
     * @return Lista de pontos [t, f(t) predito]
     */
    public static List<double[]> generatePredictedPoints(List<double[]> data, double[] coefficients) {
        List<double[]> predictedPoints = new ArrayList<>();
        double minT = data.stream().mapToDouble(d -> d[0]).min().orElse(0);
        double maxT = data.stream().mapToDouble(d -> d[0]).max().orElse(0);
        
        // Gerar pontos em um intervalo para a curva, não apenas os pontos originais
        int numPoints = 100; // Número de pontos para suavizar a curva
        double step = (maxT - minT) / (numPoints - 1);

        for (int i = 0; i < numPoints; i++) {
            double t = minT + i * step;
            double predictedFt = 0;
            for (int j = 0; j < coefficients.length; j++) {
                predictedFt += coefficients[j] * Math.pow(t, j);
            }
            predictedPoints.add(new double[]{t, predictedFt});
        }
        return predictedPoints;
    }
}
