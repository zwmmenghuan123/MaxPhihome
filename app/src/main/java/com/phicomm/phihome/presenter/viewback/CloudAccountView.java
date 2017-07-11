package com.phicomm.phihome.presenter.viewback;


import com.phicomm.phihome.bean.Captcha;
import com.phicomm.phihome.bean.CloudAccount;

/**
 * 云账号相关View回调
 * Created by qisheng.lv on 2017/4/12.
 */
public class CloudAccountView {
    public void onAuthorizationError(int code, String msg) {

    }

    public void onAuthorizationSuccess(String authorizationcode) {

    }

    public void onLoginError(int code, String msg) {

    }

    public void onLoginSuccess(CloudAccount cloudAccount) {

    }

    public void onLogoutError(int code, String msg) {

    }

    public void onLogoutSuccess() {

    }


    public void onGetCaptchaError(int code, String msg) {

    }

    public void onGetCaptchaSuccess(Captcha captcha) {

    }


    public void onGetVerCodeError(int code, String msg) {

    }

    public void onGetVerCodeSuccess() {

    }

    public void onRegisterError(int code, String msg) {

    }

    public void onRegisterSuccess() {

    }


}
