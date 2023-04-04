package com.oleg.smarthomedashboard;

import android.util.Log;

import com.king.app.dialog.AppDialog;
import com.king.app.dialog.AppDialogConfig;
import com.king.app.updater.AppUpdater;
import com.king.app.updater.callback.UpdateCallback;
import com.king.app.updater.http.OkHttpManager;

import org.json.JSONObject;

import java.io.File;

public class CheckUpdate {
    protected static final String jsonUrl = "https://raw.githubusercontent.com/OlegPV2/SmartHomeApp/master/update.json"; //"https://github.com/OlegPV2/SmartHomeApp/releases/latest/download/Smart_Home_App.apk"
    protected static UpdateModel JSONData;

    public static void checkUpdate(MainActivity mainActivity) {
        RetrieveJSON task = null;
        task = (RetrieveJSON) new RetrieveJSON(mainActivity, jsonUrl, new UpdateListener() {
            @Override
            public void onJsonDataReceived(UpdateModel updateModel, JSONObject jsonObject) {
                if (RetrieveJSON.getCurrentVersionCode(mainActivity) < updateModel.getVersionCode()) {
                    JSONData = updateModel;
                }
            }

            @Override
            public void onError(String error) {

            }
        }).execute();
//        downloadAndInstall(mainActivity);
//        task.cancel(false);
    }

    protected static void downloadAndInstall(MainActivity mainActivity) {
        AppDialogConfig config = new AppDialogConfig(mainActivity);
        config.setTitle("Upgrade available")
                .setConfirm("Upgrade")
                .setContent(JSONData.updateMessage)
                .setOnClickConfirm(v -> {
                    AppUpdater appUpdater = new AppUpdater.Builder()
                            .setUrl(JSONData.url)
                            .build(mainActivity);
                    appUpdater.setHttpManager(OkHttpManager.getInstance())
                            .setUpdateCallback(new UpdateCallback() {
                                @Override
                                public void onDownloading(boolean isDownloading) {
                                    // Downloading: When isDownloading is true, it means that the
                                    // download is already in progress, that is, the download has been
                                    // started before; when it is false, it means that the download
                                    // has not started yet, and the download will start soon
                                }

                                @Override
                                public void onStart(String url) {

                                }

                                @Override
                                public void onProgress(long progress, long total, boolean isChanged) {
                                    // Download progress update: It is recommended to update the
                                    // progress of the interface only when isChanged is true;
                                    // because the actual progress changes frequently
                                }

                                @Override
                                public void onFinish(File file) {

                                }

                                @Override
                                public void onError(Exception e) {

                                }

                                @Override
                                public void onCancel() {

                                }
                            }).start();

                    AppDialog.INSTANCE.dismissDialogFragment(mainActivity.getSupportFragmentManager());
                });
        if (JSONData.cancellable) config.setCancel("Cancel");
        AppDialog.INSTANCE.showDialogFragment(mainActivity.getSupportFragmentManager(), config);
    }
}
