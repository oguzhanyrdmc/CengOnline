package com.deuceng.cengonline;

public class SubmittedWork {

    private String id;
    private String StudentId;
    private String sentDate;
    private String text;

    SubmittedWork(){

    }

    public SubmittedWork(String id, String studentId, String sentDate, String text) {
        this.id = id;
        StudentId = studentId;
        this.sentDate = sentDate;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentId() {
        return StudentId;
    }

    public void setStudentId(String studentId) {
        StudentId = studentId;
    }

    public String getSentDate() {
        return sentDate;
    }

    public void setSentDate(String sentDate) {
        this.sentDate = sentDate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
