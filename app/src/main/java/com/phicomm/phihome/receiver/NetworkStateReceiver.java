package com.phicomm.phihome.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;

import com.phicomm.phihome.PhApplication;
import com.phicomm.phihome.event.NetworkNameChangeEvent;
import com.phicomm.phihome.utils.NetworkUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * 监听网络状态变化
 * Created by xiaolei.yang on 2017/7/7.
 */

public class NetworkStateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            return;
        }
        if (!NetworkUtils.isWifiConnect()) {
            return;
        }
        ConnectivityManager connectMgr = (ConnectivityManager) PhApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectMgr.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            WifiInfo wifiInfo = NetworkUtils.getWifiInfo();
            if (wifiInfo != null) {
                EventBus.getDefault().post(new NetworkNameChangeEvent());
            }
        }
    }
}
