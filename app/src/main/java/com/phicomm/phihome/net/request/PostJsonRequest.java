package com.phicomm.phihome.net.request;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;


/**
 * Post请求：请求体为json
 * Created by qisheng.lv on 2017/4/12.
 */
public class PostJsonRequest extends BaseRequest {
    private JSONObject jsonObj;

    public PostJsonRequest(String url) {
        this.mUrl = url;
        jsonObj = new JSONObject();
        mBuilder = new Request.Builder().url(url);
    }

    @Override
    public PostJsonRequest addParams(String key, String value) {
        if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
            jsonObj.put(key, value);
        }
        return this;
    }

    @Override
    public Request generateRequest() {
        final MediaType jsonType = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(jsonType, jsonObj.toString());
        Request.Builder builder = new Request.Builder().url(mUrl);
        return builder.post(requestBody).build();
    }

}
