package com.liuge.taobaounion.utils;

/**
 * FileName: TextUtils
 * Author: LiuGe
 * Date: 2020/8/24 21:22
 * Description: 拼接url工具类
 */
public class UrlUtils {

    public static String createHomePagerUrl(int materialId, int page) {
        return "discovery/" + materialId + "/" + page;
    }

    public static String getCoverPath(String pict_url, int size) {
        if(pict_url.startsWith("http") || pict_url.startsWith("https")){
            return pict_url;
        }else{
            return "https:" + pict_url + "_" + size + "x" + size + ".jpg";
        }
    }

    public static String getCoverPath(String pict_url) {
        if(pict_url.startsWith("http") || pict_url.startsWith("https")){
            return pict_url;
        }else{
            return "https:" + pict_url;
        }
    }

    public static String getTicketUrl(String url) {
        if(url.startsWith("http") || url.startsWith("https")){
            return url;
        }else{
            return "https:" + url;
        }
    }
}
