package com.phicomm.phihome.net.request;

import android.text.TextUtils;

import com.phicomm.phihome.manager.AccountManager;
import com.phicomm.phihome.net.callback.BaseCallback;
import com.phicomm.phihome.net.engine.OkHttpUtil;

import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

/**
 * OkHttp Request基类
 * Created by qisheng.lv on 2017/4/19.
 */
public abstract class BaseRequest {
    protected String mUrl;
    protected Map<String, String> mParams;
    protected Request.Builder mBuilder;
    protected int mCacheSecond;


    public BaseRequest addParams(String key, String value) {
        if (this.mParams == null) {
            mParams = new LinkedHashMap<>();
        }
        if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
            mParams.put(key, value);
        }
        return this;
    }

    public BaseRequest addHeader(String name, String value) {
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(value) && mBuilder != null) {
            mBuilder.addHeader(name, value);
        }

        return this;
    }

    public abstract Request generateRequest();

    /**
     * 执行请求
     *
     * @param callback
     * @param <T>
     * @return
     */
    public <T> Call run(String tag, BaseCallback<T> callback) {
        Request request = generateRequest();
        mBuilder.tag(TextUtils.isEmpty(tag) ? request.url() : tag);
        addHeader("Authorization", AccountManager.getInstance().getToken());
        return OkHttpUtil.execute(request, callback);
    }

}
