package com.adslinfosoft.softberry.model;

/**
 * Created by Abhi on 13 Nov 2017 013.
 */

public class NotificationVO {
    private String title;
    private String message;
    private String iconUrl;
    private String action;
    private String actionDestination;
    private String time;
    private String ImageURL;
    private String Path;
    private int isRead;
    private int Id;
    private String Date;
    private Boolean IsPDF;
    private String JobNo;
    private String CorrectionCount;
    private String CoordinatorEmail;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getActionDestination() {
        return actionDestination;
    }

    public void setActionDestination(String actionDestination) {
        this.actionDestination = actionDestination;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public String getPath() {
        return Path;
    }

    public void setPath(String path) {
        Path = path;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public Boolean getPDF() {
        return IsPDF;
    }

    public void setPDF(Boolean PDF) {
        IsPDF = PDF;
    }

    public String getJobNo() {
        return JobNo;
    }

    public void setJobNo(String jobNo) {
        JobNo = jobNo;
    }

    public String getCorrectionCount() {
        return CorrectionCount;
    }

    public void setCorrectionCount(String correctionCount) {
        CorrectionCount = correctionCount;
    }

    public String getCoordinatorEmail() {
        return CoordinatorEmail;
    }

    public void setCoordinatorEmail(String coordinatorEmail) {
        CoordinatorEmail = coordinatorEmail;
    }
}
