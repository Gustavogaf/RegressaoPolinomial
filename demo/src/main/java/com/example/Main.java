package com.example;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    private static final String DATA_FILE = "dados.txt"; // Certifique-se de que este arquivo está na raiz do seu projeto

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                List<double[]> data = DataReader.readData(DATA_FILE);

                if (data.isEmpty()) {
                    System.out.println("Nenhum dado lido do arquivo. Verifique o arquivo " + DATA_FILE);
                    return;
                }

                Map<Integer, Double> rSquaredValues = new HashMap<>();

                // Gerar gráficos de regressão para cada grau
                for (int degree = 1; degree <= 10; degree++) {
                    // Verificação para garantir que há dados suficientes para o grau
                    // (Número de pontos deve ser maior que o grau para uma regressão estável)
                    if (data.size() <= degree) {
                        System.out.println("Dados insuficientes para regressão de grau " + degree + ". Pulando.");
                        continue;
                    }

                    double[] coefficients = PolynomialRegressionSolver.fitPolynomial(data, degree);
                    double r2 = PolynomialRegressionSolver.calculateR2(data, coefficients);
                    rSquaredValues.put(degree, r2);

                    List<double[]> predictedPoints = PolynomialRegressionSolver.generatePredictedPoints(data, coefficients);

                    ChartGenerator chart1 = new ChartGenerator("Regressão Polinomial - Grau " + degree);
                    chart1.createRegressionChart(
                            "Regressão Polinomial - Grau " + degree + " (R² = " + String.format("%.4f", r2) + ")",
                            "t",
                            "f(t)",
                            data,
                            predictedPoints,
                            degree
                    );
                }

                // Gerar o gráfico da evolução do coeficiente de determinação
                ChartGenerator chart2 = new ChartGenerator("Evolução do Coeficiente de Determinação (R²)");
                chart2.createR2Chart(
                        "Evolução do Coeficiente de Determinação (R²)",
                        "Grau do Polinômio",
                        "R²",
                        rSquaredValues
                );

            } catch (IOException e) {
                System.err.println("Erro ao ler o arquivo de dados: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Ocorreu um erro inesperado: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}