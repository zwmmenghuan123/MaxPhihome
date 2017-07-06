package com.phicomm.phihome.jsbridge;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.phicomm.phihome.bean.JsCallback;
import com.phicomm.phihome.bean.JsMessage;
import com.phicomm.phihome.bean.JsParams;
import com.phicomm.phihome.constants.AppConstans;
import com.phicomm.phihome.utils.LogUtils;
import com.tencent.smtt.sdk.WebView;

import java.lang.reflect.Method;

/**
 * Created by qisheng.lv on 2017/6/30.
 */

public class JsBridgeUtils {

//    public static String MESSAGE = "phihome://common/showToast?{data:{\"toast_msg\":\"hello i js\"}}";

    public static final String JS_CALLBACK_COMPLETE = "javascript:jsBridge.onComplete(%s)";

    /**
     * 反射调用原生方法
     *
     * @param webView
     * @param message
     * @param object
     * @return
     */
    public static String callJava(WebView webView, String message, Object object) {
        JsMessage jsMessage = parseMessage(webView, message);
        if (jsMessage == null) {
            return "failure";
        }

        Class<?> targetClass = null;
        if (jsMessage.getClasz().equals(AppConstans.JSConfig.JS_BRIDGE_COMMON)) {
            targetClass = CommonNativeImpl.class;
        }

        if (targetClass == null) {
            callBack(webView, jsMessage.getMethod(), AppConstans.JSConfig.ERR_CODE_CLASS_NOT_FOUND, jsMessage.getClasz() + "class not found", null);
            return "failure";
        }

        Method[] methods = targetClass.getDeclaredMethods();
        for (Method method : methods) {
            LogUtils.debug("JsBridgeUtils: callJava: " + method.getName());
            if (method.getName().equals(jsMessage.getMethod())) {
                try {
                    String result = (String) method.invoke(CommonNativeImpl.class.newInstance(), webView, jsMessage, object);
                    okCallBack(webView, method.getName(), result);
                    return result;
                } catch (Exception e) {
                    LogUtils.debug("JsBridgeUtils callJava Exception: " + e);
                    callBack(webView, jsMessage.getMethod(), AppConstans.JSConfig.ERR_CODE_METHOD_CALL_FAILURE, "method call exception", null);
                }
                return "failure";
            }
        }
        LogUtils.debug("JsBridgeUtils callJava no method found: " + message);
        callBack(webView, jsMessage.getMethod(), AppConstans.JSConfig.ERR_CODE_METHOD_NOT_FOUND, "method not found", null);
        return "failure";
    }

    /**
     * JS调用Native方法结束后，Natvie调用JS的方法进行结果回调
     *
     * @param webView
     * @param method
     * @param err_code
     * @param error_msg
     * @param data
     */
    public static void callBack(final WebView webView, String method, int err_code, String error_msg, String data) {
        if (webView != null) {
            final String callbackStr = getCallback(method, err_code, error_msg, data);
            webView.post(new Runnable() {
                @Override
                public void run() {
                    final String execJs = String.format(JS_CALLBACK_COMPLETE, String.valueOf(callbackStr));
                    webView.loadUrl(execJs);
                }
            });
            LogUtils.debug("JsBridgeUtils callBack: " + callbackStr);
        }
    }

    private static String getCallback(String method, int err_code, String error_msg, String result) {
        JsCallback jsCallback = new JsCallback();
        jsCallback.setMethod(method);
        jsCallback.setErr_code(err_code);
        jsCallback.setErr_msg(error_msg);
        jsCallback.setResult(result);
        return JSON.toJSONString(jsCallback);
    }

    public static void okCallBack(WebView webView, String method, String result) {
        callBack(webView, method, 0, "success", result);
    }

    /**
     * 解析JS传过来的完整message
     *
     * @param webView
     * @param message
     * @return
     */
    private static JsMessage parseMessage(WebView webView, String message) {
        LogUtils.debug("parseMessage: "+message);

        if (TextUtils.isEmpty(message) || !message.startsWith("phihome") || !message.contains("//") || !message.contains("?")) {
            callBack(webView, null, AppConstans.JSConfig.ERR_CODE_MESSAGE_ILLEGAL, message + " message illegal", null);
            return null;
        }

        String clsName;
        String method;
        String params;
        JsParams jsParams = null;
        try {
            clsName = message.substring(message.indexOf("//") + 2, message.lastIndexOf("/"));

            if (TextUtils.isEmpty(clsName)) {
                callBack(webView, null, AppConstans.JSConfig.ERR_CODE_CLASS_NOT_FOUND, "class empty", null);
                return null;
            }

            method = message.substring(message.lastIndexOf("/") + 1, message.indexOf("?"));

            if (TextUtils.isEmpty(method)) {
                callBack(webView, null, AppConstans.JSConfig.ERR_CODE_METHOD_NOT_FOUND, "method empty", null);
                return null;
            }

            params = message.substring(message.indexOf("?") + 1, message.length());

            if (TextUtils.isEmpty(params)) {
                callBack(webView, method, AppConstans.JSConfig.ERR_CODE_PARAMS_ILLEGAL, "params empty", null);
                return null;
            }

            try {
                jsParams = JSONObject.parseObject(params, JsParams.class);
            } catch (Exception e) {
                LogUtils.debug(params + " parseMessage JsonException: " + e);
            }

            if (jsParams == null) {
                callBack(webView, method, AppConstans.JSConfig.ERR_CODE_PARAMS_ILLEGAL, "params illegal", null);
                return null;
            }

        } catch (Exception e) {
            callBack(webView, null, AppConstans.JSConfig.ERR_CODE_MESSAGE_ILLEGAL, e.toString(), null);
            return null;
        }

        JsMessage jsMessage = new JsMessage();
        jsMessage.setScheme("phihome");
        jsMessage.setClasz(clsName);
        jsMessage.setMethod(method);
        jsMessage.setParams(params);
        jsMessage.setJsParams(jsParams);
        LogUtils.debug(jsMessage);
        return jsMessage;
    }


}