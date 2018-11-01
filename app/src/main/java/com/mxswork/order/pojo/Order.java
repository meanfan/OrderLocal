package com.mxswork.order.pojo;

import java.util.Arrays;

public class Order {
    private int id;
    private int uid;
    private OrderDishInfo[] dishes;
    private int desk;
    private long time;
    private float total_price;

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

    public float getTotal_price() {
        return total_price;
    }

    public void setTotal_price(float total_price) {
        this.total_price = total_price;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", uid=" + uid +
                ", dishes=" + Arrays.toString(dishes) +
                ", desk=" + desk +
                ", time=" + time +
                ", total_price=" + total_price +
                '}';
    }
}
