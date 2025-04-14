package com.example.absoluteweb.server.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "accounts")
public class Accounts {

    @Id
    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "lastactive")
    private int lastactive;

    @Column(name = "accessLevel" , columnDefinition = "TINYINT")
    private int accessLevel;

    @Column(name = "lastIP", length = 15)
    private String lastIP;

    @Column(name = "lastHWID")
    private String lastHWID;

    @Column(name = "lastServerId")
    private int lastServerId;

    @Column(name = "ban_expire")
    private int ban_expire;

    @Column(name = "allow_ip")
    private String allow_ip;

    @Column(name = "l2email")
    private String l2email;

    @Column(name = "privatekey")
    private String privatekey;

    public Accounts() {
    }

    public Accounts(String login, String password, String l2email) {
        this.login = login;
        this.password = password;
        this.l2email = l2email;
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

    public int getLastactive() {
        return lastactive;
    }

    public void setLastactive(int lastactive) {
        this.lastactive = lastactive;
    }

    public int getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(int accessLevel) {
        this.accessLevel = accessLevel;
    }

    public String getLastIP() {
        return lastIP;
    }

    public void setLastIP(String lastIP) {
        this.lastIP = lastIP;
    }

    public String getLastHWID() {
        return lastHWID;
    }

    public void setLastHWID(String lastHWID) {
        this.lastHWID = lastHWID;
    }

    public int getLastServerId() {
        return lastServerId;
    }

    public void setLastServerId(int lastServerId) {
        this.lastServerId = lastServerId;
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

    public String getL2email() {
        return l2email;
    }

    public void setL2email(String l2email) {
        this.l2email = l2email;
    }

    public String getPrivatekey() {
        return privatekey;
    }

    public void setPrivatekey(String privatekey) {
        this.privatekey = privatekey;
    }



}
