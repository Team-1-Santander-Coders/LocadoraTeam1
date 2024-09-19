package main.java.com.team1.test;

import main.java.com.team1.util.FileUtil;

import java.util.ArrayList;
import java.util.List;

public class FileUtilTest {
    public static void main(String[] args) {
        List<String> testList = new ArrayList<>();
        testList.add("Item 1");
        testList.add("Item 2");
        testList.add("Item 3");

        String fileName = "src/resources/data/testFile.txt";

        // Teste de escrita no arquivo
        System.out.println("Gravando a lista no arquivo...");
        FileUtil.writeToFile(testList, fileName);

        // Teste de leitura do arquivo
        System.out.println("Lendo a lista do arquivo...");
        List<String> resultList = FileUtil.readFromFile(fileName);

        if (resultList != null) {
            for (String item : resultList) {
                System.out.println(item);
            }
        } else {
            System.out.println("A lista está vazia ou houve um erro.");
        }

        // Teste de adição (append) de um item no arquivo
        System.out.println("Adicionando um novo item ao arquivo...");
        FileUtil.appendToFile("Item 4", fileName);

        // Lendo o arquivo novamente após o append
        System.out.println("Lendo a lista atualizada do arquivo...");
        List<String> updatedList = FileUtil.readFromFile(fileName);

        if (updatedList != null) {
            for (String item : updatedList) {
                System.out.println(item);
            }
        } else {
            System.out.println("A lista está vazia ou houve um erro.");
        }
    }
}
