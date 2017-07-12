package com.phicomm.phihome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.phicomm.phihome.R;
import com.phicomm.phihome.bean.CloudAccount;
import com.phicomm.phihome.constants.AppConstans;
import com.phicomm.phihome.manager.AccountManager;
import com.phicomm.phihome.presenter.CloudAccountPresenter;
import com.phicomm.phihome.presenter.viewback.CloudAccountView;
import com.phicomm.phihome.utils.RegexUtils;
import com.phicomm.phihome.utils.SpfUtils;
import com.phicomm.phihome.utils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * 登陆云账号
 * Created by qisheng.lv on 2017/4/12.
 */
public class LoginCloudActivity extends BaseActivity {

    @BindView(R.id.et_user)
    EditText mEtUser;
    @BindView(R.id.et_pwd)
    EditText mEtPwd;

    private CloudAccountPresenter mPresenter;
    private String mUser;
    private String mPwd;

    @Override
    public void initLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login_cloud);
    }

    @Override
    public void afterInitView() {
        hideBack();
        setPageTitle(getString(R.string.title_login_cloud));
        initPresenter();
        getUser();
    }

    private void getUser() {
        String user = (String) SpfUtils.get(AppConstans.Sp.CLOUD_ACCOUNT_PHONE, "");
        if (!TextUtils.isEmpty(user)) {
            mEtUser.setText(user);
        }

        String pwd = (String) SpfUtils.get(AppConstans.Sp.CLOUD_ACCOUNT_PWD, "");
        if (!TextUtils.isEmpty(pwd)) {
            mEtPwd.setText(pwd);
        }
    }


    private void initPresenter() {
        mPresenter = new CloudAccountPresenter(new CloudAccountView() {
            //获取授权码失败，终止整个流程
            @Override
            public void onAuthorizationError(String code, String msg) {
//                hideLoading();
                ToastUtil.show(msg);
            }

            //获取授权码成功，发起登录
            @Override
            public void onAuthorizationSuccess(String authorizationcode) {
                doLogin();
            }

            //登录失败
            @Override
            public void onLoginError(String code, String msg) {
//                hideLoading();
                ToastUtil.show(msg);
            }

            //登录成功
            @Override
            public void onLoginSuccess(CloudAccount cloudAccount) {
//                hideLoading();
                gotoMainActivity();
            }
        });
    }


    //点击登录
    @OnClick(R.id.btn_login)
    public void btn_login() {
        mUser = mEtUser.getText().toString().trim();
        mPwd = mEtPwd.getText().toString().trim();
        if (checkInput()) {
            loginPrepare();
        }
    }

    @OnLongClick(R.id.btn_login)
    public boolean btn_login_long() {
//        String url = "file:///android_asset/test.html";
//        String url = "http://geek.csdn.net/";
//        startActivity(new Intent(this, X5WebActivity.class).putExtra(AppConstans.Common.INTENT_URL, url));
        startActivity(new Intent(this, TestActivity.class));
        return true;
    }

    @OnClick(R.id.tv_register)
    public void tv_register() {
        gotoVerCodeActivity();
    }


    /**
     * 有授权码，直接登陆，否则先获取授权码。
     */
    private void loginPrepare() {
//        showLoading();
        if (AccountManager.getInstance().hasAuthCode()) {
            doLogin();
        } else {
            authorization();
        }
    }

    /**
     * 获取授权码
     */
    private void authorization() {
        mPresenter.authorization();
    }

    /**
     * 发起登陆
     */
    private void doLogin() {
        mPresenter.loginCloud(mUser, mPwd);
    }

    /**
     * 进入主页
     */
    private void gotoMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void gotoVerCodeActivity() {
        startActivity(new Intent(this, VerificationCodeActivity.class));
    }


    private boolean checkInput() {
        if (!RegexUtils.checkMobilePhone(mUser)) {
            ToastUtil.show(this, R.string.login_user_illegal);
            return false;
        }

        if (TextUtils.isEmpty(mPwd) || mPwd.length() < 6) {
            ToastUtil.show(this, R.string.login_pwd_illegal);
            return false;
        }

        return true;
    }


}
