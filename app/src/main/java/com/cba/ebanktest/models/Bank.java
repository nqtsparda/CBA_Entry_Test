package com.cba.ebanktest.models;

/**
 * Created by TungNQ5 on 2016/09/14.
 */
public class Bank {
    private int id;
    private String name;
    private String code;

    public Bank() {
    }

    public Bank(String name, String code) {
        this.code = code;
        this.name = name;
    }

    public Bank(int id, String name, String code) {
        this.code = code;
        this.id = id;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
