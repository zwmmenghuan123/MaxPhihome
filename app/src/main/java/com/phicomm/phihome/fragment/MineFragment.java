package com.phicomm.phihome.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.phicomm.phihome.R;
import com.phicomm.phihome.activity.PersonalInformationActivity;
import com.phicomm.phihome.bean.AccountDetailsBean;
import com.phicomm.phihome.event.ChangeNicknameEvent;
import com.phicomm.phihome.event.UploadHeadPortraitEvent;
import com.phicomm.phihome.manager.imageloader.ImageLoader;
import com.phicomm.phihome.presenter.UserInfoPresenter;
import com.phicomm.phihome.presenter.viewback.UserInfoView;
import com.phicomm.phihome.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * “我的”页
 * Created by qisheng.lv on 2017/7/5.
 */
public class MineFragment extends BaseFragment {
    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.iv_head_portrait)
    ImageView mIvHeadPortrait;
    @BindView(R.id.tv_nickname)
    TextView mTvNickname;

    UserInfoPresenter mUploadBasePresenter;

    AccountDetailsBean mAccountDetailsBean;

    @Override
    public View initView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_mine, null);
    }

    @Override
    public void afterInitView() {
        EventBus.getDefault().register(this);
        mTvTitle.setText(R.string.me);

        mUploadBasePresenter = new UserInfoPresenter(new UserInfoView() {
            @Override
            public void accountDetailSuccess(AccountDetailsBean accountDetailsBean) {
                if (accountDetailsBean != null) {
                    mAccountDetailsBean = accountDetailsBean;
                    ImageLoader.getLoader(getActivity()).load(accountDetailsBean.getImg()).into(mIvHeadPortrait);
                    mTvNickname.setText(TextUtils.isEmpty(accountDetailsBean.getNickname()) ? "" : accountDetailsBean.getNickname());
                } else {
                    accountDetailError("0", null);
                }
            }

            @Override
            public void accountDetailError(String code, String msg) {
                ToastUtil.show(getActivity(), TextUtils.isEmpty(msg) ? "获取个人信息失败，请稍后再试" : msg);
            }
        });

        mUploadBasePresenter.accountDetail();

    }


    @Override
    public void onDestroyView() {
        EventBus.getDefault().register(this);
        super.onDestroyView();
    }

    @Subscribe
    public void onEventMainThread(UploadHeadPortraitEvent event) {
        if (mAccountDetailsBean != null) {
            mAccountDetailsBean.setImg(event.getUrl());
            ImageLoader.getLoader(getActivity()).load(mAccountDetailsBean.getImg()).into(mIvHeadPortrait);
        }
    }

    @Subscribe
    public void onEventMainThread(ChangeNicknameEvent event) {
        if (mAccountDetailsBean != null) {
            mAccountDetailsBean.setNickname(event.getNickname());
            mTvNickname.setText(TextUtils.isEmpty(mAccountDetailsBean.getNickname()) ? "" : mAccountDetailsBean.getNickname());
        }
    }

    @OnClick(R.id.ll_nickname)
    public void ll_nickname() {
        Intent intent = new Intent(getActivity(), PersonalInformationActivity.class);
        intent.putExtra("account_details_bean", mAccountDetailsBean);
        startActivity(intent);
    }

}
