package com.oleg.smarthomedashboard;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.util.Log;

import com.oleg.smarthomedashboard.helper.ConfigurationButtonsHelper;
import com.oleg.smarthomedashboard.helper.ConfigurationHelper;
import com.oleg.smarthomedashboard.helper.SettingsMetersHelper;
import com.oleg.smarthomedashboard.model.ButtonTypes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

public class ConfigFromJSON {
    private final Activity activity;

    private final List<ConfigurationHelper> configurationHelperList;
    private final List<SettingsMetersHelper> settingsMetersHelperList;

    public ConfigFromJSON(Activity activity, String jsonUrl, List<ConfigurationHelper> configurationHelperList, List<SettingsMetersHelper> settingsMetersHelperList) {
        this.activity = activity;
        this.configurationHelperList = configurationHelperList;
        this.settingsMetersHelperList = settingsMetersHelperList;
        JSONObject jsonObject = loadJSONData(jsonUrl);
        if (jsonObject != null) parsingData(jsonObject);
    }

    private void parsingData(JSONObject jsonObject) {
        try {
            JSONArray jsonCardsArray = jsonObject.getJSONArray("card");
            ConfigurationHelper configurationHelper;
            for (int i = 0; i < jsonCardsArray.length(); i++) {
                JSONObject jsonCard = jsonCardsArray.getJSONObject(i);
                configurationHelper = new ConfigurationHelper(jsonCard.getString("titleTextID"),
                        jsonCard.getString("titleImageID"),
                        jsonCard.getBoolean("showTemperature"),
                        jsonCard.getString("titleTemperatureID"),
                        jsonCard.getBoolean("showHumidity"),
                        jsonCard.getString("titleHumidityID"),
                        getButton(jsonCard.getJSONObject("button1")),
                        getButton(jsonCard.getJSONObject("button2")),
                        getButton(jsonCard.getJSONObject("button3")),
                        getButton(jsonCard.getJSONObject("button4"))
                );
                configurationHelperList.add(configurationHelper);
            }
            jsonCardsArray = jsonObject.getJSONArray("meters");
            SettingsMetersHelper settingsMetersHelper;
            for (int i = 0; i < jsonCardsArray.length(); i++) {
                JSONObject jsonMeters = jsonCardsArray.getJSONObject(i);
                settingsMetersHelper = new SettingsMetersHelper(
                        jsonMeters.getString("titleTextID"),
                        jsonMeters.getString("meterCorrectionDecreaseID"),
                        jsonMeters.getString("meterCorrectionValueID"),
                        jsonMeters.getString("meterCorrectionIncreaseID")
                );
                settingsMetersHelperList.add(settingsMetersHelper);
            }
        } catch (JSONException e) {
            Log.e("parsingData", String.valueOf(e));
        }
    }

    private ConfigurationButtonsHelper getButton(JSONObject jsonObject) {
        ConfigurationButtonsHelper configurationButtonsHelper = null;
        try {
            switch (ButtonTypes.values()[jsonObject.getInt("buttonType")]) {
                case LIGHT:
                    configurationButtonsHelper = new ConfigurationButtonsHelper(
                            jsonObject.getInt("buttonType"),
                            jsonObject.getString("buttonID"),
                            jsonObject.getString("buttonDrawableID"),
                            jsonObject.getBoolean("buttonClickable"),
                            jsonObject.getBoolean("dimming"),
                            jsonObject.getString("dimmingSwitchID"),
                            jsonObject.getString("dimmingSliderID"),
                            jsonObject.getString("note")
                    );
                    break;
                case FAN:
                case AUTO:
                case DOOR:
                case WINDOW:
                case NOTHING:
                    configurationButtonsHelper = new ConfigurationButtonsHelper(
                            jsonObject.getInt("buttonType"),
                            jsonObject.getString("buttonID"),
                            jsonObject.getString("buttonDrawableID"),
                            jsonObject.getBoolean("buttonClickable"),
                            jsonObject.getString("note")
                    );
                    break;
                case WARM_FLOOR:
                    configurationButtonsHelper = new ConfigurationButtonsHelper(
                            jsonObject.getInt("buttonType"),
                            jsonObject.getString("buttonID"),
                            jsonObject.getString("buttonDrawableID"),
                            jsonObject.getBoolean("buttonClickable"),
                            jsonObject.getInt("warmFloorPreset"),
                            jsonObject.getString("warmFloorDecreaseID"),
                            jsonObject.getString("warmFloorTemperatureID"),
                            jsonObject.getString("warmFloorIncreaseID"),
                            jsonObject.getString("note")
                    );
                    break;
            }
        } catch (JSONException e) {
            Log.e("getButton", String.valueOf(e));
        }
        return configurationButtonsHelper;
    }

    private boolean isNetworkAvailable(Context ctx) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network nw = connectivityManager.getActiveNetwork();
        if (nw == null) return false;
        NetworkCapabilities actNw = connectivityManager.getNetworkCapabilities(nw);
        return actNw != null && (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI));
    }

    private JSONObject loadJSONData(String jsonUrl) {
        try {
            StringBuilder sb = new StringBuilder();
            if (Objects.equals(jsonUrl, "") || !isNetworkAvailable(activity)) {
                sb.append(loadJSONFromAsset());
            } else {
                Log.d("ConfigFromJSON", "Load json");
                URL url = new URL(jsonUrl);
                InputStream is = url.openStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

                int cp;
                while ((cp = bufferedReader.read()) != -1) {
                    sb.append((char) cp);
                }
                is.close();
            }
            return new JSONObject(sb.toString());
        } catch (Exception e) {
            Log.e("loadJSONData", String.valueOf(e));
        }

        return null;
    }

    private String loadJSONFromAsset() {
        String json;
        try {
            InputStream is = activity.getAssets().open("config.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            Log.e("loadJSONFromAsset", String.valueOf(ex));
            return null;
        }
        return json;
    }
}