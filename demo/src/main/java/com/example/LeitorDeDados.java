package com.example;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe utilitária para leitura de dados de um arquivo de texto.
 * Espera um formato de arquivo onde cada linha contém dois valores numéricos
 * separados por espaço ou tab (t e f(t)), ignorando a primeira linha que é o
 * cabeçalho.
 */
public class LeitorDeDados {

    /**
     * Lê os dados de um arquivo especificado no classpath.
     * A primeira linha do arquivo é ignorada (assumida como cabeçalho).
     *
     * @param nomeArquivo O nome do arquivo a ser lido (ex: "dados.txt").
     * @return Uma lista de arrays double, onde cada array contém [t, f(t)].
     * @throws IOException Se ocorrer um erro durante a leitura do arquivo ou se o
     *                     arquivo não for encontrado.
     */
    public static List<double[]> lerDados(String nomeArquivo) throws IOException {
        List<double[]> dados = new ArrayList<>();
        // Usa ClassLoader para obter o recurso do classpath, adequado para arquivos
        // dentro do JAR.
        try (InputStream is = LeitorDeDados.class.getClassLoader().getResourceAsStream(nomeArquivo);
                BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            String linha;
            br.readLine();

            while ((linha = br.readLine()) != null) {
                String[] partes = linha.trim().split("\\s+");
                if (partes.length == 2) {
                    try {
                        double t = Double.parseDouble(partes[0]);
                        double ft = Double.parseDouble(partes[1]);
                        dados.add(new double[] { t, ft });
                    } catch (NumberFormatException e) {
                        System.err.println("Erro ao converter número na linha: " + linha);
                    }
                } else {
                    System.err.println("Formato de linha inválido: " + linha);
                }
            }
        } catch (NullPointerException e) {
            // Isso pode acontecer se o recurso não for encontrado (is será null)
            throw new IOException("Arquivo de recurso não encontrado: " + nomeArquivo, e);
        }
        return dados;
    }
}