package com.phicomm.phihome.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;

import com.phicomm.phihome.PhApplication;
import com.phicomm.phihome.R;
import com.phicomm.phihome.bean.Captcha;
import com.phicomm.phihome.presenter.CloudAccountPresenter;
import com.phicomm.phihome.presenter.viewback.CloudAccountView;
import com.phicomm.phihome.utils.Base64Utils;
import com.phicomm.phihome.utils.RegexUtils;
import com.phicomm.phihome.utils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 验证码页面
 * Created by qisheng.lv on 2017/7/10.
 */
public class VerificationCodeActivity extends BaseActivity {
    @BindView(R.id.et_captcha)
    EditText mEtCaptcha;
    @BindView(R.id.iv_captcha)
    ImageView mIvCaptcha;
    @BindView(R.id.et_phone)
    EditText mEtPhone;
    @BindView(R.id.et_verification_code)
    EditText mEtVerCode;

    private static final int REQUEST_CODE_REGISTER = 501;
    private Captcha mCaptcha;
    private String mChaCode;
    private String mPhone;
    private String mVerCode;
    private CloudAccountPresenter mPresenter;
    private boolean mIsCaptchaRequest = true;

    @Override
    public void initLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_verification_code);
    }

    @Override
    public void afterInitView() {
        setPageTitle(R.string.register);
        initPresenter();
        getCaptcha();
    }

    private void initPresenter() {
        mPresenter = new CloudAccountPresenter(new CloudAccountView() {
            @Override
            public void onAuthorizationError(int code, String msg) {
                ToastUtil.show(VerificationCodeActivity.this, msg);
            }

            @Override
            public void onAuthorizationSuccess(String authorizationcode) {
                if (mIsCaptchaRequest) {
                    getCaptcha();
                } else {
                    getVerCode();
                }
            }

            @Override
            public void onGetCaptchaError(int code, String msg) {
                ToastUtil.show(VerificationCodeActivity.this, msg);
            }

            @Override
            public void onGetCaptchaSuccess(Captcha captcha) {
                mCaptcha = captcha;
                showCaptcha(captcha);
            }

            @Override
            public void onGetVerCodeError(int code, String msg) {
                ToastUtil.show(VerificationCodeActivity.this, msg);
            }

            @Override
            public void onGetVerCodeSuccess() {
                gotoRegister();
            }

        });
    }

    @OnClick(R.id.iv_captcha)
    public void iv_captcha() {
        mIsCaptchaRequest = true;
        getCaptcha();
    }

    @OnClick(R.id.tv_get_code)
    public void tv_get_code() {
        mIsCaptchaRequest = false;
        mPhone = mEtPhone.getText().toString().trim();
        mChaCode = mEtCaptcha.getText().toString().trim();
        if (checkChaCode()) {
            getVerCode();
        }
    }

    @OnClick(R.id.btn_next)
    public void btn_next() {
        mPhone = mEtPhone.getText().toString().trim();
        mVerCode = mEtVerCode.getText().toString().trim();
        if (checkInput()) {
            gotoRegister();
        }
    }


    /**
     * 获取图形验证码
     */
    private void getCaptcha() {
        mPresenter.getCaptcha();
    }

    /**
     * 获取短信验证码
     */
    private void getVerCode() {
        mPresenter.getVerCode(mChaCode, mCaptcha.getCaptchaid(), mPhone);
    }

    private void showCaptcha(Captcha captcha) {
        if (captcha == null || TextUtils.isEmpty(captcha.getCaptcha()) || TextUtils.isEmpty(captcha.getCaptchaid())) {
            ToastUtil.show(this, R.string.captcha_failure);
            return;
        }
        byte[] imageByte = Base64Utils.decode(captcha.getCaptcha());
        Drawable image = new BitmapDrawable(PhApplication.getContext().getResources(), BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length));
        mIvCaptcha.setImageDrawable(image);
    }

    private void gotoRegister() {
        Intent intent = new Intent(this, AccountRegisterActivity.class);
        intent.putExtra("ver_code", mVerCode);
        startActivityForResult(intent, REQUEST_CODE_REGISTER);
    }


    private boolean checkChaCode() {
        if (!RegexUtils.checkMobilePhone(mPhone)) {
            ToastUtil.show(this, R.string.login_user_illegal);
            return false;
        }

        if (TextUtils.isEmpty(mChaCode) || mChaCode.length() < 4) {
            ToastUtil.show(this, R.string.captcha_illegal);
            return false;
        }

        if (mCaptcha == null || TextUtils.isEmpty(mCaptcha.getCaptcha()) || TextUtils.isEmpty(mCaptcha.getCaptchaid())) {
            ToastUtil.show(this, R.string.captcha_not_request);
            return false;
        }

        return true;
    }

    private boolean checkInput() {
        if (!RegexUtils.checkMobilePhone(mPhone)) {
            ToastUtil.show(this, R.string.login_user_illegal);
            return false;
        }

        if (TextUtils.isEmpty(mVerCode) || mVerCode.length() < 6) {
            ToastUtil.show(this, R.string.verifiaction_code_illegal);
            return false;
        }

        return true;
    }

}
