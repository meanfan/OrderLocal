package com.mxswork.order.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.mxswork.order.OrderActivity;
import com.mxswork.order.R;
import com.mxswork.order.adpater.BannerViewAdapter;
import com.mxswork.order.adpater.DishLeftListViewAdapter;
import com.mxswork.order.adpater.DishRightListViewAdapter;
import com.mxswork.order.pojo.Dish;
import com.mxswork.order.pojo.Order;
import com.mxswork.order.pojo.OrderDishInfo;
import com.mxswork.order.pojo.User;
import com.mxswork.order.utils.LocalJsonHelper;
import com.mxswork.order.view.BannerView;
import com.mxswork.order.view.DishRightListView;
import com.mxswork.order.view.SelectFlavourPopupWindow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class DishFragment extends Fragment
        implements AdapterView.OnItemClickListener,
        DishRightListViewAdapter.DishItemOnClickListener,
        BannerViewAdapter.BannerItemOnClickListener,
        SelectFlavourPopupWindow.ConfrimButtonOnClickListener{
    public static final String TAG = "DishFragment";
    private BannerView bannerView;
    private BannerViewAdapter bannerViewAdapter;
    private ListView dishLeftListView;
    private DishRightListView dishRightListView;
    private DishLeftListViewAdapter leftAdapter;
    private DishRightListViewAdapter rightAdapter;
    private Button btn_purchase;
    private TextView tv_cart_info;
    private TextView tv_cart_price;
    private int position;
    private List<Dish> dishes;
    private List<Dish> featureDishes;
    private List<OrderDishInfo> cartDishInfos;
    private List<String> tags;
    private List<Integer> firstTagInDishes;
    private HashMap<String,Object> savedVariables;
    private float selected_dish_price;
    private User user;
    private int desk;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dish,container,false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initView();
        initListener();
        selected_dish_price = 0;
        desk = 1;
        cartDishInfos = new ArrayList<>();
    }

    private void initView(){
//        bannerView = getActivity().findViewById(R.id.banner_view);
//        bannerViewAdapter = new BannerViewAdapter(featureDishes);
//        bannerView.setAdapter(bannerViewAdapter);
//        bannerViewAdapter.setBannerItemOnClickListener(this);

        leftAdapter = new DishLeftListViewAdapter(getActivity());
        leftAdapter.updateTagList(getTags());

        rightAdapter = new DishRightListViewAdapter(getActivity());
        rightAdapter.updateDishesList(dishes);
        rightAdapter.setOnInnerItemOnClickListener(this);

        dishLeftListView = getActivity().findViewById(R.id.lv_dish_left);
        dishLeftListView.setAdapter(leftAdapter);

        dishRightListView = getActivity().findViewById(R.id.lv_dish_right);
        dishRightListView.setAdapter(rightAdapter);
        dishRightListView.setHeadView(getHeadTextView());
        dishRightListView.setOnItemClickListener(this);

        btn_purchase =getActivity().findViewById(R.id.btn_purchase);
        tv_cart_info = getActivity().findViewById(R.id.tv_cart_info);
        tv_cart_price = getActivity().findViewById(R.id.tv_cart_price);

    }

    private void initListener(){
        dishLeftListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int rightPos = firstTagInDishes.get(i);
                dishRightListView.setSelection(rightPos);
                leftAdapter.setSelectPosition(i);
                Log.d(TAG, "onItemClick: dishLeftListView");
            }
        });

        dishRightListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            private boolean isScroll = false;
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                isScroll = true;
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisible, int i1, int i2) {
                if(isScroll){
                    dishRightListView.configHeadView(firstVisible);
                    String tag = dishes.get(firstVisible).getTag();
                    for(int i=0;i<tags.size();i++){

                        if(TextUtils.equals(tags.get(i),tag)){
                            Log.d(TAG, "onScroll: "+position);
                            dishLeftListView.smoothScrollToPosition(i);
                            leftAdapter.setSelectPosition(i);
                            break;
                        }
                    }
                }

            }
        });

        btn_purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Order order = generateOrder();
                Log.d(TAG, "onClick: "+order.toString());
                LocalJsonHelper.insertOrder(getActivity(),order);
                //Toast.makeText(getActivity(),"下单成功",Toast.LENGTH_SHORT).show();
                Snackbar.make(getActivity().findViewById(R.id.cl_snackbar),"下单成功",Snackbar.LENGTH_LONG).setAction("查看订单", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showOrderActivity(order);
                    }
                }).show();
                cleanDishSelected();
            }
        });
    }

    private void showOrderActivity(Order order){
        Intent intent = new Intent(getActivity(),OrderActivity.class);
        intent.putExtra("order",order); //要确保Order及其嵌套类都实现序列化接口
        startActivity(intent);
    }

    private void cleanDishSelected(){
        selected_dish_price = 0;
        cartDishInfos = new ArrayList<>();
        for(Dish dish:dishes){
            dish.setAmount(0);
        }

        //界面更新
        rightAdapter.updateDishesList(dishes);
        rightAdapter.notifyDataSetChanged();
        updateCartLayout(0,0);


    }

    private OrderDishInfo generateOrderDishInfo(int id, int amount, String flavour){
        OrderDishInfo orderDishInfo = new OrderDishInfo();
        orderDishInfo.setCount(amount);
        orderDishInfo.setId(id);
        orderDishInfo.setFlavour(flavour);
        return orderDishInfo;
    }

    private Order generateOrder(){
        Order order = new Order();
        order.setUid(user.getUid());
        List<OrderDishInfo> mergedInfos = mergeOrderDishInfo(cartDishInfos);
        OrderDishInfo[] infos = new OrderDishInfo[mergedInfos.size()];
        mergedInfos.toArray(infos);
        order.setDishes(infos);
        order.setDesk(desk);
        Date time = new Date();
        order.setTime(time.getTime());
        order.setTotal_price(selected_dish_price);
        return order;
    }

    private List<OrderDishInfo> mergeOrderDishInfo(List<OrderDishInfo> infos){
        List<OrderDishInfo> sortedInfos = sortOrdersDishInfo(infos);
        List<OrderDishInfo> mergedInfos = new ArrayList<>();
        for(OrderDishInfo info:sortedInfos){
            if(mergedInfos.size() == 0){
                mergedInfos.add(info);
            }else {
                OrderDishInfo lastMergedInfo = mergedInfos.get(mergedInfos.size()-1);
                if(lastMergedInfo.getId()==info.getId() && TextUtils.equals(lastMergedInfo.getFlavour(),info.getFlavour())){
                    lastMergedInfo.setCount(lastMergedInfo.getCount()+1);
                }else {
                    mergedInfos.add(info);
                }
            }
        }
        return mergedInfos;
    }

    private List<OrderDishInfo> sortOrdersDishInfo(List<OrderDishInfo> infos){
        Collections.sort(infos, new Comparator<OrderDishInfo>() {
            @Override
            public int compare(OrderDishInfo info, OrderDishInfo i1) {
                if(info.getId() < i1.getId()){
                    return 1;
                }else if(info.getId() > i1.getId()){
                    return -1;
                }else {
                    if(TextUtils.equals(info.getFlavour(),i1.getFlavour())){
                        return 0;
                    }else{
                        //TODO 测试是否保持了顺序
                        return -1;
                    }
                }
            }
        });
        return infos;
    }

    private List<String> getTags(){
        tags = new ArrayList<>();
        firstTagInDishes = new ArrayList<>();
        if(dishes != null){
            for(int i=0;i<dishes.size();i++){
                String tag = dishes.get(i).getTag();
                if(!tags.contains(tag)){
                    tags.add(tag);
                    firstTagInDishes.add(i);
                    Log.d(TAG, "firstTagInDishes: "+i);
                }
            }
        }
        return tags;
    }

    private View getHeadTextView() {
        TextView itemView = new TextView(getActivity());
        itemView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                70));
        itemView.setBackgroundColor(Color.WHITE);
        itemView.setTextSize(16);
        itemView.setTextColor(Color.BLACK);
        itemView.setPadding(0, 0, 0, itemView.getPaddingBottom());
        return itemView;
    }

    private Dish findDishById(List<Dish> dishes, int id){
        for(Dish dish:dishes){
            if(dish.getId() == id){
                return dish;
            }
        }
        return null;
    }

    public void setUser(User user){
        this.user = user;
    }

    public void setDishes(List<Dish> dishes){
        this.dishes = dishes;
    }

    public void setFeatureDishes(List<Dish> dishes){this.featureDishes = dishes;}

    @Override
    public void dishItemOnClick(View v) {
        switch (v.getId()){
            case R.id.ib_dish_amount_plus:{
                int dishId = (Integer)v.getTag(R.id.tag_dish_id);
                TextView tv_amount = (TextView)v.getTag(R.id.tag_list_tv_amount);
                ImageButton ib_sub = (ImageButton) v.getTag(R.id.tag_list_ib_amount_sub);
                Dish dish = findDishById(dishes,dishId);
                OrderDishInfo orderDishInfo;
                List<String> flavours = dish.getFlavour();
                if(flavours != null && flavours.size()>0) {
                    SelectFlavourPopupWindow popupWindow = new SelectFlavourPopupWindow(getActivity(),flavours);
                    popupWindow.setConfrmButtonOnClickListener(this);
                    popupWindow.popup();
                    //暂存当前变量
                    savedVariables = new HashMap<>();
                    savedVariables.put("dish",dish);
                    savedVariables.put("tv_amount",tv_amount);
                    savedVariables.put("ib_sub",ib_sub);
                }else {
                    orderDishInfo = generateOrderDishInfo(dish.getId(),1,"");
                    add2Cart(orderDishInfo,dish,tv_amount,ib_sub);
                }
                break;
            }

            case R.id.ib_dish_amount_sub:{
                int dishId = (Integer)v.getTag(R.id.tag_dish_id);
                TextView tv_amount = (TextView)v.getTag(R.id.tag_list_tv_amount);
                Dish dish = findDishById(dishes,dishId);
                removeFromCart(dish,tv_amount,(ImageButton)v);
                break;
            }
        }
    }

    @Override
    public void bannerItemOnClick(int pos) {
        Dish featureDish = featureDishes.get(pos);
        for(int i=0;i<dishes.size();i++){
            if(dishes.get(i).getId() == featureDish.getId()){
                dishRightListView.setSelection(i);
                dishRightListView.getChildAt(0).performClick();
                break;
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //Toast.makeText(getActivity(),"暂无详情",Toast.LENGTH_SHORT).show();
        Snackbar.make(getActivity().findViewById(R.id.cl_snackbar),"暂无详情",Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void setSelectedFlavourPos(int pos) {
        if(pos<0){
            showSnackBarMsg("你没有选择口味哦");
        }else {
            Dish dish = (Dish)savedVariables.get("dish");
            TextView tv_amount = (TextView)savedVariables.get("tv_amount");
            ImageButton ib_sub = (ImageButton)savedVariables.get("ib_sub");
            OrderDishInfo orderDishInfo = generateOrderDishInfo(dish.getId(),1,dish.getFlavour().get(pos));
            add2Cart(orderDishInfo,dish,tv_amount,ib_sub);
        }
        savedVariables.clear();
    }

    private void add2Cart(OrderDishInfo orderDishInfo, Dish dish, TextView tv_num, ImageButton ib_sub){
        cartDishInfos.add(orderDishInfo);
        //界面更新
        int dishAmount = dish.getAmount();
        if(dishAmount == 0){
            tv_num.setVisibility(View.VISIBLE);
            ib_sub.setVisibility(View.VISIBLE);
        }
        dishAmount++;
        dish.setAmount(dishAmount);
        tv_num.setText(String.valueOf(dishAmount));
        selected_dish_price+=dish.getPrice();
        updateCartLayout(dishAmount,selected_dish_price);
    }

    private void removeFromCart(Dish dish,TextView textView,ImageButton imageButton){
        //去除最近一次同种商品的加入(倒序查找）
        for(int i = cartDishInfos.size()-1;i>=0;i--){
            if(cartDishInfos.get(i).getId() == dish.getId()){
                cartDishInfos.remove(i);
                break;
            }
        }

        //界面更新
        int dishAmount = dish.getAmount();
        dishAmount--;
        if(dishAmount == 0){
            dish.setSelectedFlavour("");
            textView.setVisibility(View.INVISIBLE);
            imageButton.setVisibility(View.INVISIBLE);
        }
        dish.setAmount(dishAmount);
        textView.setText(String.valueOf(dishAmount));
        selected_dish_price-=dish.getPrice();
        updateCartLayout(dishAmount,selected_dish_price);
    }

    private void updateCartLayout(int num, float price){
        String info;
        String priceString;
        if(num == 0){
            info = "未选择商品";
            priceString = " 0 元";
            btn_purchase.setEnabled(false);
        }else {
            info = String.format("已选择 %d 件商品",num);
            priceString = String.format(" %.2f 元",price);
            btn_purchase.setEnabled(true);
        }
        tv_cart_info.setText(info);
        tv_cart_price.setText(priceString);
    }

    private void showSnackBarMsg(String msg){
        Snackbar.make(getActivity().findViewById(R.id.cl_snackbar),msg,Snackbar.LENGTH_SHORT).show();
    }

}

