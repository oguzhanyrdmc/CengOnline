package com.deuceng.cengonline;

public class Message {

    private String ID;
    private String toID;
    private String fromID;
    private String text;
    private String date;

    public Message() {
    }

    public Message(String ID, String toID, String fromID, String text, String date) {
        this.ID = ID;
        this.toID = toID;
        this.fromID = fromID;
        this.text = text;
        this.date = date;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getToID() {
        return toID;
    }

    public void setToID(String toID) {
        this.toID = toID;
    }

    public String getFromID() {
        return fromID;
    }

    public void setFromID(String fromID) {
        this.fromID = fromID;
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
