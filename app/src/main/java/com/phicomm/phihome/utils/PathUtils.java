package com.phicomm.phihome.utils;

import android.os.Environment;

import com.phicomm.prototype.MyApplication;

import java.io.File;

/**
 * 文件路径管理类
 * Created by weiming.zeng on 2017/4/25.
 */

public class PathUtils {
    /**
     * 内部存储目录
     */
    public static final String INTERNAL_CACHE = MyApplication.getContext().getCacheDir().getAbsolutePath() + File.separator;
    public static final String INTERNAL_FILE = MyApplication.getContext().getCacheDir().getAbsolutePath() + File.separator;

    public static final String INTERNAL_APP = INTERNAL_FILE + File.separator + "app";
    public static final String INTERNAL_USER = INTERNAL_FILE + File.separator + "user";
    public static final String INTERNAL_DEVICE = INTERNAL_FILE + File.separator + "device";

    public static final String INTERNAL_APP_CRASHRECORD = INTERNAL_APP + File.separator + "app_crashrecord";
    public static final String INTERNAL_DOWNLOAD = INTERNAL_APP + File.separator + "download";

    public static final String INTERNAL_GUEST = INTERNAL_USER + File.separator + "guest";


    /**
     * 外部存储目录
     */
    public static final String EXTERNAL_PHICOMM = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "PhiComm";

    private static final String APP_LOGS = File.separator + "logs" + File.separator;
    private static final String APP_DOWNLOAD = File.separator + "download" + File.separator;
    private static final String APP_BANNER = File.separator + "banner" + File.separator;
    private static final String APP_DEVICE = File.separator + "device" + File.separator;
//    private static final String APP_USER = File.separator + "user" + File.separator;

    private static final String IMAGE = File.separator + "imgage";
    private static final String VOICE = File.separator + "voice";
    private static final String VIDEO = File.separator + "video";


    public static String getUserPath(String userName) {
        return INTERNAL_USER + userName;
    }

    public static String getInternalDevicePath(String deviceID) {
        return INTERNAL_DEVICE + File.separator + deviceID;
    }

    public static String getExternalAppPath(String app) {
        return EXTERNAL_PHICOMM + File.separator + app;
    }

    public static String getExternalDevicePath(String app, String deviceID) {
        return getExternalAppPath(app) + APP_DEVICE + deviceID;
    }

    public static String getLogPath(String app) {
        return getExternalAppPath(app) + APP_LOGS;
    }

    public static String getExternalDownloadPath(String app) {
        return getExternalAppPath(app) + APP_DOWNLOAD;
    }

    public static String getBannerPath(String app) {
        return getExternalAppPath(app) + APP_BANNER;
    }
    

    public static String getExternalUserPath(String app, String userName) {
        return getExternalAppPath(app) + APP_DEVICE + userName;
    }

    public static String getExternalImagePath(String app, String userName) {
        return getExternalUserPath(app, userName) + IMAGE;
    }

    public static String getExternalVoicePath(String app, String userName) {
        return getExternalUserPath(app, userName) + VOICE;
    }

    public static String getExternalVideoPath(String app, String userName) {
        return getExternalUserPath(app, userName) + VIDEO;
    }

}
