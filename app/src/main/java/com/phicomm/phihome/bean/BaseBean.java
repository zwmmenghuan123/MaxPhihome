package com.phicomm.phihome.bean;

import java.io.Serializable;

/**
 * Created by xiaolei.yang on 2017/7/12.
 */

public class BaseBean<T> implements Serializable{
    private static final long serialVersionUID = 6547932219722087755L;

    private int status;
    private String message;
    private T result;
}
