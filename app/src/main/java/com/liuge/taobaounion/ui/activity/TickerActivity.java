package com.liuge.taobaounion.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lcodecore.tkrefreshlayout.utils.LogUtil;
import com.liuge.taobaounion.R;
import com.liuge.taobaounion.base.BaseActivity;
import com.liuge.taobaounion.model.domain.TicketResult;
import com.liuge.taobaounion.presenter.ITicketPresenter;
import com.liuge.taobaounion.utils.LogUtils;
import com.liuge.taobaounion.utils.PresenterManager;
import com.liuge.taobaounion.utils.ToastUtils;
import com.liuge.taobaounion.utils.UrlUtils;
import com.liuge.taobaounion.view.ITicketPagerCallback;

import java.util.List;

import butterknife.BindView;

import static android.content.pm.PackageManager.MATCH_UNINSTALLED_PACKAGES;

/**
 * FileName: TickerActivity
 * Author: LiuGe
 * Date: 2020/8/26 15:20
 * Description: 淘口令activity
 */
public class TickerActivity extends BaseActivity implements ITicketPagerCallback {

    private ITicketPresenter mTicketPresenter;

    private boolean mHasTaobaoApp = false;
    @BindView(R.id.ticket_cover)
    public ImageView mCover;

    @BindView(R.id.ticket_back_press)
    public View mBack;

    @BindView(R.id.ticket_code)
    public EditText mTicketCode;

    @BindView(R.id.ticket_copy_or_open_btn)
    public TextView mOpenOrCopyBtn;

    @BindView(R.id.ticket_cover_loading)
    public View loadingView;

    @BindView(R.id.ticket_retry)
    public View retryLoadText;

    @Override
    protected void initPresenter() {
        mTicketPresenter = PresenterManager.getInstance().getTicketPresenter();
        if (mTicketPresenter != null) {
            mTicketPresenter.registerViewCallback(this);
        }
        // 判断是否安装淘宝
        // act=android.intent.action.VIEW dat=http://m.taobao.com/...
        // flg=0x4000000
        // pkg=com.taobao.taobao
        // cmp=com.taobao.taobao/com
        // 包名:com.taobao.taobao
        // 检查是否安装淘宝应用
        PackageManager pm = getPackageManager();
        try {
            // getPackageInfo方法无法获取到包信息，改用getApplicationInfo()方法
            ApplicationInfo taobaoAppInfo = pm.getApplicationInfo("com.taobao.taobao", MATCH_UNINSTALLED_PACKAGES);
//            LogUtils.d(this,"taobaoAppInfo -->" + taobaoAppInfo);
            mHasTaobaoApp = taobaoAppInfo != null;
        } catch (Exception e) {
            e.printStackTrace();
            mHasTaobaoApp = false;
        }
        LogUtils.d(this, "mHasTaobaoApp --> " + mHasTaobaoApp);
        // 根据这个值去修改UI
        mOpenOrCopyBtn.setText(mHasTaobaoApp ? "打开淘宝领券" : "复制淘口令");
    }

    @Override
    protected void release() {
        if (mTicketPresenter != null) {
            mTicketPresenter.unRegisterViewCallback(this);
        }
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {
        mBack.setOnClickListener(v -> finish());
        mOpenOrCopyBtn.setOnClickListener(v -> {
            // 复制淘口令
            // 拿到内容
            String ticketCode = mTicketCode.getText().toString().trim();
            LogUtils.d(TickerActivity.this, "ticket code -->" + ticketCode);
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            // 复制到粘贴板
            ClipData clipData = ClipData.newPlainText("sob_taobao_ticket_code", ticketCode);
            cm.setPrimaryClip(clipData);
            // 判断有没有淘宝

            if (mHasTaobaoApp) {
                // 如果有就打开淘宝
                Intent taobaoIntent = new Intent();
//                taobaoIntent.setAction("android.intent.action.MAIN");
//                taobaoIntent.addCategory("android.intent.category.LAUNCHER");
                ComponentName componentName = new ComponentName("com.taobao.taobao", "com.taobao.tao.TBMainActivity");
                taobaoIntent.setComponent(componentName);
                startActivity(taobaoIntent);
            } else {
//                没有就提示复制成功
                ToastUtils.showToast("复制成功~ 粘贴分享或打开淘宝");
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_ticket;
    }

    @Override
    public void onTicketLoaded(String cover, TicketResult result) {
        // 设置图片
        if (mCover != null && !TextUtils.isEmpty(cover)) {
            String coverPath = UrlUtils.getCoverPath(cover);
            Glide.with(this).load(coverPath).into(mCover);
        }
        if (TextUtils.isEmpty(cover)) {
            mCover.setImageResource(R.mipmap.no_image);
        }
        // 设置code
        if (result != null && result.getData().getTbk_tpwd_create_response() != null) {
            mTicketCode.setText(result.getData().getTbk_tpwd_create_response().getData().getModel());
        }
        if (loadingView != null) {
            loadingView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNetworkError() {
        if (loadingView != null) {
            loadingView.setVisibility(View.GONE);
        }
        if (retryLoadText != null) {
            retryLoadText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoading() {
        if (retryLoadText != null) {
            retryLoadText.setVisibility(View.GONE);
        }
        if (loadingView != null) {
            loadingView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onEmpty() {

    }
}
