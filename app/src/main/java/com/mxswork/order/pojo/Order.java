package com.mxswork.order.pojo;

import java.io.Serializable;
import java.util.Arrays;

public class Order implements Serializable {
    private int id;
    private int uid;
    private OrderDishInfo[] dishes;
    private int desk;
    private long time;
    private int useCouponId;
    private float totalPrice;
    private float finalPrice;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public OrderDishInfo[] getDishes() {
        return dishes;
    }

    public void setDishes(OrderDishInfo[] dishes) {
        this.dishes = dishes;
    }

    public int getDesk() {
        return desk;
    }

    public void setDesk(int desk) {
        this.desk = desk;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getUseCouponId() {
        return useCouponId;
    }

    public void setUseCouponId(int useCouponId) {
        this.useCouponId = useCouponId;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public float getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(float finalPrice) {
        this.finalPrice = finalPrice;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", uid=" + uid +
                ", dishes=" + Arrays.toString(dishes) +
                ", desk=" + desk +
                ", time=" + time +
                ", useCouponId=" + useCouponId +
                ", totalPrice=" + totalPrice +
                ", finalPrice=" + finalPrice +
                '}';
    }
}
