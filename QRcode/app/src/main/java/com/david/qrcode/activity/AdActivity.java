package com.david.qrcode.activity;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.david.qrcode.R;

import net.youmi.android.nm.cm.ErrorCode;
import net.youmi.android.nm.sp.SplashViewSettings;
import net.youmi.android.nm.sp.SpotListener;
import net.youmi.android.nm.sp.SpotManager;

import butterknife.ButterKnife;

/**
 * Created by david on 2017/5/24.
 */

public class AdActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);
        ButterKnife.bind(this);

        setupSplashAd();

    }

    private void setupSplashAd() {
        // 创建开屏容器
        final RelativeLayout splashLayout = (RelativeLayout) findViewById(R.id.rl_splash);
        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.ABOVE, R.id.view_divider);

        // 对开屏进行设置
        SplashViewSettings splashViewSettings = new SplashViewSettings();
        //		// 设置是否展示失败自动跳转，默认自动跳转
        //		splashViewSettings.setAutoJumpToTargetWhenShowFailed(false);
        // 设置跳转的窗口类
        splashViewSettings.setTargetClass(MainActivity.class);
        // 设置开屏的容器
        splashViewSettings.setSplashViewContainer(splashLayout);

        // 展示开屏广告
        SpotManager.getInstance(this)
                .showSplash(this, splashViewSettings, new SpotListener() {

                    @Override
                    public void onShowSuccess() {
//                        CLog.d("david", "开屏展示成功");
                    }

                    @Override
                    public void onShowFailed(int errorCode) {
//                        CLog.d("david", "开屏展示失败");
//                        switch (errorCode) {
//                            case ErrorCode.NON_NETWORK:
//                                CLog.d("david", "网络异常");
//                                break;
//                            case ErrorCode.NON_AD:
//                                CLog.d("david", "暂无开屏广告");
//                                break;
//                            case ErrorCode.RESOURCE_NOT_READY:
//                                CLog.d("david", "开屏资源还没准备好");
//                                break;
//                            case ErrorCode.SHOW_INTERVAL_LIMITED:
//                                CLog.d("david", "开屏展示间隔限制");
//                                break;
//                            case ErrorCode.WIDGET_NOT_IN_VISIBILITY_STATE:
//                                CLog.d("david", "开屏控件处在不可见状态");
//                                break;
//                            default:
//                                CLog.d("david", "errorCode:" + errorCode);
//                                break;
//                        }
                    }

                    @Override
                    public void onSpotClosed() {
//                        CLog.d("david", "开屏被关闭");
                    }

                    @Override
                    public void onSpotClicked(boolean isWebPage) {
//                        CLog.d("david", "开屏被点击");
                    }
                });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        SpotManager.getInstance(this).onDestroy();
    }
}
