package com.david.qrcode.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.david.qrcode.R;


public class DialogFactory {

    public static Dialog loading = null;

    /**
     * 5.0以上系统不会弹出时出现状态栏的黑边
     *
     * @param context
     * @param view
     * @return
     */
    public static Dialog creatPerfectDialog(Context context, View view) {
        Dialog loading = new Dialog(context, R.style.ScanDialog);
        loading.setCancelable(true);

        DisplayMetrics metric = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;// 屏幕宽度（像素）
        loading.setContentView(view, new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT));

        return loading;
    }
}
