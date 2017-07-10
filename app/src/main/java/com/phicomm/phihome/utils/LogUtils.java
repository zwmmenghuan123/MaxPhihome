package com.phicomm.phihome.utils;

import android.os.Build;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.phicomm.phihome.BuildConfig;
import com.tencent.mars.xlog.Log;
import com.tencent.mars.xlog.Xlog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Calendar;

/**
 * 基于Xlog封装的日志工具类
 * Created by weiming.zeng on 2017/4/19.
 */

public class LogUtils {
    private static final String TAG = "phlog"; //默认TAG
    private static final String SDCARD = Environment.getExternalStorageDirectory().getAbsolutePath();
    private static final String logPath = SDCARD + "/" + TAG + "/log";

    public static int getMethodCcount() {
        return METHOD_CCOUNT;
    }

    public static void setMethodCcount(int methodCcount) {
        METHOD_CCOUNT = methodCcount;
    }

    public static void setJsonIndent(int sonIndent) {
        JSON_INDENT = sonIndent;
    }

    private static int METHOD_CCOUNT = 8;   //打印方法栈的方法数

    private static final int LEVEL_VERBOSE = 0;
    private static final int LEVEL_DEBUG = 1;
    private static final int LEVEL_INFO = 2;
    private static final int LEVEL_WARNING = 3;
    private static final int LEVEL_ERROR = 4;
    private static final int LEVEL_FATAL = 5;
//    private static final int LEVEL_NONE = 6;

    private static int JSON_INDENT = 2;    //the number of spaces to indent for each level of nesting.
    //SP中记录最新日志日期的key
    private static final String LOG_DATE = "logDate";

