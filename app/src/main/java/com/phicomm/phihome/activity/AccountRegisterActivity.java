package com.phicomm.phihome.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.phicomm.phihome.R;
import com.phicomm.phihome.manager.AccountManager;
import com.phicomm.phihome.presenter.CloudAccountPresenter;
import com.phicomm.phihome.presenter.viewback.CloudAccountView;
import com.phicomm.phihome.utils.LogUtils;
import com.phicomm.phihome.utils.RegexUtils;
import com.phicomm.phihome.utils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 斐讯云账号注册页
 * Created by qisheng.lv on 2017/4/12.
 */
public class AccountRegisterActivity extends BaseActivity {
    @BindView(R.id.et_pwd)
    EditText mEtPwd;
    @BindView(R.id.et_pwd_comfirm)
    EditText mEtPwdComfirm;

    private String mVerCode;
    private String mPhone;
    private String mPwd;
    private String mPwdComfirm;
    private CloudAccountPresenter mPresenter;

    @Override
    public void initLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register);
    }

    @Override
    public void afterInitView() {
        setPageTitle(R.string.password_set);
        mPhone = getIntent().getStringExtra("register_phone");
        mVerCode = getIntent().getStringExtra("ver_code");
        initPresenter();
    }

    private void initPresenter() {
        mPresenter = new CloudAccountPresenter(new CloudAccountView() {
            @Override
            public void onAuthorizationError(int code, String msg) {
                ToastUtil.show(msg);
            }

            @Override
            public void onAuthorizationSuccess(String authorizationcode) {
                doRegister();
            }

            @Override
            public void onRegisterError(int code, String msg) {
                ToastUtil.show(msg);
            }

            @Override
            public void onRegisterSuccess() {
                ToastUtil.show(AccountRegisterActivity.this, R.string.register_success);
                onRegisterOk();
            }
        });
    }

    @OnClick(R.id.btn_submit)
    public void btn_submit() {
        mPwd = mEtPwd.getText().toString().trim();
        mPwdComfirm = mEtPwdComfirm.getText().toString().trim();
        LogUtils.debug("btn_submit: " + mPhone + " * " + mVerCode + " 8 " + mPwd);
        if (checkInput()) {
            doRegister();
        }
    }

    /**
     * 发起注册
     */
    private void doRegister() {
        if (AccountManager.getInstance().hasAuthCode()) {
            mPresenter.register(mPwd, mPhone, mVerCode);
        } else {
            mPresenter.authorization();
        }
    }

    private void onRegisterOk() {
        setResult(RESULT_OK);
        finish();
    }

    private boolean checkInput() {
        if (TextUtils.isEmpty(mVerCode) || !RegexUtils.checkMobilePhone(mPhone)) {
            ToastUtil.show(this, R.string.register_pwd_diff);
            finish();
            return false;
        }

        if (TextUtils.isEmpty(mPwd) || mPwd.length() < 6) {
            ToastUtil.show(this, R.string.register_pwd_illegal);
            return false;
        }

        if (!mPwd.equals(mPwdComfirm)) {
            ToastUtil.show(this, R.string.register_pwd_diff);
            return false;
        }

        return true;
    }

}
