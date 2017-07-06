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
     * @param callback
     */
    public void readDeviceInfo(BaseCallback callback) {
        OkHttpUtil.get(UrlConfig.ReadDeviceInfoUrl.READ_DEVICE_INFO_URL)
                .addParams("","")
                .run(null, callback);
    }

}
