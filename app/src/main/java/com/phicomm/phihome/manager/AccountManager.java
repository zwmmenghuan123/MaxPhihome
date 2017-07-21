package com.phicomm.phihome.manager;

import android.text.TextUtils;

import com.phicomm.phihome.constants.AppConstans;
import com.phicomm.phihome.utils.LogUtils;
import com.phicomm.phihome.utils.SpfUtils;


/**
 * 云账号管理类
 * Created by qisheng.lv on 2017/4/12.
 */

public class AccountManager {

    private static volatile AccountManager mInstance;

    private AccountManager() {

    }

    public static AccountManager getInstance() {
        if (mInstance == null) {
            synchronized (AccountManager.class) {
                if (mInstance == null) {
                    mInstance = new AccountManager();
                }
            }
        }
        return mInstance;
    }


    public void saveAuthCode(String code) {
        if (!TextUtils.isEmpty(code)) {
            SpfUtils.put(AppConstans.Sp.AUTHORIZATION_CODE, code);
        }
    }

    public String getAuthCode() {
        return (String) SpfUtils.get(AppConstans.Sp.AUTHORIZATION_CODE, "");
//        return "feixun.SH_7";
    }

    public boolean hasAuthCode() {
        String code = getAuthCode();
        return !TextUtils.isEmpty(code);
    }

    public void saveToken(String token) {
        if (!TextUtils.isEmpty(token)) {
            SpfUtils.put(AppConstans.Sp.ACCESS_TOKEN, token);
            LogUtils.debug("token: " + token);
        }
    }

    public String getToken() {
        return (String) SpfUtils.get(AppConstans.Sp.ACCESS_TOKEN, "");
    }

    public void saveRefreshToken(String refreshToken) {
        if (!TextUtils.isEmpty(refreshToken)) {
            SpfUtils.put(AppConstans.Sp.REFRESH_TOKEN, refreshToken);
        }
    }

    public String getrefreshToken() {
        return (String) SpfUtils.get(AppConstans.Sp.REFRESH_TOKEN, "");
    }

    public void saveUid(String code) {
        if (!TextUtils.isEmpty(code)) {
            SpfUtils.put(AppConstans.Sp.CLOUD_ACCOUNT_UID, code);
        }
    }

    public String getUid() {
        return (String) SpfUtils.get(AppConstans.Sp.CLOUD_ACCOUNT_UID, "");
    }

    public void saveRegisterCodeTime(int left) {
        SpfUtils.put("register_code_time", System.currentTimeMillis() / 1000 + "-" + left);
    }

    public int getRegisterCodeTime() {
        String lastStr = (String) SpfUtils.get("register_code_time", "");
        if (TextUtils.isEmpty(lastStr)) {
            return AppConstans.Common.REGISTER_CODE_TIME;
        }

        try {
            String[] split = lastStr.split("-");
            int lastExitTime = Integer.valueOf(split[0]);
            int lastLef = Integer.valueOf(split[1]);
            int now = (int) (System.currentTimeMillis() / 1000);

            int now2Last = now - lastExitTime;
            if (lastLef > now2Last) {
                return lastLef - now2Last;
            }

            return AppConstans.Common.REGISTER_CODE_TIME;

        } catch (Exception e) {
            LogUtils.debug("getRegisterCodeTime exception: " + e);
        }
        return AppConstans.Common.REGISTER_CODE_TIME;
    }

}
