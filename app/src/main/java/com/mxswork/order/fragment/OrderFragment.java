package com.mxswork.order.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.mxswork.order.OrderActivity;
import com.mxswork.order.R;
import com.mxswork.order.adpater.OrderListViewAdapter;
import com.mxswork.order.pojo.Order;
import com.mxswork.order.pojo.User;
import com.mxswork.order.utils.LocalJsonHelper;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class OrderFragment extends Fragment {
    public static final String TAG = "OrderFragment";
    private ListView ordersListView;
    private List<Order> orders;
    private User user;
    private OrderListViewAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_order,container,false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ordersListView = getActivity().findViewById(R.id.lv_orders);
        ordersListView.setVerticalFadingEdgeEnabled(true);
        ordersListView.setFadingEdgeLength(50);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initData();
                initListView();
            }
        });
        //initListener();
    }

    private void initData(){
        //默认先设置预置的游客账号
        orders = LocalJsonHelper.readOrders(getContext());
        for(Order order:orders){
            if(order.getUid() != user.getUid()){
                orders.remove(order);
            }
        }
        sortOrdersByTime();
        Log.d(TAG, "initData: orders: ");
    }

    private void sortOrdersByTime(){
        Collections.sort(orders, new Comparator<Order>() {
            @Override
            public int compare(Order order, Order t1) {
                if(order.getTime() < t1.getTime()){
                    return 1;
                }else if(order.getTime() > t1.getTime()){
                    return -1;
                }else {
                    return 0;
                }
            }
        });
    }

    private void initListView(){
        adapter = new OrderListViewAdapter(getActivity());
        adapter.updateOrders(orders);
        ordersListView.setAdapter(adapter);
        ordersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showOrderActivity(orders.get(i));
            }
        });
    }

    private void showOrderActivity(Order order){
        Intent intent = new Intent(getActivity(),OrderActivity.class);
        intent.putExtra("order",order); //要确保Order及其嵌套类都实现序列化接口
        Log.d(TAG, "showOrderActivity: "+order.toString());
        startActivity(intent);
    }

    public void refresh(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initData();
                adapter.updateOrders(orders);
            }
        });
        ordersListView.startLayoutAnimation();
    }

    public void setUser(User user){
        this.user = user;
    }

    public void setOrders(List<Order> orders){
        this.orders = orders;
    }
}

