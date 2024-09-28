package main.java.com.team1.dto;

import java.util.UUID;
import java.io.Serial;

/**
 * Classe que representa um Data Transfer Object (DTO) para o usuário do sistema,
 * estendendo a classe {@link CustomerDTO}.
 * <p>
 * Esta classe encapsula os dados de um usuário, incluindo informações como nome,
 * endereço, telefone, documento, email, senha, tipo de cliente (pessoa física ou jurídica),
 * e se o usuário tem privilégios de administrador.
 * </p>
 * <p>
 * A classe também oferece métodos para criar usuários com ou sem privilégios de
 * administrador, utilizando o padrão Factory Method para a criação de administradores.
 * </p>
 */

public class UserDTO extends CustomerDTO {
    @Serial
    private static final long serialVersionUID = 1L;
    private String id;
    private String email;
    private String password;
    private String document;
    private String type;
    private boolean isAdmin;

    /**
     * Construtor da classe abstrata, que será acessada via <code>super()</code> pelas classes filhas
     *
     * @param name
     * @param address
     * @param phone
     * @param email
     * @param password
     * @param type
     */

    public UserDTO(String name, String address, String phone, String document, String email, String password, String type) {
        super(name, address, phone);
        this.id = UUID.randomUUID().toString();
        this.document = document;
        this.email = email;
        this.password = password;
        this.type = type;
        this.isAdmin = false;
    }

    /**
     * Construtor privado para criação de usuários com a opção de especificar
     * se o usuário é administrador ou não.
     *
     * @param name O nome do usuário.
     * @param address O endereço do usuário.
     * @param phone O telefone do usuário.
     * @param document O documento do usuário (CPF ou CNPJ).
     * @param email O email do usuário.
     * @param password A senha do usuário.
     * @param type O tipo de usuário (pessoa física ou jurídica).
     * @param isAdmin Se o usuário tem privilégios de administrador.
     */

    private UserDTO(String name, String address, String phone, String document, String email, String password, String type, boolean isAdmin) {
        super(name, address, phone);
        this.id = UUID.randomUUID().toString();
        this.document = document;
        this.email = email;
        this.password = password;
        this.type = type;
        this.isAdmin = isAdmin;
    }

    /**
     * Método de fábrica para criar um objeto {@code UserDTO} com privilégios de administrador.
     * <p>
     * Este método utiliza um padrão Factory Method para diferenciar a criação de usuários padrão e
     * administradores.
     * </p>
     *
     * @param name O nome do usuário.
     * @param address O endereço do usuário.
     * @param phone O telefone do usuário.
     * @param document O documento do usuário (CPF ou CNPJ).
     * @param email O email do usuário.
     * @param password A senha do usuário.
     * @param type O tipo de usuário (pessoa física ou jurídica).
     * @return Um objeto {@code UserDTO} com privilégios de administrador.
     */

    public static UserDTO createAdmin(String name, String address, String phone, String document, String email, String password, String type) {
        return new UserDTO(name, address, phone, document, email, password, type, true);
    }

    /**
     * @return O email do usuário.
     */

    public String getEmail() {
        return email;
    }

    /**
     * @return A senha do usuário.
     */

    public String getPassword() {
        return password;
    }

    /**
     * @return O documento do usuário.
     */
    @Override
    public String getDocument() {
        return document;
    }

    /**
     * Retorna o ID único do usuário, gerado automaticamente.
     *
     * @return O ID do usuário.
     */

    public String getId() {
        return id;
    }

    /**
     * Verifica se o usuário tem privilégios de administrador.
     *
     * @return {@code true} se o usuário for administrador, {@code false} caso contrário.
     */

    public boolean isAdmin(){
        return isAdmin;
    }

    /**
     * Retorna o tipo do usuário (pessoa física ou jurídica).
     *
     * @return O tipo do usuário.
     */

    @Override
    public String getTipo() {
        return type;
    }

    /**
     * Retorna uma representação textual do objeto {@code UserDTO}, exibindo
     * as principais informações como ID, email, documento, tipo de usuário, e se é administrador.
     *
     * @return Uma string representando o {@code UserDTO}.
     */

    @Override
    public String toString() {
        return "UserDTO{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", document='" + document + '\'' +
                ", type='" + type + '\'' +
                ", isAdmin=" + isAdmin +
                '}';
    }
}
