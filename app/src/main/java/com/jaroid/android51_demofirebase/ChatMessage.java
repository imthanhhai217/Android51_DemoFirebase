package com.jaroid.android51_demofirebase;

public class ChatMessage {

    private String uid;
    private String email;
    private String name;
    private String avatar;
    private String date;
    private String message;

    public ChatMessage() {
    }

    public ChatMessage(String uid, String email, String name, String avatar, String date, String message) {
        this.uid = uid;
        this.email = email;
        this.name = name;
        this.avatar = avatar;
        this.date = date;
        this.message = message;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "uid='" + uid + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                ", date='" + date + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
