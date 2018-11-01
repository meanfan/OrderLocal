package com.mxswork.order;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
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
import android.widget.Toast;

import com.mxswork.order.adpater.DishLeftListViewAdapter;
import com.mxswork.order.adpater.DishRightListViewAdapter;
import com.mxswork.order.pojo.Dish;
import com.mxswork.order.pojo.DishPackage;
import com.mxswork.order.pojo.DishSingle;
import com.mxswork.order.pojo.Order;
import com.mxswork.order.pojo.OrderDishInfo;
import com.mxswork.order.pojo.User;
import com.mxswork.order.utils.FileUtils;
import com.mxswork.order.utils.LocalJsonHelper;
import com.mxswork.order.view.DishRightListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class FragmentOne extends Fragment implements DishRightListViewAdapter.InnerItemOnClickListener {
    public static final String TAG = "FragmentOne";
    private ListView dishLeftListView;
    private DishRightListView dishRightListView;
    private DishLeftListViewAdapter leftAdapter;
    private DishRightListViewAdapter rightAdapter;
    private Button btn_purchase;
    private TextView tv_cart_info;
    private TextView tv_cart_price;
    private List<Dish> dishes;
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
        initData();
        initView();
        initListener();
        selected_dish_count = 0;
        selected_dish_price = 0;
        //默认先设置预置的游客账号
        setUser(LocalJsonHelper.readUsers(getActivity()).get(0));
        desk = 1;
        Log.d(TAG, "onStart: "+user.toString());
    }
    private void initData(){
        loadDishes();
        //FileUtils.copyAssetsFile2DiskFileDir(getContext(),LocalJsonHelper.FILENAME_DISH);
        FileUtils.copyAssetsFile2DiskFileDir(getActivity(),LocalJsonHelper.FILENAME_ORDER);
        FileUtils.copyAssetsFile2DiskFileDir(getActivity(),LocalJsonHelper.FILENAME_USER);
    }

    private void initView(){
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
            }
        });

        dishRightListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            private boolean isScroll = false;
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                isScroll = true;
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                if(isScroll){
                    dishRightListView.configHeadView(i);
                    String tag = dishes.get(i).getTag();
                    for(int ii=0;ii<tags.size();ii++){
                        if(tags.get(ii).compareTo(tag)==0){
                            dishLeftListView.smoothScrollToPosition(ii);
                            leftAdapter.setSelectPosition(ii);
                            break;
                        }
                    }
                }

            }
        });

        btn_purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Order order = generateOrder();
                Log.d(TAG, "onClick: "+order.toString());
                LocalJsonHelper.insertOrder(getActivity(),order);
                Toast.makeText(getActivity(),"下单成功",Toast.LENGTH_SHORT).show();
                //cleanDishSelected();


            }
        });
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

    private void loadDishes(){
        dishes = LocalJsonHelper.readDishes(getActivity());
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

    @Override
    public void itemClick(View v) {
        switch (v.getId()){
            case R.id.ib_dish_amount_plus:{
                int dishId = (Integer)v.getTag(R.id.tag_dish_id);
                TextView tv_amount = (TextView)v.getTag(R.id.tag_list_tv_amount);
                ImageButton ib_sub = (ImageButton) v.getTag(R.id.tag_list_ib_amount_sub);
                Dish dish = findDishById(dishes,dishId);
                if(dish instanceof DishSingle){
                    Log.d(TAG, "itemClick: DishSingle");
                    List<String> flavours = ((DishSingle)dish).getFlavour();
                    if(flavours.size()>0){
                        //TODO 弹窗选择辣度
                        //先只选第一个
                        dish.setSelectedFlavour(((DishSingle) dish).getFlavour().get(0));
                    }
                    add2Cart(dish,tv_amount,ib_sub);

                }else if(dish instanceof DishPackage){
                    Log.d(TAG, "itemClick: DishPackage");
                    add2Cart(dish,tv_amount,ib_sub);
                }
                break;
            }

            case R.id.ib_dish_amount_sub:{
                int dishId = (Integer)v.getTag(R.id.tag_dish_id);
                TextView tv_amount = (TextView)v.getTag(R.id.tag_list_tv_amount);
                ImageButton ib_plus = (ImageButton) v.getTag(R.id.tag_list_ib_amount_plus);
                Dish dish = findDishById(dishes,dishId);
                if(dish instanceof DishSingle){
                    Log.d(TAG, "itemClick: DishSingle");
                    removeFromCart(dish,tv_amount,(ImageButton)v);
                }else if(dish instanceof DishPackage){
                    Log.d(TAG, "itemClick: DishPackage");
                    removeFromCart(dish,tv_amount,(ImageButton)v);
                }
                break;
            }
        }
    }

    private void add2Cart(Dish dish,TextView textView,ImageButton imageButton){
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

