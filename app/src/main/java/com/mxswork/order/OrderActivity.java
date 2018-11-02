package com.mxswork.order;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mxswork.order.pojo.Dish;
import com.mxswork.order.pojo.DishPackage;
import com.mxswork.order.pojo.Order;
import com.mxswork.order.pojo.OrderDishInfo;
import com.mxswork.order.utils.LocalJsonHelper;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class OrderActivity extends AppCompatActivity {
    private static final String TAG = "OrderActivity";
    private RelativeLayout in_desk,in_id,in_time;
    private LinearLayout ll_order_list_dish;
    private TextView tv_order_list_total_price;
    private Order order;
    private String time;
    private String totalPriceString;

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
        totalPriceString = String.format("￥%.1f",order.getTotal_price());
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
            Dish dish = LocalJsonHelper.readDishById(this,id);
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

            View itemView  = LayoutInflater.from(this).inflate(R.layout.item_order_info_dish,null);

            ImageView iv_pic = itemView.findViewById(R.id.iv_order_list_pic);
            iv_pic.setImageBitmap(LocalJsonHelper.readDishPic(this,dish));

            TextView tv_order_list_name = itemView.findViewById(R.id.tv_order_list_name);

            tv_order_list_name.setText(name_flavour);

            TextView tv_order_dish_count = itemView.findViewById(R.id.tv_order_dish_count);
            tv_order_dish_count.setText(countString);

            TextView tv_order_dish_price = itemView.findViewById(R.id.tv_order_dish_price);
            tv_order_dish_price.setText(priceString);

            if(dish instanceof DishPackage){
                //TODO 针对套餐显示套餐内容
            }
            ll_order_list_dish.addView(itemView);
        }

        View totalView = LayoutInflater.from(this).inflate(R.layout.item_order_info_dish_total,null);
        tv_order_list_total_price = totalView.findViewById(R.id.tv_order_list_total_price);
        tv_order_list_total_price.setText(totalPriceString);
        ll_order_list_dish.addView(totalView);
    }
}
