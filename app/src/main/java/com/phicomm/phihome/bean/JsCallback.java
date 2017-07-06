package com.phicomm.phihome.bean;

import java.io.Serializable;

/**
 * Created by qisheng.lv on 2017/7/3.
 */

public class JsCallback implements Serializable{

    private static final long serialVersionUID = -5917378546714344193L;

    private String method;

    private int err_code;

    private String err_msg;

    private String result;


    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getErr_code() {
        return err_code;
    }

    public void setErr_code(int err_code) {
        this.err_code = err_code;
    }

    public String getErr_msg() {
        return err_msg;
    }

    public void setErr_msg(String err_msg) {
        this.err_msg = err_msg;
    }


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
