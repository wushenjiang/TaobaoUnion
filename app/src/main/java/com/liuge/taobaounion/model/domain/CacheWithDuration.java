package com.liuge.taobaounion.model.domain;

/**
 * FileName: CacheWithDuration
 * Author: LiuGe
 * Date: 2020/8/27 21:33
 * Description: 缓存bean类
 */
public class CacheWithDuration {

    private long duration;

    private String cache;

    public CacheWithDuration(long duration, String cache) {
        this.duration = duration;
        this.cache = cache;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getCache() {
        return cache;
    }

    public void setCache(String cache) {
        this.cache = cache;
    }
}
