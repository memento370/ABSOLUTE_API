package com.example.absoluteweb.server.DTO;

import jakarta.persistence.Column;

public class ServerRegistrationRequest {

    private String login;
    private String password;
    private String l2email;
    private int ban_expire;
    private String allow_ip;

    public ServerRegistrationRequest() {}

    public ServerRegistrationRequest(String login, String password, String l2email) {
        this.login = login;
        this.password = password;
        this.l2email = l2email;
    }

    public int getBan_expire() {
        return ban_expire;
    }

    public void setBan_expire(int ban_expire) {
        this.ban_expire = ban_expire;
    }

    public String getAllow_ip() {
        return allow_ip;
    }

    public void setAllow_ip(String allow_ip) {
        this.allow_ip = allow_ip;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getL2email() {
        return l2email;
    }

    public void setL2email(String l2email) {
        this.l2email = l2email;
    }
}
