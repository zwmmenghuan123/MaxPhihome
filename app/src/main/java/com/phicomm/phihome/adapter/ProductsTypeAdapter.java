package com.phicomm.phihome.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseViewHolder;
import com.phicomm.phihome.R;
import com.phicomm.phihome.bean.ProductTypeBean;
import com.phicomm.phihome.constants.AppConstans;
import com.phicomm.phihome.views.recyclerview.BasePullUpDownAdapter;

import java.util.List;

/**
 * 添加设备时选择设备类型的列表
 * Created by xiaolei.yang on 2017/7/18.
 */

public class ProductsTypeAdapter extends BasePullUpDownAdapter<ProductTypeBean, BaseViewHolder> {

    public ProductsTypeAdapter(@Nullable List<ProductTypeBean> data) {
        super(R.layout.activity_products_item,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProductTypeBean item) {
        helper.setText(R.id.tv_product_type, item.getProductTypeName() == null ? "" : item.getProductTypeName());
        switch (item.getProductType()) {
            case AppConstans.Products.TYPE_INSERTS:
                helper.setImageResource(R.id.iv_product_type, R.mipmap.ic_launcher);
                break;
            default:
                break;
        }

    }
}
