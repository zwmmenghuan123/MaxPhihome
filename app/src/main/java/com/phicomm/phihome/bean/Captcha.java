package com.phicomm.phihome.bean;

/**
 * 图形验证码
 * Created by qisheng.lv on 2017/7/11.
 */
public class Captcha extends BaseResponse {
    private String captcha;

    private String captchaid;

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getCaptchaid() {
        return captchaid;
    }

    public void setCaptchaid(String captchaid) {
        this.captchaid = captchaid;
    }

}
