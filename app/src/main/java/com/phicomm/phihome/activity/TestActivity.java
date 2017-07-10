package com.phicomm.phihome.activity;

import android.os.Bundle;

import com.phicomm.phihome.R;
import com.phicomm.phihome.utils.ToastUtil;

import butterknife.OnClick;

public class TestActivity extends BaseFragmentActivity {

    @Override
    public void initLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_test);
    }

    @Override
    public void afterInitView() {

    }


    @OnClick(R.id.btn_test)
    public void btn_test() {
        ToastUtil.show("btn_test");
        int a = 3 / 0;
    }

}
