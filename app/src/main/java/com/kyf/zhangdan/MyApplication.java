package com.kyf.zhangdan;

import android.app.Application;
import android.content.Context;

/**
 * Created by keyf on 2015/9/18.
 */
public class MyApplication extends Application {

    private static Context mContext;

    public void onCreate(){
        super.onCreate();
        mContext = this.getApplicationContext();
    }

    public static Context getContext(){
        return mContext;
    }

}
