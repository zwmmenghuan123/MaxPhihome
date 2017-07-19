package com.phicomm.phihome.presenter;

import com.phicomm.phihome.bean.SocketDevice;
import com.phicomm.phihome.model.SocketModel;
import com.phicomm.phihome.net.callback.BaseCallback;
import com.phicomm.phihome.net.callback.BeanCallback;
import com.phicomm.phihome.presenter.viewback.SocketView;

import okhttp3.Request;

/**
 * Created by qisheng.lv on 2017/7/14.
 */
public class SocketPresenter {

    private SocketModel mModel;
    private SocketView mView;

    public SocketPresenter(SocketView socketView) {
        mModel = new SocketModel();
        this.mView = socketView;
    }

    public void getStatus(String deviceId) {
        mModel.getStatus(deviceId, new BeanCallback<SocketDevice>() {

            @Override
            public void onError(String code, String msg) {
                if (mView != null) {
                    mView.onGetStatusError(code, msg);
                }
            }

            @Override
            public void onSuccess(SocketDevice socketDevice) {
                if (mView != null) {
                    mView.onGetStatusSuccess(socketDevice);
                }
            }
        });
    }

    public void setStatus(String deviceId, int sw0, int sw1, int sw2, int sw3, int sw4, int sw5) {
        mModel.setStatus(deviceId, sw0, sw1, sw2, sw3, sw4, sw5, new BaseCallback() {
            @Override
            public void onError(String code, String msg) {
                if (mView != null) {
                    mView.onSetStatusError(code, msg);
                }
            }

            @Override
            public void onSuccess(String result, Request request) {
                if (mView != null) {
                    mView.onSetStatusSuccess();
                }
            }
        });
    }

}
