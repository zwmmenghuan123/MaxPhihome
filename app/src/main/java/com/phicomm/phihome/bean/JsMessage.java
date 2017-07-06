package com.phicomm.phihome.bean;

import java.io.Serializable;

/**
 * Created by qisheng.lv on 2017/7/3.
 */

public class JsMessage implements Serializable{


    private static final long serialVersionUID = -2498327529077475559L;

    private String scheme;

    private String clasz;

    private String method;

    private String params;

    private JsParams jsParams;

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getClasz() {
        return clasz;
    }

    public void setClasz(String clasz) {
        this.clasz = clasz;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public JsParams getJsParams() {
        return jsParams;
    }

    public void setJsParams(JsParams jsParams) {
        this.jsParams = jsParams;
    }
}
