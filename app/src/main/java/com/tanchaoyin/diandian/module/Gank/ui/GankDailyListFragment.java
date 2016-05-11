package com.tanchaoyin.diandian.module.gank.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.tanchaoyin.diandian.R;
import com.tanchaoyin.diandian.base.BaseFragment;
import com.tanchaoyin.diandian.base.BaseSpacesItemDecoration;
import com.tanchaoyin.diandian.base.adapter.BaseRecyclerAdapter;
import com.tanchaoyin.diandian.base.adapter.BaseRecyclerViewHolder;
import com.tanchaoyin.diandian.bean.gank.BaseGankData;
import com.tanchaoyin.diandian.bean.gank.GankDaily;
import com.tanchaoyin.diandian.callback.OnItemClickAdapter;
import com.tanchaoyin.diandian.common.CommonConstant;
import com.tanchaoyin.diandian.common.DataLoadState;
import com.tanchaoyin.diandian.api.gank.GankApi;
import com.tanchaoyin.diandian.module.gank.presenter.IGankDataPresenter;
import com.tanchaoyin.diandian.module.gank.presenter.impl.IGankDailyDataPresenterImpl;
import com.tanchaoyin.diandian.module.gank.view.IGankDailyView;
import com.tanchaoyin.diandian.utils.AttrsHelper;
import com.tanchaoyin.diandian.utils.DateUtils;
import com.tanchaoyin.diandian.utils.GlideUtils;
import com.tanchaoyin.diandian.utils.MeasureUtil;
import com.tanchaoyin.diandian.widget.AutoLoadMoreRecyclerView;
import com.tanchaoyin.diandian.widget.MultiSwipeRefreshLayout;

import java.util.List;

import butterknife.Bind;

/**
 * Created by TanChaoyin on 2016/3/18.
 */
public class GankDailyListFragment extends BaseFragment<IGankDataPresenter> implements IGankDailyView {

    @Bind(R.id.gank_list_rv)
    AutoLoadMoreRecyclerView autoLoadMoreRecyclerView;

    @Bind(R.id.refresh_layout)
    MultiSwipeRefreshLayout refreshLayout;

    private BaseRecyclerAdapter<GankDaily> recyclerAdapter;

    private Context context;

    protected String gankType;

    protected static final String GANK_TYPE = "gank_type";

    protected int position;

    protected static final String POSITION = "position";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity();

