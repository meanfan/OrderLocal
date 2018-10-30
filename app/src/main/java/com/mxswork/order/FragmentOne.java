package com.mxswork.order;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.mxswork.order.utils.LocalJsonHelper;


public class FragmentOne extends Fragment {
    private HeadListView headListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one,container,false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        HeadListViewAdapter adapter = new HeadListViewAdapter(getActivity());
        adapter.setDishes(LocalJsonHelper.getDishes(getActivity()));
        headListView = getActivity().findViewById(R.id.lv_dish);
        headListView.setAdapter(adapter);
        headListView.setHeadView(getHeadTextView());
        headListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            private boolean isScroll = false;
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                isScroll = true;
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                if(isScroll){
                    headListView.configHeadView(i);
                }

            }
        });
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
}

