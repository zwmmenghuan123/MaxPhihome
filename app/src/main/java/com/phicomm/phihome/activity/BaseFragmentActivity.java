package com.phicomm.phihome.activity;


import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * FragmentActivity基类
 * Created by qisheng.lv on 2017/4/12.
 */
public abstract class BaseFragmentActivity extends FragmentActivity {

    private Unbinder mUnbinder;

    public abstract void initLayout(Bundle savedInstanceState);

    public abstract void afterInitView();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置只能竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initLayout(savedInstanceState);

        try {
            mUnbinder = ButterKnife.bind(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        afterInitView();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        DataStatisticsManager.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        DataStatisticsManager.onResume(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }


}
