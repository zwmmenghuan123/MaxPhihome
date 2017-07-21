package com.phicomm.phihome.popup;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;

import com.phicomm.phihome.R;
import com.phicomm.phihome.listener.GetPhotoListener;

/**
 * 拍照、相册取图的弹窗
 * Created by xiaolei.yang on 2017/7/19.
 */

public class GetPhotoPopup {


    private Activity mContext;
    private PopupWindow popupWindow;

    public GetPhotoPopup(Activity context, final GetPhotoListener listener) {
        mContext = context;


        View photoView = LayoutInflater.from(context).inflate(R.layout.popup_get_photo, null);
        TextView mTvTakePhoto = (TextView) photoView.findViewById(R.id.tv_take_photo);
        TextView mTvGetPhotoFromAlbum = (TextView) photoView.findViewById(R.id.tv_get_photo_from_album);
        TextView mTvCancel = (TextView) photoView.findViewById(R.id.tv_cancel);
        popupWindow = new PopupWindow(photoView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x000000));


        mTvTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (listener != null) {
                    listener.getPhotoFromCamera();
                }
            }
        });
        mTvGetPhotoFromAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (listener != null) {
                    listener.getPhotoFromAlbum();
                }
            }
        });
        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        popupWindow.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });

    }


    public void showAsDropDown(View parent) {
        backgroundAlpha(0.7f);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        // 使其聚集
        popupWindow.setFocusable(true);
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);
        // 设置动画
        popupWindow.setAnimationStyle(R.style.PopupBottomAnimation);
        // 刷新状态
        popupWindow.update();
    }

    // 隐藏菜单
    public void dismiss() {
        popupWindow.dismiss();
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha 透明度
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
        lp.alpha = bgAlpha;// 0.0~1.0
        mContext.getWindow().setAttributes(lp);
    }

}