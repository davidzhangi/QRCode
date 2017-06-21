package com.david.qrcode.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.CardView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.david.qrcode.R;
import com.david.qrcode.activity.BaseActivity;
import com.david.qrcode.utils.StringUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.Hashtable;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by david on 2017/5/22.
 */
public class QRCodeEncodeFragment extends BaseFragment {

    @BindView(R.id.image_view)
    ImageView imageView;
    @BindView(R.id.input_tv)
    TextInputEditText inputTv;
    @BindView(R.id.button_next)
    CardView buttonNext;
    @BindView(R.id.tv_toolbar_left_text)
    TextView tvToolbarLeftText;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_qrcode_encode;
    }

    @Override
    protected void initView() {
        tvToolbarLeftText.setText("生成二维码");

        inputTv.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (KeyEvent.KEYCODE_ENTER == keyCode && KeyEvent.ACTION_DOWN == event.getAction()) {
                    return true;
                }
                return false;
            }
        });
    }


    @Override
    protected void initData() {

    }

    @OnClick(R.id.button_next)
    public void nextClick() {
        String s = inputTv.getText().toString();
        if (StringUtils.isEmpty(s)) {
            Toast.makeText(mContext, "请输入二维码信息", Toast.LENGTH_SHORT).show();
            return;
        }

        getORCode(s);
        ((BaseActivity) mContext).hideSoftInput();
    }

    public Bitmap mBitmap = null;

    private void getORCode(String s) {
        mBitmap = createImage(s, 400, 400, BitmapFactory.decodeResource(getResources(), R.mipmap.icon_logo));
//        mBitmap = createImage(s, 400, 400, null);
        imageView.setImageBitmap(mBitmap);
    }

    /**
     * 生成二维码图片
     *
     * @param text
     * @param w
     * @param h
     * @param logo
     * @return
     */
    public static Bitmap createImage(String text, int w, int h, Bitmap logo) {
        if (StringUtils.isEmpty(text)) {
            return null;
        }
        try {
            Bitmap scaleLogo = getScaleLogo(logo, w, h);

            int offsetX = w / 2;
            int offsetY = h / 2;

            int scaleWidth = 0;
            int scaleHeight = 0;
            if (scaleLogo != null) {
                scaleWidth = scaleLogo.getWidth();
                scaleHeight = scaleLogo.getHeight();
                offsetX = (w - scaleWidth) / 2;
                offsetY = (h - scaleHeight) / 2;
            }
            Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //容错级别
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            //设置空白边距的宽度
            hints.put(EncodeHintType.MARGIN, 0);
            BitMatrix bitMatrix = new QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, w, h, hints);
            int[] pixels = new int[w * h];
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    if (x >= offsetX && x < offsetX + scaleWidth && y >= offsetY && y < offsetY + scaleHeight) {
                        int pixel = scaleLogo.getPixel(x - offsetX, y - offsetY);
                        if (pixel == 0) {
                            if (bitMatrix.get(x, y)) {
                                pixel = 0xff000000;
                            } else {
                                pixel = 0xffffffff;
                            }
                        }
                        pixels[y * w + x] = pixel;
                    } else {
                        if (bitMatrix.get(x, y)) {
                            pixels[y * w + x] = 0xff000000;
                        } else {
                            pixels[y * w + x] = 0xffffffff;
                        }
                    }
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(w, h,
                    Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Bitmap getScaleLogo(Bitmap logo, int w, int h) {
        if (logo == null) return null;
        Matrix matrix = new Matrix();
        float scaleFactor = Math.min(w * 1.0f / 5 / logo.getWidth(), h * 1.0f / 5 / logo.getHeight());
        matrix.postScale(scaleFactor, scaleFactor);
        Bitmap result = Bitmap.createBitmap(logo, 0, 0, logo.getWidth(), logo.getHeight(), matrix, true);
        return result;
    }
}
