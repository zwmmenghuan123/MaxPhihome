package com.phicomm.phihome;

import android.app.Application;
import android.content.Context;

import com.phicomm.phihome.utils.LogUtils;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.smtt.sdk.QbSdk;

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
        //bugly
        CrashReport.initCrashReport(getApplicationContext());
        //X5WebView
        QbSdk.initX5Environment(getApplicationContext(), new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {

            }

            @Override
            public void onViewInitFinished(boolean b) {
                LogUtils.debug("isX5initSuccess: " + b);
            }
        });
    }

    public static Context getContext() {
        return mContext;
    }


}
