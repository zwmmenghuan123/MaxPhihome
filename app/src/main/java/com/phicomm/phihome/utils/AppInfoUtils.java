package com.phicomm.phihome.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.phicomm.phihome.PhApplication;

import static com.tencent.mars.comm.PlatformComm.context;


/**
 *
 */
public class AppInfoUtils {

    /**
     * 获取当前包的版本信息
     *
     * @return
     */
    public static String getAppVersionName() {
        String strVer = null;
        try {
            PackageManager manager = PhApplication.getContext().getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            strVer = info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strVer;
    }


    /**
     * 获取当前包的版本编号
     *
     * @return
     */
    public static int getAppVersionCode() {
        int nVer = 0;
        try {
            PackageManager manager = PhApplication.getContext().getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            nVer = info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nVer;
    }

    /**
     * 获取渠道标志符
     *
     * @param context
     * @return
     */
    public static String getChannel(Context context) {
        return (String) CommonUtils.getMetaDataByKey(context, "UMENG_CHANNEL");
    }

    /**
     * 获取当前targetSdkVersion
     */

    public static int getTargetSdkVersion(Context context) {
        int version = 0;
        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo(context.getPackageName(), 0);
            if (applicationInfo != null) {
                version = applicationInfo.targetSdkVersion;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }


}
