package com.oleg.smarthomedashboard.fragments.elements;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;

import com.oleg.smarthomedashboard.MainActivity;

public class MetersInfo {
    private final int titleTextID;
    private final int meterCorrectionDecreaseID;
    private final int meterCorrectionValueID;
    private final int meterCorrectionIncreaseID;

    @SuppressLint("DiscouragedApi")
    public MetersInfo(String titleTextID, String meterCorrectionDecreaseID, String meterCorrectionValueID, String meterCorrectionIncreaseID) {
        Context context = MainActivity.getContext();
        Resources resources = context.getResources();
        this.titleTextID = resources.getIdentifier(titleTextID, "string", context.getPackageName());
        this.meterCorrectionDecreaseID = resources.getIdentifier(meterCorrectionDecreaseID, "string", context.getPackageName());
        this.meterCorrectionValueID = resources.getIdentifier(meterCorrectionValueID, "string", context.getPackageName());
        this.meterCorrectionIncreaseID = resources.getIdentifier(meterCorrectionIncreaseID, "string", context.getPackageName());
    }

    public int getTitleTextID() {
        return titleTextID;
    }

    public int getMeterCorrectionDecreaseID() {
        return meterCorrectionDecreaseID;
    }

    public int getMeterCorrectionValueID() {
        return meterCorrectionValueID;
    }

    public int getMeterCorrectionIncreaseID() {
        return meterCorrectionIncreaseID;
    }
}
