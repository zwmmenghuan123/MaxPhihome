package com.phicomm.phihome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.phicomm.phihome.R;
import com.phicomm.phihome.bean.GetConnStateBean;
import com.phicomm.phihome.bean.WriteSsidInfoBean;
import com.phicomm.phihome.presenter.SoftApDevicePresenter;
import com.phicomm.phihome.presenter.viewback.SoftApDeviceView;
import com.phicomm.phihome.utils.CommonUtils;
import com.phicomm.phihome.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设备配网
 * Created by xiaolei.yang on 2017/7/6.
 */
public class DeviceConfigNetActivity extends BaseActivity {
    @BindView(R.id.tv_getting_wifi)
    TextView mTvGettingWifi;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.tv_right_arrow)
    TextView mTvRightArrow;
    @BindView(R.id.et_password)
    EditText mEtPassword;

    SoftApDevicePresenter mSoftApDevicePresenter;

    Map<String, String> mWifiScan;

    private static final int SELECT_SSID = 1;
    private String mSsid;


    @Override
    public void initLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_device_config_net);
    }

    @Override
    public void afterInitView() {
        setPageTitle(R.string.device_connect_to_router);
        mSoftApDevicePresenter = new SoftApDevicePresenter(new SoftApDeviceView() {
            @Override
            public void readDeviceSsidSSuccess(Map<String, String> wifiScan) {
                if (wifiScan == null || wifiScan.size() == 0) {
                    readDeviceSsidError("0", null);
                } else {
                    mSsid = "";
                    mWifiScan = wifiScan;
                    mTvGettingWifi.setText("");
                    for (String value : wifiScan.values()) {
                        if (!TextUtils.isEmpty(value)) {
                            mTvGettingWifi.setText(value);
                            mSsid = value;
                            break;
                        }
                    }
                }
            }

            @Override
            public void readDeviceSsidError(String code, String msg) {
                msg = TextUtils.isEmpty(msg) ? CommonUtils.getString(R.string.get_device_wifi_fail) : msg;
                mTvGettingWifi.setText(msg);
                mSoftApDevicePresenter.readDeviceInfo();

            }

            @Override
            public void connecting() {
                mProgressBar.setVisibility(View.VISIBLE);
                mTvRightArrow.setVisibility(View.GONE);
            }

            @Override
            public void connectOver() {
                mProgressBar.setVisibility(View.GONE);
                mTvRightArrow.setVisibility(View.VISIBLE);
            }

            @Override
            public void writeSsidSSuccess(WriteSsidInfoBean writeSsidInfoBean) {
                if (writeSsidInfoBean != null) {
                    if (0 == writeSsidInfoBean.getErrorCode()) {
                        ToastUtil.show(DeviceConfigNetActivity.this, R.string.write_ssid_info_success);
                        getConnectionState();
                    } else {
                        String msg = writeSsidInfoBean.getMessage();
                        writeSsidError("0", msg);
                    }
                } else {
                    String msg = CommonUtils.getString(R.string.write_ssid_info_fail);
                    writeSsidError("0", msg);
                }
            }

            @Override
            public void writeSsidError(String code, String msg) {
                msg = TextUtils.isEmpty(msg) ? CommonUtils.getString(R.string.write_ssid_info_fail) : msg;
                ToastUtil.show(DeviceConfigNetActivity.this, msg);
            }

            @Override
            public void getConnStateSuccess(GetConnStateBean getConnStateBean) {
                if (getConnStateBean != null) {
                    if (1 == getConnStateBean.getConn_to_router() && 1 == getConnStateBean.getConn_to_server()) {
                        ToastUtil.show(DeviceConfigNetActivity.this, R.string.close_device_soft_ap);
                        closeSoftAp();
                    } else {
                        getConnStateError("0", null);
                    }
                } else {
                    getConnStateError("0", null);
                }
            }

            @Override
            public void getConnStateError(String code, String msg) {
                getConnectionState();
            }

            @Override
            public void closeSoftApSuccess(WriteSsidInfoBean writeSsidInfoBean) {
                ToastUtil.show(DeviceConfigNetActivity.this, R.string.close_device_soft_ap_success);
            }

            @Override
            public void closeSoftApError(String code, String msg) {

            }
        });

        mSoftApDevicePresenter.readDeviceInfo();
    }

    /**
     * 关闭设备的SoftAp
     */
    private void closeSoftAp() {
        mSoftApDevicePresenter.closeSoftAp();
    }

    /**
     * 获取设备与路由器和后台的连接状态
     */
    private void getConnectionState() {
        mSoftApDevicePresenter.getConnState();
    }


    @OnClick(R.id.rl_wifi_name)
    public void rl_wifi_name() {
        if (mWifiScan == null || mWifiScan.size() == 0) {
            CommonUtils.getString(R.string.get_device_wifi_fail);
        } else {
            ArrayList<String> wifiList = new ArrayList<>();
            for (String value : mWifiScan.values()) {
                wifiList.add(value);
            }
            Intent intent = new Intent(this, WifiScanListActivity.class);
            intent.putStringArrayListExtra("wifi_scan_list", wifiList);
            startActivityForResult(intent, SELECT_SSID);
        }
    }

    @OnClick(R.id.tv_connect)
    public void tv_connect() {
        if (mSsid == null) {
            ToastUtil.show(this, R.string.get_device_wifi_fail);
        } else {
            if (mEtPassword.getText() == null) {
                mEtPassword.setText("");
            }
            mSoftApDevicePresenter.writeSsidInfo(mSsid, mEtPassword.getText().toString());
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SELECT_SSID:
                if (resultCode == RESULT_OK && null != data && null != data.getStringExtra("ssid")) {
                    mTvGettingWifi.setText(data.getStringExtra("ssid"));
                    mSsid = data.getStringExtra("ssid");
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }

    }


}
