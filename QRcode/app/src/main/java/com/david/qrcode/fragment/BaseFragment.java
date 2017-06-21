package com.david.qrcode.fragment;


import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by david on 2017/5/22.
 */
public abstract class BaseFragment extends Fragment {

    protected Context mContext;

    protected View rootView;

    protected boolean hasLoadData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();

        if (null == rootView) {
            rootView = inflater.inflate(getLayoutId(), null);
            ButterKnife.bind(this, rootView);
            initView();
            initListener();
        }
        if (!hasLoadData) {
            initData();
        }
        return rootView;
    }

    @Override
    public void onDestroyView() {
        if (rootView != null) {
            ((ViewGroup) rootView.getParent()).removeView(rootView);
        }
        super.onDestroyView();
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected void initListener() {
    }

    protected abstract void initData();

    public String getTitle() {
        return getClass().getSimpleName();
    }

    protected <V extends View> V findViewById(int id) {
        return (V) rootView.findViewById(id);
    }

    protected void requestPermission(String permission, int permissionCode) {
        if (ContextCompat.checkSelfPermission(mContext, permission) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{permission}, permissionCode);
        } else {
            onPermissionSuccess(permissionCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            onPermissionSuccess(requestCode);
        } else {
            onPermissionFaliure(requestCode);
        }
    }

    protected void onPermissionSuccess(int requestCode) {
    }

    protected void onPermissionFaliure(int requestCode) {
    }
}
