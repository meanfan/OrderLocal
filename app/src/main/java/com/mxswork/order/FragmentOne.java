package com.mxswork.order;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mxswork.order.adpater.DishLeftListViewAdapter;
import com.mxswork.order.adpater.DishRightListViewAdapter;
import com.mxswork.order.pojo.Dish;
import com.mxswork.order.pojo.DishPackage;
import com.mxswork.order.pojo.DishSingle;
import com.mxswork.order.pojo.User;
import com.mxswork.order.utils.LocalJsonHelper;
import com.mxswork.order.view.DishRightListView;

import java.util.ArrayList;
import java.util.List;


public class FragmentOne extends Fragment implements DishRightListViewAdapter.InnerItemOnClickListener,AdapterView.OnItemClickListener {
    public static final String TAG = "FragmentOne";
    private ListView dishLeftListView;
    private DishRightListView dishRightListView;
    private DishLeftListViewAdapter leftAdapter;
    private DishRightListViewAdapter rightAdapter;
    private List<Dish> dishes;
    private List<String> tags;
    private List<Integer> firstTagInDishes;
    private List<Dish> shopCart;

    private User user;

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
        //默认先设置预置的游客账号
        setUser(LocalJsonHelper.readUser(getActivity()).get(0));
        Log.d(TAG, "onStart: "+user.toString());
        shopCart = new ArrayList<>();
    }
    private void initView(){
        loadDishes();
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
                    int lfPos;
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
                Dish dish = findDishById(dishes,dishId);
                if(dish instanceof DishSingle){
                    Log.d(TAG, "itemClick: DishSingle");
                    List<String> flavours = ((DishSingle)dish).getFlavour();
                    if(flavours.size()>0){
                        //TODO 弹窗选择辣度
                        //先只选第一个
                        dish.setSelectedFlavour(((DishSingle) dish).getFlavour().get(0));
                    }else {
                        int dishAmount= dish.getAmount();
                        if(dishAmount==0){
                            ((View)v.getTag(R.id.tag_list_ib_amount_sub)).setVisibility(View.VISIBLE);
                            ((View)v.getTag(R.id.tag_list_tv_amount)).setVisibility(View.VISIBLE);
                        }
                        dishAmount++;
                        ((TextView)v.getTag(R.id.tag_list_tv_amount)).setText(String.valueOf(dishAmount));
                        dish.setAmount(dishAmount);
                    }
                }else if(dish instanceof DishPackage){
                    Log.d(TAG, "itemClick: DishPackage");
                    int dishAmount= dish.getAmount();
                    if(dishAmount==0){
                        ((View)v.getTag(R.id.tag_list_ib_amount_sub)).setVisibility(View.VISIBLE);
                        ((View)v.getTag(R.id.tag_list_tv_amount)).setVisibility(View.VISIBLE);
                    }
                    dishAmount++;
                    ((TextView)v.getTag(R.id.tag_list_tv_amount)).setText(String.valueOf(dishAmount));
                    dish.setAmount(dishAmount);
                }
                break;
            }
            case R.id.ib_dish_amount_sub:{

                break;
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(getActivity(),"item onClick",Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onItemClick: ");
    }
}

