package com.liuge.taobaounion.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.liuge.taobaounion.base.BaseApplication;
import com.liuge.taobaounion.model.domain.CacheWithDuration;

/**
 * FileName: JsonCacheUtils
 * Author: LiuGe
 * Date: 2020/8/27 21:25
 * Description: 缓存工具类
 */
public class JsonCacheUtils {
    public static String JSON_CACHE_SP_NAME = "json_cache_sp_name";
    private final SharedPreferences mSp;
    private final Gson mGson;

    private JsonCacheUtils(){
        mSp = BaseApplication.getAppContext().getSharedPreferences(JSON_CACHE_SP_NAME, Context.MODE_PRIVATE);
        mGson = new Gson();
    }

    public void saveCache(String key,Object value){
        this.saveCache(key,value,-1L);
    }

    public void saveCache(String key, Object value, long duration){
        SharedPreferences.Editor edit = mSp.edit();
        String valueStr = mGson.toJson(value);
        if(duration != -1L){
            // 当前时间
            duration += System.currentTimeMillis();
        }
        // 保存了一个带有数据和时间的内容
        CacheWithDuration cacheWithDuration = new CacheWithDuration(duration,valueStr);
        String cacheWithTime = mGson.toJson(cacheWithDuration);
        edit.putString(key,cacheWithTime);
        edit.apply();
    }

    public void delCache(String key){
        SharedPreferences.Editor edit = mSp.edit();
        edit.remove(key).apply();
    }

    /**
     * <>之后的T代表返回类型
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getValue(String key,Class<T> clazz){
        String valueWithDuration = mSp.getString(key,null);
        if (valueWithDuration == null) {
            return null;
        }
        CacheWithDuration cacheWithDuration = mGson.fromJson(valueWithDuration, CacheWithDuration.class);
        // 对时间进行判断
        long duration = cacheWithDuration.getDuration();
        if(duration != -1 && duration - System.currentTimeMillis() <= 0){
            // 过期了
            return null;
        }else{
            // 没过期
            String cache = cacheWithDuration.getCache();
            T result = mGson.fromJson(cache, clazz);
            return result;
        }
    }

    private static JsonCacheUtils sJsonCacheUtils = null;

    public static JsonCacheUtils getInstance(){
        if (sJsonCacheUtils == null) {
            sJsonCacheUtils = new JsonCacheUtils();
        }
        return sJsonCacheUtils;
    }
}
