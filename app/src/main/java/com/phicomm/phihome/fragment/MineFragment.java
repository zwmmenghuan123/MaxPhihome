package com.phicomm.phihome.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.phicomm.phihome.R;
import com.phicomm.phihome.activity.PersonalInformationActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * “我的”页
 * Created by qisheng.lv on 2017/7/5.
 */
public class MineFragment extends BaseFragment {
    @BindView(R.id.tv_title)
    TextView mTvTitle;


    @Override
    public View initView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_mine, null);
    }

    @Override
    public void afterInitView() {
        mTvTitle.setText(R.string.me);
    }



    @OnClick(R.id.ll_nickname)
    public void ll_nickname() {
        Intent intent = new Intent(getActivity(), PersonalInformationActivity.class);
        startActivity(intent);
    }

}
