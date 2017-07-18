package com.phicomm.phihome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.phicomm.phihome.R;
import com.phicomm.phihome.adapter.WifiScanListAdapter;
import com.phicomm.phihome.views.recyclerview.MyDecoration;

import java.util.ArrayList;

import butterknife.BindView;

public class WifiScanListActivity extends BaseActivity {
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;


    ArrayList<String> mList;
    WifiScanListAdapter mAdapter;

    @Override
    public void initLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_wifi_scan_list);
    }

    @Override
    public void afterInitView() {
        mList = new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new MyDecoration(this, MyDecoration.VERTICAL_LIST));
        mAdapter = new WifiScanListAdapter(mList);
        mAdapter.setOnRefreshEnabled(false);
        mAdapter.enableLoadMoreEndClick(false);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                String ssid = "";
                if (position < mList.size()) {
                    ssid = mList.get(position);
                }
                intent.putExtra("ssid", ssid);
                setResult(RESULT_OK, intent);
                WifiScanListActivity.this.finish();
            }
        });
        mRecyclerView.setAdapter(mAdapter);

        ArrayList<String> tempList = getIntent().getStringArrayListExtra("wifi_scan_list");
        if (tempList != null && tempList.size() > 0) {
            mList.addAll(tempList);
            mAdapter.notifyDataSetChanged();
        }

    }


}
