package com.deuceng.cengonline;

import androidx.annotation.NonNull;

public class Announcement {
    private String id;
    private String title;
    private String content;

    public Announcement(){

    }

    public Announcement(String id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @NonNull
    @Override
    public String toString() {
        return getId()+" - "+getTitle()+" - "+ getContent();
    }
}
