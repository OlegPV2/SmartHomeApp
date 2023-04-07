package com.oleg.smarthomedashboard.update;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;

import org.json.JSONObject;

public abstract class RetrieveJSON {
    private final Activity activity;
    String jsonUrl;
    UpdateListener listener;

    public RetrieveJSON(Activity activity, String jsonUrl, UpdateListener listener) {
        this.activity = activity;
        this.jsonUrl = jsonUrl;
        this.listener = listener;
    }

    private void startBackground() {
        boolean cancel = false;
        if (listener == null || jsonUrl == null) {
//            Log.d(TAG, "onPreExecute: context == null || listener == null || jsonUrl == null");
            cancel = true;
        } else if (!isNetworkAvailable(activity)) {
            listener.onError("Please check your network connection");
            cancel = true;
        } else if (jsonUrl.isEmpty()) {
            listener.onError("Please provide a valid JSON URL");
            cancel = true;
        }

        if(!cancel) new Thread(() -> {

            JSONObject jsonObject = doInBackground();
            activity.runOnUiThread(() -> onPostExecute(jsonObject));

        }).start();
    }
    public void execute(){
        startBackground();
    }

    private boolean isNetworkAvailable(Context ctx) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network nw = connectivityManager.getActiveNetwork();
        if (nw == null) return false;
        NetworkCapabilities actNw = connectivityManager.getNetworkCapabilities(nw);
        return actNw != null && (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI));
    }

    public static int getCurrentVersionCode(Context context) {
        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (pInfo != null)
            return (int) pInfo.getLongVersionCode();

        return 0;
    }

    public abstract JSONObject doInBackground();
    public abstract void onPostExecute(JSONObject jsonObject);

}
