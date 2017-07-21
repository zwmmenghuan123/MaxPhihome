package com.phicomm.phihome.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.phicomm.phihome.R;
import com.phicomm.phihome.views.clipview.ClipImageLayout;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 图片裁剪
 * Created by xiaolei.yang on 2017/7/20.
 */

public class CropImageActivity extends BaseActivity {
    @BindView(R.id.clip_image_layout)
    ClipImageLayout mClipImageLayout;

    @BindView(R.id.tv_menu)
    TextView tv_menu;

    @Override
    public void initLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_crop_image);
    }

    @Override
    public void afterInitView() {
        tv_menu.setVisibility(View.VISIBLE);
        tv_menu.setText(R.string.crop);

        Uri uri = getIntent().getData();
        String imagePath;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            imagePath = getImagePathOnKitKat(uri, this);
        } else {
            imagePath = getImagePath(uri, null, this);
        }

        Log.e("=======", "CropImageActivity: " + imagePath);

        String path = getIntent().getStringExtra("path");
        mClipImageLayout.setPath(path);
    }

    @OnClick(R.id.tv_menu)
    public void tv_menu() {
//        Bitmap bitmap = mClipImageLayout.clip();
//
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] datas = baos.toByteArray();
//
//
//
//        Intent intent = new Intent();
//        intent.putExtra("bitmap", datas);
//        setResult(RESULT_OK, intent);
//        finish();

//        Intent intent = new Intent();
//        Uri uri = mClipImageLayout.clipUri();
//
//        String imagePath;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            imagePath = getImagePathOnKitKat(uri, this);
//        } else {
//            imagePath = getImagePath(uri, null, this);
//        }
//
//        Log.e("=======", "tv_menu: " + imagePath);
//
//        intent.setData(uri);
//        setResult(RESULT_OK, intent);
//        finish();


        Intent intent = new Intent();
        try {
            String path = mClipImageLayout.saveFile();


            Log.e("=======", "path: " + path);

            intent.putExtra("path", path);
            setResult(RESULT_OK, intent);
            finish();

        } catch (IOException e) {
            e.printStackTrace();
        }


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

}
