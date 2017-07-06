package com.phicomm.phihome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.phicomm.phihome.R;
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

    SoftApDevicePresenter mSoftApDevicePresenter;

    Map<String, String> mWifiScan;

    private final int SELECT_SSID = 1;

    @Override
    public void initLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_device_config_net);
    }

    @Override
    public void afterInitView() {
        setPageTitle(R.string.device_connect_to_router);

        mSoftApDevicePresenter = new SoftApDevicePresenter(new SoftApDeviceView() {
            @Override
            public void readDeviceSSIDSSuccess(Map<String, String> wifi_scan) {
                if (wifi_scan == null || wifi_scan.size() == 0) {
                    String msg = CommonUtils.getString(R.string.get_device_wifi_fail);
                    ToastUtil.show(DeviceConfigNetActivity.this, msg);
                    mTvGettingWifi.setText(msg);
                } else {
                    mWifiScan = wifi_scan;
                    mTvGettingWifi.setText("");
                    for (String value : wifi_scan.values()) {
                        if (!TextUtils.isEmpty(value)) {
                            mTvGettingWifi.setText(value);
                            break;
                        }
                    }

                }

            }

            @Override
            public void readDeviceSSIDFail(int code, String msg) {
                msg = TextUtils.isEmpty(msg) ? CommonUtils.getString(R.string.get_device_wifi_fail) : msg;
                ToastUtil.show(DeviceConfigNetActivity.this, msg);
                mTvGettingWifi.setText(msg);

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
        });

        mSoftApDevicePresenter.readDeviceInfo();
    }

    @OnClick(R.id.rl_wifi_name)
    public void rl_wifi_name() {
        if (mWifiScan == null || mWifiScan.size() == 0) {
            CommonUtils.getString(R.string.get_device_wifi_fail);
        } else {
            ArrayList<String> wifiList = new ArrayList<>();
            for (String value:mWifiScan.values()){
                wifiList.add(value);
            }
            Intent intent = new Intent(DeviceConfigNetActivity.this, WifiScanListActivity.class);
            intent.putStringArrayListExtra("wifi_scan_list", wifiList);
            startActivityForResult(intent, SELECT_SSID);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SELECT_SSID:
                if (resultCode == RESULT_OK && null != data && null != data.getStringExtra("ssid")) {
                    mTvGettingWifi.setText(data.getStringExtra("ssid"));
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }

    }

    //    private void getConnState() {
//        String url = "http://192.168.2.139:8000/conn-state";
//        RequestParams params = new RequestParams(url);
//
//        x.http().get(params, new org.xutils.common.Callback.CommonCallback<String>() {
//            @Override
//            public void onSuccess(String result) {
//                tvConnState.setText("config-state:success " + result);
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//                tvConnState.setText("config-state:error" + ex.toString());
//            }
//
//            @Override
//            public void onCancelled(CancelledException cex) {
//                tvConnState.setText("config-state: cancell");
//            }
//
//            @Override
//            public void onFinished() {
//            }
//        });
//    }
//    private void configWriteUap(){
//        String url = "http://192.168.2.139:8000/config-write-uap";
//        RequestParams params = new RequestParams(url);
//
//        params.addParameter("GATEWAY", "10.10.10.1");
//        params.addParameter("DNS1", "10.10.10.1");
//        params.addParameter("IDENTIFIER", 168430083);
//        params.addParameter("SSID", "Phicomm_SZ");
//        params.addParameter("NETMASK", "255.255.255.0");
//        params.addParameter("IP", "10.10.10.4");
//        params.addParameter("PASSWORD", "phicomm2017");
//        params.addParameter("DHCP", true);
//
//        x.http().post(params, new org.xutils.common.Callback.CommonCallback<String>() {
//            @Override
//            public void onSuccess(String result) {
//                tvConfigWriteUap.setText("config-write-uap:success " + result);
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//                tvConfigWriteUap.setText("config-write-uap:error" + ex.toString());
//            }
//
//            @Override
//            public void onCancelled(CancelledException cex) {
//                tvConfigWriteUap.setText("config-write-uap: cancell");
//            }
//
//            @Override
//            public void onFinished() {
//            }
//        });
//    }
}
