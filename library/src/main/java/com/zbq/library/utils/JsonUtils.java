package com.zbq.library.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
    private static final String TAG = "JsonUtils";

    private JsonUtils() {
    }
    private static final Gson gson = new Gson();
    public static <T> T parse(String jsonData, Class<T> type)  {
        if (jsonData == null || jsonData.length() == 0){
            return null;
        }
        T result = null;
        try {
            result = gson.fromJson(jsonData, type);
        }catch (Exception e){
            Log.e(TAG,  "解析失败：" + jsonData);
        }
        return result;
    }

    /**
     * 将Json数组解析成相应的映射对象列表
     *
     * @param jsonData
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> List<T> parseArray(String jsonData, Class<T> cls) {
        List<T> list = new ArrayList<>();
        try {
            JsonArray array = new JsonParser().parse(jsonData).getAsJsonArray();
            for (final JsonElement elem : array) {
                list.add(gson.fromJson(elem, cls));
            }
        } catch (Exception e) {
            Log.e(TAG, "解析失败：" + jsonData);
        }

        return list;
    }

    /**
     * 将Json数组解析成相应的映射对象列表
     *
     * @param jsonData
     * @param <T>
     * @return
     */
    public static <T> List<T> parseJsonList(String jsonData) {
        List<T> result = gson.fromJson(jsonData, new TypeToken<List<T>>() {
        }.getType());
        return result;
    }

    /**
     * 将一个对象转为json字符串
     * @param object Object实例对象
     * @return 返回json字符串
     */
    public static String toJson(Object object) {
        String json = gson.toJson(object);
        return json;
    }
}
