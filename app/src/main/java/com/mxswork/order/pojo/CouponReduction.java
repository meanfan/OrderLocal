package com.mxswork.order.pojo;

import android.content.Context;

public class CouponReduction extends Coupon {
    private float  threshold;
    private float reduction;

    @Override
    public float calcPrice(float price) {
        if(price>=threshold){
            float rst= price-reduction;
            if(rst<0f){
                rst=0f;
            }
            return rst;
        }else {
            return price;
        }
    }

    public float getThreshold() {
        return threshold;
    }

    public void setThreshold(float threshold) {
        this.threshold = threshold;
    }

    public float getReduction() {
        return reduction;
    }

    public void setReduction(float reduction) {
        this.reduction = reduction;
    }
}
