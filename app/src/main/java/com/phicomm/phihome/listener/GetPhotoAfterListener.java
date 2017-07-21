package com.phicomm.phihome.listener;

import android.net.Uri;

import java.io.File;

/**
 * 拍照、取图结果回调
 * Created by xiaolei.yang on 2017/7/21.
 */

public interface GetPhotoAfterListener {
    void getPhotoFromCameraComplete(Uri uri);

    void getPhotoFromAlbumComplete(Uri uri);

    void cropPhotoComplete(Uri uri);


    void compressPhotoCompleteStart();

    void compressPhotoCompleteSuccess(File file);

    void compressPhotoCompleteError(Throwable e);
}
