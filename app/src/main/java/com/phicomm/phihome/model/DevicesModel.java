package com.phicomm.phihome.model;

import com.phicomm.phihome.constants.UrlConfig;
import com.phicomm.phihome.manager.AccountManager;
import com.phicomm.phihome.net.callback.BaseCallback;
import com.phicomm.phihome.net.engine.OkHttpUtil;

/**
 * 获取设备已绑定的设备列表
 * Created by xiaolei.yang on 2017/7/12.
 */

public class DevicesModel {
    public void getDevices(BaseCallback callback) {
        OkHttpUtil.get(UrlConfig.SzUrl.DEVICES_URL)
                .addHeader("Authorization", AccountManager.getInstance().getToken())
                .run(null, callback);
    }
}
