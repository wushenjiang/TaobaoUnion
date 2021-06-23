package com.liuge.taobaounion.model.domain;

import java.util.List;

/**
 * FileName: RecommendContent
 * Author: LiuGe
 * Date: 2020/8/26 19:04
 * Description: 精选内容bean
 */
public class RecommendContent {


    private Boolean success;
    private Integer code;
    private String message;
    private DataBean data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }


    public static class DataBean {
        private TbkDgOptimusMaterialResponseBean tbk_dg_optimus_material_response;

        public TbkDgOptimusMaterialResponseBean getTbk_dg_optimus_material_response() {
            return tbk_dg_optimus_material_response;
        }

        public void setTbk_dg_optimus_material_response(TbkDgOptimusMaterialResponseBean tbk_dg_optimus_material_response) {
            this.tbk_dg_optimus_material_response = tbk_dg_optimus_material_response;
        }

        public static class TbkDgOptimusMaterialResponseBean {
            private String is_default;
            private ResultListBean result_list;
            private Integer total_count;
            private String request_id;

            public String getIs_default() {
                return is_default;
            }

            public void setIs_default(String is_default) {
                this.is_default = is_default;
            }

            public ResultListBean getResult_list() {
                return result_list;
            }

            public void setResult_list(ResultListBean result_list) {
                this.result_list = result_list;
            }

            public Integer getTotal_count() {
                return total_count;
            }

            public void setTotal_count(Integer total_count) {
                this.total_count = total_count;
            }

            public String getRequest_id() {
                return request_id;
            }

            public void setRequest_id(String request_id) {
                this.request_id = request_id;
            }

            public static class ResultListBean {
                private List<MapDataBean> map_data;

                public List<MapDataBean> getMap_data() {
                    return map_data;
                }

                public void setMap_data(List<MapDataBean> map_data) {
                    this.map_data = map_data;
                }

                public static class MapDataBean implements IBaseInfo {
                    private Integer category_id;
                    private String click_url;
                    private String commission_rate;
                    private Integer coupon_amount;
                    private String coupon_click_url;
                    private String coupon_end_time;
                    private String coupon_info;
                    private Integer coupon_remain_count;
                    private String coupon_share_url;
                    private String coupon_start_fee;
                    private String coupon_start_time;
                    private Integer coupon_total_count;
                    private Long item_id;
                    private Integer level_one_category_id;
                    private String nick;
                    private String pict_url;
                    private String reserve_price;
                    private Long seller_id;
                    private String shop_title;
                    private SmallImagesBean small_images;
                    private String title;
                    private Integer user_type;
                    private Integer volume;
                    private String white_image;
                    private String zk_final_price;

                    public Integer getCategory_id() {
                        return category_id;
                    }

                    public void setCategory_id(Integer category_id) {
                        this.category_id = category_id;
                    }

                    public String getClick_url() {
                        return click_url;
                    }

                    public void setClick_url(String click_url) {
                        this.click_url = click_url;
                    }

                    public String getCommission_rate() {
                        return commission_rate;
                    }

                    public void setCommission_rate(String commission_rate) {
                        this.commission_rate = commission_rate;
                    }

                    public Integer getCoupon_amount() {
                        return coupon_amount;
                    }

                    public void setCoupon_amount(Integer coupon_amount) {
                        this.coupon_amount = coupon_amount;
                    }

                    public String getCoupon_click_url() {
                        return coupon_click_url;
                    }

                    public void setCoupon_click_url(String coupon_click_url) {
                        this.coupon_click_url = coupon_click_url;
                    }

                    public String getCoupon_end_time() {
                        return coupon_end_time;
                    }

                    public void setCoupon_end_time(String coupon_end_time) {
                        this.coupon_end_time = coupon_end_time;
                    }

                    public String getCoupon_info() {
                        return coupon_info;
                    }

                    public void setCoupon_info(String coupon_info) {
                        this.coupon_info = coupon_info;
                    }

                    public Integer getCoupon_remain_count() {
                        return coupon_remain_count;
                    }

                    public void setCoupon_remain_count(Integer coupon_remain_count) {
                        this.coupon_remain_count = coupon_remain_count;
                    }

                    public String getCoupon_share_url() {
                        return coupon_share_url;
                    }

                    public void setCoupon_share_url(String coupon_share_url) {
                        this.coupon_share_url = coupon_share_url;
                    }

                    public String getCoupon_start_fee() {
                        return coupon_start_fee;
                    }

                    public void setCoupon_start_fee(String coupon_start_fee) {
                        this.coupon_start_fee = coupon_start_fee;
                    }

                    public String getCoupon_start_time() {
                        return coupon_start_time;
                    }

                    public void setCoupon_start_time(String coupon_start_time) {
                        this.coupon_start_time = coupon_start_time;
                    }

                    public Integer getCoupon_total_count() {
                        return coupon_total_count;
                    }

                    public void setCoupon_total_count(Integer coupon_total_count) {
                        this.coupon_total_count = coupon_total_count;
                    }

                    public Long getItem_id() {
                        return item_id;
                    }

                    public void setItem_id(Long item_id) {
                        this.item_id = item_id;
                    }

                    public Integer getLevel_one_category_id() {
                        return level_one_category_id;
                    }

                    public void setLevel_one_category_id(Integer level_one_category_id) {
                        this.level_one_category_id = level_one_category_id;
                    }

                    public String getNick() {
                        return nick;
                    }

                    public void setNick(String nick) {
                        this.nick = nick;
                    }

                    public String getPict_url() {
                        return pict_url;
                    }

                    public void setPict_url(String pict_url) {
                        this.pict_url = pict_url;
                    }

                    public String getReserve_price() {
                        return reserve_price;
                    }

                    public void setReserve_price(String reserve_price) {
                        this.reserve_price = reserve_price;
                    }

                    public Long getSeller_id() {
                        return seller_id;
                    }

                    public void setSeller_id(Long seller_id) {
                        this.seller_id = seller_id;
                    }

                    public String getShop_title() {
                        return shop_title;
                    }

                    public void setShop_title(String shop_title) {
                        this.shop_title = shop_title;
                    }

                    public SmallImagesBean getSmall_images() {
                        return small_images;
                    }

                    public void setSmall_images(SmallImagesBean small_images) {
                        this.small_images = small_images;
                    }

                    @Override
                    public String getCover() {
                        return pict_url;
                    }

                    public String getTitle() {
                        return title;
                    }

                    @Override
                    public String getUrl() {
                        if(coupon_click_url != null){
                            return coupon_click_url;
                        }
                        return click_url;
                    }

                    public void setTitle(String title) {
                        this.title = title;
                    }

                    public Integer getUser_type() {
                        return user_type;
                    }

                    public void setUser_type(Integer user_type) {
                        this.user_type = user_type;
                    }

                    public Integer getVolume() {
                        return volume;
                    }

                    public void setVolume(Integer volume) {
                        this.volume = volume;
                    }

                    public String getWhite_image() {
                        return white_image;
                    }

                    public void setWhite_image(String white_image) {
                        this.white_image = white_image;
                    }

                    public String getZk_final_price() {
                        return zk_final_price;
                    }

                    public void setZk_final_price(String zk_final_price) {
                        this.zk_final_price = zk_final_price;
                    }

                    public static class SmallImagesBean {
                        private List<String> string;

                        public List<String> getString() {
                            return string;
                        }

                        public void setString(List<String> string) {
                            this.string = string;
                        }
                    }
                }
            }
        }
    }
}
