package com.phicomm.phihome.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Switch;
import android.widget.TextView;

import com.phicomm.phihome.R;
import com.phicomm.phihome.bean.SocketDevice;
import com.phicomm.phihome.presenter.SocketPresenter;
import com.phicomm.phihome.presenter.viewback.SocketView;
import com.phicomm.phihome.utils.ToastUtil;

import java.util.List;

import butterknife.BindViews;

public class SocketControlActivity extends BaseActivity {
    @BindViews({R.id.switch_socket1, R.id.switch_socket2, R.id.switch_socket3, R.id.switch_socket4, R.id.switch_socket5, R.id.switch_socket6})
    List<Switch> mListSwitchs;

    private ProgressDialog mPd;
    private SocketPresenter mPresenter;
    private String mDeviceId;

    @Override
    public void initLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_socket_control);
        mPd = new ProgressDialog(this);
    }

    @Override
    public void afterInitView() {
        setPageTitle(R.string.page_socket_control);
        showTvMenu(getString(R.string.save));
        getDeviceId();
        initPresenter();
        getStatus();
    }

    private void getDeviceId() {
        mDeviceId = getIntent().getStringExtra("device_id");
        if (TextUtils.isEmpty(mDeviceId)) {
            ToastUtil.show(this, "未获取到DeviceId");
            finish();
        }
    }

    private void initPresenter() {
        mPresenter = new SocketPresenter(new SocketView() {
            @Override
            public void onGetStatusError(String code, String msg) {
                mPd.dismiss();
                ToastUtil.show(SocketControlActivity.this, msg);
            }

            @Override
            public void onGetStatusSuccess(SocketDevice socketDevice) {
                mPd.dismiss();
                initStatus(socketDevice);
            }

            @Override
            public void onSetStatusError(String code, String msg) {
                mPd.dismiss();
                ToastUtil.show(SocketControlActivity.this, msg);
            }

            @Override
            public void onSetStatusSuccess() {
                mPd.dismiss();
                ToastUtil.show(SocketControlActivity.this, "设置成功");
                finish();
            }
        });
    }


    @Override
    public void onTvMenuClick(TextView view) {
        setStatus();
    }


    private void getStatus() {
        mPd.show();
        mPresenter.getStatus(mDeviceId);
    }

    private void setStatus() {
        int sw0 = mListSwitchs.get(0).isChecked() ? 1 : 0;
        int sw1 = mListSwitchs.get(1).isChecked() ? 1 : 0;
        int sw2 = mListSwitchs.get(2).isChecked() ? 1 : 0;
        int sw3 = mListSwitchs.get(3).isChecked() ? 1 : 0;
        int sw4 = mListSwitchs.get(4).isChecked() ? 1 : 0;
        int sw5 = mListSwitchs.get(5).isChecked() ? 1 : 0;

        mPd.show();
        mPresenter.setStatus(mDeviceId, sw0, sw1, sw2, sw3, sw4, sw5);
    }

    private void initStatus(SocketDevice socketDevice) {
        if (socketDevice == null || socketDevice.device == null) {
            ToastUtil.show(this, "获取状态失败");
            return;
        }

        mListSwitchs.get(0).setChecked(socketDevice.device.getSw0() == 1);
        mListSwitchs.get(1).setChecked(socketDevice.device.getSw1() == 1);
        mListSwitchs.get(2).setChecked(socketDevice.device.getSw2() == 1);
        mListSwitchs.get(3).setChecked(socketDevice.device.getSw3() == 1);
        mListSwitchs.get(4).setChecked(socketDevice.device.getSw4() == 1);
        mListSwitchs.get(5).setChecked(socketDevice.device.getSw5() == 1);
    }


}
