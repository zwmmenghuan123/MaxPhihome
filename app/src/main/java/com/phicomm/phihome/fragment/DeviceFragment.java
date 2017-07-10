package com.phicomm.phihome.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import com.phicomm.phihome.R;
import com.phicomm.phihome.activity.ProductsActivity;

import butterknife.OnClick;

/**
 * 设备页
 * Created by qisheng.lv on 2017/7/5.
 */
public class DeviceFragment extends BaseFragment {
    @Override
    public View initView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_device, null);
    }

    @Override
    public void afterInitView() {

    }

    @OnClick(R.id.iv_add)
    public void iv_add() {
        Intent intent = new Intent(getActivity(), ProductsActivity.class);
        startActivity(intent);
    }

}
