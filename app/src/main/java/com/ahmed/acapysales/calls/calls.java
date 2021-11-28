package com.ahmed.acapysales.calls;

public class calls {
    int id;
    int user_id;
    int client_id;
    String client_name;
    String details;
    String date;
    String time;

    public calls() {
    }

    public calls(int id, int user_id, int client_id, String client_name, String details, String date, String time) {
        this.id = id;
        this.user_id = user_id;
        this.client_id = client_id;
        this.client_name = client_name;
        this.details = details;
        this.date = date;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
