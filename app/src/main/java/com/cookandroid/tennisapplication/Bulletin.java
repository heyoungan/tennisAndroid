package com.cookandroid.tennisapplication;

public class Bulletin {
    private final String title;
    private final String date;
    private final String content;

    public Bulletin(String title, String date, String content) {
        this.title = title;
        this.date = date;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }
}
