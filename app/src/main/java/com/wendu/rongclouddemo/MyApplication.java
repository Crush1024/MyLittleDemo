package com.wendu.rongclouddemo;

import android.app.Application;

import io.rong.imkit.RongIM;

/**
 * Created by xs on 2017/5/15.
 */

public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        RongIM.init(this);
    }
}
