package com.phicomm.phihome.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.ImageView;

import com.phicomm.phihome.constants.AppConstans;
import com.phicomm.phihome.manager.imageloader.ImageLoader;

import java.io.File;
import java.io.IOException;
import java.util.Map;

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

        File file = new File(PathUtils.getCameraImageDir(), System.currentTimeMillis() + ".jpg");
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

    public static void onActivityResult(int requestCode, int resultCode, Intent data,
                                        Activity mContext, Uri imageUri, ImageView imageView) {
        switch (requestCode) {
            case AppConstans.GetPhoto.GET_PHOTO_FROM_CAMERA:
                if (resultCode == RESULT_OK) {
                    ImageLoader.getLoader(mContext).load(imageUri).into(imageView);
                }
                break;
            case AppConstans.GetPhoto.GET_PHOTO_FROM_ALBUM:
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        handleImageOnKitKat(data, mContext, imageView);
                    } else {
                        handleImageBeforeKitKat(data, mContext, imageView);
                    }
                }
                break;

        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static void handleImageOnKitKat(Intent data, Activity mContext, ImageView imageView) {
        String imagePath = null;
        Uri uri = data.getData();
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
        displayImage(imagePath, mContext, imageView);
    }

    private static void handleImageBeforeKitKat(Intent data, Activity mContext, ImageView imageView) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null, mContext);
        displayImage(imagePath, mContext, imageView);
    }

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
