package com.phicomm.phihome.model;

import com.phicomm.phihome.constants.UrlConfig;
import com.phicomm.phihome.net.callback.BaseCallback;
import com.phicomm.phihome.net.engine.OkHttpUtil;

/**
 * 云账号网络Model
 * Created by qisheng.lv on 2017/4/12.
 */
public class CloudAccountModel {

    /**
     * 获取授权码
     *
     * @param client_id
     * @param client_secret
     * @param redirect_uri
     * @param response_type
     * @param scope
     * @param callback
     */
    public void authorization(String client_id, String client_secret, String redirect_uri, String response_type, String scope, BaseCallback callback) {
        OkHttpUtil.get(UrlConfig.CloudAccountUrl.GET_AUTHORIZATION)
                .addParams("client_id", client_id)
                .addParams("client_secret", client_secret)
                .addParams("redirect_uri", redirect_uri)
                .addParams("response_type", response_type)
                .addParams("scope", scope)
                .run(null, callback);
    }

    /**
     * 登录云账号
     *
     * @param authorizationcode
     * @param mailaddress
     * @param password
     * @param phonenumber
     * @param username
     * @param callback
     */
    public void loginCloud(String authorizationcode, String mailaddress, String password, String phonenumber, String username, BaseCallback callback) {
        OkHttpUtil.post(UrlConfig.CloudAccountUrl.LOGIN)
                .addParams("authorizationcode", authorizationcode)
                .addParams("mailaddress", mailaddress)
                .addParams("password", password)
                .addParams("phonenumber", phonenumber)
                .addParams("username", username)
                .run(null, callback);
    }

    /**
     * 退出登录
     *
     * @param callback
     */
    public void logoutCloud(BaseCallback callback) {
        OkHttpUtil.post(UrlConfig.CloudAccountUrl.LOGOUT)
                .run(null, callback);
    }

    /**
     * 校验手机号是否已注册
     *
     * @param authorizationcode
     * @param phonenumber
     * @param callback
     */
    public void checkPhone(String authorizationcode, String mailaddress, String phonenumber, BaseCallback callback) {
        OkHttpUtil.get(UrlConfig.CloudAccountUrl.CHECK_PHONE)
                .addParams("authorizationcode", authorizationcode)
                .addParams("mailaddress", mailaddress)
                .addParams("phonenumber", phonenumber)
                .run(null, callback);
    }

    /**
     * 获取图形验证码
     *
     * @param authorizationcode
     * @param callback
     */
    public void getCaptcha(String authorizationcode, BaseCallback callback) {
        OkHttpUtil.get(UrlConfig.CloudAccountUrl.GET_CAPTCHA)
                .addParams("authorizationcode", authorizationcode)
                .run(null, callback);
    }


    /**
     * 获取短信验证码
     *
     * @param authorizationcode
     * @param captcha
     * @param captchaid
     * @param mailaddress
     * @param notsend
     * @param phonenumber
     * @param verificationtype
     * @param callback
     */
    public void getVerCode(String authorizationcode, String captcha, String captchaid, String mailaddress, String notsend,
                           String phonenumber, String verificationtype, BaseCallback callback) {
        OkHttpUtil.get(UrlConfig.CloudAccountUrl.GET_VER_CODE)
                .addParams("authorizationcode", authorizationcode)
                .addParams("captcha", captcha)
                .addParams("captchaid", captchaid)
                .addParams("mailaddress", mailaddress)
                .addParams("notsend", notsend)
                .addParams("phonenumber", phonenumber)
                .addParams("verificationtype", verificationtype)
                .run(null, callback);
    }

    /**
     * 校验短信验证码
     *
     * @param authorizationcode
     * @param phonenumber
     * @param verificationcode
     * @param callback
     */
    public void checkVerCode(String authorizationcode, String phonenumber, String verificationcode, BaseCallback callback) {
        OkHttpUtil.get(UrlConfig.CloudAccountUrl.CHECK_VER_CODE)
                .addParams("authorizationcode", authorizationcode)
                .addParams("phonenumber", phonenumber)
                .addParams("verificationcode", verificationcode)
                .run(null, callback);
    }


    /**
     * 注册斐讯云账号
     *
     * @param authorizationcode
     * @param data
     * @param mailaddress
     * @param password
     * @param phonenumber
     * @param registersource
     * @param username
     * @param verificationcode
     */
    public void register(String authorizationcode, String data, String mailaddress, String password, String phonenumber,
                         String registersource, String username, String verificationcode, BaseCallback callback) {
        OkHttpUtil.post(UrlConfig.CloudAccountUrl.REGISTER)
                .addParams("authorizationcode", authorizationcode)
                .addParams("data", data)
                .addParams("mailaddress", mailaddress)
                .addParams("password", password)
                .addParams("phonenumber", phonenumber)
                .addParams("registersource", registersource)
                .addParams("username", username)
                .addParams("verificationcode", verificationcode)
                .run(null, callback);
    }


}
