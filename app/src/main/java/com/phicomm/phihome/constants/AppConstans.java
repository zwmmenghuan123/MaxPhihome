package com.phicomm.phihome.constants;

/**
 * 常量
 * Created by qisheng.lv on 2017/4/12.
 */

public interface AppConstans {

    /**
     * 公共
     */
    interface Common {
        String PLATFORM_ID = "2015100011";

        /**
         * header头部
         */
        String AUTHORIZATION = "Authorization";

        String INTENT_URL = "INTENT_URL";
    }

    /**
     * 网络配置相关
     */
    interface NetConfig {
        int HTTP_CONNECT_TIME_OUT = 10 * 1000;
        int HTTP_READ_TIME_OUT = 20 * 1000;
        int HTTP_WRITE_TIME_OUT = 20 * 1000;
    }

    /**
     * SharedPreferences
     */
    interface Sp {
        String COOKIE_TOKEN = "COOKIE_TOKEN";

        String AUTHORIZATION_CODE = "AUTHORIZATION_CODE";

        String ACCESS_TOKEN = "ACCESS_TOKEN";

        String REFRESH_TOKEN = "REFRESH_TOKEN";

        String CLOUD_ACCOUNT_PHONE = "CLOUD_ACCOUNT_PHONE";

        String CLOUD_ACCOUNT_PWD = "CLOUD_ACCOUNT_PWD";

        String CLOUD_ACCOUNT_UID = "CLOUD_ACCOUNT_UID";
    }

    interface Products {
        int TYPE_INSERTS = 1; //插座
    }

    /**
     * 取图
     */
    interface GetPhoto {
        int GET_PHOTO_FROM_CAMERA = 1; //拍照
        int GET_PHOTO_FROM_ALBUM = 2; //相册取图
    }

    /**
     * JSBridge
     */
    interface JsConfig {
        String JS_BRIDGE_COMMON = "common";


        int ERR_CODE_MESSAGE_ILLEGAL = 101;//消息不合法（为空或格式不对）

        int ERR_CODE_CLASS_NOT_FOUND = 102; //class找不到

        int ERR_CODE_METHOD_NOT_FOUND = 103;//

        int ERR_CODE_METHOD_CALL_FAILURE = 104;

        int ERR_CODE_PARAMS_ILLEGAL = 105;
    }

    /**
     * 第三方相关
     */
    interface ThirdParty {


    }


}