        if (getArguments() != null) {
            this.gankType = getArguments().getString(GANK_TYPE);
            this.position = getArguments().getInt(POSITION);
        }
    }

    public static GankDailyListFragment newInstance(String gankType, int position) {
        GankDailyListFragment fragment = new GankDailyListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(GANK_TYPE, gankType);
        bundle.putInt(POSITION, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View fragmentRootView) {

        refreshLayout.setColorSchemeColors(AttrsHelper.getColor(this.context, R.attr.colorPrimary), AttrsHelper.getColor(this.context, R.attr.colorPrimaryLight));

        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
            }
        });

        presenter = new IGankDailyDataPresenterImpl(this);

    }

    @Override
    protected int getLayoutView() {
        return R.layout.fragment_ganklist;
    }

    private void initGankDailyList(final List<GankDaily> data) {
        // mAdapter为空肯定为第一次进入状态
        recyclerAdapter = new BaseRecyclerAdapter<GankDaily>(context, data) {
            @Override
            public int getItemLayoutId(int viewType) {
                return R.layout.item_daily;
            }

            @Override
            public void bindData(BaseRecyclerViewHolder holder, int position, GankDaily item) {

                 /*
                 * 标题 和 日期
                 *
                 * 如果没有视频的title和date就找福利的title和date，实在没有就，完！
                 */
                if (item.results.videoData != null && item.results.videoData.size() > 0) {
                    BaseGankData video = item.results.videoData.get(0);
                    holder.getTextView(R.id.daily_title_tv).setText(video.desc.trim());
                    holder.getTextView(R.id.daily_date_tv).setText(DateUtils.date2String(video.publishedAt.getTime(), CommonConstant.DAILY_DATE_FORMAT));
                } else if (item.results.welfareData != null && item.results.welfareData.size() > 0) {
                    BaseGankData welfare = item.results.welfareData.get(0);
                    holder.getTextView(R.id.daily_title_tv).setText(welfare.desc.trim());
                    holder.getTextView(R.id.daily_date_tv).setText(DateUtils.date2String(welfare.publishedAt.getTime(), CommonConstant.DAILY_DATE_FORMAT));
                } else {
                    holder.getTextView(R.id.daily_title_tv).setText("这期没福利了，安心学习吧！");
                    holder.getTextView(R.id.daily_date_tv).setText("");
                }

                // 图片
                if (item.results.welfareData != null && item.results.welfareData.size() > 0) {
                    final BaseGankData welfare = item.results.welfareData.get(0);
                    GlideUtils.display(holder.getImageView(R.id.daily_iv), welfare.url);
                    holder.getImageView(R.id.daily_iv).setOnClickListener(v -> {
//                        if (this.listener != null)
//                            MainAdapter.this.listener.onClickPicture(welfare.url, welfare.desc, v);
                    });
                } else {
                    GlideUtils.displayNative(holder.getImageView(R.id.daily_iv), R.mipmap.img_default_gray);
                }

                /*
                 * 标签 ListView 和 RecyclerView 都要写else 因为复用问题
                 * 忧伤
                 */
                if (item.category == null) {
                    holder.getTextView(R.id.daily_android_tag_tv).setVisibility(View.GONE);
                    holder.getTextView(R.id.daily_ios_tag_tv).setVisibility(View.GONE);
                    holder.getTextView(R.id.daily_js_tag_tv).setVisibility(View.GONE);
                } else {
                    if (item.category.contains(GankApi.DATA_TYPE_ANDROID)) {
                        holder.getTextView(R.id.daily_android_tag_tv).setVisibility(View.VISIBLE);
                    } else {
                        holder.getTextView(R.id.daily_android_tag_tv).setVisibility(View.GONE);
                    }
                    if (item.category.contains(GankApi.DATA_TYPE_IOS)) {
                        holder.getTextView(R.id.daily_ios_tag_tv).setVisibility(View.VISIBLE);
                    } else {
                        holder.getTextView(R.id.daily_ios_tag_tv).setVisibility(View.GONE);
                    }
                    if (item.category.contains(GankApi.DATA_TYPE_JS)) {
                        holder.getTextView(R.id.daily_js_tag_tv).setVisibility(View.VISIBLE);
                    } else {
                        holder.getTextView(R.id.daily_js_tag_tv).setVisibility(View.GONE);
                    }
                }
            }
        };

        recyclerAdapter.setOnItemClickListener(new OnItemClickAdapter() {
            @Override
            public void onItemClick(View view, int position) {
                // imgextra不为空的话，无新闻内容，直接打开图片浏览
               /* KLog.e(recyclerAdapter.getData().get(position).desc + ";" + recyclerAdapter.getData()
                        .get(position).objectId);

                WebAcitivity.toUrl(
                        context,
                        recyclerAdapter.getData().get(position).url,
                        recyclerAdapter.getData().get(position).desc,
                        recyclerAdapter.getData().get(position).type
                );*/
            }
        });

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.context,
                LinearLayoutManager.VERTICAL, false);

        autoLoadMoreRecyclerView.setAutoLayoutManager(linearLayoutManager).setAutoHasFixedSize(true)
                .addAutoItemDecoration(
                        new BaseSpacesItemDecoration(MeasureUtil.dip2px(context, 4)))
                .setAutoItemAnimator(new DefaultItemAnimator()).setAutoAdapter(recyclerAdapter);

        autoLoadMoreRecyclerView.setOnLoadMoreListener(new AutoLoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore() {
                // 状态停止，并且滑动到最后一位
                presenter.loadMoreData();
                // 显示尾部加载
                recyclerAdapter.showFooter();
                autoLoadMoreRecyclerView.scrollToPosition(recyclerAdapter.getItemCount() - 1);
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.refreshData();
            }
        });
    }

    @Override
    public void updateGankListView(List<GankDaily> data, DataLoadState state) {

        switch (state) {
            case STATE_REFRESH_SUCCESS:
                refreshLayout.setRefreshing(false);
                if (null == recyclerAdapter) {
                    initGankDailyList(data);
                } else {
                    recyclerAdapter.setData(data);
                }
                if (autoLoadMoreRecyclerView.isAllLoaded()) {
                    // 之前全部加载完了的话，这里把状态改回来供底部加载用
                    autoLoadMoreRecyclerView.notifyMoreLoaded();
                }
                break;
            case STATE_REFRESH_FAIL:
                refreshLayout.setRefreshing(false);
                break;
            case STATE_LOAD_MORE_SUCCESS:
                // 隐藏尾部加载
                recyclerAdapter.hideFooter();
                if (data == null || data.size() == 0) {
                    autoLoadMoreRecyclerView.notifyAllLoaded();
                    toast("全部加载完毕噜(☆＿☆)");
                } else {
                    recyclerAdapter.addMoreData(data);
                    autoLoadMoreRecyclerView.notifyMoreLoaded();
                }
                break;
            case STATE__LOAD_MORE_FAIL:
                if (null != recyclerAdapter)
                    recyclerAdapter.hideFooter();
                autoLoadMoreRecyclerView.notifyMoreLoaded();
                break;
        }
    }
}
