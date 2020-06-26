package dev.setephano.chatapp;

import java.util.Date;

public class ChatMessage {
    private String messageText;
    private long messageTime;
    private String messageUser;

    public ChatMessage(String messageText2, String messageUser2) {
        this.messageText = messageText2;
        this.messageUser = messageUser2;
        this.messageTime = new Date().getTime();
    }

    public ChatMessage() {
    }

    public String getMessageText() {
        return this.messageText;
    }

    public void setMessageText(String messageText2) {
        this.messageText = messageText2;
    }

    public String getMessageUser() {
        return this.messageUser;
    }

    public void setMessageUser(String messageUser2) {
        this.messageUser = messageUser2;
    }

    public long getMessageTime() {
        return this.messageTime;
    }

    public void setMessageTime(long messageTime2) {
        this.messageTime = messageTime2;
    }
}
