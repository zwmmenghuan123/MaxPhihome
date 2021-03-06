package com.phicomm.phihome.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 读取设备周围的ssid列表。其他数据不重要，只使用了wifi_scan字段。
 * Created by xiaolei.yang on 2017/7/6.
 */

public class ReadDeviceInfoBean extends BaseResponse implements Serializable{
    private static final long serialVersionUID = 8481346501884918770L;

    private String T;
    private String N;
    private String PO;
    private String HD;
    private String FW;
    private String RF;
    private String MAC;
    private Map<String,String> wifi_scan; //设备周围的ssid的列表
//    private List<CCCC> C;


    public String getT() {
        return T;
    }

    public void setT(String t) {
        T = t;
    }

    public String getN() {
        return N;
    }

    public void setN(String n) {
        N = n;
    }

    public String getPO() {
        return PO;
    }

    public void setPO(String PO) {
        this.PO = PO;
    }

    public String getHD() {
        return HD;
    }

    public void setHD(String HD) {
        this.HD = HD;
    }

    public String getFW() {
        return FW;
    }

    public void setFW(String FW) {
        this.FW = FW;
    }

    public String getRF() {
        return RF;
    }

    public void setRF(String RF) {
        this.RF = RF;
    }

    public String getMAC() {
        return MAC;
    }

    public void setMAC(String MAC) {
        this.MAC = MAC;
    }

    public Map<String, String> getWifi_scan() {
        return wifi_scan;
    }

    public void setWifi_scan(Map<String, String> wifi_scan) {
        this.wifi_scan = wifi_scan;
    }

    @Override
    public String toString() {
        return "ReadDeviceInfoBean{" +
                "T='" + T + '\'' +
                ", N='" + N + '\'' +
                ", PO='" + PO + '\'' +
                ", HD='" + HD + '\'' +
                ", FW='" + FW + '\'' +
                ", RF='" + RF + '\'' +
                ", MAC='" + MAC + '\'' +
                ", wifi_scan=" + wifi_scan +
                '}';
    }
}
//不知道什么东西，返回的数据结构中有它。里面的C是个空列表，不知道类型。
class CCCC{
    private String N;
    private List<Object> C;

    public String getN() {
        return N;
    }

    public void setN(String n) {
        N = n;
    }

    public List<Object> getC() {
        return C;
    }

    public void setC(List<Object> c) {
        C = c;
    }
}
