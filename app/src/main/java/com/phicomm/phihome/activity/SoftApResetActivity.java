package com.phicomm.phihome.activity;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.phicomm.phihome.R;
import com.phicomm.phihome.constants.Products;
import com.phicomm.phihome.utils.NetworkUtils;
import com.phicomm.phihome.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设备重置
 * Created by xiaolei.yang on 2017/7/6.
 */
public class SoftApResetActivity extends BaseActivity {
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    WifiManager wifiManager;

    private String currentDeviceSsid = "PhiHomeTest";
    private final int STATE_GETTING_WIFI = 0;
    private final int STATE_GET_WIFI_SUCCESS = 1;
    private final int STATE_GET_WIFI_FAIL = 2;

    @Override
    public void initLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_soft_ap_reset);
    }

    @Override
    public void afterInitView() {
        if (Products.TYPE_INSERTS == getIntent().getIntExtra("type", 0)) {
            setPageTitle(R.string.add_x1_smart_inserts);
        } else {
            setPageTitle(R.string.add_smart_devices);
        }

        wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
    }

    @OnClick(R.id.tv_next_step)
    public void tv_next_step() {
        mProgressBar.setVisibility(View.VISIBLE);
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        if (configAndConnectWifi()) {
            mProgressBar.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mProgressBar.setVisibility(View.GONE);
//            if (NetworkUtils.getWifiInfo() != null && ("\""+currentDeviceSsid+"\"").equals(NetworkUtils.getWifiInfo().getSSID())) {
                    Intent intent = new Intent(SoftApResetActivity.this, DeviceConfigNetActivity.class);
                    startActivity(intent);
//            } else {
//                ToastUtil.show(SoftApResetActivity.this, R.string.get_device_wifi_fail);
//            }
                }
            }, 3000);

        } else {
            mProgressBar.setVisibility(View.GONE);
            ToastUtil.show(SoftApResetActivity.this, R.string.get_wifi_fail);
        }
    }

    private boolean configAndConnectWifi() {
        WifiConfiguration wifiConfiguration = createWifiInfo(currentDeviceSsid, "", 1);
        int wcgId = wifiManager.addNetwork(wifiConfiguration);
        return wifiManager.enableNetwork(wcgId, true);
    }

    /**
     * 配置wifi
     *
     * @param SSID     要配置的wifi名称
     * @param Password wifi密码
     * @param type     密码类型 1-无密码，2- wep加密 3-wpa家吗
     */
    private WifiConfiguration createWifiInfo(String SSID, String Password, int type) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";

        if (type == 1) { // WIFICIPHER_NOPASS
//            config.wepKeys[0] = "";
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
//            config.wepTxKeyIndex = 0;
        }
        if (type == 2) { // WIFICIPHER_WEP
            config.hiddenSSID = true;
            config.wepKeys[0] = "\"" + Password + "\"";
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        if (type == 3) { // WIFICIPHER_WPA
            config.preSharedKey = "\"" + Password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            // config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        }
        return config;
    }


    private void checkAndConnectWifi() {
        setState(STATE_GETTING_WIFI, "");
        if (NetworkUtils.isWifiConnect()) {//是wifi连接时
            checkWifiName();
        } else {//非wifi连接
            if (wifiManager.isWifiEnabled()) {//wifi可用
                scanAndHandlerResults();
            } else {//wifi不可用
                if (wifiOpen()) {//开启wifi成功
                    scanAndHandlerResults();
                } else {//开启wifi失败
                    ToastUtil.show(this, R.string.auto_open_wifi_fail);
                    setState(STATE_GET_WIFI_FAIL, null);
                }
            }
        }
    }

    //开启WIFI
    public boolean wifiOpen() {
        return !wifiManager.isWifiEnabled() && wifiManager.setWifiEnabled(true);
    }

    //扫描wifi
    public void wifiStartScan() {
        wifiManager.startScan();
    }

    //得到Scan结果
    public List<ScanResult> getScanResults() {
        return wifiManager.getScanResults();//得到扫描结果
    }

    /**
     * 检查当前网络是否为设备网络
     */
    private void checkWifiName() {
        WifiInfo wifiInfo = NetworkUtils.getWifiInfo();
        if (wifiInfo != null && currentDeviceSsid.equals(wifiInfo.getSSID())) {
            setState(STATE_GET_WIFI_SUCCESS, currentDeviceSsid);
        } else {
            scanAndHandlerResults();
        }
    }

    /**
     * 扫描wifi并处理扫描结果。
     */
    private void scanAndHandlerResults() {
        wifiStartScan();
        List<ScanResult> scanResults = getScanResults();
        if (scanResults == null) {
            setState(STATE_GET_WIFI_FAIL, null);
        } else {
            if (scanResults.size() == 0) {
                setState(STATE_GET_WIFI_FAIL, null);
            } else {
                int tag = -1;
                for (int i = 0; i < scanResults.size(); i++) {
                    if (currentDeviceSsid.equals(scanResults.get(i).SSID)) {
                        tag = i;
                        break;
                    }
                }
                if (tag == -1) {
                    setState(STATE_GET_WIFI_FAIL, null);
                } else {
                    connectWifi(currentDeviceSsid);
                }
            }
        }
    }

    /**
     * 连接网络
     */
    private void connectWifi(String ssid) {
        List<WifiConfiguration> wifiConfigList = getConfiguration();
        if (wifiConfigList != null && wifiConfigList.size() != 0) {
            int networkId = isConfiguration(wifiConfigList, ssid);
            if (networkId != -1) {
                if (realConnectWifi(wifiConfigList, networkId)) {
                    setState(STATE_GET_WIFI_SUCCESS, currentDeviceSsid);
                } else {
                    setState(STATE_GET_WIFI_FAIL, null);
                }
            } else {
                configAndConnectWifi();
            }
        } else {
            setState(STATE_GET_WIFI_FAIL, null);
        }

    }

    //得到Wifi配置好的信息
    public List<WifiConfiguration> getConfiguration() {
        return wifiManager.getConfiguredNetworks();//得到配置好的网络信息
    }

    //判定指定WIFI是否已经配置好,依据WIFI的地址BSSID,返回NetId
    public int isConfiguration(List<WifiConfiguration> wifiConfigList, String SSID) {
        for (int i = 0; i < wifiConfigList.size(); i++) {
            if (wifiConfigList.get(i).SSID.equals("\"" + SSID + "\"")) {//地址相同
                return wifiConfigList.get(i).networkId;
            }
        }
        return -1;
    }

    //连接指定Id的WIFI
    public boolean realConnectWifi(List<WifiConfiguration> wifiConfigList, int wifiId) {
        for (int i = 0; i < wifiConfigList.size(); i++) {
            WifiConfiguration wifi = wifiConfigList.get(i);
            if (wifi.networkId == wifiId) {
                while (!(wifiManager.enableNetwork(wifiId, true))) {//激活该Id，建立连接
                    //status:0--已经连接，1--不可连接，2--可以连接
                    Log.e("ConnectWifi", String.valueOf(wifiConfigList.get(wifiId).status));
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 设置文本框显示的状态
     *
     * @param state 获取wifi连接情况的状态
     * @param ssid  wifi名称
     */
    private void setState(int state, String ssid) {
        switch (state) {
            case STATE_GETTING_WIFI:
                break;
            case STATE_GET_WIFI_SUCCESS:
                break;
            case STATE_GET_WIFI_FAIL:
                break;
        }

    }


}
