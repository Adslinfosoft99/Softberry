package com.adslinfosoft.softberry.model;

import java.io.Serializable;

public class GridItem implements Serializable{
    private String image;
    private String title;
    private int groupId;
    private int ImgCount;
    private boolean isPdf;

    public GridItem() {
        super();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getImgCount() {
        return ImgCount;
    }

    public void setImgCount(int imgCount) {
        ImgCount = imgCount;
    }

    public boolean isPdf() {
        return isPdf;
    }

    public void setPdf(boolean pdf) {
        isPdf = pdf;
    }
}
