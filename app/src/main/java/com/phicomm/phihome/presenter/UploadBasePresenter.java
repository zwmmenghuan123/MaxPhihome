package com.phicomm.phihome.presenter;

import com.phicomm.phihome.bean.UploadBaseBean;
import com.phicomm.phihome.model.UploadBaseModel;
import com.phicomm.phihome.net.callback.BeanCallback;
import com.phicomm.phihome.presenter.viewback.UploadBaseView;

/**
 * 上传Base64字符串
 * Created by xiaolei.yang on 2017/7/24.
 */

public class UploadBasePresenter {
    UploadBaseModel mUploadBaseModel;
    UploadBaseView mUploadBaseView;

    public UploadBasePresenter(UploadBaseView uploadBaseView) {
        mUploadBaseView = uploadBaseView;
        mUploadBaseModel = new UploadBaseModel();
    }

    public void uploadBase64(String imgBase64, String type) {
        mUploadBaseModel.uploadBase64(imgBase64, type, new BeanCallback<UploadBaseBean>() {
            @Override
            public void onSuccess(UploadBaseBean uploadBaseBean) {
                if (mUploadBaseView!=null){
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


}
