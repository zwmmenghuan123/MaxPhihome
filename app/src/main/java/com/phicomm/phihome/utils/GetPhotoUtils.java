package com.phicomm.phihome.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.phicomm.phihome.activity.CropImageActivity;
import com.phicomm.phihome.constants.AppConstans;
import com.phicomm.phihome.listener.GetPhotoListener;
import com.phicomm.phihome.manager.imageloader.ImageLoader;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static android.app.Activity.RESULT_OK;

/**
 * 拍照、取图的工具类
 * Created by xiaolei.yang on 2017/7/20.
 */

public class GetPhotoUtils {

    //启动拍照
    public static Uri startTakePhoto(Activity mContext) {
        if (mContext == null) {
            return null;
        }

        Uri imageUri;
        String dir = PathUtils.getCameraImageDir();
        if (TextUtils.isEmpty(dir)) {
            ToastUtil.show(mContext, "创建照片路径失败，请检查应用读写权限或稍后再试。");
            return null;
        }

        File file = new File(dir, System.currentTimeMillis() + ".jpg");
        try {
            boolean createFileSuccess = file.createNewFile();
            if (!createFileSuccess) {
                ToastUtil.show(mContext, "创建照片失败，请检查应用权限或稍后再试");
            }
        } catch (IOException e) {
            e.printStackTrace();
            ToastUtil.show(mContext, "创建照片失败，请检查应用读写权限或稍后再试。");
        }

        Map<String, Object> map = IntentUtils.getCameraIntent(file);
        imageUri = (Uri) map.get("uri");
        Intent intent = (Intent) map.get("intent");
        mContext.startActivityForResult(intent, AppConstans.GetPhoto.GET_PHOTO_FROM_CAMERA);
        return imageUri;
    }

    //启动取图
    public static void startChoosePhoto(Activity mContext) {
        if (mContext == null) {
            return;
        }
        Intent intent = IntentUtils.getAlbumIntent();
        mContext.startActivityForResult(intent, AppConstans.GetPhoto.GET_PHOTO_FROM_ALBUM);
    }

    public static void startCropImage(Activity mContext, Uri imageUri) {
        String imagePath;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            imagePath = getImagePathOnKitKat(imageUri, mContext);
        } else {
            imagePath = getImagePath(imageUri, null, mContext);
        }

        Log.e("=======", "startCropImage: " + imagePath);


        Intent intent = new Intent(mContext, CropImageActivity.class);
        intent.setData(imageUri);
        intent.putExtra("path", imagePath);
        mContext.startActivityForResult(intent, AppConstans.GetPhoto.CROP_IMAGE);
    }

    /**
     * 拍照结果回调
     *
     * @param requestCode 请求码
     * @param resultCode  返回码
     * @param imageUri    拍照的uri
     */
    public static void onActivityResultForTakePhoto(int requestCode, int resultCode,
                                                    Uri imageUri, GetPhotoListener getPhotoListener) {
        if (requestCode != AppConstans.GetPhoto.GET_PHOTO_FROM_CAMERA) {
            return;
        }
        if (resultCode == RESULT_OK) {
            if (getPhotoListener != null) {
                getPhotoListener.getPhotoFromCameraComplete(imageUri);
            }
        }
    }

    /**
     * 取图结果回调
     *
     * @param requestCode 请求码
     * @param resultCode  返回码
     * @param data        取图数据
     */
    public static void onActivityResultForChoosePhoto(int requestCode, int resultCode, Intent data, GetPhotoListener getPhotoListener) {
        if (requestCode != AppConstans.GetPhoto.GET_PHOTO_FROM_ALBUM) {
            return;
        }
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            if (getPhotoListener != null) {
                getPhotoListener.getPhotoFromAlbumComplete(uri);
            }
        }

    }

    /**
     * 裁剪结果回调
     *
     * @param requestCode 请求码
     * @param resultCode  返回码
     * @param data        取图数据
     */
    public static void onActivityResultForCropImage(int requestCode, int resultCode, Intent data,
                                                    final Activity mContext, final ImageView imageView, boolean needCompress) {
        if (requestCode != AppConstans.GetPhoto.CROP_IMAGE) {
            return;
        }
        if (resultCode == RESULT_OK && data != null) {
            if (needCompress) {
                if (data.getStringExtra("path") != null) {
//                    Uri uri = data.getData();
//                    if (uri == null) {
//                        return;
//                    }
//
//                    String imagePath;
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                        imagePath = getImagePathOnKitKat(uri, mContext);
//                    } else {
//                        imagePath = getImagePath(uri, null, mContext);
//                    }

                    String imagePath = data.getStringExtra("path");
                    if (TextUtils.isEmpty(imagePath)) {
                        return;
                    }
                    File file = new File(imagePath);
                    if (!file.exists()) {
                        return;
                    }
                    Luban.with(mContext)
                            .load(file)                     //传入要压缩的图片
                            .setCompressListener(new OnCompressListener() { //设置回调
                                @Override
                                public void onStart() {
                                    // TODO 压缩开始前调用，可以在方法内启动 loading UI
                                }

                                @Override
                                public void onSuccess(File file) {
                                    // TODO 压缩成功后调用，返回压缩后的图片文件
                                    ImageLoader.getLoader(mContext).load(file.getAbsoluteFile()).into(imageView);
                                }

                                @Override
                                public void onError(Throwable e) {
                                    // TODO 当压缩过程出现问题时调用
                                }
                            }).launch();    //启动压缩
                }

            } else {
                if (data.getData() != null) {
                    Uri uri = data.getData();
                    ImageLoader.getLoader(mContext).load(uri).into(imageView);
                } else {
                    byte[] b = data.getByteArrayExtra("bitmap");
                    Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    }
                }
            }
        }
    }

    /**
     * 取图后展示图片
     *
     * @param uri       onActivityResultForChoosePhoto中getPhotoListener.getPhotoFromAlbumComplete(uri)的参数uri
     * @param mContext  展示图片的控件所在的Activity
     * @param imageView 展示图片的控件
     */
    public static void handlerChooseImage(Uri uri, Activity mContext, ImageView imageView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            handleImageOnKitKat(uri, mContext, imageView);
        } else {
            handleImageBeforeKitKat(uri, mContext, imageView);
        }
    }

    /**
     * 安卓4.4及以上处理取图结果
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static void handleImageOnKitKat(Uri uri, Activity mContext, ImageView imageView) {
        String imagePath = getImagePathOnKitKat(uri, mContext);
        displayImage(imagePath, mContext, imageView);
    }

    /**
     * 安卓4.4以下处理取图结果
     */
    private static void handleImageBeforeKitKat(Uri uri, Activity mContext, ImageView imageView) {
        String imagePath = getImagePath(uri, null, mContext);
        displayImage(imagePath, mContext, imageView);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static String getImagePathOnKitKat(Uri uri, Activity mContext) {
        String imagePath = null;
        if (DocumentsContract.isDocumentUri(mContext, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection, mContext);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null, mContext);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            imagePath = getImagePath(uri, null, mContext);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            imagePath = uri.getPath();
        }
        return imagePath;
    }


    //从uri获取图片路径
    private static String getImagePath(Uri uri, String selection, Activity mContext) {
        String path = null;
        Cursor cursor = mContext.getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private static void displayImage(String imagePath, Activity mContext, ImageView imageView) {
        if (imagePath != null) {
            ImageLoader.getLoader(mContext).load(imagePath).into(imageView);
        } else {
            ToastUtil.show(mContext, "获取图片失败 ");
        }
    }

}

interface GetPhotoComplete {
    void getPhotoComplete(Uri uri);
}
