package com.liuge.taobaounion.model.domain;

/**
 * FileName: ILinearItemInfo
 * Author: LiuGe
 * Date: 2020/8/28 22:51
 * Description: 主页的itemBean
 */
public interface ILinearItemInfo extends IBaseInfo {
    /**
     * 获取原价
     *
     * @return
     */
    String getFinalPrice();

    /**
     * 获取优惠价格
     * @return
     */
    long getCouponAmount();

    /**
     * 获取销量
     * @return
     */
    long getVolume();
}
