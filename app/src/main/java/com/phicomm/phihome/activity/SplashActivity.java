package com.phicomm.phihome.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.phicomm.phihome.BuildConfig;
import com.phicomm.phihome.bean.CloudAccount;
import com.phicomm.phihome.constants.AppConstans;
import com.phicomm.phihome.presenter.CloudAccountPresenter;
import com.phicomm.phihome.presenter.viewback.CloudAccountView;
import com.phicomm.phihome.utils.AppInfoUtils;
import com.phicomm.phihome.utils.LogUtils;
import com.phicomm.phihome.utils.SpfUtils;
import com.phicomm.phihome.utils.ToastUtil;

public class SplashActivity extends BaseActivity {
    private static final long SPLASH_TIME = 2000;
    Handler mHandler;
    private boolean mIsLoginSuccess;
    private CloudAccountPresenter mPresenter;
    private String mPhone;
    private String mPwd;

    Runnable mR = new Runnable() {
        @Override
        public void run() {
            gotoNextActivity();
        }
    };

    @Override
    public void initLayout(Bundle savedInstanceState) {

    }

    @Override
    public void afterInitView() {
        mHandler = new Handler();
        showTargetVersionAlert();
        mHandler.postDelayed(mR, SPLASH_TIME);
        initPresenter();
        getAuthorization();
    }

    private void initPresenter() {
        mPresenter = new CloudAccountPresenter(new CloudAccountView() {
            //获取授权码失败，终止整个流程
            @Override
            public void onAuthorizationError(int code, String msg) {
                LogUtils.debug("onAuthorizationError: " + msg);
            }

            //获取授权码成功，发起登录
            @Override
            public void onAuthorizationSuccess(String authorizationcode) {
                doLogin();
            }

            //登录失败
            @Override
            public void onLoginError(int code, String msg) {
                LogUtils.debug("onLoginError: " + msg);
            }

            //登录成功
            @Override
            public void onLoginSuccess(CloudAccount cloudAccount) {
                mIsLoginSuccess = true;
            }
        });
    }


    private void getAuthorization() {
        mPhone = (String) SpfUtils.get(AppConstans.Sp.CLOUD_ACCOUNT_PHONE, "");
        mPwd = (String) SpfUtils.get(AppConstans.Sp.CLOUD_ACCOUNT_PWD, "");
        if (!TextUtils.isEmpty(mPhone) && !TextUtils.isEmpty(mPwd)) {
            mPresenter.authorization();
        }

    }

    private void doLogin() {
        mPresenter.loginCloud(mPhone, mPwd);
    }

    private void gotoNextActivity() {
        startActivity(new Intent(this, mIsLoginSuccess ? MainActivity.class : LoginCloudActivity.class));
        finish();
    }

    private void showTargetVersionAlert() {
        if (BuildConfig.isDebug && AppInfoUtils.getAppVersionCode() > 22) {
            ToastUtil.show("当前SDK版本为" + Build.VERSION.SDK_INT);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mR != null) {
            mHandler.removeCallbacks(mR);
        }
    }

}
