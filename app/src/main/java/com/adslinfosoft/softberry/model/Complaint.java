package com.adslinfosoft.softberry.model;

import java.io.Serializable;

public class Complaint implements Serializable {
    private int jcId;
    private String jobNo;
    private String status;
    private String issue;
    private String issueDescr;
    private String date;

    public int getJcId() {
        return jcId;
    }

    public void setJcId(int jcId) {
        this.jcId = jcId;
    }

    public String getJobNo() {
        return jobNo;
    }

    public void setJobNo(String jobNo) {
        this.jobNo = jobNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getIssueDescr() {
        return issueDescr;
    }

    public void setIssueDescr(String issueDescr) {
        this.issueDescr = issueDescr;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
