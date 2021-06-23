package com.liuge.taobaounion.model;

import com.liuge.taobaounion.model.domain.Categories;
import com.liuge.taobaounion.model.domain.HomePagerContent;
import com.liuge.taobaounion.model.domain.OnSellContent;
import com.liuge.taobaounion.model.domain.RecommendContent;
import com.liuge.taobaounion.model.domain.RecommendPageCategory;
import com.liuge.taobaounion.model.domain.SearchRecommend;
import com.liuge.taobaounion.model.domain.SearchResult;
import com.liuge.taobaounion.model.domain.TicketParams;
import com.liuge.taobaounion.model.domain.TicketResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * FileName: API
 * Author: LiuGe
 * Date: 2020/8/24 0:42
 * Description: 网络请求的接口
 */
public interface API {

    @GET("discovery/categories")
    Call<Categories> getCategories();

    @GET
    Call<HomePagerContent> getHomePageContent(@Url String url);

    @POST("tpwd")
    Call<TicketResult> getTicket(@Body TicketParams ticketParams);

    @GET("recommend/categories")
    Call<RecommendPageCategory> getRecommendPageCategories();

    @GET("recommend/{categoryId}")
    Call<RecommendContent> getRecommendPageContent(@Path("categoryId") int categoryId);

    @GET("onSell/{page}")
    Call<OnSellContent> getOnSellContent(@Path("page") int page);

    @GET("search/recommend")
    Call<SearchRecommend> getRecommendWords();

    @GET("search")
    Call<SearchResult> doSearch(@Query("page") int page,@Query("keyword") String keyword);
}
