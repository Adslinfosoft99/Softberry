package com.adslinfosoft.softberry.model;

import java.io.Serializable;

/**
 * Created by shreshtha on 05/07/16.
 */
public class Notification implements Serializable {
    private int Id;
    private String Message;
    private String Path;
    private String Date;
    private Boolean IsPDF;
    private String ImageURL;
    private String JobNo;
    private String CorrectionCount;
    private String CoordinatorEmail;
    private int isRead;
    private String title;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getPath() {
        return Path;
    }

    public void setPath(String path) {
        Path = path;
    }


    public Boolean getPDF() {
        return IsPDF;
    }

    public void setPDF(Boolean PDF) {
        IsPDF = PDF;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
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

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
