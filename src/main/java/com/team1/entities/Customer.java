package main.java.com.team1.entities;

import java.io.Serializable;

public interface Customer extends Serializable {

    public String getName();

    public void setName(String name);

    public String getAddress();

    public void setAddress(String address);

    public String getPhone();

    public void setPhone(String phone);

    public boolean equals(Customer customer);

    public int hashCode();

    public String toString();
}
