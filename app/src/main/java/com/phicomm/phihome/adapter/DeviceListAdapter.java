package com.phicomm.phihome.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseViewHolder;
import com.phicomm.phihome.R;
import com.phicomm.phihome.bean.DeviceBean;
import com.phicomm.phihome.views.recyclerview.BasePullUpDownAdapter;

import java.util.List;

/**
 * 账户已绑定的设备列表
 * Created by xiaolei.yang on 2017/7/13.
 */

public class DeviceListAdapter extends BasePullUpDownAdapter<DeviceBean, BaseViewHolder> {
    public DeviceListAdapter(@Nullable List<DeviceBean> data) {
        super(R.layout.fragment_device_item, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, DeviceBean item) {
        holder.setText(R.id.tv_device_id, item == null ? "" : item.getDevice_id());
    }


}
