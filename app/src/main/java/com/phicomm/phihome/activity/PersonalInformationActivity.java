package com.phicomm.phihome.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.phicomm.phihome.R;
import com.phicomm.phihome.constants.AppConstans;
import com.phicomm.phihome.listener.GetPhotoBeforeListener;
import com.phicomm.phihome.manager.imageloader.ImageLoader;
import com.phicomm.phihome.popup.GetPhotoPopup;

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

    @Override
    public void initLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_personal_information);

    }

    @Override
    public void afterInitView() {
        setPageTitle("个人信息");
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
                    String path = data.getStringExtra("path");
                    ImageLoader.getLoader(this).load(path).into(mIvHeadPortrait);
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }

    }
}
