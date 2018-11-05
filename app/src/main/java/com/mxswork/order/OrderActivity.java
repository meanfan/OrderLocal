package com.mxswork.order;

import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mxswork.order.pojo.Coupon;
import com.mxswork.order.pojo.Dish;
import com.mxswork.order.pojo.DishPackage;
import com.mxswork.order.pojo.Order;
import com.mxswork.order.pojo.OrderDishInfo;
import com.mxswork.order.utils.LocalJsonHelper;

import java.text.DateFormat;
import java.util.Date;

public class OrderActivity extends AppCompatActivity {
    private static final String TAG = "OrderActivity";
    private RelativeLayout in_desk,in_id,in_time;
    private LinearLayout ll_order_list_dish;
    private TextView tv_order_coupon_title,tv_order_coupon_discount,tv_order_list_total_price,tv_order_list_final_price;
    private Order order;
    private String time;
    private Coupon coupon;
    private boolean isUseCoupon;
    private String couponName;
    private String couponPriceString;
    private String totalPriceString;
    private String finalPriceString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        android.support.v7.widget.Toolbar tb =findViewById(R.id.tb_order);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //Log.d(TAG, "onCreate: "+order.toString());
        initData();
        initOrderView();

    }

    private void initData(){
        order = (Order)getIntent().getSerializableExtra("order");
        Date date = new Date(order.getTime());
        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        time = dateFormat.format(date);
        totalPriceString =String.format("￥%.1f",order.getTotalPrice());
        finalPriceString = String.format("￥%.1f",order.getFinalPrice());
        int useCouponId = order.getUseCouponId();
        if(useCouponId>-1) {
            coupon = LocalJsonHelper.getCouponById(this,useCouponId);
            couponName = coupon.getName();
            couponPriceString = String.format("-￥%.1f",order.getTotalPrice()-order.getFinalPrice());
            isUseCoupon =true;
        }else {
           isUseCoupon = false;
        }
    }

    private void initOrderView(){
        in_desk = findViewById(R.id.in_desk);
        in_id = findViewById(R.id.in_id);
        in_time = findViewById(R.id.in_time);
        ((TextView)in_desk.findViewById(R.id.tv_left)).setText("桌号");
        ((TextView)in_id.findViewById(R.id.tv_left)).setText("订单号");
        ((TextView)in_time.findViewById(R.id.tv_left)).setText("下单时间");
        ((TextView)in_desk.findViewById(R.id.tv_right)).setText(String.valueOf(order.getDesk()));
        ((TextView)in_id.findViewById(R.id.tv_right)).setText(String.valueOf(order.getId()));
        ((TextView)in_time.findViewById(R.id.tv_right)).setText(time);

        ll_order_list_dish = findViewById(R.id.ll_order_list_dish);
        OrderDishInfo[] orderDishInfo = order.getDishes();
        for(int i=0;i<orderDishInfo.length;i++){
            int id = orderDishInfo[i].getId();
            int count = orderDishInfo[i].getCount();
            String flavour = orderDishInfo[i].getFlavour();
            Dish dish = LocalJsonHelper.getDishById(this,id);
            String name = dish.getName();
            float price = dish.getPrice()*count;
            String name_flavour;
            if(flavour==null || flavour.length()==0){
                name_flavour = String.format("%s", name);
            }else {
                name_flavour = String.format("%s / %s", name,flavour);
            }
            String countString = String.format("x%d",count);
            String priceString = String.format("￥%.1f",price);

            LinearLayout itemView  = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.item_order_info_dish,null);

            ImageView iv_pic = itemView.findViewById(R.id.iv_order_list_pic);
            iv_pic.setImageBitmap(LocalJsonHelper.readDishPic(this,dish));

            TextView tv_order_list_name = itemView.findViewById(R.id.tv_order_list_name);

            tv_order_list_name.setText(name_flavour);

            TextView tv_order_dish_count = itemView.findViewById(R.id.tv_order_dish_count);
            tv_order_dish_count.setText(countString);

            TextView tv_order_dish_price = itemView.findViewById(R.id.tv_order_dish_price);
            tv_order_dish_price.setText(priceString);

            if(dish instanceof DishPackage){
                DishPackage dishPackage = (DishPackage) dish;
                DishPackage.DishInfo[] dishInfos = dishPackage.getDishes();
                LinearLayout packageDishView = itemView.findViewById(R.id.ll_order_list_item_package_dish);
                for(int j=0;j<dishInfos.length;j++){
                    Log.d(TAG, "initOrderView: singleDish"+dishInfos[j].getId());
                    Dish singleDish = LocalJsonHelper.getDishById(this,dishInfos[j].getId());
                    String singleDishName = singleDish.getName();
                    int singleDishCount = dishInfos[j].getAmount();
                    float singleDishPrice = singleDish.getPrice()*singleDishCount;
                    String singleDishCountString = String.format("x%d",singleDishCount);
                    String singleDishPriceString = String.format("￥%.1f",singleDishPrice);

                    View view = LayoutInflater.from(this).inflate(R.layout.item_order_info_dish,null);
                    ((TextView)view.findViewById(R.id.tv_order_list_name)).setText(singleDishName);
                    ((TextView)view.findViewById(R.id.tv_order_dish_count)).setText(singleDishCountString);
                    ((TextView)view.findViewById(R.id.tv_order_dish_price)).setText(singleDishPriceString);
                    packageDishView.addView(view);
                }
                packageDishView.setVisibility(View.GONE);
                TextView tv_expand_hide = (TextView)LayoutInflater.from(this).inflate(R.layout.item_show_hide_textview,null);
                tv_expand_hide.setVisibility(View.VISIBLE);
                tv_expand_hide.setText("﹀");
                tv_expand_hide.setTag("state_hide");
                tv_expand_hide.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        View parentView = (View) view.getParent();
                        View packageDishView = parentView.findViewById(R.id.ll_order_list_item_package_dish);
                        if(TextUtils.equals((String)view.getTag(),"state_hide")){
                            packageDishView.setVisibility(View.VISIBLE);
                            ((TextView)view).setText("︿");
                            view.setTag("state_expand");
                            Log.d(TAG, "onClick: showed");
                        }else {

                            packageDishView.setVisibility(View.GONE);
                            ((TextView)view).setText("﹀");
                            view.setTag("state_hide");
                            Log.d(TAG, "onClick: hided");

                        }
                    }
                });
                itemView.addView(tv_expand_hide);
            }
            ll_order_list_dish.addView(itemView);
        }

        View couponView = LayoutInflater.from(this).inflate(R.layout.item_order_info_coupon,null);
        tv_order_coupon_title = couponView.findViewById(R.id.tv_order_coupon_title);
        tv_order_coupon_discount = couponView.findViewById(R.id.tv_order_coupon_discount);
        if(isUseCoupon){
            String couponTitle = String.format("使用优惠券：%s",couponName);
            tv_order_coupon_title.setText(couponTitle);
            tv_order_coupon_discount.setText(couponPriceString);
        }else {
            tv_order_coupon_title.setText("未使用优惠券");
            tv_order_coupon_title.setTextColor(getResources().getColor(R.color.colorDefaultGray));
            tv_order_coupon_discount.setVisibility(View.GONE);
        }
        ll_order_list_dish.addView(couponView);

        View totalView = LayoutInflater.from(this).inflate(R.layout.item_order_info_dish_total,null);
        tv_order_list_total_price = totalView.findViewById(R.id.tv_order_list_total_price);
        if(isUseCoupon){
            tv_order_list_total_price.setText(totalPriceString);
            tv_order_list_total_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);//添加删除线
        }else {
            tv_order_list_total_price.setVisibility(View.GONE);
        }

        tv_order_list_final_price = totalView.findViewById(R.id.tv_order_list_final_price);
        tv_order_list_final_price.setText(finalPriceString);

        ll_order_list_dish.addView(totalView);
    }
}
