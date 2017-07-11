package com.phicomm.phihome.activity;

import android.os.Bundle;
import android.widget.EditText;

import com.phicomm.phihome.R;

import butterknife.BindView;

/**
 * 斐讯云账号注册页
 * Created by qisheng.lv on 2017/4/12.
 */
public class AccountRegisterActivity extends BaseActivity {
    @BindView(R.id.et_pwd)
    EditText mEtPwd;
    @BindView(R.id.et_pwd_comfirm)
    EditText mEtPwdComfirm;

    @Override
    public void initLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register);
    }

    @Override
    public void afterInitView() {
        setPageTitle(R.string.password_set);
    }

}
