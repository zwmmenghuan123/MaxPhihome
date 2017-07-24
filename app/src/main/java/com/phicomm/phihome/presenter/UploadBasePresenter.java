package com.phicomm.phihome.presenter;

import com.phicomm.phihome.bean.UploadBaseBean;
import com.phicomm.phihome.model.UserInfoModel;
import com.phicomm.phihome.net.callback.BeanCallback;
import com.phicomm.phihome.presenter.viewback.UploadBaseView;

/**
 * 上传Base64字符串
 * Created by xiaolei.yang on 2017/7/24.
 */

public class UploadBasePresenter {
    private UserInfoModel mUserInfoModel;
    private UploadBaseView mUploadBaseView;

    public UploadBasePresenter(UploadBaseView uploadBaseView) {
        mUploadBaseView = uploadBaseView;
        mUserInfoModel = new UserInfoModel();
    }

    public void uploadBase64(String imgBase64, String type) {
        mUserInfoModel.uploadBase64(imgBase64, type, new BeanCallback<UploadBaseBean>() {
            @Override
            public void onSuccess(UploadBaseBean uploadBaseBean) {
                if (mUploadBaseView != null) {
                    mUploadBaseView.uploadBaseSuccess(uploadBaseBean);
                }
            }

            @Override
            public void onError(String code, String msg) {
                if (mUploadBaseView != null) {
                    mUploadBaseView.uploadBaseError(code, msg);
                }
            }
        });
    }

    public void avatarUrl() {
        mUserInfoModel.avatarUrl(new BeanCallback<UploadBaseBean>() {
            @Override
            public void onSuccess(UploadBaseBean uploadBaseBean) {
                if (mUploadBaseView != null) {
                    mUploadBaseView.avatarUrlSuccess(uploadBaseBean);
                }
            }

            @Override
            public void onError(String code, String msg) {
                if (mUploadBaseView != null) {
                    mUploadBaseView.avatarUrlError(code, msg);
                }
            }
        });
    }


}
