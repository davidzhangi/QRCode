package com.david.qrcode.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.david.qrcode.ParticleView.ParticleView;
import com.david.qrcode.R;
import com.david.qrcode.dialog.DesginDialog;
import com.david.qrcode.utils.Const;

import net.youmi.android.AdManager;
import net.youmi.android.nm.vdo.VideoAdManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by david on 2017/5/22.
 */
public class SplashActivity extends BaseActivity {

    @BindView(R.id.pv)
    ParticleView pv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        initPermission();

        boolean result = VideoAdManager.getInstance(this).checkVideoAdConfig();

        pv.setOnParticleAnimListener(new ParticleView.ParticleAnimListener() {
            @Override
            public void onAnimationEnd() {
                startActivity(new Intent(SplashActivity.this, AdActivity.class));
                finish();
            }
        });
    }

    private void initPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.CAMERA, Const.CAMERA_CODE);
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, Const.READ_EXTERNAL_STORAGE_CODE);
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.READ_PHONE_STATE, Const.READ_PHONE_STATE_CODE);
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Const.WRITE_EXTERNAL_STORAGE_CODE);
        } else {
            pv.startAnim();
            preloadData();
        }
    }

    /**
     * 预加载数据
     */
    private void preloadData() {
        AdManager.getInstance(this).init("f79b0919a931a78a", "7392c9080d262382", true);
        // 设置服务器回调 userId，一定要在请求广告之前设置，否则无效
        VideoAdManager.getInstance(this).setUserId("f79b0919a931a78a");
        // 请求视频广告
        VideoAdManager.getInstance(this).requestVideoAd(this);
    }

    @Override
    protected void onPermissionSuccess(int requestCode) {
        switch (requestCode) {
            case Const.CAMERA_CODE:
            case Const.READ_EXTERNAL_STORAGE_CODE:
            case Const.READ_PHONE_STATE_CODE:
            case Const.WRITE_EXTERNAL_STORAGE_CODE:
                initPermission();
                break;
        }
    }

    @Override
    protected void onPermissionFaliure(int requestCode) {
        switch (requestCode) {
            case Const.CAMERA_CODE:
            case Const.READ_EXTERNAL_STORAGE_CODE:
            case Const.READ_PHONE_STATE_CODE:
            case Const.WRITE_EXTERNAL_STORAGE_CODE:
                new DesginDialog().init2BtnDialog(this, "提示", "请授予我们权限或请重新开启并同意授权", "授权", "取消", new DesginDialog.DialogListener() {
                    @Override
                    public void positive() {
                        startActivity(getAppDetailSettingIntent());
                        finish();
                    }

                    @Override
                    public void negative() {
                        finish();
                    }
                });
                break;
        }
    }

    private Intent getAppDetailSettingIntent() {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        return localIntent;
    }
}
