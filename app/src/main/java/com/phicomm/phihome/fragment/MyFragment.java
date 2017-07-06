package com.phicomm.phihome.fragment;

import android.view.LayoutInflater;
import android.view.View;

import com.phicomm.phihome.R;

/**
 * “我的”页
 * Created by qisheng.lv on 2017/7/5.
 */
public class MyFragment extends BaseFragment {
    @Override
    public View initView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_my, null);
    }

    @Override
    public void afterInitView() {

    }

}
