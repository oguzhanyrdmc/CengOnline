package com.deuceng.cengonline;

public class Teacher extends User {

    private String title;

    public Teacher() {

    }

    public Teacher(String id, String name, String surname, String nickName, String title) {
        super(id, name, surname, nickName);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
