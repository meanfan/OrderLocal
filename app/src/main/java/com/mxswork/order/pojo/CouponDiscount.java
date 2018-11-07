package com.mxswork.order.pojo;

import android.content.Context;

public class CouponDiscount extends Coupon{
    private float rate;

    @Override
    public float calcPrice(float price) {
        return price*rate;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }
}
