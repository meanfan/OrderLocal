package com.mxswork.order.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.mxswork.order.OrderActivity;
import com.mxswork.order.R;
import com.mxswork.order.SelectCouponActivity;
import com.mxswork.order.adpater.DishLeftListViewAdapter;
import com.mxswork.order.adpater.DishRightListViewAdapter;
import com.mxswork.order.pojo.Coupon;
import com.mxswork.order.pojo.Dish;
import com.mxswork.order.pojo.Order;
import com.mxswork.order.pojo.OrderDishInfo;
import com.mxswork.order.pojo.User;
import com.mxswork.order.utils.LocalJsonHelper;
import com.mxswork.order.view.DishRightListView;
import com.mxswork.order.view.SelectFlavourPopupWindow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@SuppressLint("ClickableViewAccessibility")
public class DishFragment extends Fragment
        implements AdapterView.OnItemClickListener,
        DishRightListViewAdapter.DishItemOnClickListener,
        SelectFlavourPopupWindow.ConfirmButtonOnClickListener {
    public static final String TAG = "DishFragment";
    private static final int REQUEST_COUPON = 0;
    public static final int RESULT_OK = 1;
    public static final int RESULT_CANCEL = 0;
    private ViewFlipper vf_banner;
    private ListView dishLeftListView;
    private DishRightListView dishRightListView;
    private DishLeftListViewAdapter leftAdapter;
    private DishRightListViewAdapter rightAdapter;
    private Button btn_purchase;
    private TextView tv_cart_info,tv_cart_total_price,tv_cart_final_price,tv_select_coupon;
    private int position;
    private List<Dish> dishes,featureDishes;
    private List<OrderDishInfo> cartDishInfos;
    private List<String> tags;
    private List<Integer> firstTagInDishes;
    private HashMap<String,Object> savedVariables;
    private Coupon selectedCoupon;
    private int selectedCouponId = -1;
    private float selected_dish_total_price,selected_dish_final_price;
    private User user;
    private int desk;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dish,container,false);
        return view;
    }
    
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated: ");
        initView();
        initListener();
        selectedCouponId  = -1;
        selected_dish_total_price = 0;
        selected_dish_final_price = 0;
        desk = 1;
        cartDishInfos = new ArrayList<>();
        cleanDishSelected();
    }

    private void initView(){

        vf_banner = getActivity().findViewById(R.id.vf_banner);
        for(Dish dish:featureDishes){
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_banner,null);
            ((ImageView)view.findViewById(R.id.iv_banner)).
                    setImageBitmap(LocalJsonHelper.readDishPic(getActivity(),dish));
            ((TextView)view.findViewById(R.id.tv_banner)).setText(dish.getName());
            vf_banner.addView(view);
        }

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
        tv_select_coupon = getActivity().findViewById(R.id.tv_select_coupon);
        tv_cart_total_price = getActivity().findViewById(R.id.tv_cart_total_price);
        tv_cart_final_price = getActivity().findViewById(R.id.tv_cart_final_price);

    }

    private void initListener(){
        vf_banner.setOnTouchListener(new View.OnTouchListener() {
            private float startX;
            private float endX;
            private float moveX = 100f;
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //获取起点X坐标
                        startX = motionEvent.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        //获取终点X坐标
                        endX = motionEvent.getX();
                        if (endX - startX > moveX) {
                            vf_banner.setInAnimation(getActivity(), R.anim.banner_left_in);
                            vf_banner.setOutAnimation(getActivity(), R.anim.banner_right_out);
                            vf_banner.showPrevious();
                            Log.d(TAG, "onTouch: left");
                        } else if (startX - endX > moveX) {
                            vf_banner.setInAnimation(getActivity(), R.anim.banner_right_in);
                            vf_banner.setOutAnimation(getActivity(), R.anim.banner_left_out);
                            vf_banner.showNext();
                            Log.d(TAG, "onTouch: right");
                        }else {
                            vf_banner.performClick(); //必须调用，否则点击事件无效
                            Log.d(TAG, "onTouch: click");

                        }
                        break;
                }
                return true;
            }
        });

        vf_banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = featureDishes.get(vf_banner.getDisplayedChild()).getId();
                int pos = dishes.indexOf(findDishById(dishes,id));
                dishRightListView.setSelection(pos);
            }
        });


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

        tv_select_coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),SelectCouponActivity.class);
                intent.putExtra("user",user);
                intent.putExtra("selectedCouponId",selectedCouponId);
                startActivityForResult(intent, REQUEST_COUPON);
            }
        });

        btn_purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: selectedCouponId:"+selectedCouponId);
                final Order order = generateOrder(selected_dish_total_price,selectedCouponId);
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
        selectedCouponId = -1;
        selected_dish_total_price = 0;
        selectedCoupon = null;
        cartDishInfos = new ArrayList<>();
        for(Dish dish:dishes){
            dish.setAmount(0);
        }
        updateUser();

        //界面更新
        tv_select_coupon.setText(getText(R.string.tv_text_select_coupon_none));
        rightAdapter.updateDishesList(dishes);
        rightAdapter.notifyDataSetChanged();
        updateCartLayout();


    }

    private OrderDishInfo generateOrderDishInfo(int id, int amount, String flavour){
        OrderDishInfo orderDishInfo = new OrderDishInfo();
        orderDishInfo.setCount(amount);
        orderDishInfo.setId(id);
        orderDishInfo.setFlavour(flavour);
        return orderDishInfo;
    }

    private Order generateOrder(float totalPrice,int couponId){
        Order order = new Order();
        order.setUid(user.getUid());
        List<OrderDishInfo> mergedInfos = mergeOrderDishInfo(cartDishInfos);
        OrderDishInfo[] infos = new OrderDishInfo[mergedInfos.size()];
        mergedInfos.toArray(infos);
        order.setDishes(infos);
        order.setDesk(desk);
        Date time = new Date();
        order.setTime(time.getTime());
        order.setTotalPrice(totalPrice);
        float finalPrice = totalPrice;
        if(couponId>-1){
            Coupon coupon = LocalJsonHelper.getCouponById(getActivity(),couponId);
            LocalJsonHelper.deleteUserCoupons(getActivity(),user.getUid(),couponId,1);
            finalPrice = coupon.calcPrice(totalPrice);
            order.setUseCouponId(couponId);
        }else {
            order.setUseCouponId(-1);
        }
        order.setFinalPrice(finalPrice);
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

    private void updateUser(){
        user = LocalJsonHelper.getUserById(getActivity(),user.getUid());
    }

    public void setDishes(List<Dish> dishes){
        this.dishes = dishes;
    }

    public void setFeatureDishes(List<Dish> dishes){this.featureDishes = dishes;}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_COUPON: {
                if (resultCode == RESULT_OK) {
                    selectedCouponId = data.getIntExtra(SelectCouponActivity.RESULT_EXTRA_COUPON, -1);
                } else {
                    selectedCouponId = -1;
                }
                Log.d(TAG, "onActivityResult: "+selectedCouponId);
                updateSelectCoupon();
                updateCartLayout();
            }
        }
    }

    private void loadSelectedCoupon(){
        selectedCoupon = LocalJsonHelper.getCouponById(getActivity(),selectedCouponId);
    }

    private void updateSelectCoupon(){
        if(selectedCouponId >-1){
            loadSelectedCoupon();
            String text = String.format(getText(R.string.tv_text_select_coupon_has).toString(),selectedCoupon.getName());
            tv_select_coupon.setText(text);
        }else {
            tv_select_coupon.setText(getText(R.string.tv_text_select_coupon_none));
        }
    }

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
                    popupWindow.setConfirmButtonOnClickListener(this);
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
        selected_dish_total_price +=dish.getPrice();
        updateCartLayout();
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
            textView.setVisibility(View.INVISIBLE);
            imageButton.setVisibility(View.INVISIBLE);
        }
        dish.setAmount(dishAmount);
        textView.setText(String.valueOf(dishAmount));
        selected_dish_total_price -=dish.getPrice();
        updateCartLayout();
    }

    private void updateCartLayout(){
        int num = cartDishInfos.size();
        String info;
        String totalPriceString,finalPriceString;
        if(num == 0){
            info = "未选择商品";
            totalPriceString = "￥0";
            finalPriceString = "￥0";
            tv_cart_total_price.setVisibility(View.GONE);
            btn_purchase.setEnabled(false);
        }else {
            info = String.format("已选择 %d 件商品",num);
            totalPriceString = String.format("￥%.1f",selected_dish_total_price);
            if(selectedCouponId>-1 && selectedCoupon!=null){
                tv_cart_total_price.setVisibility(View.VISIBLE);
                selected_dish_final_price = selectedCoupon.calcPrice(selected_dish_total_price);
            }else {
                tv_cart_total_price.setVisibility(View.GONE);
                selected_dish_final_price = selected_dish_total_price;
            }
            btn_purchase.setEnabled(true);
            finalPriceString = String.format("￥%.1f",selected_dish_final_price);
        }

        tv_cart_info.setText(info);
        tv_cart_final_price.setText(finalPriceString);
        tv_cart_total_price.setText(totalPriceString);
        tv_cart_total_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);//添加删除线
    }

    private void showSnackBarMsg(String msg){
        Snackbar.make(getActivity().findViewById(R.id.cl_snackbar),msg,Snackbar.LENGTH_SHORT).show();
    }

}

