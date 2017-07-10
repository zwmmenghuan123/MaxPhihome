package com.phicomm.phihome.utils;

import android.os.Build;

import java.util.Locale;

/**
 * Created by yanghaibin on 16/6/17.
 */
public class SystemUtils {
    /**
     * 获取系统的版本号
     *
     * @return
     */
    public static String getSystemVer() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取系统的语言
     *
     * @return
     */
    public static String getSystemLanguage() {
        return Locale.getDefault().toString();
    }

    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getSystemModel() {
        return Build.MODEL;
    }

    /**
     * 获取手机厂商
     *
     * @return
     */
    public static String getDeviceBrand() {
        return Build.BRAND;
    }
}
