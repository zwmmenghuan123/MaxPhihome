package com.phicomm.phihome.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.phicomm.phihome.R;
import com.phicomm.phihome.utils.LogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Activity基类
 * Created by qisheng.lv on 2017/4/12.
 */
public abstract class BaseActivity extends AppCompatActivity {
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_menu)
    TextView mTvMenu;
    @BindView(R.id.iv_menu)
    ImageView mIvMenu;
    @BindView(R.id.iv_back)
    ImageView mIvBack;

//    private LoadingDialog mLoadingDialog;
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

    @OnClick(R.id.iv_back)
    public void iv_back() {
        onGoback();
    }

    public void onGoback() {
        finish();
    }

    public void hideBack() {
        try {
            mIvBack.setVisibility(View.INVISIBLE);
        } catch (Exception e) {
            LogUtils.error(e);
        }
    }

    public void setPageTitle(String title) {
        try {
            mTvTitle.setText(title);
        } catch (Exception e) {
            LogUtils.debug(e);
        }
    }
    public void setPageTitle(int titleResId) {
        String title = this.getResources().getString(titleResId);
        try {
            mTvTitle.setText(title);
        } catch (Exception e) {
            LogUtils.debug(e);
        }
    }

    public void showTvMenu(String menu) {
        try {
            mTvMenu.setText(menu);
        } catch (Exception e) {
            LogUtils.debug(e);
        }
    }


    @OnClick(R.id.tv_menu)
    public void tv_menu(TextView view) {
        onTvMenuClick(view);
    }

    public void onTvMenuClick(TextView view) {

    }

    public void showIvMenu(int ico) {
        try {
            mIvMenu.setVisibility(View.VISIBLE);
            mIvMenu.setImageResource(ico);
        } catch (Exception e) {
            LogUtils.debug(e);
        }
    }

    @OnClick(R.id.iv_menu)
    public void iv_menu(ImageView view) {
        onIvMenuClick(view);
    }

    public void onIvMenuClick(ImageView view) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    /**
     * showLoading dialog with no text message.
     */
//    protected void showLoading() {
//        showLoading(-1);
//    }

    /**
     * Show showLoading dialog with given tipid which used for showLoading message.
     *
     * @param tipid showLoading dialog message
     */
//    protected void showLoading(int tipid) {
//        if (mLoadingDialog == null) {
//            mLoadingDialog = new LoadingDialog(this);
//        }
//        mLoadingDialog.setMessage(tipid);
//        if (!mLoadingDialog.isShowing()) {
//            mLoadingDialog.show();
//        }
//    }
//
//    public void hideLoading() {
//        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
//            mLoadingDialog.dismiss();
//        }
//    }

//    @Override
//    public void onTimeout(String result) {
//
//    }


}
