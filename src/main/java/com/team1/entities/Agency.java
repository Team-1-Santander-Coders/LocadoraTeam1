package main.java.com.team1.entities;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class Agency implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    String name;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String address;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Agency agency)) return false;
        return Objects.equals(name, agency.name) && Objects.equals(address, agency.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, address);
    }
}
