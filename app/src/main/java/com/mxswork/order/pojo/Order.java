package com.mxswork.order.pojo;

import java.util.Date;

public class Order {
    private int id;
    private int uid;
    private DishInfo[] dishes;
    private int desk;
    private Date time;
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

    public DishInfo[] getDishes() {
        return dishes;
    }

    public void setDishes(DishInfo[] dishes) {
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

    class DishInfo{
        int id;
        int count;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
