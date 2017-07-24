package com.phicomm.phihome.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.phicomm.phihome.PhApplication;
import com.phicomm.phihome.R;
import com.phicomm.phihome.constants.AppConstans;
import com.phicomm.phihome.constants.CurrentDevice;
import com.phicomm.phihome.event.NetworkNameChangeEvent;
import com.phicomm.phihome.utils.NetworkUtils;
import com.phicomm.phihome.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Locale;

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

    private String currentDeviceSsid = "";
//    private static final int STATE_GETTING_WIFI = 0;
//    private static final int STATE_GET_WIFI_SUCCESS = 1;
//    private static final int STATE_GET_WIFI_FAIL = 2;

    private boolean isWaitingJump;

    @Override
    public void initLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_soft_ap_reset);
    }

    @Override
    public void afterInitView() {
        if (AppConstans.Products.TYPE_INSERTS == getIntent().getIntExtra("type", 0)) {
            setPageTitle(R.string.add_x1_smart_inserts);
        } else {
            setPageTitle(R.string.add_smart_devices);
        }

        currentDeviceSsid = getIntent().getStringExtra("ssid");
        if (currentDeviceSsid == null) {
            currentDeviceSsid = "";
        }

        wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);

    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @OnClick(R.id.tv_next_step)
    public void tv_next_step() {
//        PackageManager pm = getPackageManager();
//        boolean permissionCHANGE_WIFI_STATE = PackageManager.PERMISSION_GRANTED == pm.checkPermission("android.permission.CHANGE_WIFI_STATE", "com.phicomm.phihome");
//        boolean permissionACCESS_WIFI_STATE = PackageManager.PERMISSION_GRANTED == pm.checkPermission("android.permission.ACCESS_WIFI_STATE", "com.phicomm.phihome");
//        boolean permissionACCESS_NETWORK_STATE = PackageManager.PERMISSION_GRANTED == pm.checkPermission("android.permission.ACCESS_NETWORK_STATE", "com.phicomm.phihome");
//        boolean permissionCHANGE_WIFI_MULTICAST_STATE = PackageManager.PERMISSION_GRANTED == pm.checkPermission("android.permission.CHANGE_WIFI_MULTICAST_STATE", "com.phicomm.phihome");
//        Log.e("======", "permissionCHANGE_WIFI_STATE: " + permissionCHANGE_WIFI_STATE);
//        Log.e("======", "permissionACCESS_WIFI_STATE: " + permissionACCESS_WIFI_STATE);
//        Log.e("======", "permissionACCESS_NETWORK_STATE: " + permissionACCESS_NETWORK_STATE);
//        Log.e("======", "permissionCHANGE_WIFI_MULTICAST_STATE: " + permissionCHANGE_WIFI_MULTICAST_STATE);

        mProgressBar.setVisibility(View.VISIBLE);
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        if (configAndConnectWifi()) {
//            waitJumpNext();   //轮询方式，不再使用。改用等待网络连接的方式。
            if (isCurrentSsid()) {
                mProgressBar.setVisibility(View.GONE);
                Intent intent = new Intent(this, DeviceConfigNetActivity.class);
                startActivity(intent);
            } else {
                isWaitingJump = true;
                //等待网络监听事件回调执行
            }
        } else {
            mProgressBar.setVisibility(View.GONE);
            ToastUtil.show(R.string.get_wifi_fail);
        }
    }


    private boolean configAndConnectWifi() {
        WifiConfiguration wifiConfiguration = createWifiInfo(currentDeviceSsid, "", 1);
        int wcgId = wifiManager.addNetwork(wifiConfiguration);
        return wifiManager.enableNetwork(wcgId, true);
    }

    private void waitJumpNext() {
        if (isCurrentSsid()) {
            mProgressBar.setVisibility(View.GONE);
            Intent intent = new Intent(this, DeviceConfigNetActivity.class);
            startActivity(intent);
        } else {
            if (mProgressBar != null) {
                mProgressBar.postDelayed(runnable, 50);
            }
        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (isCurrentSsid()) {
                mProgressBar.setVisibility(View.GONE);
                Intent intent = new Intent(SoftApResetActivity.this, DeviceConfigNetActivity.class);
                startActivity(intent);
            } else {
                waitJumpNext();
            }
        }
    };

    /**
     * 判断当前是否已连接wifi，连接的wifi是否是目标wifi
     */
    private boolean isCurrentSsid() {
        if (NetworkUtils.isWifiConnect()) {
            ConnectivityManager connectMgr = (ConnectivityManager) PhApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = connectMgr.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                WifiInfo wifiInfo = NetworkUtils.getWifiInfo();
                if (wifiInfo != null) {

                    CurrentDevice.MAC = wifiInfo.getBSSID().toUpperCase(Locale.CHINA);
                    Log.e("=====MACADDRESS", "isCurrentSsid: " + CurrentDevice.MAC);
                    return currentDeviceSsid.equals(wifiInfo.getSSID().replace("\"", ""));
                }
            }
        }
        return false;
    }


    /**
     * 配置wifi
     *
     * @param ssid     要配置的wifi名称
     * @param password wifi密码
     * @param type     密码类型 1-无密码，2- wep加密 3-wpa家吗
     */
    private WifiConfiguration createWifiInfo(String ssid, String password, int type) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + ssid + "\"";

        if (type == 1) { // WIFICIPHER_NOPASS
//            config.wepKeys[0] = "";
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
//            config.wepTxKeyIndex = 0;
        }
        if (type == 2) { // WIFICIPHER_WEP
            config.hiddenSSID = true;
            config.wepKeys[0] = "\"" + password + "\"";
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        if (type == 3) { // WIFICIPHER_WPA
            config.preSharedKey = "\"" + password + "\"";
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


    @Subscribe
    public void onEventMainThread(NetworkNameChangeEvent event) {
        if (isWaitingJump && isCurrentSsid()) {
            mProgressBar.setVisibility(View.GONE);
            isWaitingJump = false;
            Intent intent = new Intent(this, DeviceConfigNetActivity.class);
            startActivity(intent);
        }
    }

//    private void checkAndConnectWifi() {
//        setState(STATE_GETTING_WIFI, "");
//        if (NetworkUtils.isWifiConnect()) { //是wifi连接时
//            checkWifiName();
//        } else { //非wifi连接
//            if (wifiManager.isWifiEnabled()) { //wifi可用
//                scanAndHandlerResults();
//            } else { //wifi不可用
//                if (wifiOpen()) { //开启wifi成功
//                    scanAndHandlerResults();
//                } else { //开启wifi失败
//                    ToastUtil.show(this, R.string.auto_open_wifi_fail);
//                    setState(STATE_GET_WIFI_FAIL, null);
//                }
//            }
//        }
//    }
//
//    //开启WIFI
//    public boolean wifiOpen() {
//        return !wifiManager.isWifiEnabled() && wifiManager.setWifiEnabled(true);
//    }
//
//    //扫描wifi
//    public void wifiStartScan() {
//        wifiManager.startScan();
//    }
//
//    //得到Scan结果
//    public List<ScanResult> getScanResults() {
//        return wifiManager.getScanResults();//得到扫描结果
//    }
//
//    /**
//     * 检查当前网络是否为设备网络
//     */
//    private void checkWifiName() {
//        WifiInfo wifiInfo = NetworkUtils.getWifiInfo();
//        if (wifiInfo != null && currentDeviceSsid.equals(wifiInfo.getSSID())) {
//            setState(STATE_GET_WIFI_SUCCESS, currentDeviceSsid);
//        } else {
//            scanAndHandlerResults();
//        }
//    }
//
//    /**
//     * 扫描wifi并处理扫描结果。
//     */
//    private void scanAndHandlerResults() {
//        wifiStartScan();
//        List<ScanResult> scanResults = getScanResults();
//        if (scanResults == null) {
//            setState(STATE_GET_WIFI_FAIL, null);
//        } else {
//            if (scanResults.size() == 0) {
//                setState(STATE_GET_WIFI_FAIL, null);
//            } else {
//                int tag = -1;
//                for (int i = 0; i < scanResults.size(); i++) {
//                    if (currentDeviceSsid.equals(scanResults.get(i).SSID)) {
//                        tag = i;
//                        break;
//                    }
//                }
//                if (tag == -1) {
//                    setState(STATE_GET_WIFI_FAIL, null);
//                } else {
//                    connectWifi(currentDeviceSsid);
//                }
//            }
//        }
//    }
//
//    /**
//     * 连接网络
//     */
//    private void connectWifi(String ssid) {
//        List<WifiConfiguration> wifiConfigList = getConfiguration();
//        if (wifiConfigList != null && wifiConfigList.size() != 0) {
//            int networkId = isConfiguration(wifiConfigList, ssid);
//            if (networkId != -1) {
//                if (realConnectWifi(wifiConfigList, networkId)) {
//                    setState(STATE_GET_WIFI_SUCCESS, currentDeviceSsid);
//                } else {
//                    setState(STATE_GET_WIFI_FAIL, null);
//                }
//            } else {
//                configAndConnectWifi();
//            }
//        } else {
//            setState(STATE_GET_WIFI_FAIL, null);
//        }
//
//    }
//
//    //得到Wifi配置好的信息
//    public List<WifiConfiguration> getConfiguration() {
//        return wifiManager.getConfiguredNetworks();//得到配置好的网络信息
//    }
//
//    //判定指定WIFI是否已经配置好,依据WIFI的地址BSSID,返回NetId
//    public int isConfiguration(List<WifiConfiguration> wifiConfigList, String ssid) {
//        for (int i = 0; i < wifiConfigList.size(); i++) {
//            if (wifiConfigList.get(i).SSID.equals("\"" + ssid + "\"")) { //地址相同
//                return wifiConfigList.get(i).networkId;
//            }
//        }
//        return -1;
//    }
//
//    //连接指定Id的WIFI
//    public boolean realConnectWifi(List<WifiConfiguration> wifiConfigList, int wifiId) {
//        for (int i = 0; i < wifiConfigList.size(); i++) {
//            WifiConfiguration wifi = wifiConfigList.get(i);
//            if (wifi.networkId == wifiId) {
//                while (!(wifiManager.enableNetwork(wifiId, true))) { //激活该Id，建立连接
//                    //status:0--已经连接，1--不可连接，2--可以连接
//                    Log.e("ConnectWifi", String.valueOf(wifiConfigList.get(wifiId).status));
//                }
//                return true;
//            }
//        }
//        return false;
//    }
//
//    /**
//     * 设置文本框显示的状态
//     *
//     * @param state 获取wifi连接情况的状态
//     * @param ssid  wifi名称
//     */
//    private void setState(int state, String ssid) {
//        switch (state) {
//            case STATE_GETTING_WIFI:
//                break;
//            case STATE_GET_WIFI_SUCCESS:
//                break;
//            case STATE_GET_WIFI_FAIL:
//                break;
//            default:
//                break;
//        }
//
//    }


}
