package com.mxswork.order.pojo;


import android.support.annotation.BinderThread;

import java.util.List;

public class DishSingle extends Dish {

    private List<String> flavour;

    public List<String> getFlavour() {
        return flavour;
    }

    public void setFlavour(List<String> flavour) {
        this.flavour = flavour;
    }

    @Override
    public String toString() {
        StringBuilder flavourBuilder = new StringBuilder();
        for(String f:flavour){
            flavourBuilder.append(f+" ");
        }
        return String.format("id:%d, name:%s,flavour:%s, picPath:%s",id,name,flavourBuilder.toString(),picPath);
    }
}
