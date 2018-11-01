package com.mxswork.order.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mxswork.order.R;
import com.mxswork.order.adpater.BannerViewAdapter;
import com.mxswork.order.adpater.BannerViewBaseAdapter;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class BannerView extends FrameLayout implements ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private LinearLayout mPointContainer;
    private BannerViewBaseAdapter mAdapter;
    private int mPointCount;
    private ImageView[] mPoints;
    private int mLastPos;
    private boolean isTouch = false;
    private ScheduledExecutorService executorService;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1);
                        }
                    },1000);
                    break;
                default:
                    break;
            }
        }
    };

    public BannerView(@NonNull Context context, AttributeSet attributeSet) {
        super(context,attributeSet);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initView();
    }

    private void initView() {
        mViewPager = findViewById(R.id.views_container);
        mPointContainer = findViewById(R.id.point_container);
        mViewPager.addOnPageChangeListener(this);
    }

    public void setAdapter(BannerViewAdapter adapter) {
        this.mAdapter = adapter;
        mPointCount = mAdapter.getSize();
        mViewPager.setAdapter(mAdapter);
        initPoint();
        //防止第二次刷新后 显示空白页面
        mViewPager.setCurrentItem(mPointCount*100+3);
        startScroll();
    }

    private void initPoint() {
       if (mPointCount == 0) {
           return;
       }

       mPoints = new ImageView[mPointCount];
       mPointContainer.removeAllViews();
       for (int i=0;i < mPointCount;i++) {
           ImageView view = new ImageView(getContext());
           view.setImageResource(R.drawable.point_normal);
           mPointContainer.addView(view);
           mPoints[i] = view;
       }
       if (mPoints[0] != null) {
           mPoints[0].setImageResource(R.drawable.point_selected);
       }
       mLastPos = 0;
    }
    private void changePoint(int currentPoint) {
        if (mLastPos == currentPoint) {
            return;
        }
        mPoints[currentPoint].setImageResource(R.drawable.point_selected);
        mPoints[mLastPos].setImageResource(R.drawable.point_normal);
        mLastPos = currentPoint;
    }

    public void startScroll() {

        executorService = new ScheduledThreadPoolExecutor(1);
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (isTouch) {
                    return;
                }
                handler.sendEmptyMessage(0);
            }
        },1000,3000, TimeUnit.MILLISECONDS);
    }

    public void cancelScroll() {
        if (executorService != null) {
            executorService.shutdown();
        }
    }

    @Override
    public void destroyDrawingCache() {
        super.destroyDrawingCache();
        executorService.shutdown();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (mPointCount != 0) {
            changePoint(position % mPointCount);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isTouch = true;
                break;
            case MotionEvent.ACTION_UP:
                isTouch = false;
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
