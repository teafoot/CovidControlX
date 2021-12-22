package com.example.covidcontrolx.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JSONUtils {
    public static JSONArray listToJSONArray(List<Object> list) throws JSONException {
        JSONArray jsonArray = new JSONArray();

        for (Object obj : list) {
            if (obj instanceof Map) {
                jsonArray.put(mapToJSONObject((Map) obj));
            } else if (obj instanceof List) {
                jsonArray.put(listToJSONArray((List) obj));
            } else {
                jsonArray.put(obj);
            }
        }

        return jsonArray;
    }

    public static JSONObject mapToJSONObject(Map<String, Object> map) throws JSONException {
        JSONObject jsonObject = new JSONObject();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof Map) {
                Map<String, Object> subMap = (Map<String, Object>) value;
                jsonObject.put(key, mapToJSONObject(subMap));
            } else if (value instanceof List) {
                jsonObject.put(key, listToJSONArray((List) value));
            } else {
                jsonObject.put(key, value);
            }
        }

        return jsonObject;
    }

    public static <T> ArrayList<T> jsonArrayToList(JSONArray jsonArray) throws JSONException {
        ArrayList<T> list = new ArrayList<T>();

        for (int i = 0; i < jsonArray.length(); i++) {
            list.add((T) jsonArray.get(i));
        }

        return list;
    }
}
