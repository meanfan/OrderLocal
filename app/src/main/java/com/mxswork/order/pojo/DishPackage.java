package com.mxswork.order.pojo;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class DishPackage implements DishImpl{
    private int id;
    private String name;
    private String tag;
    private boolean isFeature;
    private DishInfo[] dishes; // dish的id & dish的数量
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

    public DishInfo[] getDishes() {
        return dishes;
    }

    public void setDishes(DishInfo[] dishes) {
        this.dishes = dishes;
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
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("id:%d, name:%s, tag:%s ,dishes:",id,name,tag));
        for(int i=0;i<dishes.length;i++){
            stringBuilder.append(dishes[i].toString()+" ");
        }
        return stringBuilder.toString();
    }
}

class DishInfo{
    private int id;
    private int amount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "{id=" + id +
                ", amount=" + amount +
                '}';
    }
}
