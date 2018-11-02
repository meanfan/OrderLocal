package com.mxswork.order;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.mxswork.order.pojo.Order;

public class OrderActivity extends AppCompatActivity {
    private static final String TAG = "OrderActivity";
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
        Order order = (Order)getIntent().getSerializableExtra("order");
        Log.d(TAG, "onCreate: "+order.toString());
        ((TextView)findViewById(R.id.tv_order_detail)).setText(order.toString());
    }
}
