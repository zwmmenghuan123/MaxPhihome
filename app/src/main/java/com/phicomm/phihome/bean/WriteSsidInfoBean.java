package com.phicomm.phihome.bean;

import java.io.Serializable;

/**
 * 向智能设备写入wifi信息的返回结果
 * Created by xiaolei.yang on 2017/7/7.
 */

public class WriteSsidInfoBean extends BaseResponse implements Serializable {
    private static final long serialVersionUID = -8999409410693030556L;
    private int errorCode;
    private String message;
    private String result;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "WriteSsidInfoBean{" +
                "errorCode=" + errorCode +
                ", message='" + message + '\'' +
                ", result='" + result + '\'' +
                '}';
    }
}
