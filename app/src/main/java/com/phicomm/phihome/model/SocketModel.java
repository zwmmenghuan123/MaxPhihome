package com.phicomm.phihome.model;

import com.phicomm.phihome.constants.UrlConfig;
import com.phicomm.phihome.net.callback.BaseCallback;
import com.phicomm.phihome.net.engine.OkHttpUtil;

/**
 * 插座控制Model
 * Created by qisheng.lv on 2017/7/14.
 */
public class SocketModel {

    public void getStatus(String device_id, BaseCallback callback) {
        OkHttpUtil.get(UrlConfig.SzUrl.DEVICE_CONTROL + device_id)
                .run(null, callback);
    }

    public void setStatus(String device_id, int sw0, int sw1, int sw2, int sw3, int sw4, int sw5, BaseCallback callback) {
        OkHttpUtil.postJson(UrlConfig.SzUrl.DEVICE_CONTROL + device_id)
                .addParams("sw0", String.valueOf(sw0))
                .addParams("sw1", String.valueOf(sw1))
                .addParams("sw2", String.valueOf(sw2))
                .addParams("sw3", String.valueOf(sw3))
                .addParams("sw4", String.valueOf(sw4))
                .addParams("sw5", String.valueOf(sw5))
                .run(null, callback);
    }

}
