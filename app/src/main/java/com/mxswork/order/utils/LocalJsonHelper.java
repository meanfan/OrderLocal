package com.mxswork.order.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.google.gson.Gson;
import com.mxswork.order.pojo.DishSingle;
import com.mxswork.order.pojo.DishPackage;
import com.mxswork.order.pojo.DishImpl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LocalJsonHelper {
    public static final String TAG = "LocalJsonHelper";

    public static String getJson(Context context, String fileName){
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

    public static <T> T Json2Object(String json,Class<T> type){
        Gson gson = new Gson();
        return gson.fromJson(json,type);
    }

    public static List<DishImpl> getDishes(Context context){
        String json = LocalJsonHelper.getJson(context,"dishes.json");
        List<DishImpl> dishes = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("dishSingle");
            for(int i=0;i<jsonArray.length();i++){
                DishSingle dish = LocalJsonHelper.Json2Object(jsonArray.get(i).toString(),DishSingle.class);
                dishes.add(dish);
                Log.d(TAG, "getDishSingle: "+dish.toString());
            }

            jsonArray = jsonObject.getJSONArray("dishPackage");
            Log.d(TAG, jsonArray.toString());
            for(int i=0;i<jsonArray.length();i++){
                DishPackage pack = LocalJsonHelper.Json2Object(jsonArray.get(i).toString(),DishPackage.class);
                dishes.add(pack);
                Log.d(TAG, "getDishPackage: "+pack.toString());
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return dishes;
    }
}
