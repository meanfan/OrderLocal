package com.mxswork.order;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mxswork.order.fragment.DishFragment;
import com.mxswork.order.pojo.Dish;
import com.mxswork.order.pojo.User;
import com.mxswork.order.utils.LocalJsonHelper;

public class SelectCouponActivity extends AppCompatActivity {
    private static final String TAG = "SelectCouponActivity";
    public static final String RESULT_EXTRA_COUPON = "selectedCouponId";
    private User user;
    private User.UserCouponInfo[] userCouponInfos;
    private Button btn_select_coupon;
    private RadioGroup rg_select_coupon;
    private int selectedCouponId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_coupon);
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
        initListener();
    }
    private void initData(){
        user = (User) getIntent().getSerializableExtra("user");
        userCouponInfos = user.getCouponInfo();
        selectedCouponId = getIntent().getIntExtra("selectedCouponId",-1);
    }
    private void initListView(){
        if(userCouponInfos.length==0){
            findViewById(R.id.tv_coupon_none).setVisibility(View.VISIBLE);
        }
        rg_select_coupon = findViewById(R.id.rg_select_coupon);
        RadioButton firstRadioButton = (RadioButton) LayoutInflater.from(this).inflate(R.layout.item_select_coupon_radio_button,null);
        firstRadioButton.setId(0);
        firstRadioButton.setText("不使用优惠券");
        rg_select_coupon.addView(firstRadioButton);
        for(int i=0;i<userCouponInfos.length;i++){
            String name = LocalJsonHelper.getCouponById(this,userCouponInfos[i].getId()).getName();
            int amount = userCouponInfos[i].getCount();
            String title = String.format("%s （ %d 张）",name,amount);
            RadioButton radioButton = (RadioButton) LayoutInflater.from(this).inflate(R.layout.item_select_coupon_radio_button,null);
            radioButton.setId(userCouponInfos[i].getId());  //为方便将优惠券id设置为radioButton的id
            radioButton.setText(title);
            rg_select_coupon.addView(radioButton);
        }
        rg_select_coupon.clearCheck();
        if(selectedCouponId == -1){
            rg_select_coupon.check(firstRadioButton.getId());
        }else {
            rg_select_coupon.check(selectedCouponId);
        }
    }

    private void initListener(){
        btn_select_coupon = findViewById(R.id.btn_select_coupon);
        btn_select_coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = rg_select_coupon.getCheckedRadioButtonId();
                Log.d(TAG, "onClick: selectID:"+selectedId);
                if(selectedId > 0){
                    Intent intent = new Intent();
                    intent.putExtra(RESULT_EXTRA_COUPON,selectedId);
                    setResult(DishFragment.RESULT_OK,intent);
                }
                else {
                    setResult(DishFragment.RESULT_CANCEL);
                }
                finish();
            }
        });
    }
}
