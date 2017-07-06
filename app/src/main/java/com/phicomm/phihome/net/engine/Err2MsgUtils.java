package com.phicomm.phihome.net.engine;

import com.phicomm.phihome.constants.NetConfig;

/**
 * 该工具类统一将网络请求的错误码转化成对应的信息，以便上层解读
 * Created by qisheng.lv on 2017/4/12.
 */
public class Err2MsgUtils {

    public static String getErrMsg(int code) {
        switch (code) {
            case NetConfig.ERROR_NET_UNAVALIABLE_CODE:
                return NetConfig.ERROR_NET_UNAVALIABLE;

            case NetConfig.ERROR_TIMEOUT_CODE:
                return NetConfig.ERROR_TIMEOUT;

            case NetConfig.ERROR_SERVER_NO_RESPONSE_CODE:
                return NetConfig.ERROR_SERVER_NO_RESPONSE;

            case NetConfig.ERROR_PARSE_RESULT_CODE:
                return NetConfig.ERROR_PARSE_RESULT;

            case NetConfig.ERROR_TOKEN_INVALID_CODE:
                return NetConfig.ERROR_TOKEN_INVALID;

            case NetConfig.ERROR_USER_NOET_EXIST_CODE:
                return NetConfig.ERROR_USER_NOET_EXIST;

            case NetConfig.ERROR_CLOUD_PASSWORD_CODE:
                return NetConfig.ERROR_CLOUD_PASSWORD;

            case NetConfig.ERROR_AUTHCODE_CODE:
                return NetConfig.ERROR_AUTHCODE;

            case NetConfig.ERROR_PARAMS_CODE:
                return NetConfig.ERROR_PARAMS;

            case NetConfig.ERROR_TOKEN_REFRESH_CODE:
                return NetConfig.ERROR_TOKEN_REFRESH;

            default:
                return NetConfig.ERROR_UNKNOW;
        }

    }

}
