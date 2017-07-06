package com.phicomm.phihome.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.phicomm.phihome.PhApplication;

/**
 * 通用工具类
 * Created by qisheng.lv on 2017/4/12.
 */

public class CommonUtils {

    public static String getString(int resId) {
        if (resId > 0) {
            return PhApplication.getContext().getString(resId);
        }
        return "";
    }

    /**
     * 从 manifest 获取 meta 数据
     *
     * @param context
     * @param key
     * @return
     */
    public static Object getMetaDataByKey(Context context, String key) {
        Object obj = null;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            obj = info.metaData.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

}
