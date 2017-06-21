package com.david.qrcode.activity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.david.qrcode.R;
import com.david.qrcode.dialog.DesginDialog;
import com.david.qrcode.fragment.QRCodeDecodeFragment;
import com.david.qrcode.fragment.QRCodeEncodeFragment;
import com.david.qrcode.utils.ApkUpdataDownloadUtils;
import com.david.qrcode.utils.Const;
import com.david.qrcode.utils.DoubleTabExitController;
import com.david.qrcode.utils.PrefUtil;

import net.youmi.android.AdManager;
import net.youmi.android.nm.sp.SpotManager;
import net.youmi.android.update.AppUpdateInfo;
import net.youmi.android.update.a;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by david on 2017/5/22.
 */
public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.content)
    FrameLayout mContent;
    @BindView(R.id.navigation)
    BottomNavigationView mNavigation;

    private ArrayList<Fragment> fragments;

    private FragmentTransaction fragmentTransaction;
    private FragmentManager supportFragmentManager;

    public static final int EXIT_TIME_INTERVAL = 1500;//返回键间隔时间 退出
    private DoubleTabExitController mDoubleTabExitController;

    @Override
    protected boolean isCanSlideFinish() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initUpdate();
        initView();
        setupSpotAd();
    }

    private void initUpdate() {
        AdManager.getInstance(this).asyncCheckAppUpdate(new a() {
            @Override
            public void a(AppUpdateInfo appUpdateInfo) {
                if (appUpdateInfo == null || appUpdateInfo.getUrl() == null) {
                    return;
                }

                int versionCode = appUpdateInfo.getVersionCode();
                if (versionCode <= Integer.valueOf(getVersionCode(MainActivity.this))) {
                    return;
                }

                PrefUtil.savePref(MainActivity.this, Const.UPDATE_URL, appUpdateInfo.getUrl());
                new DesginDialog().init2BtnDialog(MainActivity.this, "提示", "发现新版本是否升级", "立即", "稍后", new DesginDialog.DialogListener() {
                    @Override
                    public void positive() {
                        new ApkUpdataDownloadUtils(MainActivity.this);
                    }

                    @Override
                    public void negative() {
                    }
                });
            }
        });
    }

    public String getVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo;
        String versionCode = "";
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = packageInfo.versionCode + "";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }


    /**
     * 设置插屏广告
     */
    private void setupSpotAd() {
        // 设置插屏图片类型，默认竖图
        //		// 横图
        //		SpotManager.getInstance(mContext).setImageType(SpotManager
        // .IMAGE_TYPE_HORIZONTAL);
        // 竖图
        SpotManager.getInstance(this).setImageType(SpotManager.IMAGE_TYPE_VERTICAL);

        // 设置动画类型，默认高级动画
        //		// 无动画
        //		SpotManager.getInstance(mContext).setAnimationType(SpotManager
        // .ANIMATION_TYPE_NONE);
        //		// 简单动画
        //		SpotManager.getInstance(mContext).setAnimationType(SpotManager
        // .ANIMATION_TYPE_SIMPLE);
        // 高级动画
        SpotManager.getInstance(this)
                .setAnimationType(SpotManager.ANIMATION_TYPE_ADVANCED);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_scan:
                show(0);
                return true;
            case R.id.navigation_create:
                show(1);
                return true;
//            case R.id.navigation_notifications:
//                return true;
        }
        return false;
    }

    private void initView() {
        mNavigation.setOnNavigationItemSelectedListener(this);
        fragments = getTab();
        show(0);
    }

    private ArrayList<Fragment> getTab() {
        ArrayList<Fragment> list = new ArrayList<>();
        list.add(new QRCodeDecodeFragment());
        list.add(new QRCodeEncodeFragment());
        return list;
    }

    public void show(int index) {
        supportFragmentManager = getSupportFragmentManager();
        fragmentTransaction = supportFragmentManager.beginTransaction();
        for (int i = 0; i < fragments.size(); i++) {
            Fragment fragment = supportFragmentManager.findFragmentByTag(String.valueOf(i));

            if (fragment == null) {
                fragment = fragments.get(i);
            }

            if (i == index) {
                if (fragment.isAdded()) {
                    return;
                }
                fragmentTransaction.add(R.id.content, fragment, String.valueOf(i));
            } else {
                fragmentTransaction.remove(fragment);
            }
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        if (SpotManager.getInstance(this).isSpotShowing()) {
            SpotManager.getInstance(this).hideSpot();
            return;
        }

        if (mDoubleTabExitController == null) {
            mDoubleTabExitController = new DoubleTabExitController(
                    EXIT_TIME_INTERVAL,
                    new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "再按一次退出扫码助手", Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Runnable() {
                        @Override
                        public void run() {
                            MainActivity.super.onBackPressed();
                        }
                    }
            );
        }
        mDoubleTabExitController.onProcessBack();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SpotManager.getInstance(this).onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SpotManager.getInstance(this).onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SpotManager.getInstance(this).onDestroy();
    }
}
