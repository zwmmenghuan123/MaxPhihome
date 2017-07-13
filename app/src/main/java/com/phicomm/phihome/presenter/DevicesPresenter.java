package com.phicomm.phihome.presenter;

import android.util.Log;

import com.phicomm.phihome.model.DevicesModel;
import com.phicomm.phihome.net.callback.BeanCallback;
import com.phicomm.phihome.presenter.viewback.DevicesView;

/**
 * 获取账户已绑定的设备列表
 * Created by xiaolei.yang on 2017/7/12.
 */

public class DevicesPresenter {
    private DevicesView mDevicesView;
    private DevicesModel mDevicesModel;

    public DevicesPresenter(DevicesView mDevicesView) {
        this.mDevicesView = mDevicesView;
        mDevicesModel = new DevicesModel();
    }

    public void getDevices() {
        mDevicesModel.getDevices(new BeanCallback<String>() {
            @Override
            public void onSuccess(String str) {
                Log.e("=====getDevices", "onSuccess: " + str);
            }

            @Override
            public void onError(String code, String msg) {
                Log.e("=====getDevices", "onError: " + code + "===" + msg);
            }
        });
    }
}