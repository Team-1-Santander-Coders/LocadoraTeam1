package main.java.com.team1.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public record AgencyDTO(String name, String address) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AgencyDTO agencyDTO)) return false;
        return Objects.equals(name, agencyDTO.name) && Objects.equals(address, agencyDTO.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, address);
    }

    @Override
    public String toString() {
        return "AgencyDTO{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
