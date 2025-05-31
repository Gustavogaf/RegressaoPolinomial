package com.example;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction; // Importar

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

                Map<Integer, Double> rSquaredAdjustedValues = new HashMap<>(); // Alterado para R² Ajustado

                // Gerar gráficos de regressão para cada grau
                for (int degree = 1; degree <= 10; degree++) {
                    // O número de pontos deve ser maior que o grau para uma regressão estável
                    // e para evitar divisão por zero no cálculo do R² Ajustado (n - k - 1 > 0)
                    if (data.size() <= degree + 1) { // Condição ajustada para R² Ajustado
                        System.out.println("Dados insuficientes para regressão de grau " + degree + " e cálculo de R² Ajustado. Pulando.");
                        continue;
                    }

                    // Chama o novo método fitPolynomial que retorna PolynomialFunction
                    PolynomialFunction polynomial = PolynomialRegressionSolver.fitPolynomial(data, degree);

                    // Calcula o R² Ajustado
                    double r2Adjusted = PolynomialRegressionSolver.calculateR2Adjusted(data, polynomial, degree);
                    rSquaredAdjustedValues.put(degree, r2Adjusted);

                    // Gera pontos preditos usando o objeto PolynomialFunction
                    List<double[]> predictedPoints = PolynomialRegressionSolver.generatePredictedPoints(data, polynomial);

                    ChartGenerator chart1 = new ChartGenerator("Regressão Polinomial - Grau " + degree);
                    chart1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    chart1.createRegressionChart(
                            "Regressão Polinomial - Grau " + degree + " (R² Ajustado = " + String.format("%.4f", r2Adjusted) + ")",
                            "t",
                            "f(t)",
                            data,
                            predictedPoints,
                            degree
                    );
                }

                // Gerar o gráfico da evolução do coeficiente de determinação ajustado
                ChartGenerator chart2 = new ChartGenerator("Evolução do Coeficiente de Determinação (R² Ajustado)");
                chart2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                chart2.createR2Chart(
                        "Evolução do Coeficiente de Determinação (R² Ajustado)",
                        "Grau do Polinômio",
                        "R² Ajustado", // Alterado para R² Ajustado
                        rSquaredAdjustedValues
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