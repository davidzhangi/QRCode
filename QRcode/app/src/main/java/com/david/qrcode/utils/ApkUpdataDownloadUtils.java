package com.david.qrcode.utils;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static android.content.Context.DOWNLOAD_SERVICE;
import static android.os.Environment.DIRECTORY_NOTIFICATIONS;

/**
 * Created by David on 16/11/21.
 */

public class ApkUpdataDownloadUtils {

    public static final int HANDLE_DOWNLOAD = 1;//下载中
    public static final int FINISH_DOWNLOAD = -1;//下载完成

    private Context context;

    private String downloadUrl;//下载url
    private long downloadId;

    private DownloadManager downloadManager;

    private DownLoadReceiver downLoadReceiver;//监听下载广播
    private DownloadObserver downloadObserver;
    private ScheduledExecutorService scheduledExecutorService;

    private OnProgressListener onProgressListener;
    public Handler downLoadHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (onProgressListener != null && HANDLE_DOWNLOAD == msg.what) {
                //被除数可以为0，除数必须大于0
                if (msg.arg1 >= 0 && msg.arg2 > 0) {
                    onProgressListener.onProgress((int) (msg.arg1 / (float) msg.arg2 * 100));
                }
            }
        }
    };
    private Runnable progressRunnable = new Runnable() {
        @Override
        public void run() {
            updateProgress();
        }
    };

    public ApkUpdataDownloadUtils(Context context) {
        this.context = context;

        downloadApk(context);
    }

    public ApkUpdataDownloadUtils(Context context, OnProgressListener onProgressListener) {
        this.context = context;
        this.onProgressListener = onProgressListener;

        downloadApk(context);
    }

    public void downloadApk(Context context) {

        try {
            downloadUrl = PrefUtil.getStringPref(context, Const.UPDATE_URL);
            downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUrl));
            /**设置用于下载时的网络状态*/
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
            /**设置通知栏是否可见*/
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            /**设置漫游状态下是否可以下载*/
            request.setAllowedOverRoaming(false);
            /**如果我们希望下载的文件可以被系统的Downloads应用扫描到并管理，
             我们需要调用Request对象的setVisibleInDownloadsUi方法，传递参数true.*/
            request.setVisibleInDownloadsUi(true);
            /**设置文件保存路径*/
            request.setDestinationInExternalFilesDir(context, DIRECTORY_NOTIFICATIONS, "jiudouyu.apk");
            /**将下载请求放入队列， return下载任务的ID*/
            downloadId = downloadManager.enqueue(request);
            // 把当前下载的ID保存起来
            PrefUtil.savePref(context, Const.KEY_DOWNLOAD_ID, downloadId);
//        注册内容观察者
            registerContentObserver();
//        注册广播接受者
            registerBroadcast();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送Handler消息更新进度和状态
     */
    private void updateProgress() {
        int[] bytesAndStatus = getBytesAndStatus(downloadId);
        downLoadHandler.sendMessage(downLoadHandler.obtainMessage(HANDLE_DOWNLOAD, bytesAndStatus[0], bytesAndStatus[1], bytesAndStatus[2]));
    }

    /**
     * 通过query查询下载状态，包括已下载数据大小，总大小，下载状态
     *
     * @param downloadId
     * @return
     */
    private int[] getBytesAndStatus(long downloadId) {
        int[] bytesAndStatus = new int[]{
                -1, -1, 0
        };
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        Cursor cursor = null;
        try {
            cursor = downloadManager.query(query);
            if (cursor != null && cursor.moveToFirst()) {
                //已经下载文件大小
                bytesAndStatus[0] = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                //下载文件的总大小
                bytesAndStatus[1] = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                //下载状态
                bytesAndStatus[2] = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return bytesAndStatus;
    }

    /**
     * 注册内容观察者
     */
    private void registerContentObserver() {
        if (downloadObserver == null) {
            downloadObserver = new DownloadObserver();
        }
        context.getContentResolver().registerContentObserver(Uri.parse("content://downloads/"), true, downloadObserver);
    }

    /**
     * 注册广播
     */
    private void registerBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        intentFilter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED);
        context.registerReceiver(downLoadReceiver = new DownLoadReceiver(), intentFilter);
    }

    /**
     * 安装apk
     *
     * @param doidwnloadId
     */
    public void installApk(Long doidwnloadId) {
        Intent install = new Intent(Intent.ACTION_VIEW);
        File apkFile = queryDownloadedApk(doidwnloadId);
        if (apkFile != null && apkFile.exists()) {
            install.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(install);
        }
    }

    public File queryDownloadedApk(long downloadId) {
        File targetApkFile = null;
        if (downloadId != -1) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(downloadId);
            query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL);
            Cursor cur = downloadManager.query(query);
            if (cur != null) {
                if (cur.moveToFirst()) {
                    String uriString = cur.getString(cur.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                    if (!TextUtils.isEmpty(uriString)) {
                        targetApkFile = new File(Uri.parse(uriString).getPath());
                    }
                }
                cur.close();
            }
        }
        return targetApkFile;
    }

    /**
     * 注销内容观察者
     */
    private void unregisterContentObserver() {
        if (downloadObserver != null) {
            context.getContentResolver().unregisterContentObserver(downloadObserver);
        }
    }

    /**
     * 注销广播
     */
    private void unregisterBroadcast() {
        if (downLoadReceiver != null) {
            context.unregisterReceiver(downLoadReceiver);
            downLoadReceiver = null;
        }
    }

    /**
     * 关闭资源
     */
    public void onDestroy() {
        if (scheduledExecutorService != null && !scheduledExecutorService.isShutdown()) {
            scheduledExecutorService.shutdown();
        }

        if (downLoadHandler != null) {
            downLoadHandler.removeCallbacksAndMessages(null);
        }
        unregisterBroadcast();
        unregisterContentObserver();
    }

    /**
     * 回调下载进度
     */
    public interface OnProgressListener {
        void onProgress(int fraction);
    }

    /**
     * 监听下载进度
     */
    private class DownloadObserver extends ContentObserver {

        public DownloadObserver() {
            super(downLoadHandler);
            scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        }

        @Override
        public void onChange(boolean selfChange) {
            scheduledExecutorService.scheduleAtFixedRate(progressRunnable, 0, 100, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * 接受下载完成广播
     */
    private class DownLoadReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            long myDwonloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            long downloadId = PrefUtil.getLongPref(context, Const.KEY_DOWNLOAD_ID, 0L);

            if (downloadId == myDwonloadID) {
                installApk(downloadId);
                onDestroy();
            }

            if (onProgressListener != null) {
                onProgressListener.onProgress(FINISH_DOWNLOAD);
            }
        }
    }
}
