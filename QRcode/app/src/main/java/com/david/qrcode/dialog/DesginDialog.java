package com.david.qrcode.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

/**
 * 文 件 名:MD风格Dialog
 * 创 建 人: wlvoid
 * 创建日期: 2017/3/20 11:27
 * 修改时间：
 * 修改备注：
 */
public class DesginDialog {

    public static boolean hasShow = false;

    /**
     * 回调事件
     */
    public interface DialogListener {

        void positive();

        void negative();
    }


    /**
     * 初始化两个按钮Dialog（默认不可通过点击其他地方消除dialog）
     *
     * @param context        上下文对象
     * @param title          标题
     * @param content        内容
     * @param positiveStr    按钮内容
     * @param negativeStr    按钮内容
     * @param dialogListener 按钮点击事件
     */
    public void init2BtnDialog(final Context context, String title, final String content, String positiveStr, String negativeStr, final DialogListener dialogListener) {
        if (hasShow) {
            return;
        } else {
            hasShow = true;
        }
        new MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .cancelable(false)
                .positiveText(positiveStr)
                .negativeText(negativeStr)
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        hasShow = false;
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        dialogListener.positive();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        dialogListener.negative();
                    }
                })
                .show();
    }


    /**
     * 初始化两个按钮Dialog（默认不可通过点击其他地方消除dialog）
     *
     * @param context        上下文对象
     * @param title          标题
     * @param content        内容
     * @param positiveStr    按钮内容
     * @param negativeStr    按钮内容
     * @param dialogListener 按钮点击事件
     */
    public void initImageDialog(final Context context, String title, final String imageUrl, String positiveStr, String negativeStr, final DialogListener dialogListener) {
        if (hasShow) {
            return;
        } else {
            hasShow = true;
        }
        new MaterialDialog.Builder(context)
                .title(title)
                .cancelable(false)
                .positiveText(positiveStr)
                .negativeText(negativeStr)
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        hasShow = false;
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        dialogListener.positive();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        dialogListener.negative();
                    }
                })
                .show();
    }


    /**
     * 初始化一个按钮Dialog（默认不可通过点击其他地方消除dialog）
     *
     * @param context        上下文对象
     * @param title          标题
     * @param content        内容
     * @param positiveStr    按钮内容
     * @param dialogListener 按钮点击事件
     */
    public void init1BtnDialog(Context context, String title, String content, String positiveStr, final DialogListener dialogListener) {
        if (hasShow) {
            return;
        } else {
            hasShow = true;
        }
        new MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .cancelable(false)
                .positiveText(positiveStr)
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        hasShow = false;
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        dialogListener.positive();
                    }
                })
                .show();
    }


    /**
     * 初始化一个按钮Dialog（默认不可通过点击其他地方消除dialog）
     *
     * @param context     上下文对象
     * @param title       标题
     * @param content     内容
     * @param positiveStr 按钮内容
     */
    public void init1BtnDialog(Context context, String title, String content, String positiveStr) {
        if (hasShow) {
            return;
        } else {
            hasShow = true;
        }
        new MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .cancelable(false)
                .positiveText(positiveStr)
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        hasShow = false;
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    /**
     * 初始化两个按钮Dialog
     *
     * @param context        上下文对象
     * @param cancelable     是否可以通过点击其他地方取消掉
     * @param content        内容
     * @param positiveStr    按钮内容
     * @param negativeStr    按钮内容
     * @param dialogListener 按钮点击事件
     */
    public void init2BtnDialog(Context context, boolean cancelable, String title, String content, String positiveStr, String negativeStr, final DialogListener dialogListener) {
        if (hasShow) {
            return;
        } else {
            hasShow = true;
        }
        new MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .cancelable(cancelable)
                .positiveText(positiveStr)
                .negativeText(negativeStr)
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        hasShow = false;
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        dialogListener.positive();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        dialogListener.negative();
                    }
                })
                .show();
    }


    /**
     * 初始化一个按钮Dialog
     *
     * @param context        上下文对象
     * @param cancelable     是否可以通过点击其他地方取消掉
     * @param content        内容
     * @param positiveStr    按钮内容
     * @param dialogListener 按钮点击事件
     */
    public void init1BtnDialog(Context context, boolean cancelable, String title, String content, String positiveStr, final DialogListener dialogListener) {
        if (hasShow) {
            return;
        } else {
            hasShow = true;
        }
        new MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .cancelable(cancelable)
                .positiveText(positiveStr)
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        hasShow = false;
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        dialogListener.positive();
                    }
                })
                .show();
    }


}
