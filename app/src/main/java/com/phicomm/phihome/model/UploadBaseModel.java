package com.phicomm.phihome.model;

import com.phicomm.phihome.constants.UrlConfig;
import com.phicomm.phihome.manager.AccountManager;
import com.phicomm.phihome.net.callback.BaseCallback;
import com.phicomm.phihome.net.engine.OkHttpUtil;

/**
 * 上传Base64字符串
 * Created by xiaolei.yang on 2017/7/24.
 */

public class UploadBaseModel {

    /**
     * 上传Base64字符串
     * 该接口Header里要传入token
     *
     * @param imgBase64 图片的Base64编码
     * @param type      1-头像
     * @param callback  请求回调。
     */
    public void uploadBase64(String imgBase64, String type, BaseCallback callback) {
        OkHttpUtil.post(UrlConfig.CloudAccountUrl.UPLOAD_BASE64)
                .addHeader("Authorization", AccountManager.getInstance().getToken())
                .addParams("imgBase64", String.valueOf(imgBase64))
                .addParams("type", String.valueOf(type))
                .run(null, callback);
    }

}
