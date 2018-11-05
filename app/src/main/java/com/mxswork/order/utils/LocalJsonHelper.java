package com.mxswork.order.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.gson.Gson;
import com.mxswork.order.pojo.Coupon;
import com.mxswork.order.pojo.CouponDiscount;
import com.mxswork.order.pojo.CouponReduction;
import com.mxswork.order.pojo.Dish;
import com.mxswork.order.pojo.DishSingle;
import com.mxswork.order.pojo.DishPackage;
import com.mxswork.order.pojo.Order;
import com.mxswork.order.pojo.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LocalJsonHelper {
    public static final String FILENAME_DISH = "dishes.json";
    public static final String FILENAME_ORDER = "orders.json";
    public static final String FILENAME_USER = "users.json";
    public static final String FILENAME_COUPON = "coupons.json";

    public static final String TAG = "LocalJsonHelper";

    public static String readJsonFromAssets(Context context, String fileName){
        StringBuilder stringBuilder = new StringBuilder();
        AssetManager assetManager = context.getAssets();
        try{
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName),"utf-8"));
            String line;
            while((line = bufferedReader.readLine())!=null){
                stringBuilder.append(line);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static String readJsonFromDisk(Context context, String filename){
        return FileUtils.readStringFromDiskFileDir(context,filename);
    }

    public static void writeJson2Disk(Context context, String filename, String json){
        FileUtils.writeString2DiskFileDir(context,json,filename);
    }

    public static <T> T json2Object(String json, Class<T> type){
        Gson gson = new Gson();
        return gson.fromJson(json,type);
    }

    public static String object2Json(Object o){
        Gson gson = new Gson();
        return gson.toJson(o);
    }

    public static Bitmap readBitmapFromAssets(Context context, String fileName){

        AssetManager assetManager = context.getAssets();
        try{
            InputStream inputStream = assetManager.open(fileName);
            return BitmapFactory.decodeStream(inputStream);
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public static List<Dish> readDishes(Context context){
        String json = LocalJsonHelper.readJsonFromAssets(context,FILENAME_DISH);
        List<Dish> dishes = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);

            JSONArray jsonArray = jsonObject.getJSONArray("dishPackage");
            //Log.d(TAG, jsonArray.toString());
            for(int i=0;i<jsonArray.length();i++){
                DishPackage pack = LocalJsonHelper.json2Object(jsonArray.get(i).toString(),DishPackage.class);
                pack.setAmount(0);
                dishes.add(pack);
                //Log.d(TAG, "getDishPackage: "+pack.toString());
            }

            jsonArray = jsonObject.getJSONArray("dishSingle");
            for(int i=0;i<jsonArray.length();i++){
                DishSingle dish = LocalJsonHelper.json2Object(jsonArray.get(i).toString(),DishSingle.class);
                dish.setAmount(0);
                //dish.setAmount(1);
                dishes.add(dish);
                //Log.d(TAG, "getDishSingle: "+dish.toString());
            }

        }catch (JSONException e){
            e.printStackTrace();
        }
        return dishes;
    }

    public static Bitmap readDishPic(Context context, Dish dish){
        return readBitmapFromAssets(context,dish.getPicPath());
    }

    public static List<User> readUsers(Context context){
        String json = LocalJsonHelper.readJsonFromDisk(context,FILENAME_USER);
        Log.d(TAG, "readUsers: "+json);
        List<User> users = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for(int i=0;i<jsonArray.length();i++){
                User user = LocalJsonHelper.json2Object(jsonArray.get(i).toString(),User.class);
                Log.d(TAG, "readUsers: "+user.toString());
                users.add(user);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return users;
    }

    public static User getUserById(Context context,int id){
        List<User> users = readUsers(context);
        for(User user:users){
            if(user.getUid()==id){
                return user;
            }
        }
        return null;
    }

    public static void writeUsers(Context context,String json){
        FileUtils.writeString2DiskFileDir(context,json,FILENAME_USER);
    }

    public static void deleteUserCoupons(Context context,int userId,int couponId,int amount){
        List<User> users = readUsers(context);
        for(User user:users){
            if(user.getUid()==userId){
                User.UserCouponInfo[] couponInfo = user.getCouponInfo();
                for(int i=0;i<couponInfo.length;i++){
                    if(couponInfo[i].getId() == couponId){
                        int num = couponInfo[i].getCount() - amount;
                        User.UserCouponInfo[] newCouponInfo;
                        if(num>0){
                            couponInfo[i].setCount(num);
                            newCouponInfo = couponInfo.clone();
                        }else {
                            newCouponInfo = new User.UserCouponInfo[couponInfo.length-1];
                            for(int j=0;j<i;j++){
                                newCouponInfo[j] = couponInfo[j];
                            }
                            for(int j=i;j<newCouponInfo.length;j++){
                                newCouponInfo[j] = couponInfo[j+1];
                            }
                        }
                        user.setCouponInfo(newCouponInfo);
                        writeUsers(context,object2Json(users));
                        return;
                    }
                }
            }
        }
    }

    public static List<Order> readOrders(Context context){
        String json = readJsonFromDisk(context,FILENAME_ORDER);
        Log.d(TAG, "readOrders: JSON:"+json);
        List<Order> orders = new ArrayList<>();
        try{
            JSONArray jsonArray = new JSONArray(json);
            for(int i=0;i<jsonArray.length();i++){
                Order order = LocalJsonHelper.json2Object(jsonArray.get(i).toString(),Order.class);
                orders.add(order);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return orders;
    }

    public static Dish getDishById(Context context, int id){
        List<Dish> dishes = readDishes(context);
        for(Dish dish:dishes){
            if(dish.getId()==id){
                return dish;
            }
        }
        return null;
    }

    public static void writeOrders(Context context,String json){
        FileUtils.writeString2DiskFileDir(context,json,FILENAME_ORDER);
    }

    public static Order insertOrder(Context context, Order order){
        order.setId(getNewOrderId(context));
        List<Order> orders = readOrders(context);
        orders.add(order);
        String json = object2Json(orders);
        writeOrders(context,json);
        return order;
    }

    public static int getNewOrderId(Context context){
        String json = LocalJsonHelper.readJsonFromDisk(context,FILENAME_ORDER);
        List<Order> orders = new ArrayList<>();
        try{
            JSONArray jsonArray = new JSONArray(json);
            if(jsonArray.length() == 0){
                return 0;
            }
            //获取下一个的id (!要求按id从小到大顺序存储)
            int max_id = 0;
            for(int i=0;i<jsonArray.length();i++){
                int id = jsonArray.getJSONObject(i).getInt("id");
                if(id>max_id){
                    max_id = id;
                }
            }
            int index = max_id + 1;
            Log.d(TAG, "getNewOrderId: "+index);
            return index;
        }catch (JSONException e){
            e.printStackTrace();
        }
        return -1;
    }

    public static List<Coupon> readCoupons(Context context){
        String json = readJsonFromDisk(context,FILENAME_COUPON);
        Log.d(TAG, "readOrders: JSON:"+json);
        List<Coupon> coupons = new ArrayList<>();
        try{
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("couponDiscount");
            for(int i=0;i<jsonArray.length();i++){
                CouponDiscount coupon = LocalJsonHelper.json2Object(jsonArray.get(i).toString(),CouponDiscount.class);
                coupons.add(coupon);
            }
            jsonArray = jsonObject.getJSONArray("couponReduction");
            for(int i=0;i<jsonArray.length();i++){
                CouponReduction coupon = LocalJsonHelper.json2Object(jsonArray.get(i).toString(),CouponReduction.class);
                coupons.add(coupon);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return coupons;
    }

    public static Coupon getCouponById(Context context,int id) {
        List<Coupon> coupons = readCoupons(context);
        for(Coupon coupon:coupons){
            if(coupon.getId() == id){
                return coupon;
            }
        }
        return null;
    }




}
