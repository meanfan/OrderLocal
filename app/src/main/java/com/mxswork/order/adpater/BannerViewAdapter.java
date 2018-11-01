package com.mxswork.order.adpater;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mxswork.order.R;
import com.mxswork.order.pojo.Dish;
import com.mxswork.order.utils.LocalJsonHelper;

import java.util.List;

public class BannerViewAdapter extends BannerViewBaseAdapter {

    private List<Dish> dishList;
    private Context context;

    public BannerViewAdapter(List<Dish> dishes) {
        this.dishList = dishes;
    }

    @Override
    public View getView(ViewGroup container, int position) {
        AppCompatImageView imageView;
        TextView title;

        if (context == null) {
            context = container.getContext();
        }
        View mView = LayoutInflater.from(context).inflate(R.layout.banner_item_layout,null);

        final Dish dish = dishList.get(position);
        imageView = mView.findViewById(R.id.image);
        title = mView.findViewById(R.id.banner_title);
        title.setText(dish.getName());
        imageView.setBackgroundColor(Color.WHITE);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setImageBitmap(LocalJsonHelper.readDishPic(context,dish));
        //Glide.with(context).load(dish.getImageId()).error(R.drawable.ic_launcher_background).into(imageView);
        notifyDataSetChanged();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });
        return mView;
    }

    @Override
    public int getSize() {
        return dishList.size();
    }
}
