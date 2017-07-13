package com.phicomm.phihome.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.RadioButton;

import com.phicomm.phihome.R;
import com.phicomm.phihome.adapter.MainVpAdapter;
import com.phicomm.phihome.utils.AppManager;
import com.phicomm.phihome.utils.LogUtils;
import com.phicomm.phihome.utils.ToastUtil;
import com.phicomm.phihome.views.MyViewPager;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 主页，包含三个Fragment：设备、场景、我的
 * Created by qisheng.lv on 2017/7/5.
 */
public class MainActivity extends BaseFragmentActivity {
    @BindView(R.id.vp_main)
    MyViewPager mViewPager;
    @BindView(R.id.rb_device)
    RadioButton mRbDevice;
    @BindView(R.id.rb_scene)
    RadioButton mRbScene;
    @BindView(R.id.rb_mine)
    RadioButton mRbMine;

    private long mFirstTime;

    @Override
    public void initLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
    }

    @Override
    public void afterInitView() {
        mViewPager.setAdapter(new MainVpAdapter(getSupportFragmentManager()));
        startActivity(new Intent(this,SocketControlActivity.class));
    }

    @OnClick(R.id.rb_device)
    public void rb_device() {
        togglePage(0);
    }

    @OnClick(R.id.rb_scene)
    public void rb_scene() {
        togglePage(1);
    }

    @OnClick(R.id.rb_mine)
    public void rb_my() {
        togglePage(2);
    }

    /**
     * 切换Fragment
     * @param position
     */
    private void togglePage(int position) {
        mViewPager.setCurrentItem(position);

        toggleRb(getResources().getDrawable(R.drawable.ico_home), mRbDevice, getResources().getColor(R.color.text));
        toggleRb(getResources().getDrawable(R.drawable.ico_setting), mRbScene, getResources().getColor(R.color.text));
        toggleRb(getResources().getDrawable(R.drawable.ico_setting), mRbMine, getResources().getColor(R.color.text));

        if (position == 0) {
            toggleRb(getResources().getDrawable(R.drawable.ico_home_focus), mRbDevice, getResources().getColor(R.color.status_bar));
        } else if (position == 1) {
            toggleRb(getResources().getDrawable(R.drawable.ico_setting_focus), mRbScene, getResources().getColor(R.color.status_bar));
        } else {
            toggleRb(getResources().getDrawable(R.drawable.ico_setting_focus), mRbMine, getResources().getColor(R.color.status_bar));
        }

    }

    /**
     * 切换底部Tab状态
     * @param drawable
     * @param rb
     * @param color
     */
    private void toggleRb(Drawable drawable, RadioButton rb, int color) {
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        rb.setCompoundDrawables(null, drawable, null, null);
        rb.setTextColor(color);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long secondTime = System.currentTimeMillis();
            if (0.0 != mFirstTime && secondTime - mFirstTime < 2000) {
                AppManager.getAppManager().finishAllActivity();
                LogUtils.unLoad();
                System.exit(0);
            } else {
                ToastUtil.show(this, R.string.app_exit_hit);
                mFirstTime = System.currentTimeMillis();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
