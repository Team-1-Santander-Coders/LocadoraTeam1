package main.java.com.team1.repository;

import main.java.com.team1.entities.User;
import main.java.com.team1.util.FileUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositório responsável pela gestão de usuários.
 * <p>
 * Esta classe permite adicionar usuários e recuperar a lista de usuários
 * armazenados em um arquivo de dados persistente.
 * </p>
 */
public class UserRepository implements Serializable {

    /**
     * Caminho para o arquivo onde os dados dos usuários são armazenados.
     */
    private static String USERS_FILE = "src/resources/data/users.dat";

    /**
     * Lista de usuários que estão atualmente carregados na memória.
     */
    List<User> usersList = new ArrayList<>();

    /**
     * Adiciona um novo usuário à lista de usuários e salva a lista no arquivo de dados.
     *
     * @param user O objeto {@link User} a ser adicionado.
     */
    public void addUser(User user) {
        usersList.add(user);
        FileUtil.writeToFile(usersList, USERS_FILE);
    }

    /**
     * Retorna todos os usuários cadastrados.
     *
     * @return Uma lista de {@link User} contendo todos os usuários armazenados.
     *         Se não houver usuários cadastrados, retorna uma lista vazia.
     */
    public List<User> findAll(){
        return FileUtil.readFromFile(USERS_FILE);
    }
}
