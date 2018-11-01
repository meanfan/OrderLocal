package com.mxswork.order.adpater;

import android.content.Context;
import android.text.TextPaint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mxswork.order.R;

import java.util.ArrayList;
import java.util.List;

public class DishLeftListViewAdapter extends BaseAdapter {
    private List<String> tagList;
    private Context context;
    private int selectPosition;

    public DishLeftListViewAdapter(Context context) {
        this.context = context;
    }

    public void updateTagList(List<String> map){
        if(tagList == null){
            tagList = new ArrayList<>();
        }
        tagList.clear();
        tagList.addAll(map);
    }

    public void setSelectPosition(int selectPosition) {
        if(this.selectPosition == selectPosition){
            return;
        }
        this.selectPosition = selectPosition;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return tagList.size();
    }

    @Override
    public Object getItem(int i) {
        return tagList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        TextView tv_tag;
        if(convertView == null){
            convertView = View.inflate(context, R.layout.item_dish_list_left,null);
            tv_tag = convertView.findViewById(R.id.tv_dish_tag);
            convertView.setTag(tv_tag);
        }else {
            tv_tag = (TextView) convertView.getTag();
        }
        tv_tag.setText(tagList.get(pos));
        TextPaint tp = tv_tag.getPaint();
        if(selectPosition == pos){
            tp.setFakeBoldText(true);
            tv_tag.setBackgroundColor(context.getResources().getColor(R.color.colorListWhiteBg));

        }else {
            tv_tag.setBackgroundColor(context.getResources().getColor(R.color.colorListGrayBg));
            tp.setFakeBoldText(false);

        }
        return convertView;
    }
}
