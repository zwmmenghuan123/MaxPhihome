package com.phicomm.phihome.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.phicomm.phihome.R;
import com.phicomm.phihome.bean.FxResponse;
import com.phicomm.phihome.presenter.UserInfoPresenter;
import com.phicomm.phihome.presenter.viewback.UserInfoView;
import com.phicomm.phihome.utils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 修改密码界面
 * Created by xiaolei.yang on 2017/7/24.
 */

public class ModifyPasswordActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.et_old_password)
    EditText mEtOldPassword;
    @BindView(R.id.et_new_password)
    EditText mEtNewPassword;

    UserInfoPresenter mUserInfoPresenter;

    @Override
    public void initLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_modify_password);
    }

    @Override
    public void afterInitView() {
        mTvTitle.setText(R.string.modify_password);
        mUserInfoPresenter = new UserInfoPresenter(new UserInfoView() {
            @Override
            public void modifyPasswordSuccess(FxResponse fxResponse) {
                finish();
            }

            @Override
            public void modifyPasswordError(String code, String msg) {
                ToastUtil.show(ModifyPasswordActivity.this, TextUtils.isEmpty(msg) ? "修改密码失败" : msg);
            }
        });

    }

    @OnClick(R.id.tv_save)
    public void tv_save() {
        if (TextUtils.isEmpty(mEtOldPassword.getText()) || TextUtils.isEmpty(mEtOldPassword.getText().toString())) {
            ToastUtil.show(ModifyPasswordActivity.this, "请输入旧密码");
            return;
        }
        if (TextUtils.isEmpty(mEtNewPassword.getText()) || TextUtils.isEmpty(mEtNewPassword.getText().toString())) {
            ToastUtil.show(ModifyPasswordActivity.this, "请输入新密码");
            return;
        }
        if (mEtNewPassword.getText().toString().length() < 6 || mEtNewPassword.getText().toString().length() > 20) {
            ToastUtil.show(ModifyPasswordActivity.this, "请输入0~20位的新密码");
            return;
        }

        mUserInfoPresenter.password(mEtOldPassword.getText().toString(), mEtNewPassword.getText().toString());
    }

}
