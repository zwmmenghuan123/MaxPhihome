package com.phicomm.phihome.presenter;

import com.phicomm.phihome.R;
import com.phicomm.phihome.bean.GetConnStateBean;
import com.phicomm.phihome.bean.ReadDeviceInfoBean;
import com.phicomm.phihome.bean.WriteSsidInfoBean;
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
        if (mSoftApDeviceView != null) {
            mSoftApDeviceView.connecting();
        }
        mSoftApDeviceModel.readDeviceInfo(new BeanCallback<ReadDeviceInfoBean>() {
            @Override
            public void onSuccess(ReadDeviceInfoBean readDeviceInfoBean) {
                if (mSoftApDeviceView != null) {
                    mSoftApDeviceView.connectOver();
                    if (readDeviceInfoBean != null) {
                        mSoftApDeviceView.readDeviceSsidSSuccess(readDeviceInfoBean.getWifi_scan());
                    } else {
                        mSoftApDeviceView.readDeviceSsidError("0", CommonUtils.getString(R.string.get_device_wifi_fail));
                    }
                }
            }

            @Override
            public void onError(String code, String msg) {
                if (mSoftApDeviceView != null) {
                    mSoftApDeviceView.readDeviceSsidError(code, msg);
                    mSoftApDeviceView.connectOver();
                }

            }


        });
    }

    /**
     * 向设备写入要连接的ssid信息
     *
     * @param ssid     要连接的ssid
     * @param password 要连接的ssid的密码
     */
    public void writeSsidInfo(String ssid, String password) {
        mSoftApDeviceModel.writeSsidInfo(ssid, password, new BeanCallback<WriteSsidInfoBean>() {
            @Override
            public void onSuccess(WriteSsidInfoBean writeSsidInfoBean) {
                if (mSoftApDeviceView != null) {
                    mSoftApDeviceView.writeSsidSSuccess(writeSsidInfoBean);
                }
            }

            @Override
            public void onError(String code, String msg) {
                if (mSoftApDeviceView != null) {
                    mSoftApDeviceView.writeSsidError(code, msg);
                }
            }
        });
    }


    /**
     * 获取设备与ssid之间的连接状态
     */
    public void getConnState() {
        mSoftApDeviceModel.getConnState(new BeanCallback<GetConnStateBean>() {
            @Override
            public void onSuccess(GetConnStateBean getConnStateBean) {
                if (mSoftApDeviceView != null) {
                    mSoftApDeviceView.getConnStateSuccess(getConnStateBean);
                }
            }

            @Override
            public void onError(String code, String msg) {
                if (mSoftApDeviceView != null) {
                    mSoftApDeviceView.getConnStateError(code, msg);
                }
            }
        });
    }

    /**
     * 关闭设备的SoftAp
     */
    public void closeSoftAp() {
        mSoftApDeviceModel.closeSoftAp(new BeanCallback<WriteSsidInfoBean>() {
            @Override
            public void onSuccess(WriteSsidInfoBean writeSsidInfoBean) {
                if (mSoftApDeviceView != null) {
                    mSoftApDeviceView.closeSoftApSuccess(writeSsidInfoBean);
                }

            }

            @Override
            public void onError(String code, String msg) {
                if (mSoftApDeviceView != null) {
                    mSoftApDeviceView.closeSoftApError(code, msg);
                }

            }
        });
    }


    /**
     * 获取设备与ssid之间的连接状态
     */
    public void bindDevice(String macAddress) {
        mSoftApDeviceModel.bindDevice(macAddress, new BeanCallback() {
            @Override
            public void onSuccess(Object o) {
                if (mSoftApDeviceView != null) {
                    mSoftApDeviceView.bindDeviceSuccess();
                }
            }

            @Override
            public void onError(String code, String msg) {
                if (mSoftApDeviceView != null) {
                    mSoftApDeviceView.bindDeviceError(code, msg);
                }
            }
        });
    }

}
