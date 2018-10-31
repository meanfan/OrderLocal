package com.mxswork.order.pojo;

public class Dish {
    protected int id;
    protected String name;
    protected String tag;
    protected boolean isFeature;
    protected String desc;
    protected float price;
    protected String picPath;
    protected int amount;
    protected String selectedFlavour;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean isFeature() {
        return isFeature;
    }

    public void setFeature(boolean feature) {
        isFeature = feature;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getSelectedFlavour() {
        return selectedFlavour;
    }

    public void setSelectedFlavour(String selectedFlavour) {
        this.selectedFlavour = selectedFlavour;
    }
}