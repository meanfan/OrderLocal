package com.mxswork.order.pojo;

import java.io.Serializable;

public class OrderDishInfo implements Serializable {
        int id;
        int count;
        String flavour;

        public String getFlavour() {
            return flavour;
        }

        public void setFlavour(String flavour) {
            this.flavour = flavour;
        }

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

    @Override
    public String toString() {
        return "OrderDishInfo{" +
                "id=" + id +
                ", count=" + count +
                ", flavour='" + flavour + '\'' +
                '}';
    }
}
