package com.example;

import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

public class PolynomialRegressionSolver {

    /**
     * Realiza a regressão polinomial para um dado grau utilizando PolynomialCurveFitter.
     *
     * @param data Lista de pontos [t, f(t)]
     * @param degree Grau do polinômio
     * @return Objeto PolynomialFunction representando o polinômio ajustado.
     */
    public static PolynomialFunction fitPolynomial(List<double[]> data, int degree) {
        WeightedObservedPoints obs = new WeightedObservedPoints();
        for (double[] point : data) {
            obs.add(point[0], point[1]);
        }

        // PolynomialCurveFitter lida internamente com a estabilidade numérica
        PolynomialCurveFitter fitter = PolynomialCurveFitter.create(degree);
        double[] coefficients = fitter.fit(obs.toList());

        return new PolynomialFunction(coefficients);
    }

    /**
     * Calcula o coeficiente de determinação ajustado (R-quadrado Ajustado).
     * O R-quadrado ajustado leva em conta o número de preditores no modelo.
     *
     * @param data Dados originais [t, f(t)]
     * @param polynomialFunção do polinômio ajustado
     * @param degree Grau do polinômio
     * @return Valor de R-quadrado Ajustado
     */
    public static double calculateR2Adjusted(List<double[]> data, PolynomialFunction polynomial, int degree) {
        double ssTotal = 0;
        double ssResidual = 0;

        // Média de f(t)
        double meanFt = data.stream().mapToDouble(d -> d[1]).average().orElse(0.0);

        for (double[] point : data) {
            double t = point[0];
            double actualFt = point[1];

            // Calcula o f(t) predito pelo modelo
            double predictedFt = polynomial.value(t);

            ssTotal += Math.pow(actualFt - meanFt, 2);
            ssResidual += Math.pow(actualFt - predictedFt, 2);
        }

        if (ssTotal == 0) { // Evita divisão por zero se todos os f(t) forem iguais
            return 1.0;
        }

        double r2 = 1 - (ssResidual / ssTotal);

        int n = data.size(); // Número de observações
        int k = degree;      // Número de preditores (grau do polinômio)

        // Evitar divisão por zero se n - k - 1 for zero ou negativo (dados insuficientes)
        if (n - k - 1 <= 0) {
            System.err.println("Dados insuficientes para calcular R² Ajustado para grau " + degree + ". Retornando NaN.");
            return Double.NaN;
        }

        return 1 - (1 - r2) * (n - 1) / (n - k - 1);
    }

    /**
     * Gera pontos f(t) preditos para um dado conjunto de t e um polinômio ajustado.
     *
     * @param data Dados originais (usados para determinar o intervalo de t)
     * @param polynomial Objeto PolynomialFunction que representa o polinômio ajustado
     * @return Lista de pontos [t, f(t) predito]
     */
    public static List<double[]> generatePredictedPoints(List<double[]> data, PolynomialFunction polynomial) {
        List<double[]> predictedPoints = new ArrayList<>();
        OptionalDouble minTOpt = data.stream().mapToDouble(d -> d[0]).min();
        OptionalDouble maxTOpt = data.stream().mapToDouble(d -> d[0]).max();

        double minT = minTOpt.orElse(0);
        double maxT = maxTOpt.orElse(0);

        int numPoints = 100; // Número de pontos para suavizar a curva
        double step = (maxT - minT) / (numPoints - 1);

        for (int i = 0; i < numPoints; i++) {
            double t = minT + i * step;
            double predictedFt = polynomial.value(t); // Calcula f(t) usando a função polinomial
            predictedPoints.add(new double[]{t, predictedFt});
        }
        return predictedPoints;
    }
}