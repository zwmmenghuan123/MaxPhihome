package com.phicomm.phihome.presenter;

import com.alibaba.fastjson.JSON;
import com.phicomm.phihome.bean.AccountBean;
import com.phicomm.phihome.bean.AccountDetailsBean;
import com.phicomm.phihome.bean.FxResponse;
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
    private UserInfoView mUserInfoView;

    public UserInfoPresenter(UserInfoView userInfoView) {
        mUserInfoView = userInfoView;
        mUserInfoModel = new UserInfoModel();
    }

    public void uploadBase64(String imgBase64, String type) {
        mUserInfoModel.uploadBase64(imgBase64, type, new BeanCallback<UploadBaseBean>() {
            @Override
            public void onSuccess(UploadBaseBean uploadBaseBean) {
                if (mUserInfoView != null) {
                    mUserInfoView.uploadBaseSuccess(uploadBaseBean);
                }
            }

            @Override
            public void onError(String code, String msg) {
                if (mUserInfoView != null) {
                    mUserInfoView.uploadBaseError(code, msg);
                }
            }
        });
    }

    public void avatarUrl() {
        mUserInfoModel.avatarUrl(new BeanCallback<UploadBaseBean>() {
            @Override
            public void onSuccess(UploadBaseBean uploadBaseBean) {
                if (mUserInfoView != null) {
                    mUserInfoView.avatarUrlSuccess(uploadBaseBean);
                }
            }

            @Override
            public void onError(String code, String msg) {
                if (mUserInfoView != null) {
                    mUserInfoView.avatarUrlError(code, msg);
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
                        if (mUserInfoView != null) {
                            mUserInfoView.accountDetailSuccess(accountDetailsBean);
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
                if (mUserInfoView != null) {
                    mUserInfoView.accountDetailError(code, msg);
                }
            }
        });
    }

    public void property(AccountDetailsBean accountDetailsBean) {
        mUserInfoModel.property(JSON.toJSONString(accountDetailsBean), new BeanCallback<FxResponse>() {
            @Override
            public void onSuccess(FxResponse fxResponse) {
                if (mUserInfoView != null) {
                    mUserInfoView.propertySuccess();
                }
            }

            @Override
            public void onError(String code, String msg) {
                if (mUserInfoView != null) {
                    mUserInfoView.propertyError(code, msg);
                }
            }
        });
    }


}
