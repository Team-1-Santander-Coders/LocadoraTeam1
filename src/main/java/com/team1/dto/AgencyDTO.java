package main.java.com.team1.dto;

import java.io.Serial;
import java.io.Serializable;

public record AgencyDTO(String name, String address) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
