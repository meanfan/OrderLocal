package com.mxswork.order.pojo;

public class DishSingle extends Dish {

    @Override
    public String toString() {
        StringBuilder flavourBuilder = new StringBuilder();
        for(String f:getFlavour()){
            flavourBuilder.append(f+" ");
        }
        return String.format("id:%d, name:%s,flavour:%s, picPath:%s",id,name,flavourBuilder.toString(),picPath);
    }
}
