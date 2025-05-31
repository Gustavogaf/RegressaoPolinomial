package com.example;

import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

/**
 * Classe responsável por realizar a regressão polinomial e calcular o
 * coeficiente de determinação ajustado (R² Ajustado).
 * Utiliza o PolynomialCurveFitter do Apache Commons Math3 para o ajuste da
 * curva.
 */
public class RegressaoPolinomial {

    /**
     * Realiza a regressão polinomial para um dado grau utilizando
     * PolynomialCurveFitter.
     * Este método é mais robusto numericamente, pois o fitter lida com a formação
     * da matriz
     * de design e a solução do sistema de equações de forma otimizada, incluindo o
     * escalonamento interno.
     *
     * @param dados Lista de pontos
     * @param grau  Polinomial do grau a ser ajustado.
     * @return Objeto PolynomialFunction representando o polinômio ajustado.
     */
    public static PolynomialFunction ajustarPolinomio(List<double[]> dados, int grau) {
        WeightedObservedPoints observacoes = new WeightedObservedPoints();
        for (double[] ponto : dados) {
            observacoes.add(ponto[0], ponto[1]); // Adiciona cada ponto (t, f(t)) como uma observação
        }

        // Cria um ajustador de curva polinomial para o grau especificado.
        // O fitter internamente cuida da estabilidade numérica (por exemplo, através do
        // escalonamento dos termos).
        PolynomialCurveFitter ajustador = PolynomialCurveFitter.create(grau);
        double[] coeficientes = ajustador.fit(observacoes.toList());

        // Retorna um objeto PolynomialFunction que pode ser usado para avaliar o
        // polinômio
        return new PolynomialFunction(coeficientes);
    }

    /**
     * Calcula o coeficiente de determinação ajustado (R-quadrado Ajustado).
     * O R-quadrado ajustado é uma versão modificada do R-quadrado que ajusta o
     * número
     * de preditores no modelo (grau do polinômio). Ele é mais adequado para
     * comparar
     * modelos com diferentes números de termos, pois penaliza modelos mais
     * complexos
     * que não melhoram significativamente o ajuste.
     *
     * @param dados     Dados originais [t, f(t)]
     * @param polinomio Função do polinômio ajustado, contendo os coeficientes.
     * @param grau      Polinomial do grau (número de preditores).
     * @return Valor de R-quadrado Ajustado, ou Double.NaN se houver dados
     *         insuficientes.
     */
    public static double calcularR2Ajustado(List<double[]> dados, PolynomialFunction polinomio, int grau) {
        double ssTotal = 0; // Soma Total dos Quadrados
        double ssResidual = 0; // Soma dos Quadrados dos Resíduos

        // Média de f(t) dos dados originais, necessária para calcular SS Total
        double mediaFt = dados.stream().mapToDouble(d -> d[1]).average().orElse(0.0);

        for (double[] ponto : dados) {
            double t = ponto[0]; // Valor de t do ponto original
            double ftReal = ponto[1];

            // Calcula o f(t) predito pelo modelo polinomial para o t atual
            double ftPredito = polinomio.value(t);

            ssTotal += Math.pow(ftReal - mediaFt, 2);
            ssResidual += Math.pow(ftReal - ftPredito, 2);
        }

        if (ssTotal == 0) {
            // Se SS Total for zero, todos os f(t) são iguais. O modelo perfeito (se
            // ssResidual também for zero)
            // ou o melhor que pode ser, resultando em R² de 1.0.
            return 1.0;
        }

        // R² convencional
        double r2 = 1 - (ssResidual / ssTotal);

        int n = dados.size();
        int k = grau;

        // Condição para evitar divisão por zero no cálculo do R² Ajustado: n - k - 1
        // deve ser > 0
        // (Número de observações deve ser maior que o número de parâmetros do modelo).
        if (n - k - 1 <= 0) {
            System.err.println("Dados insuficientes para calcular R² Ajustado para grau " + grau + ". Retornando NaN.");
            return Double.NaN;
        }

        // Fórmula do R² Ajustado
        return 1 - (1 - r2) * (n - 1) / (n - k - 1);
    }

    /**
     * Gera pontos f(t) preditos para um dado conjunto de t utilizando o polinômio
     * ajustado.
     * Estes pontos são usados para desenhar a curva de regressão nos gráficos.
     *
     * @param dados     Dados originais (utilizados para determinar o intervalo
     *                  mínimo e máximo de 't' para a curva).
     * @param polinomio Objeto PolynomialFunction que representa o polinômio
     *                  ajustado.
     * @return Lista de pontos [t, f(t) predito] que formam a curva de regressão.
     */
    public static List<double[]> gerarPontosPreditos(List<double[]> dados, PolynomialFunction polinomio) {
        List<double[]> pontosPreditos = new ArrayList<>();
        // Encontra os valores mínimo e máximo de 't' nos dados originais para definir o
        // intervalo da curva.
        OptionalDouble tMinOpt = dados.stream().mapToDouble(d -> d[0]).min();
        OptionalDouble tMaxOpt = dados.stream().mapToDouble(d -> d[0]).max();

        double tMin = tMinOpt.orElse(0);
        double tMax = tMaxOpt.orElse(0);

        int numeroDePontos = 100;
        double passo = (tMax - tMin) / (numeroDePontos - 1);

        for (int i = 0; i < numeroDePontos; i++) {
            double t = tMin + i * passo;
            double ftPredito = polinomio.value(t);
            pontosPreditos.add(new double[] { t, ftPredito });
        }
        return pontosPreditos;
    }
}