    private LogUtils() {
        try {
            throw new UnsupportedOperationException("can not instantiate LogUtils...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void init() {
        try {
            if (!checkCpu()) {
                return;
            }
            System.loadLibrary("stlport_shared");
            System.loadLibrary("marsxlog");
            //init xlog
            if (BuildConfig.isDebug) {
                Xlog.appenderOpen(Xlog.LEVEL_DEBUG, Xlog.AppednerModeAsync, "", logPath, TAG);
                Xlog.setConsoleLogOpen(true);
            } else {
                Xlog.appenderOpen(Xlog.LEVEL_INFO, Xlog.AppednerModeAsync, "", logPath, TAG);
                Xlog.setConsoleLogOpen(false);
            }
            Log.setLogImp(new Xlog());
        } catch (Exception e) {
            e.printStackTrace();
        }
        debug("xLog ok");
    }


    private static boolean checkCpu() {
        String[] abis = new String[]{};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            abis = Build.SUPPORTED_ABIS;
        } else {
            abis = new String[]{Build.CPU_ABI, Build.CPU_ABI2};
        }
        //这里目前只支持armeabi架构
        for (String abi : abis) {
            android.util.Log.d("myso", abi);
            if (abi.equals("armeabi")) {
                return true;
            }
        }
        return false;
    }

    public static String getLogLevel() {
        String level;
        switch (Log.getLogLevel()) {
            case 0:
                level = "LEVEL_VERBOSE";
                break;
            case 1:
                level = "LEVEL_DEBUG";
                break;
            case 2:
                level = "LEVEL_INFO";
                break;
            case 3:
                level = "LEVEL_WARNING";
                break;
            case 4:
                level = "LEVEL_ERROR";
                break;
            case 5:
                level = "LEVEL_FATAL";
                break;
            case 6:
                level = "LEVEL_NONE";
                break;
            default:
                level = "";
        }
        return level;
    }

    /**
     * 生成格式化的日志
     *
     * @param obj 需要格式化的内容
     * @return 格式化后的日志内容
     */
    private static String createMessage(Object obj) {
        String message;
        if (obj.getClass().isArray()) {
            message = Arrays.deepToString((Object[]) obj);
        } else {
            message = obj.toString();
        }
        StringBuilder content = new StringBuilder("Thread:" + Thread.currentThread().getName() + "---");
        content.append(message);
        return content.toString();
    }

    public static void verbose(Object message) {
        verbose(TAG, message, false);
    }

    public static void verbose(Object message, boolean isPrStack) {
        verbose(TAG, message, isPrStack);
    }

    public static void verbose(String tag, Object message, boolean isPrStack) {
        log(LEVEL_VERBOSE, tag, message, isPrStack, null);
    }

    public static void verbose(String tag, Object message, boolean isPrStack, final Object... args) {
        log(LEVEL_VERBOSE, tag, message, isPrStack, null, args);
    }

    public static void debug(Object message) {
        debug(message, false);
    }

    public static void debug(Object message, boolean isPrStack) {
        debug(TAG, message, isPrStack);
    }

    public static void debug(String tag, Object message, boolean isPrStack) {
        log(LEVEL_DEBUG, tag, message, isPrStack, null);
    }

    public static void debug(String tag, Object message, boolean isPrStack, final Object... args) {
        log(LEVEL_DEBUG, tag, message, isPrStack, null, args);
    }

    public static void info(Object message) {
        info(message, false);
    }

    public static void info(Object message, boolean isPrStack) {
        info(TAG, message, isPrStack);
    }

    public static void info(String tag, Object message, boolean isPrStack) {
        log(LEVEL_INFO, tag, message, isPrStack, null);
    }

    public static void info(String tag, Object message, boolean isPrStack, final Object... args) {
        log(LEVEL_INFO, tag, message, isPrStack, null, args);
    }

    public static void warn(Object message) {
        warn(message, false);
    }

    public static void warn(Object message, boolean isPrStack) {
        warn(TAG, message, isPrStack);
    }

    public static void warn(String tag, Object message, boolean isPrStack) {
        log(LEVEL_WARNING, tag, message, isPrStack, null);
    }

    public static void warn(String tag, Object message, boolean isPrStack, final Object... args) {
        log(LEVEL_WARNING, tag, message, isPrStack, null, args);
    }

    public static void error(Object message) {
        error(message, false);
    }

    public static void error(Object message, boolean isPrStack) {
        error(TAG, message, isPrStack);
    }

    public static void error(String tag, Object message, boolean isPrStack) {
        log(LEVEL_ERROR, tag, message, isPrStack, null);
    }

    public static void error(String tag, final Object message, boolean isPrStack, final Object... args) {
        log(LEVEL_FATAL, tag, message, isPrStack, null, args);
    }

    public static void fatal(Object message) {
        fatal(message, false);
    }

    public static void fatal(Object message, boolean isPrStack) {
        fatal(TAG, message, isPrStack);
    }

    public static void fatal(String tag, Object message, boolean isPrStack) {
        log(LEVEL_FATAL, tag, message, isPrStack, null);
    }

    public static void fatal(String tag, final Object message, boolean isPrStack, final Object... args) {
        log(LEVEL_FATAL, tag, message, isPrStack, null, args);
    }

    /**
     * 打印json日志
     *
     * @param json
     */
    public static void json(String json, boolean isPrStack) {
        if (TextUtils.isEmpty(json)) {
            log(LEVEL_INFO, TAG, "Empty/Null json content", isPrStack, null);
        }
        try {
            if (json.charAt(0) == '{') {
                JSONObject jsonObject = new JSONObject(json);
                String message = jsonObject.toString(JSON_INDENT);
                log(LEVEL_INFO, TAG, message, isPrStack, null);
                return;
            }
            if (json.charAt(0) == '[') {
                JSONArray jsonArray = new JSONArray(json);
                String message = jsonArray.toString(JSON_INDENT);
                log(LEVEL_INFO, TAG, message, isPrStack, null);
                return;
            }
            log(LEVEL_ERROR, TAG, "Invalid json", isPrStack, null);
        } catch (JSONException e) {
            log(LEVEL_ERROR, TAG, "error Json", isPrStack, e.getCause());
        }
    }

    /**
     * 获取堆栈信息
     *
     * @param tr
     * @return
     */
    private static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }
        // This is to reduce the amount of log spew that apps do in the non-error
        Throwable t = tr;
        while (t != null) {
            if (t instanceof UnknownHostException) {
                return "";
            }
            t = t.getCause();
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }

