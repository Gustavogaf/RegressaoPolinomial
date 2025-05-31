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



