package com.phicomm.phihome.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.phicomm.phihome.R;
import com.phicomm.phihome.bean.AccountDetailsBean;
import com.phicomm.phihome.bean.UploadBaseBean;
import com.phicomm.phihome.constants.AppConstans;
import com.phicomm.phihome.event.ChangeNicknameEvent;
import com.phicomm.phihome.event.UploadHeadPortraitEvent;
import com.phicomm.phihome.listener.GetPhotoBeforeListener;
import com.phicomm.phihome.manager.imageloader.ImageLoader;
import com.phicomm.phihome.popup.GetPhotoPopup;
import com.phicomm.phihome.presenter.UserInfoPresenter;
import com.phicomm.phihome.presenter.viewback.UserInfoView;
import com.phicomm.phihome.utils.Base64Utils;
import com.phicomm.phihome.utils.LogUtils;
import com.phicomm.phihome.utils.PathUtils;
import com.phicomm.phihome.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 个人信息界面
 * Created by xiaolei.yang on 2017/7/19.
 */
public class PersonalInformationActivity extends BaseActivity implements GetPhotoBeforeListener {
    @BindView(R.id.rl_head_portrait)
    RelativeLayout mRlHeadPortrait;

    @BindView(R.id.iv_head_portrait)
    ImageView mIvHeadPortrait;
    @BindView(R.id.tv_nickname)
    TextView mTvNickname;
    @BindView(R.id.tv_mobile)
    TextView mTvMobile;

    UserInfoPresenter mUploadBasePresenter;

    AccountDetailsBean mAccountDetailsBean;

    @Override
    public void initLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_personal_information);
    }

    @Override
    public void afterInitView() {
        EventBus.getDefault().register(this);
        setPageTitle("个人信息");
        mUploadBasePresenter = new UserInfoPresenter(new UserInfoView() {
            @Override
            public void uploadBaseSuccess(UploadBaseBean uploadBaseBean) {
                if (uploadBaseBean == null) {
                    uploadBaseError("0", null);
                } else {
                    String url = uploadBaseBean.getUrl();
                    LogUtils.error("=======", "url:" + url);
                    EventBus.getDefault().post(new UploadHeadPortraitEvent(url));
                }
            }

            @Override
            public void uploadBaseError(String code, String msg) {
                ToastUtil.show(PersonalInformationActivity.this, TextUtils.isEmpty(msg) ? "上传图片失败，请稍后再试。" : msg);
            }

            @Override
            public void avatarUrlSuccess(UploadBaseBean uploadBaseBean) {
                if (uploadBaseBean == null) {
                    avatarUrlError("0", null);
                } else {
                    String avatarUrl = uploadBaseBean.getAvatarUrl();
                    ImageLoader.getLoader(PersonalInformationActivity.this).load(avatarUrl).into(mIvHeadPortrait);
                }
            }

            @Override
            public void avatarUrlError(String code, String msg) {
                ToastUtil.show(PersonalInformationActivity.this, TextUtils.isEmpty(msg) ? "获取头像失败，请稍后再试。" : msg);
            }

            @Override
            public void accountDetailSuccess(AccountDetailsBean accountDetailsBean) {
                if (accountDetailsBean != null) {
                    mAccountDetailsBean = accountDetailsBean;
                    ImageLoader.getLoader(PersonalInformationActivity.this).load(accountDetailsBean.getImg()).into(mIvHeadPortrait);
                    mTvNickname.setText(TextUtils.isEmpty(accountDetailsBean.getNickname()) ? "" : accountDetailsBean.getNickname());
                    mTvMobile.setText(TextUtils.isEmpty(accountDetailsBean.getPhonenumber()) ? "" : accountDetailsBean.getPhonenumber());

                } else {
                    accountDetailError("0", null);
                }
            }

            @Override
            public void accountDetailError(String code, String msg) {
                ToastUtil.show(PersonalInformationActivity.this, TextUtils.isEmpty(msg) ? "获取个人信息失败，请稍后再试" : msg);
            }
        });

//        mUploadBasePresenter.avatarUrl();//单独获取头像url的接口，可以使用获取账号的接口，不用这个
        mAccountDetailsBean = (AccountDetailsBean) getIntent().getSerializableExtra("account_details_bean");
        if (mAccountDetailsBean == null) {
            mUploadBasePresenter.accountDetail();
        } else {
            ImageLoader.getLoader(PersonalInformationActivity.this).load(mAccountDetailsBean.getImg()).into(mIvHeadPortrait);
            mTvNickname.setText(TextUtils.isEmpty(mAccountDetailsBean.getNickname()) ? "" : mAccountDetailsBean.getNickname());
            mTvMobile.setText(TextUtils.isEmpty(mAccountDetailsBean.getPhonenumber()) ? "" : mAccountDetailsBean.getPhonenumber());

        }

    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe
    public void onEventMainThread(ChangeNicknameEvent event) {
        if (mAccountDetailsBean != null) {
            mAccountDetailsBean.setNickname(event.getNickname());
            mTvNickname.setText(TextUtils.isEmpty(mAccountDetailsBean.getNickname()) ? "" : mAccountDetailsBean.getNickname());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @OnClick(R.id.rl_head_portrait)
    public void rl_head_portrait() {
        GetPhotoPopup photoPopup = new GetPhotoPopup(this, this);
        photoPopup.showAsDropDown(mRlHeadPortrait);
    }

    @OnClick(R.id.rl_nickname)
    public void tv_nickname() {
        Intent intent = new Intent(this, ChangeNicknameActivity.class);
        if (mAccountDetailsBean != null) {
            intent.putExtra("nickname", mAccountDetailsBean.getNickname());
        }
        startActivity(intent);
    }

    @OnClick(R.id.btn_exit)
    public void btn_exit() {
        new AlertDialog.Builder(this).setMessage("退出后将无法控制设备，无法接收消息提醒，确定要退出吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startActivity(new Intent(PersonalInformationActivity.this, LoginCloudActivity.class));
                        finish();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .create().show();
    }

    @Override
    public void getPhotoFromCamera() {
        Intent intent = new Intent(this, GetPhotoActivity.class);
        intent.putExtra("type", AppConstans.GetPhoto.GET_PHOTO_FROM_CAMERA);
        startActivityForResult(intent, AppConstans.GetPhoto.ONE_DRAGON);
    }

    @Override
    public void getPhotoFromAlbum() {
        Intent intent = new Intent(this, GetPhotoActivity.class);
        intent.putExtra("type", AppConstans.GetPhoto.GET_PHOTO_FROM_ALBUM);
        startActivityForResult(intent, AppConstans.GetPhoto.ONE_DRAGON);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case AppConstans.GetPhoto.ONE_DRAGON:
                if (resultCode == RESULT_OK && data != null) {
                    String imageString = data.getStringExtra("image_string");
                    byte[] buf = Base64Utils.decode(imageString);
                    InputStream inputStream = new ByteArrayInputStream(buf);

                    String dir = PathUtils.getCameraImageDir();
                    File file = new File(dir, System.currentTimeMillis() + ".jpg");
                    try {
                        OutputStream os = new FileOutputStream(file);
                        int bytesRead = 0;
                        byte[] buffer = new byte[8192];
                        while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
                            os.write(buffer, 0, bytesRead);
                        }
                        os.close();
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ImageLoader.getLoader(this).load(file.getAbsolutePath()).into(mIvHeadPortrait);

                    mUploadBasePresenter.uploadBase64(imageString, "1");
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }

    }
}
