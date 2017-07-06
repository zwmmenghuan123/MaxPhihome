package com.phicomm.phihome.presenter;

import android.text.TextUtils;

import com.phicomm.phihome.R;
import com.phicomm.phihome.bean.Authorization;
import com.phicomm.phihome.bean.CloudAccount;
import com.phicomm.phihome.constants.AppConstans;
import com.phicomm.phihome.manager.AccountManager;
import com.phicomm.phihome.model.AccountCloudModel;
import com.phicomm.phihome.net.callback.BaseCallback;
import com.phicomm.phihome.net.callback.BeanCallback;
import com.phicomm.phihome.presenter.viewback.AccountCloudView;
import com.phicomm.phihome.utils.CommonUtils;
import com.phicomm.phihome.utils.EntryUtils;
import com.phicomm.phihome.utils.SpfUtils;

import okhttp3.Request;

/**
 * 云账号Presenter
 * Created by qisheng.lv on 2017/4/12.
 */
public class AccountCloudPresenter {
    private static final String CLIENT_ID = "7";
    private static final String CLIENT_SECRET = "feixun*123";
    private static final String RESPONSE_TYPE = "code";
    private static final String SCOPE = "write";

    private AccountCloudView mView;
    private AccountCloudModel mModel;
    private AccountManager mManager;

    public AccountCloudPresenter(AccountCloudView accountView) {
        this.mView = accountView;
        mModel = new AccountCloudModel();
        mManager = new AccountManager();
    }

    /**
     * 获取授权码
     */
    public void authorization() {
        mModel.authorization(CLIENT_ID, CLIENT_SECRET, null, RESPONSE_TYPE, SCOPE, new BeanCallback<Authorization>() {
            @Override
            public void onError(int code, String msg) {
                if (mView != null) {
                    mView.onAuthorizationError(code, msg);
                }
            }

            @Override
            public void onSuccess(Authorization authorization) {
                if (mView != null) {
                    if (TextUtils.isEmpty(authorization.getAuthorizationcode())) {
                        mView.onAuthorizationError(0, CommonUtils.getString(R.string.login_auth_fail));
                    } else {
                        mManager.saveAuthCode(authorization.getAuthorizationcode());
                        mView.onAuthorizationSuccess(authorization.getAuthorizationcode());
                    }
                }
            }

        });

    }

    /**
     * 登陆云账号
     *
     * @param phonenumber
     * @param password
     */
    public void loginCloud(final String phonenumber, final String password) {
        String authCode = mManager.getAuthCode();
        String md5Pwd = EntryUtils.getMd5(password);
        mModel.loginCloud(authCode, null, md5Pwd, phonenumber, null, new BeanCallback<CloudAccount>() {

            @Override
            public void onError(int code, String msg) {
                if (mView != null) {
                    mView.onLoginError(code, msg);
                }
            }

            @Override
            public void onSuccess(CloudAccount cloudLogin) {
                SpfUtils.put(AppConstans.Sp.CLOUD_ACCOUNT_PHONE, phonenumber);
                SpfUtils.put(AppConstans.Sp.CLOUD_ACCOUNT_PWD, password);
                if (mView != null) {
                    if (TextUtils.isEmpty(cloudLogin.getAccess_token()) || TextUtils.isEmpty(cloudLogin.getUid())) {
                        mView.onLoginError(0, CommonUtils.getString(R.string.login_token_fail));
                    } else {
                        if (!TextUtils.isEmpty(cloudLogin.getUid())) {
                            mManager.saveUid(cloudLogin.getUid());
                        }
                        mManager.saveToken(cloudLogin.getAccess_token());
                        mManager.saveRefreshToken(cloudLogin.getRefresh_token());
                        mView.onLoginSuccess(cloudLogin);
                    }
                }
            }

        });

    }


    public void exitLogin() {
        String token = mManager.getToken();
        if (TextUtils.isEmpty(token) && mView != null) {
            mView.onLogoutSuccess();
            return;
        }

        mModel.logoutCloud(new BaseCallback() {
            @Override
            public void onError(int code, String msg) {
                if (mView != null) {
                    mView.onLogoutError(code, msg);
                }
            }

            @Override
            public void onSuccess(String result, Request request) {
                if (mView != null) {
                    mView.onLogoutSuccess();
                }
            }
        });

    }


}
