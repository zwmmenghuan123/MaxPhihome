package com.phicomm.phihome.presenter;

import com.phicomm.phihome.R;
import com.phicomm.phihome.bean.ReadDeviceInfoBean;
import com.phicomm.phihome.model.SoftApDeviceModel;
import com.phicomm.phihome.net.callback.BeanCallback;
import com.phicomm.phihome.presenter.viewback.SoftApDeviceView;
import com.phicomm.phihome.utils.CommonUtils;

/**
 * 与智能设备交互
 * Created by xiaolei.yang on 2017/7/6.
 */

public class SoftApDevicePresenter {
    private SoftApDeviceView mSoftApDeviceView;
    private SoftApDeviceModel mSoftApDeviceModel;

    public SoftApDevicePresenter(SoftApDeviceView mSoftApDeviceView) {
        this.mSoftApDeviceView = mSoftApDeviceView;
        mSoftApDeviceModel = new SoftApDeviceModel();
    }

    /**
     * 读取设备周围的SSID列表
     */
    public void readDeviceInfo() {
        if (mSoftApDeviceView!=null){
            mSoftApDeviceView.connecting();
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mSoftApDeviceModel.readDeviceInfo(new BeanCallback<ReadDeviceInfoBean>() {

            @Override
            public void onError(int code, String msg) {
                if (mSoftApDeviceView!=null){
                    mSoftApDeviceView.readDeviceSSIDFail(code, msg);
                    mSoftApDeviceView.connectOver();
                }

            }

            @Override
            public void onSuccess(ReadDeviceInfoBean readDeviceInfoBean) {
                if (mSoftApDeviceView!=null) {
                    mSoftApDeviceView.connectOver();
                    if (readDeviceInfoBean != null) {
                        mSoftApDeviceView.readDeviceSSIDSSuccess(readDeviceInfoBean.getWifi_scan());
                    } else {
                        mSoftApDeviceView.readDeviceSSIDFail(0, CommonUtils.getString(R.string.get_device_wifi_fail));
                    }
                }
            }
        });
    }
}