    /**
     * 获取当前thread的堆栈信息
     *
     * @param message
     * @return
     */
    private static String getThreadStack(String message) {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        StringBuilder builder = new StringBuilder(message);
        if (elements != null) {
            for (int i = 0; i < METHOD_CCOUNT; i++) {
                builder.append(System.getProperty("line.separator"))
                        .append(elements[i].getFileName())
                        .append(File.separator)
                        .append(elements[i].getClassName())
                        .append(File.separator)
                        .append(elements[i].getMethodName())
                        .append(File.separator)
                        .append(elements[i].getLineNumber());
            }
        }
        return builder.toString();
    }

    public static void log(int priority, String tag, Object message, boolean isPrStack, @Nullable Throwable throwable, Object... args) {
        logDeviceInfo();
        if (Log.getLogLevel() == Log.LEVEL_NONE) {
            return;
        }
        if (null == message || message.toString().length() == 0) {
            Log.e(TAG, "Empty/null log content");
            return;
        }
        String contents = createMessage(message);
        try {
            if (isPrStack) {
                contents = getThreadStack(contents);
            }
            if (throwable != null) {
                contents += ":" + getStackTraceString(throwable);
            }
        } catch (Exception e) {
            Log.e("error", "error message:" + message.toString());
        }
        switch (priority) {
            case LEVEL_DEBUG:
//                Log.d(tag, contents, args);
                //debug情况不需要持久化
                android.util.Log.d(tag, contents);
                break;
            case LEVEL_INFO:
                Log.i(tag, contents, args);
                break;
            case LEVEL_WARNING:
                Log.w(tag, contents, args);
                break;
            case LEVEL_ERROR:
                Log.e(tag, contents, args);
                break;
            case LEVEL_FATAL:
                Log.f(tag, contents, args);
                break;
            default:
                Log.i(tag, contents, args);
                break;
        }
    }

    /**
     * 手机终端的信息日志记录
     */
    private static void logDeviceInfo() {
        Calendar calendar = Calendar.getInstance();
        int date = calendar.get(Calendar.DAY_OF_MONTH);
        //SP中记录最新的日志打印日期信息，如果当前日志和SP中最新的日志打印日期是同一天，则不打印设备信息，以此保证日志信息中，关于设备信息，一天只有打一次


        boolean result = false;
        Object object = SpfUtils.get(LOG_DATE, calendar.get(Calendar.DAY_OF_MONTH));
        if (null != object) {
            String in = object.toString();
            try {
                int left = Integer.parseInt(in);
                result = left != date;
            }catch (NumberFormatException e){
                e.printStackTrace();
            }
        }

        if (result) {//这里应该为!=，方便调试每次都打印改为==
            //打印设备信息，并更新SP
            String phoneInfo = String.format("%n手机品牌:%s%n型号:%s%n操作系统:%s %n手机系统版本:%s %nApp版本:%s %n",
                    SystemUtils.getDeviceBrand(),
                    SystemUtils.getSystemModel(),
                    Build.VERSION.SDK_INT,
                    SystemUtils.getSystemVer(),
                    AppInfoUtils.getAppVersionName());

            Log.i("DeviceInfo", phoneInfo);
            SpfUtils.put(LOG_DATE, calendar.get(Calendar.DAY_OF_MONTH));
        }
    }

    /**
     * 反注册，需要在程序关闭时调用
     */
    public static void unLoad() {
        try {
            Log.appenderClose();
        } catch (Exception e) {
            return;
        }

    }


}
