package main.java.com.team1.dto;

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
     */
    public UserDTO(String name, String address, String phone, String document, String email, String password, String type) {
        super(name, address, phone);
        this.document = document;
        this.email = email;
        this.password = password;
        this.type = type;
    }
}
