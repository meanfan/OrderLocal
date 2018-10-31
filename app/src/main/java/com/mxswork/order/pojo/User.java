package com.mxswork.order.pojo;

import java.util.Arrays;
import java.util.List;

public class User {
    private int uid;
    private String phone;
    private int[] ordersId;
    private int[] couponsId;
    private String name;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int[] getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(int[] ordersId) {
        this.ordersId = ordersId;
    }

    public int[] getCouponsId() {
        return couponsId;
    }

    public void setCouponsId(int[] couponsId) {
        this.couponsId = couponsId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", phone='" + phone + '\'' +
                ", ordersId=" + Arrays.toString(ordersId) +
                ", couponsId=" + Arrays.toString(couponsId) +
                ", name='" + name + '\'' +
                '}';
    }
}
