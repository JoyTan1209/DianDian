package com.tanchaoyin.diandian.module.zhihu.ui;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.socks.library.KLog;
import com.tanchaoyin.diandian.R;
import com.tanchaoyin.diandian.base.BaseFragment;
import com.tanchaoyin.diandian.base.BaseSpacesItemDecoration;
import com.tanchaoyin.diandian.base.adapter.BaseRecyclerAdapter;
import com.tanchaoyin.diandian.base.adapter.BaseRecyclerViewHolder;
import com.tanchaoyin.diandian.bean.zhihu.ZhihuDaily;
import com.tanchaoyin.diandian.bean.zhihu.ZhihuDailyItem;
import com.tanchaoyin.diandian.callback.OnItemClickAdapter;
import com.tanchaoyin.diandian.common.DataLoadState;
import com.tanchaoyin.diandian.module.zhihu.presenter.IZhihuPresenter;
import com.tanchaoyin.diandian.module.zhihu.presenter.impl.IZhihuPresenterImpl;
import com.tanchaoyin.diandian.module.zhihu.view.IZhihuView;
import com.tanchaoyin.diandian.utils.GlideUtils;
import com.tanchaoyin.diandian.utils.MeasureUtil;
import com.tanchaoyin.diandian.widget.AutoLoadMoreRecyclerView;
import com.tanchaoyin.diandian.widget.MultiSwipeRefreshLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by TanChaoyin on 2016/5/10.
 */
public class ZhihuFragment extends BaseFragment<IZhihuPresenter> implements IZhihuView {

    @Bind(R.id.gank_list_rv)
    AutoLoadMoreRecyclerView autoLoadMoreRecyclerView;

    @Bind(R.id.refresh_layout)
    MultiSwipeRefreshLayout refreshLayout;

    private BaseRecyclerAdapter<ZhihuDailyItem> recyclerAdapter;

    private Context context;

    @Override
    protected void initView(View fragmentRootView) {

        context = getActivity();

        ButterKnife.bind(this,fragmentRootView);

        presenter = new IZhihuPresenterImpl(this);

    }

    @Override
    protected int getLayoutView() {
        return R.layout.fragment_ganklist;
    }

    @Override
    public void updateZhihuListView(ZhihuDaily data, DataLoadState state) {

        switch (state) {
            case STATE_REFRESH_SUCCESS:
                refreshLayout.setRefreshing(false);
                if (null == recyclerAdapter) {
                    initZhihuList(data);
                } else {
                    recyclerAdapter.setData(data.stories);
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
                if (data == null || data.stories.size() == 0) {
                    autoLoadMoreRecyclerView.notifyAllLoaded();
                    toast("全部加载完毕噜(☆＿☆)");
                } else {
                    recyclerAdapter.addMoreData(data.stories);
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
    private void initZhihuList(final ZhihuDaily data) {
        // mAdapter为空肯定为第一次进入状态
        recyclerAdapter = new BaseRecyclerAdapter<ZhihuDailyItem>(context, data.stories) {
            @Override
            public int getItemLayoutId(int viewType) {
                return R.layout.zhihu_daily_item;
            }

            @Override
            public void bindData(BaseRecyclerViewHolder holder, int position, ZhihuDailyItem item) {

                if (item.images.length > 0) {
                    ImageView imageView = holder.getImageView(R.id.iv_zhihu_daily);
                    GlideUtils.display(imageView,item.images[0]);
                }
                holder.getTextView(R.id.tv_zhihu_daily).setText(item.title);
                holder.getTextView(R.id.tv_time).setText(item.date);
            }
        };

        recyclerAdapter.setOnItemClickListener(new OnItemClickAdapter() {
            @Override
            public void onItemClick(View view, int position) {
                // imgextra不为空的话，无新闻内容，直接打开图片浏览
                KLog.e(recyclerAdapter.getData().get(position).date + ";" + recyclerAdapter.getData()
                        .get(position).id);

               /* WebAcitivity.toUrl(
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
                presenter.loadMoreData(data.date);
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
