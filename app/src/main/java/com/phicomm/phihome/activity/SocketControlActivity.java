package com.phicomm.phihome.activity;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.phicomm.phihome.R;
import com.phicomm.phihome.utils.LogUtils;

import butterknife.BindView;
import butterknife.OnCheckedChanged;

public class SocketControlActivity extends BaseActivity {
    @BindView(R.id.switch_socket)
    Switch mSwitchSocket;

    @Override
    public void initLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_socket_control);
    }

    @Override
    public void afterInitView() {
        setPageTitle(R.string.page_socket_control);
    }

    @OnCheckedChanged(R.id.switch_socket)
    public void switch_socket(CompoundButton buttonView, boolean isChecked) {
        LogUtils.debug("switch_socket: " + isChecked);
        buttonView.setChecked(!isChecked);
        if (isChecked) {
            //打开

        } else {
            //关闭

        }
    }


}
