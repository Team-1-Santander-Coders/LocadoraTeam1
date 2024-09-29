package main.java.com.team1.entities;

import main.java.com.team1.exception.DuplicateEntityException;
import main.java.com.team1.service.CustomerService;
import main.java.com.team1.util.UserAuth;

import java.io.Serial;

/**
 * Representa o usuário de um cliente.
 * Esta classe implementa a interface {@link main.java.com.team1.entities.Customer} e encapsula
 * os detalhes do usuário, como nome, endereço, senha, email, telefone, documento e tipo de cliente (Pessoas Físicas ou Jurídicas).
 */
public class User implements Customer {
    @Serial
    private static final long serialVersionUID = 1L;

    private String name;
    private String address;
    private String password;
    private String email;
    private String phone;
    private String document;
    private String tipo;

    /**
     * Construtor da classe User.
     *
     * @param name Nome do usuário.
     * @param address Endereço do usuário.
     * @param password Senha do usuário.
     * @param email Email do usuário.
     * @param phone Telefone do usuário.
     * @param document Documento do usuário (CPF ou CNPJ).
     * @param tipo Tipo de cliente (Pessoa Física ou Jurídica.
     */
    public User(String name, String address, String password, String email, String phone, String document, String tipo) {
        this.name = name;
        this.address = address;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.document = document;
        this.tipo = tipo;
    }

    /**
     * Método utilizado para criar um novo usuário. Verifica se já há um cliente cadastrado com o documento ou email informado.
     * @param name Nome do usuário.
     * @param address Endereço do usuário.
     * @param password Senha do usuário.
     * @param email Email do usuário.
     * @param phone Telefone do usuário.
     * @param document Documento do cliente (CPF ou CNPJ).
     * @param tipo Tipo de cliente (Pessoa Física ou Jurídica).
     * @return novo usuário.
     * @throws DuplicateEntityException em caso de cliente cadastrado com o mesmo documento ou usuário cadastrado com o mesmo email.
     */
    public static User createUser(String name, String address, String password, String email, String phone, String document, String tipo) throws DuplicateEntityException {

        CustomerService customerService = new CustomerService();

        if (customerService.findCustomerByDocument(document) != null) {
            throw new DuplicateEntityException("Usuário com este documento já cadastrado.");
        }

        if (UserAuth.findUserByMail(email) != null) {
            throw new DuplicateEntityException("Usuário com este email já cadastrado.");
        }

        if (password.trim().length() < 8 || tipo.isEmpty()) {
            throw new IllegalArgumentException("Senha precisa ter no mínimo 8 dígitos");
        }

        if (tipo.trim().equals("Física")) {
            if (document.trim().length() != 11) throw new IllegalArgumentException("Erro nos dados passados");
        }

        if (tipo.trim().equals("Jurídica")) {
            if (document.trim().length() != 14) throw new IllegalArgumentException("Erro nos dados passados");
        }

        return new User(name, address, password, email, phone, document, tipo);
    }

    /**
     * Retorna o nome do usuário.
     * @return nome do usuário.
     */
    public String getName() {
        return name;
    }

    /**
     * Define o novo nome do usuário.
     * @param name O novo nome do usuário.
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retorna o endereço do usuário.
     * @return o endereço do usuário.
     */
    public String getAddress(){
        return address;
    }

    /**
     * Define o novo endereço do usuário.
     * @param address O novo endereço do cliente.
     */
    @Override
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Retorna a senha do usuário.
     * @return a senha do usuário.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Retorna o email do usuário.
     * @return o email do usuário.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Retorna o telefone do usuário.
     * @return o telefone do usuário.
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Define o novo telefone do usuário.
     * @param phone O novo telefone do usuário.
     */
    @Override
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Retorna o documento (CPF ou CNPJ) do usuário.
     * @return o documento do usuário.
     */
    public String getDocument() {
        return document;
    }

    /**
     * Retorna o tipo de usuário (Pessoa Física ou Jurídica).
     * @return o tipo do usuário.
     */
    public String getTipo() {
        return tipo;
    }

    @Override
    public boolean equals(Customer customer) {
        return false;
    }
    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                '}';
    }

}
