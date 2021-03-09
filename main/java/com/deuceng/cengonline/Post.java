package com.deuceng.cengonline;

public class Post {

    private String id;
    private String senderId;
    private String text;
    private String date;

    public Post() {
    }

    public Post(String id, String senderId, String text, String date) {
        this.id = id;
        this.senderId = senderId;
        this.text = text;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
