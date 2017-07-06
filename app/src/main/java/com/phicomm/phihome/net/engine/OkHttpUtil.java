package com.phicomm.phihome.net.engine;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.phicomm.phihome.PhApplication;
import com.phicomm.phihome.constants.NetConfig;
import com.phicomm.phihome.net.callback.BaseCallback;
import com.phicomm.phihome.net.interceptor.MyInterceptor;
import com.phicomm.phihome.net.request.GetRequest;
import com.phicomm.phihome.net.request.PostJsonRequest;
import com.phicomm.phihome.net.request.PostRequest;
import com.phicomm.phihome.net.ssl.MySslFatory;
import com.phicomm.phihome.utils.NetworkUtils;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * 以OkHttp为内核的网络请求工具类
 * Created by qisheng.lv on 2017/4/12.
 */
public class OkHttpUtil {
    private static volatile OkHttpUtil mOkHttpUtls;
    private static OkHttpClient mHttpClient;
    private Handler mHandler;

    private OkHttpUtil() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(NetConfig.HTTP_CONNECT_TIME_OUT, TimeUnit.MILLISECONDS);
        builder.readTimeout(NetConfig.HTTP_READ_TIME_OUT, TimeUnit.MILLISECONDS);
        builder.writeTimeout(NetConfig.HTTP_WRITE_TIME_OUT, TimeUnit.MILLISECONDS);
//        builder.cookieJar(new CookieManager());
        builder.addInterceptor(new MyInterceptor().setLevel(MyInterceptor.Level.BODY));

        X509TrustManager x509TrusManaer = MySslFatory.createX509TrusManaer();
        builder.sslSocketFactory(MySslFatory.createSSlSocketFactory(x509TrusManaer), x509TrusManaer);
        builder.hostnameVerifier(MySslFatory.getHostnameVerifier());

        mHttpClient = builder.build();
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static OkHttpUtil getInstance() {
        if (mOkHttpUtls == null) {
            synchronized (OkHttpUtil.class) {
                if (mOkHttpUtls == null) {
                    mOkHttpUtls = new OkHttpUtil();
                }
            }
        }
        return mOkHttpUtls;
    }

    public static OkHttpClient getHttpClient() {
        return getInstance().mHttpClient;
    }

    public static void postRunable(Runnable runnable) {
        getInstance().mOkHttpUtls.mHandler.post(runnable);
    }

    public static GetRequest get(String url) {
        return new GetRequest(url);
    }


    public static PostRequest post(String url) {
        return new PostRequest(url);
    }

    public static PostJsonRequest postJson(String url) {
        return new PostJsonRequest(url);
    }



    public static <T> Call execute(Request request, BaseCallback<T> callback) {
        if (!NetworkUtils.isNetAvailable()) {
            callback.onError(NetConfig.ERROR_NET_UNAVALIABLE_CODE, NetConfig.ERROR_NET_UNAVALIABLE);
            return null;
        }
        Call call = getHttpClient().newCall(request);

        if (!PhApplication.isJunitTest) {
            call.enqueue(callback);
            return call;
        }

        //以下是把异步网络请求改成同步
        Response response;
        try {
            response = call.execute();
            if (response == null) {
                callback.onFailure(call, new IOException("no response"));
            } else {
                callback.onResponse(call, response);
            }
        } catch (IOException e) {
            callback.onFailure(call, e);
        }

        return call;
    }


    /**
     * 取消请求
     *
     * @param tag
     */
    public static void cancelRequest(String tag) {
        try {
            List<Call> queuedCalls = mHttpClient.dispatcher().queuedCalls();
            List<Call> runningCalls = mHttpClient.dispatcher().runningCalls();

            for (Call call : queuedCalls) {
                if (call.request().tag().equals(tag)) {
                    call.cancel();
                }
            }

            for (Call call : runningCalls) {
                if (call.request().tag().equals(tag)) {
                    call.cancel();
                }
            }

        } catch (Exception e) {
            Log.d("OkHttp", "cancelRequest Exception: " + e.toString());
        }
    }


}
