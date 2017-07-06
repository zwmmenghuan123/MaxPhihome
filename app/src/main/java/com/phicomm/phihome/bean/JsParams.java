package com.phicomm.phihome.bean;

import java.io.Serializable;

/**
 * Created by qisheng.lv on 2017/7/4.
 */

public class JsParams implements Serializable {

    private static final long serialVersionUID = 4634155324004534001L;

    private String toast_msg;

    public String getToast_msg() {
        return toast_msg;
    }

    public void setToast_msg(String toast_msg) {
        this.toast_msg = toast_msg;
    }
}
