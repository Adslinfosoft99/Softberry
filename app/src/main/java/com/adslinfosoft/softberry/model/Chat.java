package com.adslinfosoft.softberry.model;

public class Chat {
    private int cacId;
    private String message;
    private String messagedate;
    private String adminname;
    private String clientname;
    private String messageusertype;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessagedate() {
        return messagedate;
    }

    public void setMessagedate(String messagedate) {
        this.messagedate = messagedate;
    }

    public String getAdminname() {
        return adminname;
    }

    public void setAdminname(String adminname) {
        this.adminname = adminname;
    }

    public String getClientname() {
        return clientname;
    }

    public void setClientname(String clientname) {
        this.clientname = clientname;
    }

    public String getMessageusertype() {
        return messageusertype;
    }

    public void setMessageusertype(String messageusertype) {
        this.messageusertype = messageusertype;
    }

    public int getCacId() {
        return cacId;
    }

    public void setCacId(int cacId) {
        this.cacId = cacId;
    }
}
