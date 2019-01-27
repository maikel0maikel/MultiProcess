package com.zbq.library.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
    private static final String TAG = "JsonUtils";

   private static Gson gson = new Gson();
    private JsonUtils(){}
    /**
     * 把Json字符串变成实例
      * @param json 字符串
     * @param clz 要序列化为实例的类类型
     * @param <T> 泛型
     * @return 返回泛型实例
     */
   public static <T> T fromObject(String json,Class<T> clz){
       if (StringUtils.isEmpty(json)) return null;
       T t = null;
       try {
          t = gson.fromJson(json,clz);
       }catch (Exception e){

       }

       return t;
   }

    /**
     *  将Json数组解析成相应的映射对象列表
     * @param json json字符串
     * @param clz 類類型
     * @param <T> 泛型
     * @return 返回T列表
     */
   public static  <T>List<T> fromObjectArray(String json,Class<T> clz){
       if (StringUtils.isEmpty(json)) return null;
       List<T> arrayTs = new ArrayList<>();
       try {
           JsonArray array = new JsonParser().parse(json).getAsJsonArray();
           for (JsonElement element:array){
               arrayTs.add(gson.fromJson(element,clz));
           }
       }catch (Exception e) {

       }
       return arrayTs;
   }
    /**
     * 将Json数组解析成相应的映射对象列表
     *
     * @param jsonData
     * @param <T>
     * @return
     */
    public static <T> List<T> parseJsonList(String jsonData) {
        Gson gson = new Gson();
        List<T> result = gson.fromJson(jsonData, new TypeToken<List<T>>() {
        }.getType());
        return result;
    }

    /**
     * 將對象轉爲json字符串
     * @param object 對象實例
     * @return json字符串
     */
    public static String fromString(Object object){
        return gson.toJson(object);
    }

}
