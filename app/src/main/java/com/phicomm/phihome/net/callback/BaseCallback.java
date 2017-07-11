package com.phicomm.phihome.net.callback;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.phicomm.phihome.bean.BaseResponse;
import com.phicomm.phihome.net.engine.Err2MsgUtils;
import com.phicomm.phihome.net.engine.OkHttpUtil;
import com.phicomm.phihome.utils.LogUtils;

import java.io.IOException;
import java.net.SocketTimeoutException;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 网络请求callback基类
 * Created by qisheng.lv on 2017/4/12.
 */
public abstract class BaseCallback<T> implements okhttp3.Callback {

    /**
     * 最终UI线程回调：请求失败
     *
     * @param code
     * @param msg
     */
    public abstract void onError(int code, String msg);

    /**
     * 最终UI线程回调：请求成功
     *
     * @param result
     */
    public abstract void onSuccess(String result, Request request);

    /**
     * OkHttp失败回调
     *
     * @param call
     * @param e
     */
    @Override
    public void onFailure(Call call, IOException e) {
        if (e instanceof SocketTimeoutException) {
            toUiError(Err2MsgUtils.CODE_NET_TIMEOUT, null, call.request());
        } else {
            toUiError(Err2MsgUtils.CODE_UNKNOW_ERROR, null, call.request());
        }
    }


    /**
     * OkHttp响应回调
     *
     * @param call
     * @param response
     * @throws IOException
     */
    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if (response == null) {
            toUiError(Err2MsgUtils.CODE_NO_RESPONSE, null, call.request());
            return;
        }
        if (response.isSuccessful()) {
            onSucessResponse(response);
        } else {
            toUiError(Err2MsgUtils.CODE_UNKNOW_ERROR, null, call.request());
        }
    }

    /**
     * 成功收到响应，在此对数据进行预处理，此处仍然是子线程
     *
     * @param response
     */
    public void onSucessResponse(Response response) {
        ResponseBody body = response.body();
        if (body == null) {
            toUiError(Err2MsgUtils.CODE_NO_RESPONSE, null, response.request());
        } else {
            String bodyStr = null;
            try {
                bodyStr = body.string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (TextUtils.isEmpty(bodyStr)) {
                toUiError(Err2MsgUtils.CODE_NO_RESPONSE, null, response.request());
                return;
            }

            BaseResponse baseObj = null;
            try {
                baseObj = JSON.parseObject(bodyStr, BaseResponse.class);
            } catch (Exception e) {
                LogUtils.debug(e);
            }
            if (baseObj == null) {
                toUiError(Err2MsgUtils.CODE_PARSE_ERROR, null, response.request());
                return;
            }

            int error = baseObj.getError();
            int tokenStatus = baseObj.getToken_status();
            String message = baseObj.getMessage();
            String httpCode = baseObj.getHttpCode();
            if (error == 0 && tokenStatus == 0 && httpCode.equals("200")) {
                toUiSuccess(bodyStr, response.request());
            } else if (tokenStatus > 0) {
                //token需要刷新，目前做法是直接回调错误，让用户重新登录。后面做成调用接口以刷新token
                toUiError(Err2MsgUtils.CODE_TOKEN_TIMEOUT, message, response.request());
            } else {
                toUiError(error, message, response.request());
            }
        }
    }


    /**
     * 从子线程转到UI线程
     *
     * @param code
     * @param message
     */
    public void toUiError(final int code, final String message, final Request request) {
        OkHttpUtil.getInstance().postRunable(new Runnable() {
            @Override
            public void run() {
                try {
                    String errorMsg = Err2MsgUtils.getErrMsg(code);
                    onError(code, errorMsg);
                } catch (Exception e) {
                    LogUtils.debug(e);
                }
            }
        });
    }


    /**
     * 从子线程转到UI线程
     *
     * @param result
     */
    public void toUiSuccess(final String result, final Request request) {
        OkHttpUtil.getInstance().postRunable(new Runnable() {
            @Override
            public void run() {
                onSuccess(result, request);
            }
        });
    }


}
