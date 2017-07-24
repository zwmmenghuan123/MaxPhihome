package com.phicomm.phihome.utils;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by qisheng.lv on 2017/7/24.
 */

public class ViewUtils {

    public static void toggleBtn(final EditText etPhone, final EditText etPwd, final Button btn) {
        if (etPhone != null) {
            etPhone.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String phone = etPhone.getText().toString().trim();
                    String pwd = etPwd.getText().toString().trim();
                    btn.setEnabled(!TextUtils.isEmpty(phone) && phone.length() == 11 && !TextUtils.isEmpty(pwd) && pwd.length() > 5);
                }
            });
        }

        etPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String phone = etPhone.getText().toString().trim();
                String pwd = etPwd.getText().toString().trim();
                btn.setEnabled(!TextUtils.isEmpty(phone) && phone.length() == 11 && !TextUtils.isEmpty(pwd) && pwd.length() > 5);
            }
        });

    }

}
