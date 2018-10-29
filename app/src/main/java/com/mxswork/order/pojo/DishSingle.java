package com.mxswork.order.pojo;


import java.util.List;

public class DishSingle implements DishImpl{
    private int id;
    private String name;
    private String tag;
    private boolean isFeature;
    private List<String> flavour;
    private String desc;
    private float price;
    private String picPath;
    private int amount;

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

    public List<String> getFlavour() {
        return flavour;
    }

    public void setFlavour(List<String> flavour) {
        this.flavour = flavour;
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

    @Override
    public String toString() {
        StringBuilder flavourBuilder = new StringBuilder();
        for(String f:flavour){
            flavourBuilder.append(f+" ");
        }
        return String.format("id:%d, name:%s,flavour:%s, picPath:%s",id,name,flavourBuilder.toString(),picPath);
    }
}
