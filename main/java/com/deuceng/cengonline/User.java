package com.deuceng.cengonline;

public abstract class User implements IUser{

    private String id;
    private String name;
    private String surname;
    private String nickName;

    public User(){

    }

    public User(String id, String name, String surname, String nickName) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.nickName = nickName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }




}
