package com.mxswork.order.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.mxswork.order.pojo.DishPackage;
import com.mxswork.order.pojo.DishSingle;
import com.mxswork.order.pojo.Order;
import com.mxswork.order.pojo.OrderDishInfo;
import com.mxswork.order.pojo.User;
import com.mxswork.order.utils.LocalJsonHelper;
import com.mxswork.order.view.BannerView;
import com.mxswork.order.view.DishRightListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class DishFragment extends Fragment
        implements AdapterView.OnItemClickListener,
        DishRightListViewAdapter.DishItemOnClickListener,
        BannerViewAdapter.BannerItemOnClickListener {
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
    private List<String> tags;
    private List<Integer> firstTagInDishes;
    private int selected_dish_count;
    private float selected_dish_price;
    private User user;
    private int desk;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one,container,false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initView();
        initListener();
        selected_dish_count = 0;
        selected_dish_price = 0;
        desk = 1;
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
                //cleanDishSelected();
            }
        });
    }

    private void showOrderActivity(Order order){
        Intent intent = new Intent(getActivity(),OrderActivity.class);
        intent.putExtra("order",order); //要确保Order及其嵌套类都实现序列化接口
        startActivity(intent);
    }

    private void cleanDishSelected(){
        selected_dish_count = 0;
        selected_dish_price = 0;
        for(Dish dish:dishes){
            dish.setSelectedFlavour("");
            dish.setAmount(0);
        }


    }

    private Order generateOrder(){
        Order order = new Order();
        order.setUid(user.getUid());
        List<OrderDishInfo> infos = new ArrayList<>();
        for(Dish dish:dishes){
            if(dish.getAmount()>0){
                OrderDishInfo orderDishInfo = new OrderDishInfo();
                orderDishInfo.setCount(dish.getAmount());
                orderDishInfo.setId(dish.getId());
                orderDishInfo.setFlavour(dish.getSelectedFlavour());
                infos.add(orderDishInfo);
            }
        }
        OrderDishInfo[] infos1 = new OrderDishInfo[infos.size()];
        infos.toArray(infos1);
        order.setDishes(infos1);
        order.setDesk(desk);
        Date time = new Date();
        order.setTime(time.getTime());
        order.setTotal_price(selected_dish_price);
        return order;
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
                if(dish instanceof DishSingle){
                    //Log.d(TAG, "bannerItemOnClick: DishSingle");
                    List<String> flavours = ((DishSingle)dish).getFlavour();
                    if(flavours.size()>0){
                        if(((DishSingle) dish).getFlavour()== null || ((DishSingle) dish).getFlavour().size()==0){ //每种菜第一次加才让用户选择口味
                            String[] flavoursArr = new String[flavours.size()];
                            for(int i=0;i<flavours.size();i++){
                                flavoursArr[i] = flavours.get(i);
                            }
                            AlertDialog.Builder build = new AlertDialog.Builder(getActivity());
                            build.setTitle(dish.getName()).setSingleChoiceItems(flavoursArr, 0, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {

                                }
                            }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            }).create().show();
                            //TODO 数据回传未实现, 先默认选第一个
                            dish.setSelectedFlavour(((DishSingle) dish).getFlavour().get(0));
                        }
                    }
                    add2Cart(dish,tv_amount,ib_sub);
                }else if(dish instanceof DishPackage){
                    //Log.d(TAG, "bannerItemOnClick: DishPackage");
                    add2Cart(dish,tv_amount,ib_sub);
                }
                break;
            }

            case R.id.ib_dish_amount_sub:{
                int dishId = (Integer)v.getTag(R.id.tag_dish_id);
                TextView tv_amount = (TextView)v.getTag(R.id.tag_list_tv_amount);
                Dish dish = findDishById(dishes,dishId);
                if(dish instanceof DishSingle){
                    Log.d(TAG, "bannerItemOnClick: DishSingle");
                    removeFromCart(dish,tv_amount,(ImageButton)v);
                }else if(dish instanceof DishPackage){
                    Log.d(TAG, "bannerItemOnClick: DishPackage");
                    removeFromCart(dish,tv_amount,(ImageButton)v);
                }
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

    private void add2Cart(Dish dish, TextView textView, ImageButton imageButton){
        int dishAmount = dish.getAmount();
        if(dishAmount == 0){
            textView.setVisibility(View.VISIBLE);
            imageButton.setVisibility(View.VISIBLE);
        }
        dishAmount++;
        textView.setText(String.valueOf(dishAmount));
        selected_dish_count++;
        selected_dish_price+=dish.getPrice();
        dish.setAmount(dishAmount);
        updateCartLayout();
    }

    private void removeFromCart(Dish dish,TextView textView,ImageButton imageButton){
        int dishAmount = dish.getAmount();
        dishAmount--;
        if(dishAmount == 0){
            dish.setSelectedFlavour("");
            textView.setVisibility(View.INVISIBLE);
            imageButton.setVisibility(View.INVISIBLE);
        }
        dish.setAmount(dishAmount);
        textView.setText(String.valueOf(dishAmount));
        selected_dish_count--;
        selected_dish_price-=dish.getPrice();
        updateCartLayout();
    }

    private void updateCartLayout(){
        String info;
        String price;
        if(selected_dish_count == 0){
            info = "未选择商品";
            price = " 0 元";
            btn_purchase.setEnabled(false);
        }else {
            info = String.format("已选择 %d 件商品",selected_dish_count);
            price = String.format(" %.2f 元",selected_dish_price);
            btn_purchase.setEnabled(true);
        }
        tv_cart_info.setText(info);
        tv_cart_price.setText(price);
    }

}
