package com.phicomm.phihome.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.phicomm.phihome.R;
import com.phicomm.phihome.bean.AccountDetailsBean;
import com.phicomm.phihome.event.ChangeNicknameEvent;
import com.phicomm.phihome.presenter.UserInfoPresenter;
import com.phicomm.phihome.presenter.viewback.UserInfoView;
import com.phicomm.phihome.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 更换昵称
 * Created by xiaolei.yang on 2017/7/24.
 */

public class ChangeNicknameActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.et_nickname)
    EditText mEtNickname;
    @BindView(R.id.tv_save)
    TextView mTvSave;

    UserInfoPresenter mUserInfoPresenter;
    private String mNickname;

    @Override
    public void initLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_change_nickname);
    }

    @Override
    public void afterInitView() {
        mTvTitle.setText(R.string.nickname);
        mNickname = getIntent().getStringExtra("nickname");

        if (!TextUtils.isEmpty(mNickname)) {
            mEtNickname.setText(mNickname);
            mEtNickname.setSelection(mNickname.length());
        }

        mUserInfoPresenter = new UserInfoPresenter(new UserInfoView() {
            @Override
            public void propertySuccess() {
                EventBus.getDefault().post(new ChangeNicknameEvent(mEtNickname.getText().toString()));
                finish();
            }

            @Override
            public void propertyError(String code, String msg) {
                ToastUtil.show(ChangeNicknameActivity.this, TextUtils.isEmpty(msg) ? "" : msg);
            }
        });

    }

    @OnClick(R.id.tv_save)
    public void tv_save() {
        if (TextUtils.isEmpty(mEtNickname.getText()) || TextUtils.isEmpty(mEtNickname.getText().toString())) {
            ToastUtil.show(this, "昵称不可为空");
            return;
        }
        if (mEtNickname.getText().toString().equals(mNickname)) {
            ToastUtil.show(this, "昵称与原昵称相同");
            return;
        }
        AccountDetailsBean bean = new AccountDetailsBean();
        bean.setNickname(mEtNickname.getText().toString());
        mUserInfoPresenter.property(bean);
    }
}
