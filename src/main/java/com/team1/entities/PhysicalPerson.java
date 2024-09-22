package main.java.com.team1.entities;

public class PhysicalPerson implements Customer{

    private String name;
    private String address;
    private String phone;
    private String cpf;

    public PhysicalPerson(String name, String address, String phone, String cpf) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.cpf = cpf;
    }

    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    @Override
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Customer customer) {
        return this.cpf.equals(((PhysicalPerson)customer).cpf);
    }

    @Override
    public int hashCode() {
        return (31*cpf.hashCode()) + cpf.hashCode();
    }
}