package com.phicomm.phihome.model;

import com.phicomm.phihome.constants.UrlConfig;
import com.phicomm.phihome.manager.AccountManager;
import com.phicomm.phihome.net.callback.BaseCallback;
import com.phicomm.phihome.net.engine.OkHttpUtil;

/**
 * 上传Base64字符串
 * Created by xiaolei.yang on 2017/7/24.
 */

public class UserInfoModel {

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

    /**
     * 获取用户头像
     *
     * @param callback 请求回调。
     */
    public void avatarUrl(BaseCallback callback) {
        OkHttpUtil.get(UrlConfig.CloudAccountUrl.AVATAR_URL)
                .addHeader("Authorization", AccountManager.getInstance().getToken())
                .addParams("access_token", AccountManager.getInstance().getToken())//可选，如果header中没有Authorization，则使用参数传入token
                .run(null, callback);
    }

    /**
     * 获取账户信息
     *
     * @param callback 请求回调。
     */
    public void accountDetail(BaseCallback callback) {
        OkHttpUtil.get(UrlConfig.CloudAccountUrl.ACCOUNT_DETAIL)
                .addHeader("Authorization", AccountManager.getInstance().getToken())
                .run(null, callback);
    }

    /**
     * 修改账户信息
     *
     * @param callback 请求回调。
     */
    public void property(String accountDetailsString, BaseCallback callback) {
        OkHttpUtil.post(UrlConfig.CloudAccountUrl.PROPERTY)
                .addHeader("Authorization", AccountManager.getInstance().getToken())
                .addParams("data", accountDetailsString)
                .run(null, callback);
    }

    /**
     * 修改密码
     *
     * @param oldPassword 旧密码（用户明文密码的MD5值）
     * @param newPassword 新密码（用户明文密码的MD5值）
     * @param callback
     */
    public void password(String oldPassword, String newPassword, BaseCallback callback) {
        OkHttpUtil.post(UrlConfig.CloudAccountUrl.PASSWORD)
                .addHeader("Authorization", AccountManager.getInstance().getToken())
                .addParams("newpassword", newPassword)
                .addParams("oldpassword", oldPassword)
                .run(null, callback);
    }


}
