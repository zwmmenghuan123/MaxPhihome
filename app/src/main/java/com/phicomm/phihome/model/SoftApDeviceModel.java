package com.phicomm.phihome.model;

import com.phicomm.phihome.constants.UrlConfig;
import com.phicomm.phihome.net.callback.BaseCallback;
import com.phicomm.phihome.net.engine.OkHttpUtil;

/**
 * 获取智能设备信息
 * Created by xiaolei.yang on 2017/7/6.
 */

public class SoftApDeviceModel {

    /**
     * 获取设备周围的SSID列表
     *
     * @param callback
     */
    public void readDeviceInfo(BaseCallback callback) {
        OkHttpUtil.get(UrlConfig.SoftApInfoUrl.READ_DEVICE_INFO_URL)
                .run(null, callback);
    }

    /**
     * 向设备写入SSID信息
     *
     * @param callback
     */
    public void writeSsidInfo(String ssid, String password, BaseCallback callback) {
        OkHttpUtil.post(UrlConfig.SoftApInfoUrl.WRITE_SSID_INFO_URL)
                .addParams("GATEWAY", "10.10.10.1")
                .addParams("DNS1", "10.10.10.1")
                .addParams("IDENTIFIER", String.valueOf(168430083))
                .addParams("SSID", ssid)
                .addParams("NETMASK", "255.255.255.0")
                .addParams("IP", "10.10.10.4")
                .addParams("PASSWORD", password)
                .addParams("DHCP", String.valueOf(true))
                .run(null, callback);

    }

    /**
     * 获取智能设备和路由器之间的连接状态
     */
    public void getConnState(BaseCallback callback) {
        OkHttpUtil.get(UrlConfig.SoftApInfoUrl.GET_CONN_STATE_URL)
                .addParams("", "")
                .run(null, callback);
    }

    /**
     * 关闭智能设备的SoftAp
     */
    public void closeSoftAp(BaseCallback callback) {
        OkHttpUtil.get(UrlConfig.SoftApInfoUrl.CLOSE_DEVICE_AP_URL)
                .addParams("", "")
                .run(null, callback);
    }



}
