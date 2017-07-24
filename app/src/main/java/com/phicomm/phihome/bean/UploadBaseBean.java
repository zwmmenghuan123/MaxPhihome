package com.phicomm.phihome.bean;

import java.io.Serializable;

/**
 * 上传Base64字符串
 * Created by xiaolei.yang on 2017/7/24.
 */

public class UploadBaseBean implements Serializable {
    String error; //错误码 0：上传成功 18：图片格式错误 19:图片为空 50：服务器异常
    String url; //图片url

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "UploadBaseBean{" +
                "error='" + error + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
