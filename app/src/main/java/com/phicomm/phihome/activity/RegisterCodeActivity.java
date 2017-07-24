package com.phicomm.phihome.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.phicomm.phihome.PhApplication;
import com.phicomm.phihome.R;
import com.phicomm.phihome.bean.Captcha;
import com.phicomm.phihome.constants.AppConstans;
import com.phicomm.phihome.manager.AccountManager;
import com.phicomm.phihome.presenter.CloudAccountPresenter;
import com.phicomm.phihome.presenter.viewback.CloudAccountView;
import com.phicomm.phihome.utils.Base64Utils;
import com.phicomm.phihome.utils.LogUtils;
import com.phicomm.phihome.utils.RegexUtils;
import com.phicomm.phihome.utils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 验证码页面
 * Created by qisheng.lv on 2017/7/10.
 */
public class RegisterCodeActivity extends BaseActivity {
    @BindView(R.id.et_captcha)
    EditText mEtCaptcha;
    @BindView(R.id.iv_captcha)
    ImageView mIvCaptcha;
    @BindView(R.id.et_phone)
    EditText mEtPhone;
    @BindView(R.id.et_verification_code)
    EditText mEtVerCode;
    @BindView(R.id.tv_get_code)
    TextView mTvMsgCode;

    private static final int REQUEST_CODE_REGISTER = 501;
    private Captcha mCaptcha;
    private String mChaCode;
    private String mPhone;
    private String mVerCode;
    private CloudAccountPresenter mPresenter;
    private boolean mHasChecPhone;
    private int mCodeTime;
    private Handler mHandler;

    private Runnable mCodeTimeR = new Runnable() {
        @Override
        public void run() {
            if (mCodeTime <= 0) {
                mTvMsgCode.setText("获取验证码");
                return;
            }
            mTvMsgCode.setText(mCodeTime + "秒重新获取");
            mCodeTime -= 1;
            AccountManager.getInstance().saveRegisterCodeTime(mCodeTime);
            mHandler.postDelayed(this,1000);
        }
    };

    @Override
    public void initLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_verification_code);
    }

    @Override
    public void afterInitView() {
        setPageTitle(R.string.register);
        mHandler = new Handler();
        initPresenter();

        mCodeTime = AccountManager.getInstance().getRegisterCodeTime();
        LogUtils.debug("mCodeTime: " + mCodeTime);
        if (mCodeTime < AppConstans.Common.REGISTER_CODE_TIME) {
            mHandler.postDelayed(mCodeTimeR, 1000);
        }

        mEtCaptcha.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!mHasChecPhone) {
                    return;
                }
                String tempCode = mEtCaptcha.getText().toString().trim();
                if (!TextUtils.isEmpty(tempCode) && tempCode.length() == 4) {
                    mChaCode = tempCode;
                    doGetVerCode();
                }
            }
        });
    }

    private void initPresenter() {
        mPresenter = new CloudAccountPresenter(new CloudAccountView() {
            @Override
            public void onAuthorizationError(String code, String msg) {
                ToastUtil.show(msg);
            }

            @Override
            public void onAuthorizationSuccess(String authorizationcode) {
                doCheckPhone();
            }

            @Override
            public void onCheckPhoneError(String code, String msg) {
                ToastUtil.show(msg);
                mHasChecPhone = false;
            }

            @Override
            public void onCheckPhoneSuccess(boolean isExist) {
                mHasChecPhone = true;
                if (isExist) {
                    ToastUtil.show(getString(R.string.check_phone_already_register));
                } else {
                    doGetCaptcha();
                }
            }

            @Override
            public void onGetCaptchaError(String code, String msg) {
                ToastUtil.show(msg);
            }

            @Override
            public void onGetCaptchaSuccess(Captcha captcha) {
                mCaptcha = captcha;
                showCaptcha(captcha);
            }

            @Override
            public void onGetVerCodeError(String code, String msg) {
                ToastUtil.show(msg);
                mHandler.postDelayed(mCodeTimeR, 0);
            }

            @Override
            public void onGetVerCodeSuccess() {
                ToastUtil.show(R.string.get_vercode_success);
                mHandler.postDelayed(mCodeTimeR, 0);
            }

            @Override
            public void onCheckVerCodeError(String code, String msg) {
                ToastUtil.show(msg);
            }

            @Override
            public void onCheckVerCodeSuccess() {
                gotoRegister();
            }
        });
    }

    @OnClick(R.id.iv_captcha)
    public void iv_captcha() {
        doGetCaptcha();
    }

    @OnClick(R.id.tv_get_code)
    public void tv_get_code() {
        if (!mTvMsgCode.getText().toString().equals("获取验证码")) {
            return;
        }

        mPhone = mEtPhone.getText().toString().trim();
        mChaCode = mEtCaptcha.getText().toString().trim();
        if (checkPhoneInput()) {
            doCheckPhone();
        }
    }


    @OnClick(R.id.btn_next)
    public void btn_next() {
        mPhone = mEtPhone.getText().toString().trim();
        mVerCode = mEtVerCode.getText().toString().trim();
        if (checkAllInput()) {
            doCheckVerCode();
        }
    }

    /**
     * 检查手机是否已注册
     */
    private void doCheckPhone() {
        if (AccountManager.getInstance().hasAuthCode()) {
            mPresenter.checkPhone(mPhone);
        } else {
            mPresenter.authorization();
        }
    }

    /**
     * 获取图形验证码
     */
    private void doGetCaptcha() {
        mPresenter.getCaptcha();
    }

    /**
     * 获取短信验证码
     */
    private void doGetVerCode() {
        mCodeTime = AppConstans.Common.REGISTER_CODE_TIME;
        mPresenter.getVerCode(mChaCode, mCaptcha.getCaptchaid(), mPhone);
    }

    /**
     * 校验短信验证码
     */
    private void doCheckVerCode() {
        mPresenter.checkVerCode(mPhone, mVerCode);
    }


    private void showCaptcha(Captcha captcha) {
        if (captcha == null || TextUtils.isEmpty(captcha.getCaptcha()) || TextUtils.isEmpty(captcha.getCaptchaid())) {
            ToastUtil.show(R.string.captcha_failure);
            return;
        }
        byte[] imageByte = Base64Utils.decode(captcha.getCaptcha());
        Drawable image = new BitmapDrawable(PhApplication.getContext().getResources(), BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length));
        mIvCaptcha.setImageDrawable(image);
    }

    /**
     * 进入设置密码页面
     */
    private void gotoRegister() {
        Intent intent = new Intent(this, AccountRegisterActivity.class);
        intent.putExtra("register_phone", mPhone);
        intent.putExtra("ver_code", mVerCode);
        startActivityForResult(intent, REQUEST_CODE_REGISTER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_REGISTER && resultCode == RESULT_OK) {
            finish();
        }
    }

    private boolean checkPhoneInput() {
        if (!RegexUtils.checkMobilePhone(mPhone)) {
            ToastUtil.show(R.string.login_user_illegal);
            return false;
        }
        return true;
    }

    private boolean checkAllInput() {
        if (!RegexUtils.checkMobilePhone(mPhone)) {
            ToastUtil.show(R.string.login_user_illegal);
            return false;
        }

        if (TextUtils.isEmpty(mVerCode) || mVerCode.length() < 6) {
            ToastUtil.show(R.string.verifiaction_code_illegal);
            return false;
        }

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCodeTimeR != null) {
            mHandler.removeCallbacks(mCodeTimeR);
        }
    }

}
