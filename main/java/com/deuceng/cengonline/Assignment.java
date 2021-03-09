package com.deuceng.cengonline;

public class Assignment {

    private String id;
    private String name;
    private String info;
    private String dueDate;

    public Assignment(){

    }

    public Assignment(String id, String name, String info, String dueDate) {
        this.id = id;
        this.name = name;
        this.info = info;
        this.dueDate = dueDate;
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

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
}
