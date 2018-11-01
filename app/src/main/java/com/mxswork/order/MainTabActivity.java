package com.mxswork.order;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.mxswork.order.fragment.DishFragment;
import com.mxswork.order.fragment.OrderFragment;
import com.mxswork.order.fragment.UserFragment;
import com.mxswork.order.pojo.Dish;
import com.mxswork.order.pojo.User;
import com.mxswork.order.utils.FileUtils;
import com.mxswork.order.utils.LocalJsonHelper;

import java.util.ArrayList;
import java.util.List;

public class MainTabActivity extends AppCompatActivity {
    public static String TAG ="Menu";
    private List<Fragment> fragments = new ArrayList<>();
    private String[] titles = {"菜单", "订单", "我"};
    User user;
    int desk;
    private List<Dish> dishes;
    private List<Dish> featureDishes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintab);
        TabLayout tabLayout = findViewById(R.id.tabs);
        final ViewPager viewPager = findViewById(R.id.vp);

        MyAdapter adapter = new MyAdapter(getSupportFragmentManager(),this);
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                //viewPager.setCurrentItem(pos);
                if(pos ==1) {
                    ((OrderFragment) fragments.get(1)).refresh();
                }
                Log.d(TAG, "onTabSelected: ");
            }


            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        //FileUtils.copyAssetsFile2DiskFileDir(getContext(),LocalJsonHelper.FILENAME_DISH);
        FileUtils.copyAssetsFile2DiskFileDir(this,LocalJsonHelper.FILENAME_ORDER);
        FileUtils.copyAssetsFile2DiskFileDir(this,LocalJsonHelper.FILENAME_USER);
        setUser();
        loadDishes();
    }
    private void loadDishes(){
        dishes = LocalJsonHelper.readDishes(this);
        if(featureDishes == null){
            featureDishes = new ArrayList<>();
        }else {
            featureDishes.clear();
        }
        for(Dish dish:dishes){
            if(dish.isFeature()){
                featureDishes.add(dish);
            }
        }
    }

    private void setUser(){
        user = LocalJsonHelper.readUsers(this).get(0);
        desk = 1;
    }

    //ViewPager的适配器
    private class MyAdapter extends FragmentPagerAdapter {
        MainTabActivity activity;
        MyAdapter(FragmentManager fm, MainTabActivity activity) {
            super(fm);
            this.activity = activity;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    DishFragment dishFragment= new DishFragment();
                    dishFragment.setUser(user);
                    dishFragment.setDishes(dishes);
                    dishFragment.setFeatureDishes(featureDishes);
                    fragment = dishFragment;
                    break;
                case 1:
                    fragment = new OrderFragment();
                    break;
                case 2:
                    UserFragment userFragment= new UserFragment();
                    userFragment.setUser(user);
                    fragment = userFragment;
                    break;
            }
            fragments.add(fragment);
            return fragment;
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        //ViewPager和TabLayout结合使用时候需要复写
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: ");
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

