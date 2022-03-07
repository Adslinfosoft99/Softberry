package com.adslinfosoft.softberry.model;

import java.io.Serializable;

public class Message implements Serializable {
    private String mobile, message, createdAt;
    private String userName;
    private int id, replyid;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String id) {
        this.mobile = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReplyId() {
        return replyid;
    }

    public void setReplyId(int replyid) {
        this.replyid = replyid;
    }
}
