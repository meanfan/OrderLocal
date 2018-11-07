package com.mxswork.order.pojo;


import android.content.Context;

import com.mxswork.order.utils.LocalJsonHelper;

public class Coupon implements CouponImpl {
    protected int id;
    protected String name;

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

    @Override
    public float calcPrice(float price) {
        return price;
    }
}
