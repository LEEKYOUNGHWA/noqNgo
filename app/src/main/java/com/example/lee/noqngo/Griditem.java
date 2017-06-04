package com.example.lee.noqngo;

/**
 * Created by jang on 2017-06-04.
 */

public class Griditem {

    private String num;
    private String token;

    public Griditem(String name, String url) {
        this.num=name;
        this.token=url;
    }

    public String getNum() {
        return num;
    }
    public void setNum(String num) {
        this.num = num;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

}