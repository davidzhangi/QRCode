package com.david.qrcode.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.david.qrcode.R;

import net.youmi.android.nm.cm.ErrorCode;
import net.youmi.android.nm.sp.SpotListener;
import net.youmi.android.nm.sp.SpotManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

/**
 * Created by david on 2017/5/22.
 */
public class DisplayResultsActivity extends AppCompatActivity {

    @BindView(R.id.tv_toolbar_left_text)
    TextView tvToolbarLeftText;
    @BindView(R.id.result_tv)
    TextView resultTv;

    @BindView(R.id.left_cope)
    RippleView left_cope;
    @BindView(R.id.right_open)
    RippleView right_open;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_results);
        ButterKnife.bind(this);

        tvToolbarLeftText.setText("扫描结果");

        setupSpotAd();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }

        resultTv.setText(intent.getStringExtra("result"));
        resultTv.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @OnClick(R.id.left_cope)
    public void copeClick() {
        ClipboardManager mClipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mClipData = ClipData.newPlainText("Label", resultTv.getText().toString());
        mClipboardManager.setPrimaryClip(mClipData);
    }

    @OnClick(R.id.right_open)
    public void openClick() {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TOP);
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(resultTv.getText().toString());
        intent.setData(content_url);
        startActivity(intent);
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
//        SpotManager.getInstance(this).setImageType(SpotManager.IMAGE_TYPE_VERTICAL);

        // 设置动画类型，默认高级动画
        //		// 无动画
        //		SpotManager.getInstance(mContext).setAnimationType(SpotManager
        // .ANIMATION_TYPE_NONE);
        //		// 简单动画
        //		SpotManager.getInstance(mContext).setAnimationType(SpotManager
        // .ANIMATION_TYPE_SIMPLE);
        // 高级动画
//        SpotManager.getInstance(this)
//                .setAnimationType(SpotManager.ANIMATION_TYPE_ADVANCED);

        // 展示插屏广告
        SpotManager.getInstance(this).showSpot(this, new SpotListener() {

            @Override
            public void onShowSuccess() {
//                CLog.d("david", "插屏展示成功");
            }

            @Override
            public void onShowFailed(int errorCode) {
//                CLog.d("david", "插屏展示失败");
//                switch (errorCode) {
//                    case ErrorCode.NON_NETWORK:
//                        CLog.d("david", "网络异常");
//                        break;
//                    case ErrorCode.NON_AD:
//                        CLog.d("david", "暂无插屏广告");
//                        break;
//                    case ErrorCode.RESOURCE_NOT_READY:
//                        CLog.d("david", "插屏资源还没准备好");
//                        break;
//                    case ErrorCode.SHOW_INTERVAL_LIMITED:
//                        CLog.d("david", "请勿频繁展示");
//                        break;
//                    case ErrorCode.WIDGET_NOT_IN_VISIBILITY_STATE:
//                        CLog.d("david", "请设置插屏为可见状态");
//                        break;
//                    default:
//                        CLog.d("david", "请稍后再试");
//                        break;
//                }
            }

            @Override
            public void onSpotClosed() {
//                CLog.d("david", "插屏被关闭");
            }

            @Override
            public void onSpotClicked(boolean isWebPage) {
//                CLog.d("david", "插屏被点击");
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (SpotManager.getInstance(this).isSpotShowing()) {
            SpotManager.getInstance(this).hideSpot();
            return;
        }
        super.onBackPressed();
    }
}
