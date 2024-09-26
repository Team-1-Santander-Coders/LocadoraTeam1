package main.java.com.team1.dto;

import java.util.UUID;
import java.io.Serial;

public class UserDTO extends CustomerDTO {
    @Serial
    private static final long serialVersionUID = 1L;
    private UUID id;
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
        this.id = UUID.randomUUID();
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

    public UUID getId() {
        return id;
    }

    @Override
    public String getTipo() {
        return type;
    }
}
