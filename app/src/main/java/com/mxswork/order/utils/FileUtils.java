package com.mxswork.order.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {
    public static final String TAG = "FileUtil";
    public static boolean copyAssetsFile2DiskFileDir(Context context, String sourceFileName,String targetFileName){
        try {
            InputStream inputStream = context.getAssets().open(sourceFileName);
            //生产用，getFilesDir() 获得应用内部目录 /data/data/<包名>/files
            //File file = new File(context.getFilesDir().getPath() + File.separator + fileName);
            //测试用，自定义getExternalFilesDir() 获得应用在外部存储上的目录 /sdcard/Android/<包名>/files
            File file = new File(getExternalFilesDir( context,Environment.DIRECTORY_DOCUMENTS) + File.separator + targetFileName);
            if(!file.exists() || file.length()==0) {
                FileOutputStream fos =new FileOutputStream(file);//如果文件不存在，FileOutputStream会自动创建文件
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                bos.write(inputStream.read());
                bos.flush();
                inputStream.close();
                bos.close();
                fos.close();
                Log.d(TAG, "copyAssetsFile2DiskFileDir: " + sourceFileName + ", success");
                return true;
            } else {
                Log.d(TAG, "copyAssetsFile2DiskFileDir: " + sourceFileName + ", failure: existed");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getExternalFilesDir(Context context, String type){
        String path = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            path = context.getExternalFilesDir(type).getAbsolutePath();
        } else {
            path = context.getFilesDir().getAbsolutePath();
        }
        return path;
    }
    public static String getDiskCacheDir(Context context) {
        String path = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            path = context.getExternalCacheDir().getAbsolutePath();
        } else {
            path = context.getCacheDir().getAbsolutePath();
        }
        return path;
    }
}
