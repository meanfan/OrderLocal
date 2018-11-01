package com.mxswork.order;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mxswork.order.adpater.OrderListViewAdapter;
import com.mxswork.order.pojo.Order;
import com.mxswork.order.pojo.User;
import com.mxswork.order.utils.LocalJsonHelper;

import java.util.List;


public class FragmentTwo extends Fragment {
    public static final String TAG = "FragmentTwo";
    private ListView ordersListView;
    private List<Order> orders;
    private User user;
    private OrderListViewAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_two,container,false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
        initView();
        //initListener();
    }
    
    private void initData(){
        //默认先设置预置的游客账号
        setUser(LocalJsonHelper.readUsers(getActivity()).get(0));
        orders = LocalJsonHelper.readOrders(getContext());
        for(Order order:orders){
            if(order.getUid() != user.getUid()){
                orders.remove(order);
            }
        }
        Log.d(TAG, "initData: orders: "+orders.toString());
    }

    private void initView(){
        ordersListView = getActivity().findViewById(R.id.lv_orders);
        adapter = new OrderListViewAdapter(getActivity());
        adapter.updateOrders(orders);
        ordersListView.setAdapter(adapter);
        ordersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //TODO 详情页面
            }
        });
    }

    public void refresh(){
        initData();
        adapter.updateOrders(orders);

    }

    public void setUser(User user){
        this.user = user;
    }


}

