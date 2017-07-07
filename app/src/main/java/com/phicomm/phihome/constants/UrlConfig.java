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

    interface AccountCloudUrl {
        String URL_HOST = "https://account.phicomm.com";
        String GET_AUTHORIZATION = URL_HOST + "/v1/authorization"; //获取授权码
        String LOGIN = URL_HOST + "/v1/login"; //登陆云
        String LOGOUT = URL_HOST + "/v1/logout"; //退出登陆
    }

    interface SoftApInfoUrl {
        String URL_HOST = "http://192.168.2.139:8000";
        String READ_DEVICE_INFO_URL = URL_HOST + "/config-read";
        String WRITE_SSID_INFO_URL = URL_HOST + "/config-write-uap";
    }


}
