package com.mxswork.order;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

public class HeadListView extends ListView {
    private boolean isHeadViewVisiable;
    private View headView;
    private int headViewWidth;
    private int headViewHeight;
    private HeadListViewAdapter adapter;

    public HeadListView(Context context) {
        super(context);
    }

    public HeadListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeadListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setHeadView(View headView){
        this.headView = headView;
        adapter = (HeadListViewAdapter) getAdapter();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取HeadView宽高
        if(headView != null){
            measureChild(headView,widthMeasureSpec,heightMeasureSpec);
            headViewHeight = headView.getMeasuredHeight();
            headViewWidth = headView.getMeasuredWidth();
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if(isHeadViewVisiable){ //根据情况选择是否绘制
            drawChild(canvas,headView,getDrawingTime());
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //放置HeadView
        if(headView != null){
            headView.layout(0,0,headViewWidth,headViewHeight);
            configHeadView(getFirstVisiblePosition());

        }
    }

    public void configHeadView(int pos){
        if(headView == null){
            return;
        }
        int state = adapter.getHeadViewState(pos);
        switch (state){
            case HeadListViewAdapter.HEAD_GONE: {
                isHeadViewVisiable = false;
                break;
            }
            case HeadListViewAdapter.HEAD_VISIABLE:{
                //设置HeadView内容
                adapter.configureHead(headView,pos);
                //HeadView放到顶部
                if(headView.getTop()!=0){
                    headView.layout(0,0,headViewWidth,headViewHeight);
                }
                isHeadViewVisiable = true;
                break;
            }
            case HeadListViewAdapter.HEAD_ON_TOP:{
                View firstView = getChildAt(0);

                //获得firstView底部到父布局顶的距离
                int bottom = firstView.getBottom();
                //获得HeadView的高度
                int headHeight = headView.getHeight();
                int y;
                if(bottom < headHeight){
                    y = bottom-headHeight; //负的
                }else {
                    y = 0;
                }
                adapter.configureHead(headView,pos);
                if(headView.getTop() != y){
                    headView.layout(0,y,headViewWidth,headViewHeight + y);
                }
                isHeadViewVisiable = true;
                break;
            }

        }

    }
}
