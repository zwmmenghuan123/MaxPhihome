package com.phicomm.phihome.net.callback;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.phicomm.phihome.bean.CommonResponse;
import com.phicomm.phihome.bean.FxResponse;
import com.phicomm.phihome.bean.SzResponse;
import com.phicomm.phihome.constants.UrlConfig;
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
    private static final int SZ_SUCCESS_CODE = 200;
    private static final String ERROR_MSG_DEFAULT = "请求失败";

    /**
     * 最终UI线程回调：请求失败
     *
     * @param code
     * @param msg
     */
    public abstract void onError(String code, String msg);

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

            String url = response.request().url().toString();

            //处理斐讯云响应
            if (url.startsWith(UrlConfig.CloudAccountUrl.URL_HOST)) {
                parseFxResponse(bodyStr, response);
                return;
            }

            //处理深圳后台响应
            if (url.startsWith(UrlConfig.SzUrl.URL_HOST)) {
                parseSzResponse(bodyStr, response);
                return;
            }

            //处理其它响应
            parseCommonResponse(bodyStr, response);
        }
    }

    private void parseFxResponse(String bodyStr, Response response) {
        FxResponse fxObj = null;
        try {
            fxObj = JSON.parseObject(bodyStr, FxResponse.class);
        } catch (Exception e) {
            LogUtils.debug(e);
        }
        if (fxObj == null) {
            toUiError(Err2MsgUtils.CODE_PARSE_ERROR, null, response.request());
            return;
        }

        String error = fxObj.getError();
        int tokenStatus = fxObj.getToken_status();
        String message = fxObj.getMessage();
        String httpCode = fxObj.getHttpCode();
        if (error.equals("0") && tokenStatus == 0 && httpCode.equals("200")) {
            toUiSuccess(bodyStr, response.request());
        } else if (tokenStatus > 0) {
            //token需要刷新，目前做法是直接回调错误，让用户重新登录。后面做成调用接口以刷新token
            toUiError(Err2MsgUtils.CODE_TOKEN_TIMEOUT, message, response.request());
        } else {
            toUiError(error, message, response.request());
        }
    }

    private void parseSzResponse(String bodyStr, Response response) {
        SzResponse szObj = null;
        try {
            szObj = JSON.parseObject(bodyStr, SzResponse.class);
        } catch (Exception e) {
            LogUtils.debug(e);
        }
        if (szObj == null) {
            toUiError(Err2MsgUtils.CODE_PARSE_ERROR, null, response.request());
            return;
        }

        int status = szObj.getStatus();
        String message = szObj.getMessage();
        String retMsg = null;
        int retStatus = 0;
        SzResponse.Result resultObj = szObj.getResult();

        if (resultObj != null) {
            retMsg = resultObj.getRet_message();
            retStatus = resultObj.getRet_status();
        }

        if (status == SZ_SUCCESS_CODE && resultObj != null && resultObj.getRet_status() == SZ_SUCCESS_CODE) {
            toUiSuccess(JSON.toJSONString(resultObj), response.request());
        } else if (status != SZ_SUCCESS_CODE) {
            toUiError(status + "", TextUtils.isEmpty(message) ? ERROR_MSG_DEFAULT : message, response.request());
        } else {
            toUiError(retStatus + "", TextUtils.isEmpty(retMsg) ? ERROR_MSG_DEFAULT : retMsg, response.request());
        }
    }

    private void parseCommonResponse(String bodyStr, Response response) {
        CommonResponse commonObj = null;
        try {
            commonObj = JSON.parseObject(bodyStr, CommonResponse.class);
        } catch (Exception e) {
            LogUtils.debug(e);
        }
        if (commonObj == null) {
            toUiError(Err2MsgUtils.CODE_PARSE_ERROR, null, response.request());
            return;
        }

        String error = commonObj.getError();
        int tokenStatus = commonObj.getToken_status();
        String message = commonObj.getMessage();
        String httpCode = commonObj.getHttpCode();
        if (error.equals("0") && tokenStatus == 0 && httpCode.equals("200")) {
            toUiSuccess(bodyStr, response.request());
        } else if (tokenStatus > 0) {
            //token需要刷新，目前做法是直接回调错误，让用户重新登录。后面做成调用接口以刷新token
            toUiError(Err2MsgUtils.CODE_TOKEN_TIMEOUT, message, response.request());
        } else {
            toUiError(error, message, response.request());
        }
    }


    /**
     * 从子线程转到UI线程
     *
     * @param code
     * @param message
     */
    public void toUiError(final String code, final String message, final Request request) {
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
