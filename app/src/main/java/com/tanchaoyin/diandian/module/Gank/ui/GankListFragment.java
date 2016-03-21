package com.tanchaoyin.diandian.module.Gank.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;

import com.socks.library.KLog;
import com.tanchaoyin.diandian.R;
import com.tanchaoyin.diandian.annotation.ActivityFragmentInject;
import com.tanchaoyin.diandian.base.BaseFragment;
import com.tanchaoyin.diandian.base.BaseSpacesItemDecoration;
import com.tanchaoyin.diandian.base.adapter.BaseRecyclerAdapter;
import com.tanchaoyin.diandian.base.adapter.BaseRecyclerViewHolder;
import com.tanchaoyin.diandian.bean.BaseGankData;
import com.tanchaoyin.diandian.callback.OnItemClickAdapter;
import com.tanchaoyin.diandian.common.DataLoadState;
import com.tanchaoyin.diandian.http.GankApi;
import com.tanchaoyin.diandian.http.GankType;
import com.tanchaoyin.diandian.module.Gank.presenter.IGankDataPresenter;
import com.tanchaoyin.diandian.module.Gank.presenter.impl.IGankDataPresenterImpl;
import com.tanchaoyin.diandian.module.Gank.view.IGankListView;
import com.tanchaoyin.diandian.utils.AttrsHelper;
import com.tanchaoyin.diandian.utils.DateUtils;
import com.tanchaoyin.diandian.utils.GanKDataTagUtil;
import com.tanchaoyin.diandian.utils.GlideUtils;
import com.tanchaoyin.diandian.utils.MeasureUtil;
import com.tanchaoyin.diandian.widget.AutoLoadMoreRecyclerView;
import com.tanchaoyin.diandian.widget.MultiSwipeRefreshLayout;
import com.tanchaoyin.diandian.widget.RatioImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by TanChaoyin on 2016/3/18.
 */
@ActivityFragmentInject(contentViewId = R.layout.fragment_ganklist)
public class GankListFragment extends BaseFragment<IGankDataPresenter> implements IGankListView {

    AutoLoadMoreRecyclerView autoLoadMoreRecyclerView;

    MultiSwipeRefreshLayout refreshLayout;

    private BaseRecyclerAdapter<BaseGankData> recyclerAdapter;

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

    public static GankListFragment newInstance(String gankType, int position) {
        GankListFragment fragment = new GankListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(GANK_TYPE, gankType);
        bundle.putInt(POSITION, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View fragmentRootView) {

        autoLoadMoreRecyclerView = (AutoLoadMoreRecyclerView)fragmentRootView.findViewById(R.id.gank_list_rv);

        refreshLayout = (MultiSwipeRefreshLayout)fragmentRootView.findViewById(R.id.refresh_layout);

        refreshLayout.setColorSchemeColors(AttrsHelper.getColor(this.context, R.attr.colorPrimary), AttrsHelper.getColor(this.context, R.attr.colorPrimaryLight));

        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
            }
        });

        presenter = new IGankDataPresenterImpl(this, gankType);

    }

    @Override
    public void updateGankListView(List<BaseGankData> data, DataLoadState state, String type) {

        switch (state) {
            case STATE_REFRESH_SUCCESS:
                refreshLayout.setRefreshing(false);
                if (null == recyclerAdapter) {
                    initGankList(data, gankType);
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

    private void initGankList(final List<BaseGankData> data, String type) {
        // mAdapter为空肯定为第一次进入状态
        recyclerAdapter = new BaseRecyclerAdapter<BaseGankData>(context, data) {
            @Override
            public int getItemLayoutId(int viewType) {
                if (GankApi.DATA_TYPE_WELFARE.equals(type))
                    return R.layout.item_welfate;
                else
                return R.layout.item_data;
            }

            @Override
            public void bindData(BaseRecyclerViewHolder holder, int position, BaseGankData item) {

                /*SkinManager.getInstance().injectSkin(holder.itemView);*/

                if (GankApi.DATA_TYPE_WELFARE.equals(type)){

                    RatioImageView imageView = (RatioImageView)holder.getImageView(R.id.welfare_iv);

                    if (position % 2 == 0) {
                        imageView.setImageRatio(0.7f);
                    } else {
                        imageView.setImageRatio(0.6f);
                    }
                    // 图片
                    if (TextUtils.isEmpty(item.url)) {
                        GlideUtils.displayNative(imageView, R.mipmap.img_default_gray);
                    } else {
                        GlideUtils.display(imageView, item.url);
                    }
                } else {
                    holder.getTextView(R.id.data_title_tv).setText(item.desc);
                    holder.getTextView(R.id.data_via_tv).setText(item.who);
                    holder.getTextView(R.id.data_date_tv).setText(DateUtils.getTimestampString(item.publishedAt));
                    if (TextUtils.isEmpty(item.url)) {
                        holder.getTextView(R.id.data_tag_tv).setVisibility(View.GONE);
                    } else {
                        GanKDataTagUtil.setTag(GankType.android, holder.getTextView(R.id.data_tag_tv), item.url);
                    }
                }
            }
        };

        recyclerAdapter.setOnItemClickListener(new OnItemClickAdapter() {
            @Override
            public void onItemClick(View view, int position) {
                // imgextra不为空的话，无新闻内容，直接打开图片浏览
                KLog.e(recyclerAdapter.getData().get(position).desc + ";" + recyclerAdapter.getData()
                        .get(position).objectId);

                WebAcitivity.toUrl(
                        context,
                        recyclerAdapter.getData().get(position).url,
                        recyclerAdapter.getData().get(position).desc,
                        recyclerAdapter.getData().get(position).type
                );
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
}
