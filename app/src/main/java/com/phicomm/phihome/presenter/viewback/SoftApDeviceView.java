package com.phicomm.phihome.presenter.viewback;

import com.phicomm.phihome.bean.GetConnStateBean;
import com.phicomm.phihome.bean.WriteSsidInfoBean;

import java.util.Map;

/**
 * 手机读取设备信息
 * Created by xiaolei.yang on 2017/7/6.
 */

public class SoftApDeviceView {
    public void readDeviceSSIDSSuccess(Map<String,String> wifi_scan){

    }
    public void readDeviceSSIDFail(int code, String msg){

    }

    public void connecting(){

    }
    public void connectOver(){

    }

    public void writeSSIDSSuccess(WriteSsidInfoBean writeSsidInfoBean){

    }
    public void writeSSIDFail(int code, String msg){

    }

    public void getConnStateSuccess(GetConnStateBean getConnStateBean){

    }
    public void getConnStateFail(int code, String msg){

    }

    public void closeSoftApSuccess(WriteSsidInfoBean writeSsidInfoBean){

    }
    public void closeSoftApFail(int code, String msg){

    }



}
