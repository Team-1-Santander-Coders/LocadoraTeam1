package main.java.com.team1.dto;

import java.io.Serial;

public class UserDTO extends CustomerDTO {
    private String email;
    private String password;
    private String document;
    private String type;

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
    @Serial
    private static final long serialVersionUID = 1L;

    public UserDTO(String name, String address, String phone, String document, String email, String password, String type) {
        super(name, address, phone);
        this.document = document;
        this.email = email;
        this.password = password;
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getDocument() {
        return document;
    }
}
