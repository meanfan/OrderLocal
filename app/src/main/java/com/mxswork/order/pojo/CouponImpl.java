package com.mxswork.order.pojo;

import android.content.Context;

public interface CouponImpl {
    float calcPrice(Context context, Order order);
}
