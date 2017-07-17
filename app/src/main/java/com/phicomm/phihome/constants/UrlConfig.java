package com.phicomm.phihome.constants;


import com.phicomm.phihome.utils.NetworkUtils;

/**
 * Url配置
 * Created by qisheng.lv on 2017/4/12.
 */
public interface UrlConfig {

    interface LocalUrl {
        String URL_ROUTER_HOST = "http://192.168.2.1";
        String URL_HOST = NetworkUtils.getLocalUrl();
    }

    interface CloudAccountUrl {
        String URL_HOST = "https://account.phicomm.com";
        String GET_AUTHORIZATION = URL_HOST + "/v1/authorization"; //获取授权码
        String LOGIN = URL_HOST + "/v1/login"; //登陆云
        String LOGOUT = URL_HOST + "/v1/logout"; //退出登陆
        String GET_CAPTCHA = URL_HOST + "/v1/captcha"; //获取图形验证码
        String GET_VER_CODE = URL_HOST + "/v1/verificationMsg"; //获取短信验证码
        String REGISTER = URL_HOST + "/v1/account"; //注册账号
    }

    interface SoftApInfoUrl {
        String URL_HOST = "http://10.10.10.1:8000";
        String READ_DEVICE_INFO_URL = URL_HOST + "/config-read";
        String WRITE_SSID_INFO_URL = URL_HOST + "/config-write-uap";
        String GET_CONN_STATE_URL = URL_HOST + "/conn-state";
        String CLOSE_DEVICE_AP_URL = URL_HOST + "/close-ap";
    }

    interface SzUrl {
        String URL_HOST = "http://172.31.34.55:8090";
        String DEVICES_URL = URL_HOST + "/phihome/v1/user/devices";
    }


}
