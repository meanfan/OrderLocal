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
    public float calcPrice(Context context, Order order) {
        if(order!=null){
            float price_sum=0;
            OrderDishInfo[] orderDishInfo = order.getDishes();
            for(int i=0;i<orderDishInfo.length;i++){
                Dish dish = LocalJsonHelper.getDishById(context,orderDishInfo[i].getId());
                price_sum+=dish.getPrice()*orderDishInfo[i].getCount();
            }
            return price_sum;
        }
        return 0;
    }

    @Override
    public float calcPrice(float price) {
        return price;
    }
}
