package com.liuge.taobaounion.model.domain;

/**
 * FileName: TicketParams
 * Author: LiuGe
 * Date: 2020/8/26 15:51
 * Description: 请求淘口令的字段
 */
public class TicketParams {

    private String url;
    private String title;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public TicketParams(String url, String title) {
        this.url = url;
        this.title = title;
    }
}
