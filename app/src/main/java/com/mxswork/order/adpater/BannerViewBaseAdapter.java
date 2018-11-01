package com.mxswork.order.adpater;

import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.mxswork.order.view.BannerView;

import java.util.ArrayList;
import java.util.List;

public abstract class BannerViewBaseAdapter extends PagerAdapter {

    private List<View> mList;
    private View mView;

    public BannerViewBaseAdapter() {
        mList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (getSize() != 0) {
            if (mList.size() <= (position % getSize())) {
                for (int i = mList.size();i <= position % getSize();++i) {
                    mList.add(getView(container,i));
                }
            }
            mView = mList.get(position % getSize());
            if (mView.getParent() != null) {
                container.removeView(mView);
            }
            container.addView(mView);
        }

        return mView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // 滑动下一张图时销毁当前的图
        if (getSize() != 0 && position != 0) {
            container.removeView(mList.get(position % getSize()));
        }
    }

    public abstract View getView(ViewGroup container, int position);

    public abstract int getSize();
}
