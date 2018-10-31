package com.mxswork.order.pojo;

public class DiscountCoupon implements Coupon{
    private int id;
    private String name;
    private String type;
    private float rate;

    @Override
    public float calcPrice(Order order) {
//        if(order!=null){
//            float price_sum=0;
//            for(Dish dish:order.readDishes()){
//                price_sum+=dish.getPrice()*dish.getAmount();
//            }
//            return price_sum;
//        }
        return 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }
}
