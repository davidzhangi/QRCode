package com.david.qrcode.utils;

import android.os.Handler;

public class DoubleTabExitController {

    private Runnable mActionForTip;
    private Runnable mActionForExit;
    private boolean mDoubleBackToExitPressedOnce = false;
    private int mTapTimeInterval;

    public DoubleTabExitController(int tapTimeInterval, Runnable actionForTip, Runnable actionForExit) {
        if (tapTimeInterval <= 0 || actionForTip == null || actionForExit == null) {
            throw new IllegalArgumentException("You may miss something.");
        }
        mTapTimeInterval = tapTimeInterval;
        mActionForTip = actionForTip;
        mActionForExit = actionForExit;
    }

    public void onProcessBack() {
        if (mDoubleBackToExitPressedOnce) {
            mActionForExit.run();
            return;
        }
        mDoubleBackToExitPressedOnce = true;
        mActionForTip.run();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                mDoubleBackToExitPressedOnce = false;
            }
        }, mTapTimeInterval);
    }
}
