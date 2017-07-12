package com.phicomm.phihome.presenter;

import android.text.TextUtils;

import com.phicomm.phihome.R;
import com.phicomm.phihome.bean.Authorization;
import com.phicomm.phihome.bean.Captcha;
import com.phicomm.phihome.bean.CloudAccount;
import com.phicomm.phihome.constants.AppConstans;
import com.phicomm.phihome.manager.AccountManager;
import com.phicomm.phihome.model.CloudAccountModel;
import com.phicomm.phihome.net.callback.BaseCallback;
import com.phicomm.phihome.net.callback.BeanCallback;
import com.phicomm.phihome.presenter.viewback.CloudAccountView;
import com.phicomm.phihome.utils.CommonUtils;
import com.phicomm.phihome.utils.EntryUtils;
import com.phicomm.phihome.utils.SpfUtils;

import okhttp3.Request;

/**
 * 云账号Presenter
 * Created by qisheng.lv on 2017/4/12.
 */
public class CloudAccountPresenter {
    private static final String CLIENT_ID = "7";
    private static final String CLIENT_SECRET = "feixun*123";
    private static final String RESPONSE_TYPE = "code";
    private static final String SCOPE = "write";
    private static final String VER_CODE_TYPE = "0";
    private static final String REGISTER_SOURCE = "7";

    private CloudAccountView mView;
    private CloudAccountModel mModel;
    private AccountManager mManager;

    public CloudAccountPresenter(CloudAccountView accountView) {
        this.mView = accountView;
        mModel = new CloudAccountModel();
        mManager = AccountManager.getInstance();
    }

    /**
     * 获取授权码
     */
    public void authorization() {
        mModel.authorization(CLIENT_ID, CLIENT_SECRET, null, RESPONSE_TYPE, SCOPE, new BeanCallback<Authorization>() {
            @Override
            public void onError(String code, String msg) {
                if (mView != null) {
                    mView.onAuthorizationError(code, msg);
                }
            }

            @Override
            public void onSuccess(Authorization authorization) {
                if (mView != null) {
                    if (TextUtils.isEmpty(authorization.getAuthorizationcode())) {
                        mView.onAuthorizationError("0", CommonUtils.getString(R.string.login_auth_fail));
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
     * @param phonenumber 账号
     * @param password  密码
     */
    public void loginCloud(final String phonenumber, final String password) {
        String md5Pwd = EntryUtils.getMd5(password);
        mModel.loginCloud(mManager.getAuthCode(), null, md5Pwd, phonenumber, null, new BeanCallback<CloudAccount>() {

            @Override
            public void onError(String code, String msg) {
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
                        mView.onLoginError("0", CommonUtils.getString(R.string.login_token_fail));
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
            public void onError(String code, String msg) {
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

    public void getCaptcha() {
        mModel.getCaptcha(mManager.getAuthCode(), new BeanCallback<Captcha>() {

            @Override
            public void onError(String code, String msg) {
                if (mView != null) {
                    mView.onGetCaptchaError(code, msg);
                }
            }

            @Override
            public void onSuccess(Captcha captcha) {
                if (mView != null) {
                    mView.onGetCaptchaSuccess(captcha);
                }
            }
        });
    }

    public void getVerCode(String captcha, String captchaId, String phonenumber) {
        mModel.getVerCode(mManager.getAuthCode(), captcha, captchaId, null, null, phonenumber, VER_CODE_TYPE, new BaseCallback() {
            @Override
            public void onError(String code, String msg) {
                if (mView != null) {
                    mView.onGetVerCodeError(code, msg);
                }
            }

            @Override
            public void onSuccess(String result, Request request) {
                if (mView != null) {
                    mView.onGetVerCodeSuccess();
                }
            }
        });

    }

    public void register(String password, String phonenumber, String verificationcode) {
        String md5Pwd = EntryUtils.getMd5(password);
        mModel.register(mManager.getAuthCode(), null, null, md5Pwd, phonenumber, REGISTER_SOURCE, null, verificationcode, new BaseCallback() {
            @Override
            public void onError(String code, String msg) {
                if (mView != null) {
                    mView.onRegisterError(code, msg);
                }
            }

            @Override
            public void onSuccess(String result, Request request) {
                if (mView != null) {
                    mView.onRegisterSuccess();
                }
            }
        });

    }


}
