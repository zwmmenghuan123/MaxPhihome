package com.phicomm.phihome.net.callback;

import com.alibaba.fastjson.JSON;
import com.phicomm.phihome.constants.NetConfig;
import com.phicomm.phihome.utils.GenericUtils;
import com.phicomm.phihome.utils.LogUtils;

import okhttp3.Request;

/**
 * 网络请求回调Bean解析类
 * Created by qisheng.lv on 2017/4/12.
 */
public abstract class BeanCallback<T> extends BaseCallback<T> {

    public abstract void onSuccess(T t);

    @Override
    public void onSuccess(String result, Request request) {
        Class clasz = GenericUtils.getGenericClass(getClass());
        T obj = null;
        try {
            obj = (T) JSON.parseObject(result, clasz);
        } catch (Exception e) {
            LogUtils.debug(e);
        }

        if (obj == null) {
            toUiError(NetConfig.ERROR_PARSE_RESULT_CODE, null, request);
        } else {
            onSuccess(obj);
        }
    }

}
