package com.oleg.smarthomedashboard.update;

public class UpdateModel {
    String url;
    String fileName;
    int versionCode;
    boolean cancellable;
    String updateMessage;

    public UpdateModel(String url, String fileName, int versionCode, boolean cancellable, String updateMessage) {
        this.url = url;
        this.fileName = fileName;
        this.versionCode = versionCode;
        this.cancellable = cancellable;
        this.updateMessage = updateMessage;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public boolean isCancellable() {
        return cancellable;
    }

    public void setCancellable(boolean cancellable) {
        this.cancellable = cancellable;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}