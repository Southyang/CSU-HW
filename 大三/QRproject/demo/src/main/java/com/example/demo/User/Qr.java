package com.example.demo.User;

public class Qr {
    private Integer id;
    private String name;
    private String tel;
    private String address;
    private String kinds;
    private String permission;
    private String state;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTel() {
        return tel;
    }

    public String getAddress() {
        return address;
    }

    public String getKinds() {
        return kinds;
    }

    public String getPermission() {
        return permission;
    }

    public String getState() {
        return state;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setKinds(String kinds) {
        this.kinds = kinds;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public void setState(String state) {
        this.state = state;
    }
}
