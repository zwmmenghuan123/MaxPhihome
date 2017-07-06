package com.phicomm.phihome.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Fragment基类
 * Created by qisheng.lv on 2017/4/12.
 */
public abstract class BaseFragment extends Fragment {
    private Unbinder unbinder;

    protected View view;

    /**
     * 在实例化布局之前处理的逻辑
     */
    public void beforeInitView() {

    }

    /**
     * 实例化布局文件/组件
     *
     * @param inflater
     * @return
     */
    public abstract View initView(LayoutInflater inflater);

    /**
     * 在实例化布局之后处理的逻辑
     */
    public void afterInitView() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        beforeInitView();
        view = initView(inflater);
        //初始化ButterKnife
        unbinder = ButterKnife.bind(this, view);
        afterInitView();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

}
