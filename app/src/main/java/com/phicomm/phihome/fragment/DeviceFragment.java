package com.phicomm.phihome.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.phicomm.phihome.R;
import com.phicomm.phihome.activity.ProductsActivity;
import com.phicomm.phihome.activity.SocketControlActivity;
import com.phicomm.phihome.adapter.DeviceListAdapter;
import com.phicomm.phihome.bean.DeviceBean;
import com.phicomm.phihome.presenter.DevicesPresenter;
import com.phicomm.phihome.presenter.viewback.DevicesView;
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

    private View emptyView;
    private View errorView;


    @Override
    public View initView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_device, null);
    }

    @Override
    public void afterInitView() {

        emptyView = getActivity().getLayoutInflater().inflate(R.layout.empty_view, (ViewGroup) mRecyclerView.getParent(), false);
        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.setRefreshing(true);
                mAdapter.getRefreshListener().onRefresh();
            }
        });

        errorView = getActivity().getLayoutInflater().inflate(R.layout.error_view, (ViewGroup) mRecyclerView.getParent(), false);
        errorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.setRefreshing(true);
                mAdapter.getRefreshListener().onRefresh();
            }
        });


        mList = new ArrayList<>();
        mAdapter = new DeviceListAdapter(mList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new MyDecoration(getActivity(), MyDecoration.VERTICAL_LIST));
        mAdapter.setOnRefreshEnabled(true);
        mAdapter.setEnableLoadMore(false);
        mAdapter.enableLoadMoreEndClick(false);


        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), SocketControlActivity.class);
                intent.putExtra("device_id", mList.get(position).getDevice_id());
                startActivity(intent);
            }
        });

        mAdapter.bind(mSwipeRefreshLayout, mRecyclerView, new RefreshLoadListener() {
            @Override
            public void onRefresh() {
                mDevicesPresenter.getDevices(true);
            }

            @Override
            public void onLoadMore() {
                mDevicesPresenter.getDevices(false);
            }
        });


        mDevicesPresenter = new DevicesPresenter(new DevicesView() {
            @Override
            public void getDevicesSuccess(List<DeviceBean> devices) {
                mList.clear();
                mAdapter.setRefreshing(false);
                if (devices != null) {
                    if (devices.size() != 0) {
                        mList.addAll(devices);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        mAdapter.setNewData(null);
                        mAdapter.setEmptyView(emptyView);
                    }

                } else {
                    mAdapter.setNewData(null);
                    mAdapter.setEmptyView(errorView);
                }
            }

            @Override
            public void getDevicesError(String errorCode, String errorInfo) {
                mAdapter.setRefreshing(false);
                mAdapter.setNewData(null);
                mAdapter.setEmptyView(errorView);
            }
        });


        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                mAdapter.setRefreshing(true);
                mAdapter.getRefreshListener().onRefresh();
            }
        });

    }

    @OnClick(R.id.iv_add)
    public void iv_add() {
        Intent intent = new Intent(getActivity(), ProductsActivity.class);
        startActivity(intent);
    }

}
