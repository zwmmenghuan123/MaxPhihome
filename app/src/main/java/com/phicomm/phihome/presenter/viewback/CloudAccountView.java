package com.phicomm.phihome.presenter.viewback;


import com.phicomm.phihome.bean.Captcha;
import com.phicomm.phihome.bean.CloudAccount;

/**
 * 云账号相关View回调
 * Created by qisheng.lv on 2017/4/12.
 */
public class CloudAccountView {
    public void onAuthorizationError(String code, String msg) {

    }

    public void onAuthorizationSuccess(String authorizationcode) {

    }

    public void onLoginError(String code, String msg) {

    }

    public void onLoginSuccess(CloudAccount cloudAccount) {

    }

    public void onLogoutError(String code, String msg) {

    }

    public void onLogoutSuccess() {

    }


    public void onGetCaptchaError(String code, String msg) {

    }

    public void onGetCaptchaSuccess(Captcha captcha) {

    }


    public void onGetVerCodeError(String code, String msg) {

    }

    public void onGetVerCodeSuccess() {

    }

    public void onRegisterError(String code, String msg) {

    }

    public void onRegisterSuccess() {

    }


}
