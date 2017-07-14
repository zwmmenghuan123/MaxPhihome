package com.phicomm.phihome.presenter;

import android.util.Log;

import com.phicomm.phihome.bean.DeviceBean;
import com.phicomm.phihome.bean.DeviceBeanResult;
import com.phicomm.phihome.model.DevicesModel;
import com.phicomm.phihome.net.callback.BeanCallback;
import com.phicomm.phihome.presenter.viewback.DevicesView;

import java.util.List;

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

    public void getDevices(final boolean isFresh) {
        mDevicesModel.getDevices(new BeanCallback<DeviceBeanResult>() {
            @Override
            public void onSuccess(DeviceBeanResult response) {
                if (mDevicesView != null) {
                    if (response != null) {
                        List<DeviceBean> devices = response.getDevices();
                        mDevicesView.getDevicesSuccess(devices);
                    } else {
                        mDevicesView.getDevicesError("0", "解析失败");
                    }
                }
            }

            @Override
            public void onError(String code, String msg) {
                Log.e("=====getDevices", "onError: " + code + "===" + msg);
                if (mDevicesView != null) {
                    mDevicesView.getDevicesError(code, msg);
                }

            }
        });
    }
}
