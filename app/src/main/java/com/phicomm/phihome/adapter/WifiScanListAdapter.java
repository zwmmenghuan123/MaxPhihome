package com.phicomm.phihome.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseViewHolder;
import com.phicomm.phihome.R;
import com.phicomm.phihome.views.recyclerview.BasePullUpDownAdapter;

import java.util.List;

/**
 * 设备周围ssid的列表
 * Created by xiaolei.yang on 2017/7/6.
 */

public class WifiScanListAdapter extends BasePullUpDownAdapter<String, BaseViewHolder> {

    public WifiScanListAdapter(@Nullable List<String> data) {
        super(R.layout.activity_wifi_scan_list_item, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, String item) {
        holder.setText(R.id.tv_ssid, item == null ? "" : item);
    }


}
