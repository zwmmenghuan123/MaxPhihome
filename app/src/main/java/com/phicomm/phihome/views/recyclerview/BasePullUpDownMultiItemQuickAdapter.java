package com.phicomm.phihome.views.recyclerview;

import android.support.annotation.LayoutRes;
import android.util.SparseIntArray;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * 多布局列表的适配器基类。
 * Created by xiaolei.yang on 2017/5/19.
 */

public abstract class BasePullUpDownMultiItemQuickAdapter<T, K extends BaseViewHolder> extends BasePullUpDownAdapter<T, K> {

    /**
     * layouts indexed with their types
     */
    private SparseIntArray layouts;

    private static final int DEFAULT_VIEW_TYPE = -0xff;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public BasePullUpDownMultiItemQuickAdapter(List<T> data) {
        super(data);
    }

    @Override
    protected int getDefItemViewType(int position) {
        Object item = mData.get(position);
        if (item instanceof MultiItemEntity) {
            return ((MultiItemEntity) item).getItemType();
        }
        return DEFAULT_VIEW_TYPE;
    }

    protected void setDefaultViewTypeLayout(@LayoutRes int layoutResId) {
        addItemType(DEFAULT_VIEW_TYPE, layoutResId);
    }

    @Override
    protected K onCreateDefViewHolder(ViewGroup parent, int viewType) {
        return createBaseViewHolder(parent, getLayoutId(viewType));
    }

    private int getLayoutId(int viewType) {
        return layouts.get(viewType);
    }

    protected void addItemType(int type, @LayoutRes int layoutResId) {
        if (layouts == null) {
            layouts = new SparseIntArray();
        }
        layouts.put(type, layoutResId);
    }

}
