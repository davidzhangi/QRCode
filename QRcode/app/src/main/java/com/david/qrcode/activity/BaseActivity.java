package com.david.qrcode.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;


import com.david.qrcode.R;
import com.david.qrcode.utils.SystemBarTintManager;

import java.lang.reflect.Field;

/**
 * Created by david on 2017/5/22.
 */
public abstract class BaseActivity extends AppCompatActivity implements SlidingPaneLayout.PanelSlideListener {

    protected SlidingPaneLayout mSlidingPaneLayout;
    private FrameLayout mContainerFl;

    protected SystemBarTintManager tintManager;

    protected boolean isCanSlideFinish() {
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (isCanSlideFinish()) {
            try {
                mSlidingPaneLayout = new SlidingPaneLayout(this);

                Field f_overHang = SlidingPaneLayout.class.getDeclaredField("mOverhangSize");
                f_overHang.setAccessible(true);
                f_overHang.set(mSlidingPaneLayout, 0);

                mSlidingPaneLayout.setPanelSlideListener(this);
                mSlidingPaneLayout.setSliderFadeColor(getResources().getColor(android.R.color.transparent));
            } catch (Exception e) {
                e.printStackTrace();
            }
            //添加两个view
            View leftView = new View(this);
            leftView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mSlidingPaneLayout.addView(leftView, 0);
            mContainerFl = new FrameLayout(this);
            mContainerFl.setBackgroundColor(getResources().getColor(android.R.color.white));
            mContainerFl.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mSlidingPaneLayout.addView(mContainerFl, 1);
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int id) {
        setContentView(getLayoutInflater().inflate(id, null));
    }

    @Override
    public void setContentView(View v) {
        setContentView(v, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void setContentView(View v, ViewGroup.LayoutParams params) {
        if (isCanSlideFinish()) {
            super.setContentView(mSlidingPaneLayout, params);

            mContainerFl.removeAllViews();
            mContainerFl.addView(v, params);
        } else {
            super.setContentView(v, params);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }

        tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        //通知栏所需颜色
        tintManager.setStatusBarTintResource(R.color.colorPrimaryDark);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    public void onPanelClosed(View view) {

    }

    @Override
    public void onPanelOpened(View view) {
        finish();
        this.overridePendingTransition(0, R.anim.slide_out_right);
    }

    @Override
    public void onPanelSlide(View view, float v) {
        //view.setAlpha(1-v);
        //view.setRotationY(v*90);
        //view.setRotationX(v*90);
    }

    /**
     * 隐藏软键盘
     *
     * @param
     */
    public void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView()
                        .getApplicationWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * Android 6.0 授权机制管理
     *
     * @param permission
     * @param permissionCode
     */
    public void requestPermission(String permission, int permissionCode) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, permissionCode);
        } else {
            onPermissionSuccess(permissionCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            onPermissionSuccess(requestCode);
        } else {
            onPermissionFaliure(requestCode);
        }
    }

    protected void onPermissionSuccess(int requestCode) {
    }

    protected void onPermissionFaliure(int requestCode) {

    }
}
