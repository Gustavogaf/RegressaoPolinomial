package com.example;

import java.io.BufferedReader;
import java.io.InputStream; // Importe esta classe
import java.io.InputStreamReader; // Importe esta classe
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataReader {

    public static List<double[]> readData(String fileName) throws IOException { // Mudança de filePath para fileName
        List<double[]> data = new ArrayList<>();
        // Usa ClassLoader para obter o recurso do classpath
        try (InputStream is = DataReader.class.getClassLoader().getResourceAsStream(fileName);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            String line;
            // Ignorar a primeira linha se for o cabeçalho "t f(t)"
            br.readLine(); // Adicione esta linha para pular o cabeçalho

            while ((line = br.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");
                if (parts.length == 2) {
                    try {
                        double t = Double.parseDouble(parts[0]);
                        double ft = Double.parseDouble(parts[1]);
                        data.add(new double[]{t, ft});
                    } catch (NumberFormatException e) {
                        System.err.println("Erro ao converter número na linha: " + line);
                    }
                } else {
                    System.err.println("Formato de linha inválido: " + line);
                }
            }
        } catch (NullPointerException e) {
            // Isso pode acontecer se o recurso não for encontrado (is será null)
            throw new IOException("Arquivo de recurso não encontrado: " + fileName, e);
        }
        return data;
    }
}
