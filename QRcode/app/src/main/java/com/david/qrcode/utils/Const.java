package com.david.qrcode.utils;


import com.david.qrcode.BuildConfig;

public class Const {

    //    ========配置相关====================
    public static final boolean APP_DEBUG = BuildConfig.DEBUG;//版本区分

    //    ========权限相关====================
    public static final int READ_EXTERNAL_STORAGE_CODE = 0;
    public static final int CAMERA_CODE = 1;
    public static final int WRITE_EXTERNAL_STORAGE_CODE = 2;
    public static final int READ_PHONE_STATE_CODE = 3;

    //    ========app版本更新相关====================
    public static final String UPDATE_LOG = "update_log";//更新内容说明
    public static final String UPDATE_URL = "update_url";//更新下载url
    public static final String APP_SECRET_KEY = "HLK@#$@sdAppha7987";
    public static final String KEY_DOWNLOAD_ID = "key_download_id";
}
