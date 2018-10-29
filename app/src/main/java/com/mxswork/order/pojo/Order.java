package com.mxswork.order.pojo;

import java.util.Date;
import java.util.List;

public class Order {
    private int id;
    private User user;
    private List<DishImpl> dishes;
    private int desk;
    private Date time;
    private float total_price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<DishImpl> getDishes() {
        return dishes;
    }

    public void setDishes(List<DishImpl> dishes) {
        this.dishes = dishes;
    }

    public int getDesk() {
        return desk;
    }

    public void setDesk(int desk) {
        this.desk = desk;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public float getTotal_price() {
        return total_price;
    }

    public void setTotal_price(float total_price) {
        this.total_price = total_price;
    }
}
