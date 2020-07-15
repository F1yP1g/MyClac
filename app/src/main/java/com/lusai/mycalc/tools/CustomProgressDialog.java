package com.lusai.mycalc.tools;

/**
 * Created by LU on 2017/8/12.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.lusai.mycalc.R;
/**
 * 自定义Loading加载框
 */
public class CustomProgressDialog extends ProgressDialog {
    private AnimationDrawable mAnimation;
    private Context mContext;
    private ImageView mImageView;
    private String mLoadingTip;
    private TextView mLoadingTv;
    private int mResid;

    public CustomProgressDialog(Context context, String content,  int theme, int id) {
        super(context, theme);
        this.mContext = context;
        this.mLoadingTip = content;
        this.mResid = id;
        setCanceledOnTouchOutside(false);// 设置不能点击对话框外边取消当前对话框
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initData() {
        mImageView.setBackgroundResource(mResid);
        mAnimation = (AnimationDrawable) mImageView.getBackground();
        mImageView.post(new Runnable() {
            @Override
            public void run() {
                mAnimation.start();

            }
        });
        mLoadingTv.setText(mLoadingTip);
    }
    public void setContent(String str) {
        mLoadingTv.setText(str);
    }
    private void initView() {
        setContentView(R.layout.start_loading);
        mLoadingTv = (TextView) findViewById(R.id.loadingTv);
        mImageView = (ImageView) findViewById(R.id.loadingIv);
    }
}