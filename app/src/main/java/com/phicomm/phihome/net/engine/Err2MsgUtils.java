package com.phicomm.phihome.net.engine;

import android.text.TextUtils;

import com.phicomm.phihome.constants.AppConstans;
import com.phicomm.phihome.utils.LogUtils;
import com.phicomm.phihome.utils.SpfUtils;

import java.util.HashMap;

/**
 * 该工具类统一将网络请求的错误码转化成对应的信息，以便上层解读
 * Created by qisheng.lv on 2017/4/12.
 */
public class Err2MsgUtils {
    public static final String CODE_UNKNOW_ERROR = "301";
    public static final String CODE_NET_DISABLE = "302";
    public static final String CODE_NET_TIMEOUT = "303";
    public static final String CODE_NO_RESPONSE = "304";
    public static final String CODE_PARSE_ERROR = "305";
    public static final String CODE_TOKEN_TIMEOUT = "306";

    private static HashMap<String, String> mMap;

    static {
        mMap = new HashMap<>();
        initMsg();
    }

    private static void initMsg() {
        //自定义错误
        mMap.put(CODE_UNKNOW_ERROR, "请求失败");
        mMap.put(CODE_NET_DISABLE, "网络不可用");
        mMap.put(CODE_NET_TIMEOUT, "网络超时");
        mMap.put(CODE_NO_RESPONSE, "服务器无响应");
        mMap.put(CODE_PARSE_ERROR, "解析出错");
        mMap.put(CODE_TOKEN_TIMEOUT, "登录过期，请重新登录");

        //服务器返回的错误
        mMap.put("1", "验证码错误");
        mMap.put("2", "验证码过期");
        mMap.put("5", "token失效");
        mMap.put("7", "用户不存在");
        mMap.put("8", "密码错误");
        mMap.put("11", "授权码错误，请重试");
        mMap.put("12", "参数错误");
        mMap.put("13", "获取验证码失败");
        mMap.put("32", "密码格式错误");
        mMap.put("36", "请刷新图形验证码");
        mMap.put("37", "图片验证码不正确");
        mMap.put("38", "验证码请求过快");
        mMap.put("39", "验证码请求超出限制");
        mMap.put("50", "服务器异常");
    }

    public static String getErrMsg(String code) {
        //授权码错误，将保存在本地的授权码清空
        if (code.equals("11")) {
            SpfUtils.put(AppConstans.Sp.AUTHORIZATION_CODE, "");
        }
        String message = mMap.get(code);
        LogUtils.debug("Err2MsgUtils getErrMsg: " + code + " * " + message);
        return TextUtils.isEmpty(message) ? mMap.get(CODE_UNKNOW_ERROR) : message;
    }

}
