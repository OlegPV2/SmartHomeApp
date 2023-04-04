package com.oleg.smarthomedashboard;

import com.king.app.dialog.AppDialog;
import com.king.app.dialog.AppDialogConfig;
import com.king.app.updater.AppUpdater;
import com.king.app.updater.callback.UpdateCallback;
import com.king.app.updater.http.OkHttpManager;

import org.json.JSONObject;

import java.io.File;

public class CheckUpdate {
    protected static final String jsonUrl = "https://raw.githubusercontent.com/OlegPV2/SmartHomeApp/master/update.json"; //"https://github.com/OlegPV2/SmartHomeApp/releases/latest/download/Smart_Home_App.apk"

    public static void checkUpdate(MainActivity mainActivity) {
        new RetrieveJSON(mainActivity, jsonUrl, new UpdateListener() {
            @Override
            public void onJsonDataReceived(UpdateModel updateModel, JSONObject jsonObject) {
                if (RetrieveJSON.getCurrentVersionCode(mainActivity) < updateModel.getVersionCode()) {
                    downloadAndInstall(mainActivity, updateModel);
                }
            }

            @Override
            public void onError(String error) {

            }
        }).execute();
    }

    protected static void downloadAndInstall(MainActivity mainActivity, UpdateModel updateModel) {
        AppDialogConfig config = new AppDialogConfig(mainActivity);
        config.setTitle("Upgrade available")
                .setConfirm("Upgrade")
                .setContent(updateModel.updateMessage)
                .setOnClickConfirm(v -> {
                    AppUpdater appUpdater = new AppUpdater.Builder()
                            .setUrl(updateModel.url + updateModel.fileName)
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
        if (updateModel.cancellable) config.setCancel("Cancel");
        AppDialog.INSTANCE.showDialogFragment(mainActivity.getSupportFragmentManager(), config);
    }
}
