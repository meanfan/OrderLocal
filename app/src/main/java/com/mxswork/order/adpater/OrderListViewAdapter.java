package com.mxswork.order.adpater;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mxswork.order.R;
import com.mxswork.order.pojo.Dish;
import com.mxswork.order.pojo.Order;
import com.mxswork.order.utils.LocalJsonHelper;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderListViewAdapter extends BaseAdapter {
    private List<Order> orders;
    Context context;

    public OrderListViewAdapter(Context context){
        this.context = context;
    }

    public void updateOrders(List<Order> orders){
        this.orders = orders;
        if(orders == null){
            this.orders = new ArrayList<>();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return orders.size();
    }

    @Override
    public Object getItem(int i) {
        return orders.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int pos, View view, ViewGroup viewGroup) {
        ViewHolder vh;
        if(view == null){
            view = View.inflate(context,R.layout.item_order_list,null);
            vh = new ViewHolder();
            vh.iv_order_list_pic = view.findViewById(R.id.iv_order_list_pic);
            vh.tv_order_list_title = view.findViewById(R.id.tv_order_list_title);
            vh.tv_order_list_time = view.findViewById(R.id.tv_order_list_time);
            vh.tv_order_list_price = view.findViewById(R.id.tv_order_list_price);
            view.setTag(vh);
        }else {
            vh = (ViewHolder) view.getTag();
        }
        Order order = orders.get(pos);
        String firstDishName;
        int firstOrderDishId = order.getDishes()[0].getId();
        Dish firstDish = LocalJsonHelper.readDishById(context,firstOrderDishId);
        if(firstDish!=null){
            firstDishName = firstDish.getName();
            vh.iv_order_list_pic.setImageBitmap(LocalJsonHelper.readDishPic(context,firstDish));
            String title;
            if(order.getDishes().length == 1){
                title = String.format("%s",firstDishName);
            }else {
                title = String.format("%s 等 %d 件商品",firstDishName,order.getDishes().length);
            }
            vh.tv_order_list_title.setText(title);
            Date date = new Date(order.getTime());
            DateFormat dateFormat = DateFormat.getDateTimeInstance();
            String time = dateFormat.format(date);
            vh.tv_order_list_time.setText(time);
            String price = String.format("￥%.1f",order.getTotal_price());
            vh.tv_order_list_price.setText(price);
        }
        return view;
    }

    class ViewHolder {
        ImageView iv_order_list_pic;
        TextView tv_order_list_title;
        TextView tv_order_list_time;
        TextView tv_order_list_price;
    }
}
