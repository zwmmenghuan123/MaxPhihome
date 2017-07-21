package com.phicomm.phihome.listener;

import android.net.Uri;

/**
 * 拍照、相册取图
 * Created by xiaolei.yang on 2017/7/19.
 */

public interface GetPhotoListener {
    void getPhotoFromCamera();

    void getPhotoFromAlbum();

    void getPhotoFromCameraComplete(Uri uri);

    void getPhotoFromAlbumComplete(Uri uri);
}
