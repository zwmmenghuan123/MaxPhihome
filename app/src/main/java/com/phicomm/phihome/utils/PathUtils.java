package com.phicomm.phihome.utils;

import android.os.Environment;

import com.phicomm.phihome.PhApplication;

import java.io.File;

/**
 * 文件路径管理类
 * Created by weiming.zeng on 2017/4/25.
 */

public class PathUtils {
    /**
     * 内部存储目录
     */
    public static final String INTERNAL_CACHE = PhApplication.getContext().getCacheDir().getAbsolutePath() + File.separator;
    public static final String INTERNAL_FILE = PhApplication.getContext().getFilesDir().getAbsolutePath() + File.separator;

    /**
     * 外部存储目录
     */
    public static final String EXTERNAL_PHIHOME = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "phihome" + File.separator;

    public static final String INTERNAL_COMMON = INTERNAL_CACHE + "common" + File.separator;
    public static final String EXTERNAL_COMMON = EXTERNAL_PHIHOME + "common" + File.separator;

    public static String getImageCache() {
        String imageCache = INTERNAL_COMMON + "image";
        File file = new File(imageCache);
        if (!file.exists()) {
            file.mkdirs();
        }
        return imageCache;
    }

    public static String getDownload() {
        if (FileUtils.isSDCardAvailable()) {
            return EXTERNAL_COMMON + "download";
        } else {
            return INTERNAL_COMMON + "download";
        }
    }

}
