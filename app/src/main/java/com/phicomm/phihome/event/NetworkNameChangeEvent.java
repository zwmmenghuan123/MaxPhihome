package com.phicomm.phihome.event;

/**
 * 连接的网络发生变化事件
 * 只有已连接上时才会发送
 * Created by xiaolei.yang on 2017/7/7.
 */

public class NetworkNameChangeEvent {
    private String ssid;

    public NetworkNameChangeEvent(String ssid) {
        this.ssid = ssid;
    }
}
