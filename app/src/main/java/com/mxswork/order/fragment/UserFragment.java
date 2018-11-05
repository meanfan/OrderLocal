package com.mxswork.order.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mxswork.order.CouponActivity;
import com.mxswork.order.R;
import com.mxswork.order.pojo.User;
import com.mxswork.order.utils.LocalJsonHelper;

public class UserFragment extends Fragment {
    private static final String TAG = "UserFragment";
    private User user;
    private TextView tv_user_name;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_user,container,false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        tv_user_name = getActivity().findViewById(R.id.tv_user_name);
        tv_user_name.setText(user.getName());
        getActivity().findViewById(R.id.rl_user_coupon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),CouponActivity.class);
                updateUser();
                intent.putExtra("user",user);
                Log.d(TAG, "onClick: "+user.toString());
                startActivity(intent);
            }
        });
    }

    private void updateUser(){
        user = LocalJsonHelper.getUserById(getActivity(),user.getUid());
    }

    public void setUser(User user){
        this.user = user;
    }
}

