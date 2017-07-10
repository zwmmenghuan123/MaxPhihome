package com.phicomm.phihome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.phicomm.phihome.R;
import com.phicomm.phihome.constants.Products;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 产品类型列表
 * Created by xiaolei.yang on 2017/7/6.
 */
public class ProductsActivity extends BaseActivity {
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Override
    public void initLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_products);

    }

    @Override
    public void afterInitView() {
        setPageTitle(R.string.add_device);

    }


    @OnClick(R.id.tv_hint)
    public void tv_hint() {
        Intent intent = new Intent(this, SoftApResetActivity.class);
        intent.putExtra("type", Products.TYPE_INSERTS);
        startActivity(intent);
    }
}
