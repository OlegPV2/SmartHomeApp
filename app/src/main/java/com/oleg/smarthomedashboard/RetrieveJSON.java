package com.oleg.smarthomedashboard;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class RetrieveJSON extends AsyncTask<Void, Void, JSONObject> {

//    String TAG = "RetrieveJSON";

    private final WeakReference<Context> context;
    String jsonUrl;
    UpdateListener listener;

    public RetrieveJSON(Context context, String jsonUrl, UpdateListener listener) {
        this.context = new WeakReference<>(context);
        this.jsonUrl = jsonUrl;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (listener == null || jsonUrl == null) {
//            Log.d(TAG, "onPreExecute: context == null || listener == null || jsonUrl == null");
            cancel(true);
        } else if (!isNetworkAvailable(context)) {
            listener.onError("Please check your network connection");
            cancel(true);
        } else if (jsonUrl.isEmpty()) {
            listener.onError("Please provide a valid JSON URL");
            cancel(true);
        }
    }

    @Override
    protected JSONObject doInBackground(Void... voids) {
        try {
            URL url = new URL(jsonUrl);
            InputStream is = url.openStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = bufferedReader.read()) != -1) {
                sb.append((char) cp);
            }

//            Log.d(TAG, "doInBackground: JSON DATA: " + sb);
            is.close();

            return new JSONObject(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);

        if (jsonObject != null) {
            try {
                UpdateModel updateModel = new UpdateModel(
                        jsonObject.getString("url"),
                        jsonObject.getString("fileName"),
                        jsonObject.getInt("versionCode"),
                        jsonObject.getBoolean("cancellable"),
                        jsonObject.getString("updateMessage")
                );

                listener.onJsonDataReceived(updateModel, jsonObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            listener.onError("JSON data null");
        }
    }

    private boolean isNetworkAvailable(WeakReference<Context> ctx) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) ctx.get().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static int getCurrentVersionCode(Context context) {
        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (pInfo != null)
            return pInfo.versionCode;

        return 0;
    }
}