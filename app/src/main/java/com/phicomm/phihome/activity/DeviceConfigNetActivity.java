package com.phicomm.phihome.activity;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.phicomm.phihome.R;
import com.phicomm.phihome.utils.NetworkUtils;
import com.phicomm.phihome.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;

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




    @Override
    public void initLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_device_config_net);
    }

    @Override
    public void afterInitView() {
        setPageTitle(R.string.device_connect_to_router);



    }


}
