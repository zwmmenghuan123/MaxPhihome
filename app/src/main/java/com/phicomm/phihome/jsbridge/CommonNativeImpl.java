package com.phicomm.phihome.jsbridge;

import android.content.Context;
import android.text.TextUtils;

import com.phicomm.phihome.bean.JsMessage;
import com.phicomm.phihome.utils.ToastUtil;
import com.tencent.smtt.sdk.WebView;

/**
 * Created by qisheng.lv on 2017/6/30.
 */

public class CommonNativeImpl {

    public String showToast(WebView webView, JsMessage jsMessage, Context context) {
        if (jsMessage == null || jsMessage.getJsParams() == null || TextUtils.isEmpty(jsMessage.getJsParams().getToast_msg())) {
            return null;
        }

        String toast = jsMessage.getJsParams().getToast_msg();
        ToastUtil.show(toast);
        return toast;
    }

}
