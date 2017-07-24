package com.phicomm.phihome.presenter;

import android.util.Log;

import com.phicomm.phihome.bean.AccountBean;
import com.phicomm.phihome.bean.AccountDetailsBean;
import com.phicomm.phihome.bean.UploadBaseBean;
import com.phicomm.phihome.model.UserInfoModel;
import com.phicomm.phihome.net.callback.BeanCallback;
import com.phicomm.phihome.presenter.viewback.UserInfoView;

/**
 * 用户信息
 * Created by xiaolei.yang on 2017/7/24.
 */

public class UserInfoPresenter {
    private UserInfoModel mUserInfoModel;
    private UserInfoView mUploadBaseView;

    public UserInfoPresenter(UserInfoView uploadBaseView) {
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

    public void accountDetail() {
        mUserInfoModel.accountDetail(new BeanCallback<AccountBean>() {


            @Override
            public void onSuccess(AccountBean accountBean) {
                if (accountBean != null) {
                    AccountDetailsBean accountDetailsBean = accountBean.getData();
                    if (accountDetailsBean != null) {
                      if (mUploadBaseView!=null){
                          mUploadBaseView.accountDetailSuccess(accountDetailsBean);
                      }
                    } else {
                        onError("0", null);
                    }
                } else {
                    onError("0", null);
                }
            }

            @Override
            public void onError(String code, String msg) {
                Log.e("=======", "onError: " + code + "===" + msg);
            }
        });
    }


}
