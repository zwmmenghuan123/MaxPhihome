package com.phicomm.phihome.utils;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;

import com.phicomm.phihome.constants.AppConstans;
import com.phicomm.phihome.listener.ImgCompressCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by QiSheng on 17:58  2016/9/7.
 */
public class ImageUtils {

    public static void compressImg(final Context context, final Uri uri, final ImgCompressCallback callback) {
        try {
            doComress(context, uri, callback);
        } catch (Exception e) {

        }
    }


    /**
     * 压缩图片-显示
     *
     * @param uri
     */
    private static void doComress(final Context context, final Uri uri, final ImgCompressCallback callback) {
        new Thread() {
            public void run() {
                String uploadPath = getAbsoluteImagePath(context, uri);
                if (callback == null || TextUtils.isEmpty(uploadPath)) {
                    return;
                }
                Bitmap bitmap = getBitampFormPath(uploadPath, 300, 200);
                if (bitmap != null) {
                    callback.onImgCompressSuccess(bitmap, uploadPath);
                } else {
                    callback.onImgCompressFail("图片压缩失败");
                }
            }
        }.start();
    }

    public static Bitmap getBitampFormPath(String uploadPath, int srcWidth, int srcHeigth) {
        File file = new File(uploadPath);
        if (!file.exists()) {
            return null;
        }

        BitmapFactory.Options opts = new BitmapFactory.Options();// 解析图片的选项参数
        opts.inPreferredConfig = Bitmap.Config.RGB_565;

        // 2.得到图片的宽高属性。
        opts.inJustDecodeBounds = true;// 不真正的解析这个bitmap ，只是获取bitmap的宽高信息
        Bitmap bitmap = BitmapFactory.decodeFile(uploadPath, opts);

        int imageHeight = opts.outHeight;
        int imageWidth = opts.outWidth;
        // 3.计算缩放比例。
        int dx = imageWidth / srcWidth;
        int dy = imageHeight / srcHeigth;

//        LogUtils.d("原图宽高： " + imageWidth + " * " + imageHeight);

        int scale = 1;
        if (dx >= dy && dx >= 1) {
            scale = dy;
        }
        if (dy >= dx && dx >= 1) {
            scale = dx;
        }

//        LogUtils.d("压缩比例： " + scale);

        opts.inJustDecodeBounds = false;// 真正的解析bitmap
        opts.inSampleSize = scale; // 指定图片缩放比例

        Bitmap resultBitmap = BitmapFactory.decodeFile(uploadPath, opts);

        //图片是否放生了旋转
        int imgDegree = getImgDegree(uploadPath);
        if (imgDegree >= 0) {
            resultBitmap = rotateBitmapByDegree(resultBitmap, imgDegree);
        }

        return resultBitmap;
    }


    /**
     * 获取图片Base64
     *
     * @param uploadPath
     * @param callback
     */
    public static void doGtBase64(final String uploadPath, final ImgCompressCallback callback) {
        try {
            new Thread() {
                public void run() {
                    String base64 = getCompressBase64(uploadPath);
                    if (!TextUtils.isEmpty(base64)) {
                        callback.onImgCompressSuccess(null, base64);
                    } else {
                        callback.onImgCompressFail("图片压缩失败");
                    }
                }
            }.start();

        } catch (Exception e) {
            callback.onImgCompressFail("图片压缩失败");
        }

    }


    public static String getCompressBase64(String uploadPath) {
        Bitmap bitmap = getBitampFormPath(uploadPath, AppConstans.GetPhoto.UPLOAD_IMG_SRC_WIDTH,
                AppConstans.GetPhoto.UPLOAD_IMG_SRC_HEIGHT);
        String base64 = "";
        if (bitmap != null) {
            base64 = qualityCompress(bitmap);
        }
        return base64;
    }

    public static String qualityCompress(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (options > 0 && baos.toByteArray().length / 1024 > AppConstans.GetPhoto.UPLOAD_IMG_SRC_LENGTH) { // 循环判断如果压缩后图片大于300kb,继续压缩
            baos.reset();// 重置baos即清空baos
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }

        return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
    }


    // ***************** 根据Uri获取文件绝对路径 *****************//

    /**
     * 根据Uri获取文件绝对路径
     *
     * @param context
     * @param uri
     * @return
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getAbsoluteImagePath(Context context, Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    // ***************** 根据Uri获取文件绝对路径 *****************//


    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getImgDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }


    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }


}
