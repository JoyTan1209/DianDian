package com.tanchaoyin.diandian.api.gank.manager;

import com.socks.library.KLog;
import com.tanchaoyin.diandian.api.gank.service.GankService;
import com.tanchaoyin.diandian.app.App;
import com.tanchaoyin.diandian.bean.gank.BaseGankData;
import com.tanchaoyin.diandian.bean.gank.GankDaily;
import com.tanchaoyin.diandian.api.gank.GankApi;
import com.tanchaoyin.diandian.module.gank.presenter.impl.IGankDailyDataPresenterImpl;
import com.tanchaoyin.diandian.utils.NetUtil;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by TanChaoyin on 2016/3/11.
 */
public class RetrofitManagerGank {

    //设缓存有效期为两天
    protected static final long CACHE_STALE_SEC = 60 * 60 * 24 * 2;
    //查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
    protected static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;
    //查询网络的Cache-Control设置，头部Cache-Control设为max-age=0时则不会使用缓存而请求服务器
    protected static final String CACHE_CONTROL_NETWORK = "max-age=0";

    private GankService gankService;

    private OkHttpClient okHttpClient;

    private static RetrofitManagerGank retrofitManagerInstance;

    public static RetrofitManagerGank getInstance() {
        if (retrofitManagerInstance == null) {
            retrofitManagerInstance = new RetrofitManagerGank();
        }
        return retrofitManagerInstance;
    }

    private RetrofitManagerGank() {

        initOkHttpClient();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GankApi.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build();

        this.gankService = retrofit.create(GankService.class);
    }

    public GankService getGankService() {
        return gankService;
    }

    private void initOkHttpClient() {
        if (okHttpClient == null) {

            synchronized (RetrofitManagerGank.class) {

                if (null == okHttpClient) {
                    KLog.e("初始化mOkHttpClient");
                    // 因为BaseUrl不同所以这里Retrofit不为静态，但是OkHttpClient配置是一样的,静态创建一次即可

                    // 指定缓存路径,缓存大小100Mb
                    Cache cache = new Cache(new File(App.getContext().getCacheDir(), "HttpCache"),
                            1024 * 1024 * 100);

                    okHttpClient = new OkHttpClient.Builder().cache(cache)
                            .addNetworkInterceptor(mRewriteCacheControlInterceptor)
                            .addInterceptor(mRewriteCacheControlInterceptor)
                            .addInterceptor(mLoggingInterceptor).retryOnConnectionFailure(true)
                            .connectTimeout(30, TimeUnit.SECONDS).build();

                }
            }
        }
    }

    // 云端响应头拦截器，用来配置缓存策略
    private Interceptor mRewriteCacheControlInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetUtil.isConnected(App.getContext())) {
                request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
                KLog.e("no network");
            }
            Response originalResponse = chain.proceed(request);

            if (NetUtil.isConnected(App.getContext())) {
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
                String cacheControl = request.cacheControl().toString();
                return originalResponse.newBuilder().header("Cache-Control", cacheControl)
                        .removeHeader("Pragma").build();
            } else {
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached," + CACHE_STALE_SEC)
                        .removeHeader("Pragma").build();
            }
        }
    };

    // 打印返回的json数据拦截器
    private Interceptor mLoggingInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {

            final Request request = chain.request();
            final Response response = chain.proceed(request);

            final ResponseBody responseBody = response.body();
            final long contentLength = responseBody.contentLength();

            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();

            Charset charset = Charset.forName("UTF-8");
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                try {
                    charset = contentType.charset(charset);
                } catch (UnsupportedCharsetException e) {
                    KLog.e("");
                    KLog.e("Couldn't decode the response body; charset is likely malformed.");
                    return response;
                }
            }

            if (contentLength != 0) {
                KLog.v("--------------------------------------------开始打印返回数据----------------------------------------------------");
                KLog.json(buffer.clone().readString(charset));
                KLog.v("--------------------------------------------结束打印返回数据----------------------------------------------------");
            }

            return response;
        }
    };

    /**
     * 获取Gank列表
     *
     * 对API调用了observeOn(MainThread)之后，线程会跑在主线程上，包括onComplete也是，
     * unsubscribe也在主线程，然后如果这时候调用call.cancel会导致NetworkOnMainThreadException
     * 加一句unsubscribeOn(io)
     *
     * @param type
     * @param sise
     * @param startPage
     * @return
     */
    public Observable<ArrayList<BaseGankData>> getGankListObservable(String type,int sise, int startPage) {
        return gankService.getData(type, sise, startPage)
                .map(gankData -> gankData.results)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    public Observable<List<GankDaily>> getGankDailyListObservable(IGankDailyDataPresenterImpl.EasyDate easyDate) {
        return Observable.just(easyDate)
                .flatMapIterable(IGankDailyDataPresenterImpl.EasyDate :: getPastTime)
                .flatMap(easyDate1 -> {
                    /*
                     * 感觉Android的数据应该不会为null
                     * 所以以Android的数据为判断是否当天有数据
                     */
                    return gankService.getDaily(easyDate.getYear(), easyDate.getMonth(), easyDate.getDay())
                            .filter(dailyData -> dailyData.results.androidData != null);
                }).toSortedList(new Func2<GankDaily, GankDaily, Integer>() {
                    @Override
                    public Integer call(GankDaily gankDaily, GankDaily gankDaily2) {
                        return gankDaily2.results.androidData.get(0).publishedAt.compareTo(gankDaily.results.androidData.get(0).publishedAt);
                    }
                });

    }

}
