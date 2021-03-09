package com.deuceng.cengonline;

import java.util.ArrayList;

public class Course {
    private String id;
    private String code;
    private String name;
    private String capacity;


    public Course(){

    }

    public Course(String id, String code, String name, String capacity) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.capacity = capacity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return "Course Name: " +name;
    }
}
