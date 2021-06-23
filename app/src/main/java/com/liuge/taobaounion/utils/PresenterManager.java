package com.liuge.taobaounion.utils;

import com.liuge.taobaounion.presenter.ICategoryPagerPresenter;
import com.liuge.taobaounion.presenter.IHomePresenter;
import com.liuge.taobaounion.presenter.IOnSellPagePresenter;
import com.liuge.taobaounion.presenter.IRecommendPagePresenter;
import com.liuge.taobaounion.presenter.ISearchPresenter;
import com.liuge.taobaounion.presenter.ITicketPresenter;
import com.liuge.taobaounion.presenter.impl.CategoryPagerPresenterImpl;
import com.liuge.taobaounion.presenter.impl.HomePresenterImpl;
import com.liuge.taobaounion.presenter.impl.OnSellPagePresenterImpl;
import com.liuge.taobaounion.presenter.impl.RecommendPagePresenterImpl;
import com.liuge.taobaounion.presenter.impl.SearchPresenterImpl;
import com.liuge.taobaounion.presenter.impl.TicketPresenterImpl;

/**
 * FileName: PresenterManager
 * Author: LiuGe
 * Date: 2020/8/26 15:40
 * Description: presenter管理器
 */
public class PresenterManager {

    private static final PresenterManager instance = new PresenterManager();
    private final ICategoryPagerPresenter mCategoryPagerPresenter;
    private final IHomePresenter mHomePresenter;
    private final ITicketPresenter mTicketPresenter;
    private final IRecommendPagePresenter mRecommendPagePresenter;
    private final IOnSellPagePresenter mOnSellPagePresenter;
    private final ISearchPresenter mSearchPresenter;

    public static PresenterManager getInstance(){
        return instance;
    }

    private PresenterManager(){
        mCategoryPagerPresenter = new CategoryPagerPresenterImpl();
        mHomePresenter = new HomePresenterImpl();
        mTicketPresenter = new TicketPresenterImpl();
        mRecommendPagePresenter = new RecommendPagePresenterImpl();
        mOnSellPagePresenter = new OnSellPagePresenterImpl();
        mSearchPresenter = new SearchPresenterImpl();
    }

    public ICategoryPagerPresenter getCategoryPagerPresenter() {
        return mCategoryPagerPresenter;
    }

    public IHomePresenter getHomePresenter() {
        return mHomePresenter;
    }

    public ITicketPresenter getTicketPresenter() {
        return mTicketPresenter;
    }

    public IRecommendPagePresenter getRecommendPagePresenter() {
        return mRecommendPagePresenter;
    }

    public IOnSellPagePresenter getOnSellPagePresenter() {
        return mOnSellPagePresenter;
    }

    public ISearchPresenter getSearchPresenter() {
        return mSearchPresenter;
    }
}
