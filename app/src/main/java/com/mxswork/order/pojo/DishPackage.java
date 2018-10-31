package com.mxswork.order.pojo;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class DishPackage extends Dish{
    private DishInfo[] dishes; // dish的id & dish的数量

    public DishInfo[] getDishes() {
        return dishes;
    }

    public void setDishes(DishInfo[] dishes) {
        this.dishes = dishes;
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

    static class DishInfo{
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

}
