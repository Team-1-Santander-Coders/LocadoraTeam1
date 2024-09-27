package main.java.com.team1.entities;

import main.java.com.team1.exception.DuplicateEntityException;
import main.java.com.team1.service.CustomerService;
import main.java.com.team1.util.UserAuth;

import java.io.Serial;

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

    public User(String name, String address, String password, String email, String phone, String document, String tipo) {
        this.name = name;
        this.address = address;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.document = document;
        this.tipo = tipo;
    }

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


    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public String getAddress(){
        return address;
    }

    @Override
    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Customer customer) {
        return false;
    }

    public String getDocument() {
        return document;
    }

    public String getTipo() {
        return tipo;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                '}';
    }

}
