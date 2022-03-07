package com.adslinfosoft.softberry.model;

public class ExpandedMenuModel {

    public String menuName, url;
    public boolean hasChildren, isGroup;

    public ExpandedMenuModel(String menuName, boolean isGroup, boolean hasChildren) {

        this.menuName = menuName;
        this.isGroup = isGroup;
        this.hasChildren = hasChildren;
    }
}
