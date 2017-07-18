package com.phicomm.phihome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.phicomm.phihome.R;
import com.phicomm.phihome.adapter.ProductsTypeAdapter;
import com.phicomm.phihome.bean.ProductTypeBean;
import com.phicomm.phihome.constants.Products;
import com.phicomm.phihome.views.recyclerview.MyDecoration;
import com.phicomm.phihome.views.recyclerview.RefreshLoadListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 产品类型列表
 * Created by xiaolei.yang on 2017/7/6.
 */
public class ProductsActivity extends BaseActivity {
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    ProductsTypeAdapter mAdapter;
    List<ProductTypeBean> mList;

    @Override
    public void initLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_products);

    }

    @Override
    public void afterInitView() {
        setPageTitle(R.string.add_device);

        mList = new ArrayList<>();
        mAdapter = new ProductsTypeAdapter(mList);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new MyDecoration(this, MyDecoration.VERTICAL_LIST));
        mAdapter.setOnRefreshEnabled(true);
        mAdapter.setEnableLoadMore(false);



        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(ProductsActivity.this, SoftApResetActivity.class);
                intent.putExtra("type", mList.get(position).getProductType());
                intent.putExtra("ssid", mList.get(position).getProductTypeSsid());
                startActivity(intent);
            }
        });

        mAdapter.bind(mSwipeRefreshLayout, mRecyclerView, new RefreshLoadListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getData();
                        mAdapter.setNewData(mList);
                        mAdapter.setRefreshing(false);
                        mAdapter.setEnableLoadMore(false);
                    }
                }, 1500);
            }

            @Override
            public void onLoadMore() {

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

    private void getData() {
        if (mList.size() == 0) {
            ProductTypeBean productTypeBean = new ProductTypeBean();
            productTypeBean.setProductType(Products.TYPE_INSERTS);
            productTypeBean.setProductTypeName("X1智能插排");
            productTypeBean.setProductTypeSsid("EasyLink_500C49");
            mList.add(productTypeBean);
        }
    }

}
