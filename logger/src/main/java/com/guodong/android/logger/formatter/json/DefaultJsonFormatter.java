package com.guodong.android.logger.formatter.json;

import com.guodong.android.logger.internal.Platform;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by guodongAndroid on 2024/5/7 13:28.
 */
public final class DefaultJsonFormatter implements JsonFormatter {

    private static final int JSON_INDENT = 4;

    @Override
    public String format(String json) {
        String formattedString;
        if (json == null || json.trim().isEmpty()) {
            Platform.get().warn("JSON empty.");
            return "";
        }

        try {
            if (json.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(json);
                formattedString = jsonObject.toString(JSON_INDENT);
            } else if (json.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(json);
                formattedString = jsonArray.toString(JSON_INDENT);
            } else {
                Platform.get().warn("JSON should start with { or [");
                return json;
            }
        } catch (Exception e) {
            Platform.get().warn(e.getMessage());
            return json;
        }

        return formattedString;
    }
}
