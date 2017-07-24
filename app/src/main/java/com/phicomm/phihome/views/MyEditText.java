package com.phicomm.phihome.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.phicomm.phihome.R;
import com.phicomm.phihome.utils.LogUtils;

/**
 * Created by qisheng.lv on 2017/7/24.
 */
public class MyEditText extends LinearLayout {
    private EditText mEtContent;
    private View mViewLine;
    private ImageView mIvDelete;
    private ImageView mIvEye;

    public MyEditText(Context context) {
        this(context, null);
    }

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public MyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_my_edittext, this);

        mEtContent = (EditText) view.findViewById(R.id.et_content);
        mViewLine = view.findViewById(R.id.view_line);
        mIvDelete = (ImageView) view.findViewById(R.id.iv_delete);
        mIvEye = (ImageView) view.findViewById(R.id.iv_eye);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MyEditTextAttr);
        int inputType = ta.getInt(R.styleable.MyEditTextAttr_input_type, InputType.TYPE_CLASS_TEXT);
        int maxLength = ta.getInt(R.styleable.MyEditTextAttr_max_length, 20);
        final boolean hasEye = ta.getBoolean(R.styleable.MyEditTextAttr_has_eye, false);
        String hint = ta.getString(R.styleable.MyEditTextAttr_hint);
        ta.recycle();

        setInType(inputType);
        mEtContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        mIvEye.setVisibility(hasEye ? View.VISIBLE : View.GONE);
        mEtContent.setHint(hint);
        setInType(inputType);

        mEtContent.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mViewLine.setBackgroundColor(getResources().getColor(R.color.focused_line));
                    setSelectionEnd();
                } else {
                    mViewLine.setBackgroundColor(getResources().getColor(R.color.default_line));
                }
            }
        });


        mEtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = mEtContent.getText().toString();
                mIvDelete.setVisibility(TextUtils.isEmpty(content) ? View.INVISIBLE : View.VISIBLE);
            }
        });

        mIvDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mEtContent.setText("");
            }
        });


        mIvEye.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean textType = mEtContent.getInputType() != (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                LogUtils.debug("textType: " + textType);
                mIvEye.setImageResource(textType ? R.drawable.look : R.drawable.look_visible);
                mEtContent.setInputType(textType ? InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD :
                        InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                setSelectionEnd();
            }
        });
    }

    public String getContent() {
        return mEtContent.getText().toString().trim();
    }

    public void setContent(String content) {
        mEtContent.setText(content);
    }


    public EditText getEt(){
        return mEtContent;
    }

    private void setInType(int type) {
        LogUtils.debug("setInType: " + type);
        switch (type) {
            case 1:
                mEtContent.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_CLASS_PHONE);
                break;

            case 2:
                mEtContent.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                break;

            default:
                mEtContent.setInputType(InputType.TYPE_CLASS_TEXT);
        }

    }

    private void setSelectionEnd(){
        String content = mEtContent.getText().toString();
        if (!TextUtils.isEmpty(content)) {
            mEtContent.setSelection(content.length());
        }
    }

}
