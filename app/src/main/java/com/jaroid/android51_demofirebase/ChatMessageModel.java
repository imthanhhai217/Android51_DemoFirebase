package com.jaroid.android51_demofirebase;

public class ChatMessageModel {
    private String name;
    private ChatMessage chatMessage;

    public ChatMessageModel() {
    }

    public ChatMessageModel(String name, ChatMessage chatMessage) {
        this.name = name;
        this.chatMessage = chatMessage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }

    @Override
    public String toString() {
        return "ChatMessageModel{" +
                "name='" + name + '\'' +
                ", chatMessage=" + chatMessage +
                '}';
    }
}
