package com.phicomm.phihome.bean;

import java.io.Serializable;

/**
 * 账户详情
 * Created by xiaolei.yang on 2017/7/24.
 */

public class AccountBean extends FxResponse implements Serializable {
    private static final long serialVersionUID = -8308296481729872103L;

    private AccountDetailsBean data;

    public AccountDetailsBean getData() {
        return data;
    }

    public void setData(AccountDetailsBean data) {
        this.data = data;
    }
}
