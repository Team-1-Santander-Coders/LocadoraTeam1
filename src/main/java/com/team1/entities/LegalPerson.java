package main.java.com.team1.entities;

public class LegalPerson implements Customer{
    private String name;
    private String address;
    private String phone;
    private String cnpj;

    public LegalPerson(String name, String address, String phone, String cnpj) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.cnpj = cnpj;
    }

    @Override
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
        return this.cnpj.equals(((LegalPerson) customer).cnpj);
    }

    @Override
    public int hashCode() {
        return (31*cnpj.hashCode()) + cnpj.hashCode();
    }
}