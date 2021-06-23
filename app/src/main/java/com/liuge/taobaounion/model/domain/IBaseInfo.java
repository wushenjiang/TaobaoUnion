package com.liuge.taobaounion.model.domain;

/**
 * FileName: IBaseInfo
 * Author: LiuGe
 * Date: 2020/8/27 20:40
 * Description: 跳转到淘口令的数据
 */
public interface IBaseInfo {
    /**
     * 商品的封面
     *
     * @return
     */
    String getCover();

    /**
     * 商品的标题
     *
     * @return
     */
    String getTitle();

    /**
     * 商品的url
     *
     * @return
     */
    String getUrl();
}
