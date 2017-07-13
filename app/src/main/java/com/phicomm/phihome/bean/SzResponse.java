package com.phicomm.phihome.bean;

import java.io.Serializable;

/**
 * 网络响应基类-深圳后台
 * Created by qisheng.lv on 2017/7/13.
 */

public class SzResponse implements Serializable{

    private static final long serialVersionUID = -2784682168478897555L;

    private int status;

    private String message;

    private Result result;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result {
        private int ret_status;

        private String ret_message;

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
    }

}
