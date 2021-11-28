package com.ahmed.acapysales.clients;

public class clients {
    int id;
    String Organization;
    String location;
    String name;
    String relation;
    String email;
    String tele1;
    String tele2;

    public clients() {
    }

    public clients(int id, String organization, String location, String name, String relation, String email, String tele1, String tele2) {
        this.id = id;
        Organization = organization;
        this.location = location;
        this.name = name;
        this.relation = relation;
        this.email = email;
        this.tele1 = tele1;
        this.tele2 = tele2;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrganization() {
        return Organization;
    }

    public void setOrganization(String organization) {
        Organization = organization;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTele1() {
        return tele1;
    }

    public void setTele1(String tele1) {
        this.tele1 = tele1;
    }

    public String getTele2() {
        return tele2;
    }

    public void setTele2(String tele2) {
        this.tele2 = tele2;
    }
}
