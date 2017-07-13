package com.phicomm.phihome.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 智能设备结果信息
 * Created by xiaolei.yang on 2017/7/13.
 */

public class DeviceBeanResult implements Serializable {
    private static final long serialVersionUID = -4119449488547142469L;

    private int ret_status;
    private String ret_message;

    private int has_more_page;
    private List<DeviceBean> devices;

    public int getRet_status() {
        return ret_status;
    }

    public void setRet_status(int ret_status) {
        this.ret_status = ret_status;
    }

    public String getRet_message() {
        return ret_message;
    }

    public void setRet_message(String ret_message) {
        this.ret_message = ret_message;
    }

    public int getHas_more_page() {
        return has_more_page;
    }

    public void setHas_more_page(int has_more_page) {
        this.has_more_page = has_more_page;
    }

    public List<DeviceBean> getDevices() {
        return devices;
    }

    public void setDevices(List<DeviceBean> devices) {
        this.devices = devices;
    }

    @Override
    public String toString() {
        return "DeviceBeanResult{" +
                "ret_status=" + ret_status +
                ", ret_message='" + ret_message + '\'' +
                ", has_more_page=" + has_more_page +
                ", devices=" + devices +
                '}';
    }
}
