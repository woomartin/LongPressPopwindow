package com.example.app.longpresspopwindow;

public class Message {
    private String content;
    private String time;
    private boolean isSent; // true表示发送的消息，false表示接收的消息

    public Message(String content, String time, boolean isSent) {
        this.content = content;
        this.time = time;
        this.isSent = isSent;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }
}
