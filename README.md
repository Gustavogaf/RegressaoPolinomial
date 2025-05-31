# RegressaoPolinomial

Este projeto Java implementa a regressão polinomial para ajustar dados a uma função polinomial de diversos graus, utilizando a biblioteca Apache Commons Math3 para o ajuste e JFreeChart para a visualização dos resultados. A aplicação gera gráficos da curva de regressão para cada grau de 1 a 10, e um gráfico da evolução do Coeficiente de Determinação Ajustado (R²) em função do grau do polinômio.

## Visão Geral

A regressão polinomial é uma forma de análise de regressão na qual a relação entre a variável independente 't' e a variável dependente 'f(t)' é modelada como um polinômio de n-ésimo grau. Este projeto demonstra como aplicar essa técnica e avaliar a qualidade do ajuste através do R² Ajustado.

**Funcionalidades:**
* Leitura de dados de um arquivo `dados.txt` (formato `t f(t)`).
* Realização de regressão polinomial para graus de 1 a 10.
* Uso do `PolynomialCurveFitter` do Apache Commons Math3 para um ajuste de curva numericamente estável.
* Cálculo do Coeficiente de Determinação Ajustado (R² Ajustado) para avaliar a qualidade do modelo, considerando a complexidade do polinômio.
* Geração de gráficos interativos para:
    * Ajuste da curva polinomial para cada grau, mostrando os dados originais e a curva predita.
    * A evolução do R² Ajustado em relação ao grau do polinômio.

## Como Rodar

Este projeto utiliza Maven para gerenciamento de dependências.

### Pré-requisitos

* JDK 11 ou superior.
* Maven 3.x.

### Configuração e Execução

1.  **Clone o repositório** (ou baixe os arquivos).
2.  **Navegue até o diretório `demo`** no terminal:
    ```bash
    cd /caminho/para/gustavogaf/regressaopolinomial/RegressaoPolinomial-main/demo
    ```
3.  **Compile o projeto com Maven:**
    ```bash
    mvn clean install
    ```
4.  **Execute a aplicação:**
    ```bash
    mvn exec:java -Dexec.mainClass="com.example.Main"
    ```
    Isso abrirá as janelas dos gráficos gerados.

## Estrutura do Projeto

* `pom.xml`: Gerenciamento de dependências (Apache Commons Math3, JFreeChart).
* `src/main/resources/dados.txt`: Arquivo de dados de entrada (`t` e `f(t)`).
* `src/main/java/com/example/`:
    * `Main.java`: Ponto de entrada da aplicação. Orquestra a leitura dos dados, o ajuste dos polinômios, o cálculo do R² e a geração dos gráficos.
    * `DataReader.java`: Classe utilitária para ler os dados do arquivo `dados.txt`.
    * `PolynomialRegressionSolver.java`: Contém a lógica principal para realizar a regressão polinomial e calcular o R² Ajustado.
    * `ChartGenerator.java`: Classe responsável por criar e exibir os gráficos utilizando JFreeChart.

## Dependências

As principais dependências são gerenciadas via Maven e declaradas no `pom.xml`:

* `org.apache.commons:commons-math3:3.6.1`: Utilizado para o ajuste de curva polinomial (`PolynomialCurveFitter`, `PolynomialFunction`).
* `org.jfree:jfreechart:1.5.3`: Biblioteca para geração de gráficos.
* `org.jfree:jcommon:1.0.23`: Dependência auxiliar para JFreeChart.

---

**Comentários essenciais no código-fonte:**

Vou adicionar comentários às classes principais (`Main.java`, `DataReader.java`, `PolynomialRegressionSolver.java`, `ChartGenerator.java`) para explicar suas responsabilidades e a lógica interna.

**1. `gustavogaf/regressaopolinomial/RegressaoPolinomial-main/demo/src/main/java/com/example/Main.java`**

```java
package com.example;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction; // Importação adicionada

/**
 * Classe principal da aplicação de Regressão Polinomial.
 * Responsável por orquestrar a leitura dos dados,
 * o ajuste dos modelos polinomiais, o cálculo do R² Ajustado
 * e a exibição dos gráficos.
 */
public class Main {

    // Define o nome do arquivo de dados a ser lido.
    // O arquivo deve estar no classpath (ex: src/main/resources/).
    private static final String DATA_FILE = "dados.txt"; //

    public static void main(String[] args) {
        // Garante que as operações da interface gráfica sejam executadas na Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            try {
                // 1. Leitura dos dados do arquivo
                List<double[]> data = DataReader.readData(DATA_FILE); //

                if (data.isEmpty()) { //
                    System.out.println("Nenhum dado lido do arquivo. Verifique o arquivo " + DATA_FILE); //
                    return;
                }

                // Mapa para armazenar os valores de R² Ajustado para cada grau do polinômio
                Map<Integer, Double> rSquaredAdjustedValues = new HashMap<>(); //

                // 2. Loop para realizar a regressão para diferentes graus (de 1 a 10)
                for (int degree = 1; degree <= 10; degree++) { //
                    // Verificação para garantir que há dados suficientes para uma regressão estável
                    // e para o cálculo válido do R² Ajustado (n - k - 1 > 0).
                    // Onde n é o número de pontos e k é o grau do polinômio.
                    if (data.size() <= degree + 1) { //
                        System.out.println("Dados insuficientes para regressão de grau " + degree + " e cálculo de R² Ajustado. Pulando."); //
                        continue;
                    }

                    // 3. Ajuste do polinômio para o grau atual
                    // Utiliza PolynomialCurveFitter para uma regressão numericamente mais estável.
                    PolynomialFunction polynomial = PolynomialRegressionSolver.fitPolynomial(data, degree); //
                    
                    // 4. Cálculo do R² Ajustado para o modelo atual
                    double r2Adjusted = PolynomialRegressionSolver.calculateR2Adjusted(data, polynomial, degree); //
                    rSquaredAdjustedValues.put(degree, r2Adjusted); //

                    // 5. Geração dos pontos preditos pelo modelo para plotagem da curva
                    List<double[]> predictedPoints = PolynomialRegressionSolver.generatePredictedPoints(data, polynomial); //

                    // 6. Criação e exibição do gráfico de regressão para o grau atual
                    ChartGenerator chart1 = new ChartGenerator("Regressão Polinomial - Grau " + degree); //
                    // Garante que a janela do gráfico seja descartada ao ser fechada, liberando recursos.
                    chart1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //
                    chart1.createRegressionChart(
                            "Regressão Polinomial - Grau " + degree + " (R² Ajustado = " + String.format("%.4f", r2Adjusted) + ")", //
                            "t", //
                            "f(t)", //
                            data, //
                            predictedPoints, //
                            degree //
                    );
                }

                // 7. Geração e exibição do gráfico da evolução do R² Ajustado por grau
                ChartGenerator chart2 = new ChartGenerator("Evolução do Coeficiente de Determinação (R² Ajustado)"); //
                // Garante que a aplicação seja encerrada ao fechar este último gráfico.
                chart2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //
                chart2.createR2Chart(
                        "Evolução do Coeficiente de Determinação (R² Ajustado)", //
                        "Grau do Polinômio", //
                        "R² Ajustado", //
                        rSquaredAdjustedValues //
                );

            } catch (IOException e) { //
                System.err.println("Erro ao ler o arquivo de dados: " + e.getMessage()); //
            } catch (Exception e) { //
                System.err.println("Ocorreu um erro inesperado: " + e.getMessage()); //
                e.printStackTrace(); //
            }
        });
    }
}

