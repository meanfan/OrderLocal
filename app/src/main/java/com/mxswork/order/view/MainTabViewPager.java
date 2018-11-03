package com.mxswork.order.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MainTabViewPager extends ViewPager {

    public MainTabViewPager(@NonNull Context context) {
        super(context);
    }

    public MainTabViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {  //去除滑动
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {  //去除滑动
        return false;
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item,false); //去除动画
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }
}
