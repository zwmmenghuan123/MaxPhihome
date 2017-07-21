package com.phicomm.phihome.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.phicomm.phihome.R;
import com.phicomm.phihome.constants.AppConstans;
import com.phicomm.phihome.listener.GetPhotoListener;
import com.phicomm.phihome.popup.GetPhotoPopup;
import com.phicomm.phihome.utils.GetPhotoUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 个人信息界面
 * Created by xiaolei.yang on 2017/7/19.
 */
//public class PersonalInformationActivity extends BaseActivity implements GetPhotoListener,
//        TakePhoto.TakeResultListener, InvokeListener {
public class PersonalInformationActivity extends BaseActivity implements GetPhotoListener {
    @BindView(R.id.rl_head_portrait)
    RelativeLayout mRlHeadPortrait;

    @BindView(R.id.iv_head_portrait)
    ImageView mIvHeadPortrait;

    Uri imageUri;

//    private TakePhoto takePhoto;
//    private InvokeParam invokeParam;

    @Override
    public void initLayout(Bundle savedInstanceState) {
//        getTakePhoto().onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);

    }

    @Override
    public void afterInitView() {
        setPageTitle("个人信息");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        getTakePhoto().onSaveInstanceState(outState);
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
        imageUri = GetPhotoUtils.startTakePhoto(this);

//        String dir = PathUtils.getCameraImageDir();
//        if (TextUtils.isEmpty(dir)) {
//            ToastUtil.show(this, "创建照片路径失败，请检查应用读写权限或稍后再试。");
//            return;
//        }
//
//        File file = new File(dir, System.currentTimeMillis() + ".jpg");
//        try {
//            boolean createFileSuccess = file.createNewFile();
//            if (!createFileSuccess) {
//                ToastUtil.show(this, "创建照片失败，请检查应用权限或稍后再试");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            ToastUtil.show(this, "创建照片失败，请检查应用读写权限或稍后再试。");
//        }
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
//            imageUri = FileProvider.getUriForFile(PhApplication.getContext(), "com.phicomm.phihome.fileprovider", file);
//        } else {
//            imageUri = Uri.fromFile(file);
//        }
//
//        CropOptions options = new CropOptions.Builder().setAspectX(100).setAspectY(100).setOutputX(100).setOutputY(100).create();
//        getTakePhoto().onPickFromCaptureWithCrop(imageUri, options);
    }

    @Override
    public void getPhotoFromAlbum() {
        GetPhotoUtils.startChoosePhoto(this);

//        String dir = PathUtils.getCameraImageDir();
//        if (TextUtils.isEmpty(dir)) {
//            ToastUtil.show(this, "创建照片路径失败，请检查应用读写权限或稍后再试。");
//            return;
//        }
//
//        File file = new File(dir, System.currentTimeMillis() + ".jpg");
//        try {
//            boolean createFileSuccess = file.createNewFile();
//            if (!createFileSuccess) {
//                ToastUtil.show(this, "创建照片失败，请检查应用权限或稍后再试");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            ToastUtil.show(this, "创建照片失败，请检查应用读写权限或稍后再试。");
//        }
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
//            imageUri = FileProvider.getUriForFile(PhApplication.getContext(), "com.phicomm.phihome.fileprovider", file);
//        } else {
//            imageUri = Uri.fromFile(file);
//        }
//        CropOptions options = new CropOptions.Builder().setAspectX(100).setAspectY(100).setOutputX(100).setOutputY(100).create();
//        getTakePhoto().onPickFromGalleryWithCrop(imageUri, options);//没有自带裁剪或第三方裁剪功能，会使用TakePhoto自带的裁剪工具裁剪
    }

    @Override
    public void getPhotoFromCameraComplete(Uri uri) {
//        if (needCrop) {
        GetPhotoUtils.startCropImage(this, uri);
//        } else {
//            ImageLoader.getLoader(this).load(uri).into(mIvHeadPortrait);
//        }
    }

    @Override
    public void getPhotoFromAlbumComplete(Uri uri) {
//        if (needCrop) {
        GetPhotoUtils.startCropImage(this, uri);
//        } else {
//            GetPhotoUtils.handlerChooseImage(uri, this, mIvHeadPortrait);
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AppConstans.GetPhoto.GET_PHOTO_FROM_CAMERA) {
            GetPhotoUtils.onActivityResultForTakePhoto(requestCode, resultCode, imageUri, this);
        } else if (requestCode == AppConstans.GetPhoto.GET_PHOTO_FROM_ALBUM) {
            GetPhotoUtils.onActivityResultForChoosePhoto(requestCode, resultCode, data, this);
        } else if (requestCode == AppConstans.GetPhoto.CROP_IMAGE) {
            GetPhotoUtils.onActivityResultForCropImage(requestCode, resultCode, data, this, mIvHeadPortrait, true);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

//        getTakePhoto().onActivityResult(requestCode, resultCode, data);
//        super.onActivityResult(requestCode, resultCode, data);
    }

    //    /**
//     * 获取TakePhoto实例
//     *
//     * @return
//     */
//    public TakePhoto getTakePhoto() {
//        if (takePhoto == null) {
//            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
//        }
//        return takePhoto;
//    }
//
//    @Override
//    public void takeSuccess(TResult result) {
//        ImageLoader.getLoader(this).load(result.getImage().getOriginalPath()).into(mIvHeadPortrait);
//        Log.e("======", "takeSuccess：" + result.getImage().getOriginalPath());
//    }
//
//    @Override
//    public void takeFail(TResult result, String msg) {
//        Log.e("======", "takeFail:" + msg);
//    }
//
//    @Override
//    public void takeCancel() {
//        Log.e("======", "takeCancel:" + getResources().getString(R.string.msg_operation_canceled));
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
//    }
//
//    @Override
//    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
//        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
//        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
//            this.invokeParam = invokeParam;
//        }
//        return type;
//    }
}
