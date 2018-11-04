package com.mxswork.order.pojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class User implements Serializable {
    @SerializedName("uid")
    private int uid;
    @SerializedName("phone")
    private String phone;
    @SerializedName("ordersId")
    private int[] ordersId;
    @SerializedName("couponInfo")
    private UserCouponInfo[] couponInfo;
    @SerializedName("name")
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

    public UserCouponInfo[] getCouponInfo() {
        return couponInfo;
    }

    public void setCouponInfo(UserCouponInfo[] couponInfo) {
        this.couponInfo = couponInfo;
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
                ", couponInfo=" + Arrays.toString(couponInfo) +
                ", name='" + name + '\'' +
                '}';
    }

    public static class UserCouponInfo implements Serializable{
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

        @Override
        public String toString() {
            return "UserCouponInfo{" +
                    "id=" + id +
                    ", count=" + count +
                    '}';
        }
    }
}
