package com.mxswork.order.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.mxswork.order.R;

import java.util.List;

public class SelectFlavourPopupWindow extends PopupWindow implements PopupWindow.OnDismissListener {
    private PopupWindow popupWindow;
    private boolean mIsShowing = false;
    Context context;
    private RadioGroup rg_flavour;
    private Button btn_flavour_confirm;
    private ImageButton ib_flavour_close;
    private List<String> flavours;
    ConfirmButtonOnClickListener listener;

    public SelectFlavourPopupWindow(Context context, List<String> flavours) {
        this.context = context;
        this.flavours = flavours;
        initView();
    }

    public void initView(){
        View view = View.inflate(context, R.layout.view_select_dish_flavour, null);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        //popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.colorWhite)));
        setBackgroundAlpha(0.7f);
        popupWindow.setAnimationStyle(R.style.anim_bottom_popup_window);
        popupWindow.setOnDismissListener(this);
        rg_flavour = view.findViewById(R.id.rg_flavour);
        for(int i =0;i<flavours.size();i++){
            RadioButton radioButton = (RadioButton) LayoutInflater.from(context).inflate(R.layout.item_tag_dish_flavour,null);
            radioButton.setText(flavours.get(i));
            radioButton.setId(i); //每个RadioButton Id需不同，否则会出现多选情况
            if(i == 0) {
                radioButton.setChecked(true);
            }
            rg_flavour.addView(radioButton);
        }

        ib_flavour_close = view.findViewById(R.id.ib_flavour_close);
        ib_flavour_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        btn_flavour_confirm = view.findViewById(R.id.btn_flavour_confirm);
        btn_flavour_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = rg_flavour.indexOfChild(rg_flavour.findViewById(rg_flavour.getCheckedRadioButtonId()));
                listener.setSelectedFlavourPos(index);
                dismiss();
            }
        });
        mIsShowing = false;
    }

    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
        lp.alpha = bgAlpha;
        ((Activity) context).getWindow().setAttributes(lp);
    }

    public void popup(){
        if(popupWindow == null){
            initView();
        }
        if(!mIsShowing){
            mIsShowing = true;
            popupWindow.showAtLocation(LayoutInflater.from(context)
                    .inflate(R.layout.fragment_dish,null),Gravity.BOTTOM,0,0);
        }
    }



    public void dismiss(){
        if(popupWindow != null && mIsShowing){
            popupWindow.dismiss();
            mIsShowing = false;
        }
    }

    @Override
    public void onDismiss() {
        setBackgroundAlpha(1f); //恢复背景变暗
    }

    public interface ConfirmButtonOnClickListener {
        void setSelectedFlavourPos(int pos);
    }

    public void setConfrmButtonOnClickListener(ConfirmButtonOnClickListener listener){
        this.listener = listener;
    }

}
