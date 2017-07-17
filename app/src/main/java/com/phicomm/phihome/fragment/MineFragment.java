package com.phicomm.phihome.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import com.phicomm.phihome.R;
import com.phicomm.phihome.activity.LoginCloudActivity;

import butterknife.OnClick;

/**
 * “我的”页
 * Created by qisheng.lv on 2017/7/5.
 */
public class MineFragment extends BaseFragment {
    @Override
    public View initView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_mine, null);
    }

    @Override
    public void afterInitView() {

    }

    @OnClick(R.id.btn_exit)
    public void btn_exit() {
        startActivity(new Intent(getActivity(), LoginCloudActivity.class));
        getActivity().finish();
    }

}
