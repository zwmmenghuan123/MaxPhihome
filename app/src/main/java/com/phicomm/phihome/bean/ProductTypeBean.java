package com.phicomm.phihome.bean;

import java.io.Serializable;

/**
 * 添加设备时选择设备类型的列表的列表项bean
 * Created by xiaolei.yang on 2017/7/18.
 */

public class ProductTypeBean implements Serializable {
    private static final long serialVersionUID = -924448836993601028L;

    private int productType;//设备类型（用以区分显示图标）
    private String productTypeName;//类型名称
    private String productTypeSsid;//该类型设备的ssid

    public int getProductType() {
        return productType;
    }

    public void setProductType(int productType) {
        this.productType = productType;
    }

    public String getProductTypeName() {
        return productTypeName;
    }

    public void setProductTypeName(String productTypeName) {
        this.productTypeName = productTypeName;
    }

    public String getProductTypeSsid() {
        return productTypeSsid;
    }

    public void setProductTypeSsid(String productTypeSsid) {
        this.productTypeSsid = productTypeSsid;
    }

    @Override
    public String toString() {
        return "ProductTypeBean{" +
                "productType=" + productType +
                ", productTypeName='" + productTypeName + '\'' +
                ", productTypeSsid='" + productTypeSsid + '\'' +
                '}';
    }
}
