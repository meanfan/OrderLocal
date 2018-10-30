package com.mxswork.order;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mxswork.order.pojo.Dish;
import com.mxswork.order.utils.LocalJsonHelper;

import java.util.ArrayList;
import java.util.List;

public class HeadListViewAdapter extends BaseAdapter {
    public static final int HEAD_GONE = 0;
    public static final int HEAD_VISIABLE = 1;
    public static final int HEAD_ON_TOP = 2;
    private Context context;
    private List<Dish> dishes = new ArrayList<>();

    public HeadListViewAdapter(Context context) {
        this.context = context;
    }

    public void setDishes(List<Dish> dishes){
        this.dishes.clear();
        this.dishes.addAll(dishes);
    }

    @Override
    public int getCount() {
        return dishes.size();
    }

    @Override
    public Object getItem(int i) {
        return dishes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return dishes.get(i).getId();
    }

    @Override
    public View getView(int pos, View view, ViewGroup viewGroup) {
        ViewHolder vh;
        if(view == null){
            view = View.inflate(context,R.layout.item_dish_list_right,null);
            vh = new ViewHolder();
            vh.tv_tag = view.findViewById(R.id.tv_tag);
            vh.iv_dish_pic = view.findViewById(R.id.iv_dish_pic);
            vh.tv_dish_title = view.findViewById(R.id.tv_dish_title);
            vh.tv_dish_desc = view.findViewById(R.id.tv_dish_desc);
            vh.tv_dish_price = view.findViewById(R.id.tv_dish_price);
            vh.ib_dish_amount_sub = view.findViewById(R.id.ib_dish_amount_sub);
            vh.tv_dish_amount_num = view.findViewById(R.id.tv_dish_amount_num);
            vh.ib_dish_amount_plus = view.findViewById(R.id.ib_dish_amount_plus);
            view.setTag(vh);
        }else {
            vh = (ViewHolder) view.getTag();
        }
        Dish dish = dishes.get(pos);

        vh.tv_tag.setText(dish.getTag());
        vh.iv_dish_pic.setImageBitmap(LocalJsonHelper.getDishPic(context,dish));
        vh.tv_dish_title.setText(dish.getName());
        vh.tv_dish_desc.setText(dish.getDesc());
        vh.tv_dish_price.setText(String.format("%.2f",dish.getPrice()));
        int amount = dish.getAmount();
        if(amount == 0){
            vh.ib_dish_amount_sub.setVisibility(View.INVISIBLE);
            vh.tv_dish_amount_num.setText("");
        }else {
            vh.ib_dish_amount_sub.setVisibility(View.VISIBLE);
            vh.tv_dish_amount_num.setText(String.valueOf(amount));
        }
        if(pos == 0 ||
                !TextUtils.equals(dishes.get(pos).getTag(),dishes.get(pos-1).getTag())){
            //第一个或者和上一个标签不同时，显示标签
            vh.tv_tag.setVisibility(View.VISIBLE);
            vh.tv_tag.setText(dishes.get(pos).getTag());
        }else { //其它情况不显示标签
            vh.tv_tag.setVisibility(View.GONE);
        }
        return view;
    }
    public int getHeadViewState(int pos) {
        if (pos < 0) {
            return HEAD_GONE;
        }
        if (pos != 0 && !TextUtils.equals(dishes.get(pos).getTag(), dishes.get(pos + 1).getTag())) {
            return HEAD_ON_TOP;
        }
        return HEAD_VISIABLE;

    }

    public void configureHead(View head,int pos){
        Dish dish =(Dish) getItem(pos);
        if(dish!=null){
            if(head instanceof TextView){
                ((TextView) head).setText(dish.getTag());
            }
        }
    }

    static class ViewHolder{
        TextView tv_tag;
        ImageView iv_dish_pic;
        TextView tv_dish_title;
        TextView tv_dish_desc;
        TextView tv_dish_price;
        ImageButton ib_dish_amount_sub;
        TextView tv_dish_amount_num;
        ImageButton ib_dish_amount_plus;


    }
}
