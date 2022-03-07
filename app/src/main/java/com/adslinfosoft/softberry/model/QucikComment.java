package com.adslinfosoft.softberry.model;

import java.io.Serializable;

/**
 * Created by alokgupta on 13/09/16.
 */
public class QucikComment implements Serializable {

    private int Id;
    private String QuickComment;
    private String Date;
    private String Jobno;
    private String JobDescription;
    private int IsAttchment;
    private String Name;
    private String Time;
    private String ImgPath;

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

    public String getQuickComment() {
        return QuickComment;
    }

    public void setQuickComment(String quickComment) {
        QuickComment = quickComment;
    }

    public String getJobno() {
        return Jobno;
    }

    public void setJobno(String jobno) {
        Jobno = jobno;
    }

    public String getJobDescription() {
        return JobDescription;
    }

    public void setJobDescription(String jobDescription) {
        JobDescription = jobDescription;
    }

    public int getIsAttchment() {
        return IsAttchment;
    }

    public void setIsAttchment(int isAttchment) {
        IsAttchment = isAttchment;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getImgPath() {
        return ImgPath;
    }

    public void setImgPath(String imgPath) {
        ImgPath = imgPath;
    }
}
