package com.david.qrcode.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.david.qrcode.R;
import com.david.qrcode.activity.DisplayResultsActivity;
import com.david.qrcode.utils.ImageUtil;
import com.david.qrcode.utils.QRCodeDecoder;
import com.github.glomadrian.loadingballs.BallView;

import butterknife.BindView;
import butterknife.OnClick;
import david.library.view.PointsOverlayView;
import david.library.view.QRCodeReaderView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by david on 2017/5/22.
 */
public class QRCodeDecodeFragment extends BaseFragment implements QRCodeReaderView.OnQRCodeReadListener {

    @BindView(R.id.qrdecoder_view)
    QRCodeReaderView qrCodeReaderView;

    @BindView(R.id.points_overlay_view)
    PointsOverlayView pointsOverlayView;

    @BindView(R.id.flash_light_layout)
    LinearLayout flash_light_layout;
    @BindView(R.id.flash_light)
    ImageView flash_light;

    @BindView(R.id.local_file_layout)
    LinearLayout local_file_layout;

    @BindView(R.id.progress_ball_view)
    BallView progress_ball_view;

    private final int CODE_TO_STORAGE = 1;
    public static boolean isOpen = false;//是否开启闪光灯

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            stopCamera();
        } else {
            startCamera();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopCamera();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_qrcode_decode;
    }

    @Override
    protected void initView() {
        qrCodeReaderView.setAutofocusInterval(2000L);
        qrCodeReaderView.setOnQRCodeReadListener(this);
        qrCodeReaderView.setBackCamera();

        qrCodeReaderView.startCamera();
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CODE_TO_STORAGE:
                if (resultCode == RESULT_OK) {
                    Bitmap bitmap = null;
                    if (Build.VERSION.SDK_INT >= 19) {
                        bitmap = ImageUtil.handleImageOnKitKat(mContext, data);
                    } else {
                        bitmap = ImageUtil.handleImageBeforeKitKat(mContext, data);
                    }
                    decoe(bitmap);
                }
                break;
            default:
                break;
        }
    }

    @OnClick(R.id.flash_light_layout)
    public void openFlash() {
        isOpen = !isOpen;
        flash_light.setSelected(isOpen);
        qrCodeReaderView.setTorchEnabled(isOpen);
    }

    @OnClick(R.id.local_file_layout)
    public void openFiles() {
        Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
//        Intent openAlbumIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        Intent openAlbumIntent = new Intent(Intent.ACTION_PICK, null);
        openAlbumIntent.setType("image/*");
        startActivityForResult(openAlbumIntent, CODE_TO_STORAGE);
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        pointsOverlayView.setPoints(points);
        getShowInfo(text);
    }

    private void decoe(final Bitmap bitmap) {
        progress_ball_view.setVisibility(View.VISIBLE);

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                return QRCodeDecoder.syncDecodeQRCode(bitmap);
            }

            @Override
            protected void onPostExecute(String result) {
                progress_ball_view.setVisibility(View.INVISIBLE);

                if (TextUtils.isEmpty(result)) {
                    Toast.makeText(mContext, "图片识别失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                getShowInfo(result);
            }
        }.execute();
    }

    private void getShowInfo(String s) {
        stopCamera();

        Intent intent = new Intent(mContext, DisplayResultsActivity.class);
        intent.putExtra("result", s);
        startActivity(intent);
    }

    private void startCamera() {
        if (qrCodeReaderView != null) {
            qrCodeReaderView.startCamera();
        }
    }

    private void stopCamera() {
        if (qrCodeReaderView != null) {
            qrCodeReaderView.stopCamera();
        }
    }
}
