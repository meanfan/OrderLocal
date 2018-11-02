package com.mxswork.order.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mxswork.order.R;
import com.mxswork.order.pojo.User;


public class UserFragment extends Fragment {
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
    }

    public void setUser(User user){
        this.user = user;
    }
}

