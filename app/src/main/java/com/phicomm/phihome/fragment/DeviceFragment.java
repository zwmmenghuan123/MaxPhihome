package com.phicomm.phihome.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.phicomm.phihome.R;
import com.phicomm.phihome.activity.ProductsActivity;
import com.phicomm.phihome.adapter.DeviceListAdapter;
import com.phicomm.phihome.bean.DeviceBean;
import com.phicomm.phihome.bean.DeviceBeanResult;
import com.phicomm.phihome.presenter.DevicesPresenter;
import com.phicomm.phihome.presenter.viewback.DevicesView;
import com.phicomm.phihome.utils.ToastUtil;
import com.phicomm.phihome.views.recyclerview.MyDecoration;
import com.phicomm.phihome.views.recyclerview.RefreshLoadListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设备页
 * Created by qisheng.lv on 2017/7/5.
 */
public class DeviceFragment extends BaseFragment {
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    DevicesPresenter mDevicesPresenter;
    DeviceListAdapter mAdapter;
    List<DeviceBean> mList;

    @Override
    public View initView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_device, null);
    }

    @Override
    public void afterInitView() {
        mList = new ArrayList<>();
        mAdapter = new DeviceListAdapter(R.layout.fragment_device_item, mList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new MyDecoration(getActivity(), MyDecoration.VERTICAL_LIST));
        mAdapter.setOnRefreshEnabled(true);
        mAdapter.enableLoadMoreEndClick(false);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ToastUtil.show(getActivity(), "点击了第" + position + "项");
            }
        });

        mAdapter.bind(mSwipeRefreshLayout, mRecyclerView, new RefreshLoadListener() {
            @Override
            public void onRefresh() {
                mDevicesPresenter.getDevices();
            }

            @Override
            public void onLoadMore() {

            }
        });


        mDevicesPresenter = new DevicesPresenter(new DevicesView() {
            @Override
            public void getDevicesSuccess(DeviceBeanResult response) {
                Log.e("=====getDevices", "onSuccess: " + response.toString());
            }

            @Override
            public void getDevicesError(String errorCode, String errorInfo) {
                super.getDevicesError(errorCode, errorInfo);
            }
        });


    }

    @OnClick(R.id.iv_add)
    public void iv_add() {
        Intent intent = new Intent(getActivity(), ProductsActivity.class);
        startActivity(intent);
    }

}
