package com.mxswork.order;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class menu extends AppCompatActivity {

    private String[] titles = {"菜单", "订单", "我的"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //Toolbar toolBar = findViewById(R.id.toolbar);
        //toolBar.setTitle("订餐系统");
        //setSupportActionBar(toolBar);      //替换toolbar的相关配置需要在这个方法前完成
        //if(getSupportActionBar() != null){
            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);  //显示返回键
       // }
        TabLayout tabLayout = findViewById(R.id.tabs);
//      tabLayout.addTab();//添加
        ViewPager viewPager = findViewById(R.id.vp);

        MyAdapter adapter = new MyAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
    }

    //ViewPager的适配器
    private class MyAdapter extends FragmentPagerAdapter {

        MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new FragmentOne();
                    break;
                case 1:
                    fragment = new FragmentTwo();
                    break;
                case 2:
                    fragment = new FragmentThree();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        //ViewPager和Tablayout结合使用时候需要复写
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

