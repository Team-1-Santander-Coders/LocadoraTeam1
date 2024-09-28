package main.java.com.team1.dto;

import java.util.UUID;
import java.io.Serial;

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

    private UserDTO(String name, String address, String phone, String document, String email, String password, String type, boolean isAdmin) {
        super(name, address, phone);
        this.id = UUID.randomUUID().toString();
        this.document = document;
        this.email = email;
        this.password = password;
        this.type = type;
        this.isAdmin = isAdmin;
    }

    public static UserDTO createAdmin(String name, String address, String phone, String document, String email, String password, String type) {
        return new UserDTO(name, address, phone, document, email, password, type, true);
    }

    @Override
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getDocument() {
        return document;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean isAdmin(){
        return isAdmin;
    }

    @Override
    public String getTipo() {
        return type;
    }

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
