package main.java.com.team1.util;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class FileUtil<T> implements Serializable {
    private static final String LOG_FILE = "src/resources/data/logs.txt";
    /**
     *<p>O método writeToFile faz o uso do método <code>ObjectOutputStream</code> para serializar e salvar os objetos, isto é, converter os objetos para bytes e gravar em um arquivo.</p>
     * @param data Lista de objetos do tipo T, que representa os dados a serem escritos no arquivo.
     * @param filename String que referencia o arquivo onde as informações serão salvas.
     * @param <T>
     *
     *
     */

    public static <T> void writeToFile(List<T> data, String filename){
        try(ObjectOutputStream objectOutput = new ObjectOutputStream(new FileOutputStream(filename))){
            objectOutput.writeObject(data);
            objectOutput.close();
        } catch (IOException e) {
            logError(e);
        }
    }

    /**
     *<p>rO método readFromFile utiliza <code>ObjectInputStream</code> para ler objetos serializados a partir de um arquivo. Ele carrega o arquivo especificado, desserializa os objetos armazenados e os retorna em uma lista do tipo T.</p>
     * @param filename String que referencia o arquivo a ser consultado.
     * @return data Lista de  objetos de tipo T.
     * @param <T>
     */

    @SuppressWarnings("unchecked")
    public static <T> List<T> readFromFile(String filename) {
        try{
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename));
            return (List<T>) ois.readObject();

        } catch (IOException | ClassNotFoundException e) {
            logError(e);
            return null;
        }
    }
    /**
     *<p>O método appendToFile carrega os objetos serializados do tipo T a partir de um arquivo,
     desserializa-os e os adiciona a uma lista. Em seguida, o objeto <code>content</code> é
     adicionado à lista, que é novamente serializada e salva no arquivo. </p>
     * @param content Objeto que será serializado e incluído ao final do arquivo.
     * @param fileName String que referencia o arquivo onde as informações estão armazenadas.
     * @param <T>
     */
    public static <T> void appendToFile(T content, String fileName) {
        try {
            List<T> data = readFromFile(fileName);
            if(!data.isEmpty()){
                if(!data.get(0).getClass().equals(content.getClass())) throw new RuntimeException("Tipo incompativel: " + content.getClass());
            }
            data.add(content);
            writeToFile(data, fileName);

        } catch (RuntimeException e) {
            logError(e);
        }
    }

    /**<p>O método readLines desserializa os objetos a partir de um arquivo e os adiciona a uma lista de strings.</p>
     *
     * @param fileName String que referencia o arquivo onde as informações estão armazenadas.
     * @return Lista de Strings
     * @param <T>
     */
    public static <T> List<String> readLines(String fileName) {
        List<String> dataConvertida = new ArrayList<>();
        try{
            List<T> dataBase = readFromFile(fileName);
            for(T data : dataBase){
                dataConvertida.add(data.toString());
            }

        } catch (RuntimeException e) {
            logError(e);

        }
        return dataConvertida;
    }

    private static void logError(Exception e) {
        System.out.println("Operação não concluída. Verificar Log.");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[dd/MM/yyyy HH:mm:ss] ");
        try (BufferedWriter logWriter = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            logWriter.write(LocalDateTime.now().format(formatter));
            logWriter.write("[Error] " + e.getMessage());
            logWriter.newLine();
            for (StackTraceElement element : e.getStackTrace()) {
                logWriter.write("\tat " + element.toString());
                logWriter.newLine();
            }
            logWriter.newLine();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
