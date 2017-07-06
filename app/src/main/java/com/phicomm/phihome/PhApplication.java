package com.phicomm.phihome;

import android.app.Application;
import android.content.Context;

import com.phicomm.phihome.utils.LogUtils;

/**
 * Application基类
 * Created by qisheng.lv on 2017/7/5.
 */
public class PhApplication extends Application{
    private static Context mContext;
    public static boolean isJunitTest;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        initThirdParty();
    }

    private void initThirdParty() {
        //XLog
        LogUtils.init();
    }

    public static Context getContext() {
        return mContext;
    }


}
