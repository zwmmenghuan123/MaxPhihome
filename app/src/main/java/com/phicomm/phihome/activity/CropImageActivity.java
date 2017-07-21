package com.phicomm.phihome.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.phicomm.phihome.R;
import com.phicomm.phihome.views.clipview.ClipImageLayout;

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

        String path = getIntent().getStringExtra("path");
        mClipImageLayout.setPath(path);
    }

    @OnClick(R.id.tv_menu)
    public void tv_menu() {
        Intent intent = new Intent();
        Uri uri = mClipImageLayout.clipUri();
        intent.setData(uri);
        setResult(RESULT_OK, intent);
        finish();
    }
}
