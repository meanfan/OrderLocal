package com.mxswork.order;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.mxswork.order.pojo.DishImpl;
import com.mxswork.order.utils.FileUtils;
import com.mxswork.order.utils.LocalJsonHelper;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private Context appContext = InstrumentationRegistry.getTargetContext();

    @Test
    public void testGetDishes() {
        List<DishImpl> dishes = LocalJsonHelper.getDishes(appContext);
        assertNotNull(dishes);
        assertNotEquals(dishes.size(),0);
    }
    @Test
    public void testCopyJson(){
        Date date = new Date();
        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        assertTrue(FileUtils.copyAssetsFile2DiskFileDir(appContext,
                "dishes.json",
                "dishes-"+dateFormat.format(date)+".json"));

    }
}
