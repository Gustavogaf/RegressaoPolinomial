package com.example;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;

public class Main {

    private static final String NOME_ARQUIVO_DADOS = "dados.txt";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Leitura dos dados usando a classe renomeada e método renomeado
                List<double[]> dados = LeitorDeDados.lerDados(NOME_ARQUIVO_DADOS);

                if (dados.isEmpty()) {
                    System.out.println("Nenhum dado lido do arquivo. Verifique o arquivo " + NOME_ARQUIVO_DADOS);
                    return;
                }

                Map<Integer, Double> valoresRSquaredAjustado = new HashMap<>();

                // Loop para realizar a regressão para diferentes graus (de 1 a 10)
                for (int grau = 1; grau <= 10; grau++) {
                    // Verificação para garantir que há dados suficientes para uma regressão estável
                    // e para o cálculo válido do R² Ajustado (n - k - 1 > 0).
                    // Onde n é o número de pontos e k é o grau do polinômio.
                    if (dados.size() <= grau + 1) {
                        System.out.println("Dados insuficientes para regressão de grau " + grau
                                + " e cálculo de R² Ajustado. Pulando."); //
                        continue;
                    }

                    // Ajuste do polinômio para o grau atual usando a classe renomeada e método
                    // renomeado
                    PolynomialFunction polinomio = RegressaoPolinomial.ajustarPolinomio(dados, grau);

                    // Cálculo do R² Ajustado para o modelo atual usando a classe renomeada e método
                    // renomeado
                    double r2Ajustado = RegressaoPolinomial.calcularR2Ajustado(dados, polinomio, grau);
                    valoresRSquaredAjustado.put(grau, r2Ajustado);

                    // Geração dos pontos preditos pelo modelo para plotagem da curva usando a
                    // classe renomeada e método renomeado
                    List<double[]> pontosPreditos = RegressaoPolinomial.gerarPontosPreditos(dados, polinomio);

                    // Criação e exibição do gráfico de regressão para o grau atual usando a classe
                    // renomeada e método renomeado
                    GeradorDeGraficos graficoRegressao = new GeradorDeGraficos("Regressão Polinomial - Grau " + grau);
                    graficoRegressao.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    graficoRegressao.criarGraficoRegressao(
                            "Regressão Polinomial - Grau " + grau + " (R² Ajustado = "
                                    + String.format("%.4f", r2Ajustado) + ")",
                            "t",
                            "f(t)",
                            dados,
                            pontosPreditos,
                            grau);
                }

                // Geração e exibição do gráfico da evolução do R² Ajustado por grau usando a
                // classe renomeada e método renomeado
                GeradorDeGraficos graficoR2 = new GeradorDeGraficos(
                        "Evolução do Coeficiente de Determinação (R² Ajustado)");
                graficoR2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                graficoR2.criarGraficoR2(
                        "Evolução do Coeficiente de Determinação (R² Ajustado)",
                        "Grau do Polinômio",
                        "R² Ajustado",
                        valoresRSquaredAjustado);

            } catch (IOException e) {
                System.err.println("Erro ao ler o arquivo de dados: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Ocorreu um erro inesperado: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}