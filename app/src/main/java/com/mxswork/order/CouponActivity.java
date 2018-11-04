package com.mxswork.order;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mxswork.order.pojo.User;
import com.mxswork.order.utils.LocalJsonHelper;

public class CouponActivity extends AppCompatActivity {
    private static final String TAG = "CouponActivity";
    User user;
    User.UserCouponInfo[] userCouponInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);
        android.support.v7.widget.Toolbar tb =findViewById(R.id.tb_coupon);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initData();
        initListView();
    }

    private void initData(){
        user = (User) getIntent().getSerializableExtra("user");
        //Log.d(TAG, "initData: user:" + user.toString());
        userCouponInfos = user.getCouponInfo();
    }

    private void initListView(){
        if(userCouponInfos.length==0){
            //TODO 显示无优惠券
        }
        LinearLayout ll_coupon_list = findViewById(R.id.ll_coupon_list);
        for(int i=0;i<userCouponInfos.length;i++){
            String name = LocalJsonHelper.getCouponById(this,userCouponInfos[i].getId()).getName();
            int amount = userCouponInfos[i].getCount();
            String amountString = String.format("x%d",amount);
            View view = LayoutInflater.from(this).inflate(R.layout.item_coupon_list,null);
            TextView tv_coupon_list_name = view.findViewById(R.id.tv_coupon_list_name);
            TextView tv_coupon_list_amount = view.findViewById(R.id.tv_coupon_list_amount);
            tv_coupon_list_name.setText(name);
            tv_coupon_list_amount.setText(amountString);
            ll_coupon_list.addView(view);
        }
    }
}
