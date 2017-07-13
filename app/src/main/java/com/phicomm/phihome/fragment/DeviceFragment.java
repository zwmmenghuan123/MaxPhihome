package com.phicomm.phihome.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import com.phicomm.phihome.R;
import com.phicomm.phihome.activity.ProductsActivity;
import com.phicomm.phihome.presenter.DevicesPresenter;
import com.phicomm.phihome.presenter.viewback.DevicesView;

import butterknife.OnClick;

/**
 * 设备页
 * Created by qisheng.lv on 2017/7/5.
 */
public class DeviceFragment extends BaseFragment {
    DevicesPresenter mDevicesPresenter;

    @Override
    public View initView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_device, null);
    }

    @Override
    public void afterInitView() {
        mDevicesPresenter = new DevicesPresenter(new DevicesView() {
            @Override
            public void getDevicesSuccess() {
                super.getDevicesSuccess();
            }

            @Override
            public void getDevicesError(String errorCode, String errorInfo) {
                super.getDevicesError(errorCode, errorInfo);
            }
        });

        mDevicesPresenter.getDevices();
    }

    @OnClick(R.id.iv_add)
    public void iv_add() {
        Intent intent = new Intent(getActivity(), ProductsActivity.class);
        startActivity(intent);
    }

}
