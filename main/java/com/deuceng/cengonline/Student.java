package com.deuceng.cengonline;

public class Student extends User {

    private int number;

    public Student(){

    }

    public Student(String id, String name, String surname, String nickName, int number) {
        super(id, name, surname, nickName);
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
