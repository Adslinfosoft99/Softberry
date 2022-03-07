package com.adslinfosoft.softberry.Utils.dialog;

import java.io.Serializable;

public class ListModel implements Serializable {


    private int ID;
    private String Name;
    private int fId;
    private int fgId;
    private String company;
    private String imgPath;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getfId() {
        return fId;
    }

    public void setfId(int fId) {
        this.fId = fId;
    }

    public int getFgId() {
        return fgId;
    }

    public void setFgId(int fgId) {
        this.fgId = fgId;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}
