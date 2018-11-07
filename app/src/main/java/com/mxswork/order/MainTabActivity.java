package com.mxswork.order;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.util.Log;

import com.mxswork.order.fragment.DishFragment;
import com.mxswork.order.fragment.OrderFragment;
import com.mxswork.order.fragment.UserFragment;
import com.mxswork.order.pojo.Dish;
import com.mxswork.order.pojo.User;
import com.mxswork.order.utils.FileUtils;
import com.mxswork.order.utils.LocalJsonHelper;
import com.mxswork.order.view.MainTabViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainTabActivity extends AppCompatActivity {
    public static String TAG ="MainTabActivity";
    private List<Fragment> fragments = new ArrayList<>();
    int tabImagesUnchecked[] = {R.drawable.ic_menu_black,R.drawable.ic_order_black,R.drawable.ic_people_black};
    int tabImageChecked[] = {R.drawable.ic_menu_fill_red,R.drawable.ic_order_fill_red,R.drawable.ic_people_fill_red};
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
        MainTabViewPager viewPager = findViewById(R.id.vp);
        MainTabAdapter adapter = new MainTabAdapter(getSupportFragmentManager(),this);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        for(int i = 0; i< titles.length; i++){
            if(i==0){
                tabLayout.getTabAt(0).setIcon(tabImageChecked[0]);
            }else {
                tabLayout.getTabAt(i).setIcon(tabImagesUnchecked[i]);
            }
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                //viewPager.setCurrentItem(pos);
                tab.setIcon(tabImageChecked[pos]);
                if(pos == 1) {
                    ((OrderFragment) fragments.get(1)).refresh();
                }
                Log.d(TAG, "onTabSelected: "+pos);
            }


            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                tab.setIcon(tabImagesUnchecked[pos]);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        //FileUtils.copyAssetsFile2DiskFileDir(getContext(),LocalJsonHelper.FILENAME_DISH);
        FileUtils.copyAssetsFile2DiskFileDir(this,LocalJsonHelper.FILENAME_ORDER);
        FileUtils.copyAssetsFile2DiskFileDir(this,LocalJsonHelper.FILENAME_USER);
        FileUtils.copyAssetsFile2DiskFileDir(this,LocalJsonHelper.FILENAME_COUPON);
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
    private class MainTabAdapter extends FragmentPagerAdapter {
        MainTabActivity activity;
        DishFragment dishFragment;
        OrderFragment orderFragment;
        UserFragment userFragment;
        MainTabAdapter(FragmentManager fm, MainTabActivity activity) {
            super(fm);
            this.activity = activity;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    if(dishFragment == null){
                        dishFragment= new DishFragment();
                        fragments.add(dishFragment);
                    }
                    dishFragment.setUser(user);
                    dishFragment.setDishes(dishes);
                    dishFragment.setFeatureDishes(featureDishes);
                    fragment = dishFragment;
                    break;
                case 1:
                    if(orderFragment == null){
                        orderFragment= new OrderFragment();
                        fragments.add(orderFragment);
                    }
                    orderFragment.setUser(user);
                    fragment = orderFragment;
                    break;
                case 2:
                    if(userFragment == null){
                        userFragment= new UserFragment();
                        fragments.add(userFragment);
                    }
                    userFragment.setUser(user);
                    fragment = userFragment;
                    break;
            }
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
}

