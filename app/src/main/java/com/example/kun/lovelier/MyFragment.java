package com.example.kun.lovelier;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.kun.lovelier.URL.HttpUrl;
import com.example.kun.lovelier.adapter.BaseRecyclerAdapter;
import com.example.kun.lovelier.adapter.RecyclerHolder;
import com.example.kun.lovelier.adapter.SpacesItemDecoration;
import com.example.kun.lovelier.dialog.ImageDiaLog;
import com.example.kun.lovelier.moudle.DataBean;
import com.example.kun.lovelier.view.DetailsActivity;
import com.example.kun.lovelier.view.SuperSwipeRefreshLayout;
import com.google.gson.Gson;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.rey.material.widget.ProgressView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kun on 2016/4/20.
 */
public class MyFragment extends Fragment {

    private static final String TAG = "MyFragment";

    private List<DataBean.NewslistBean> datas; //数据集合

    private Gson gson;

    private SuperSwipeRefreshLayout mRefreshLayout;//刷新控件

    private StaggeredGridLayoutManager lm;

    private int Page = 1; //页数

    private int flag;  //fragment标记

    private ProgressView progressView;  //底部刷新圈

    private ProgressView headprogressView;//头部刷新圈

    private RecyclerView mRecyclerView;

    private BaseRecyclerAdapter<DataBean.NewslistBean> adapter; //适配器


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flag = (int) getArguments().get("flag");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);

        /**
         * 刚进入程序刷新
         */
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                headprogressView.setVisibility(View.VISIBLE);
                mRefreshLayout.setRefreshing(true);
            }
        });
        //2秒后关闭
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                mRefreshLayout.setRefreshing(false);
                headprogressView.setVisibility(View.GONE);
            }
        }, 2000);
    }

    private void init(View view) {

        mRecyclerView = (RecyclerView) view.findViewById(R.id.Rv);
        mRefreshLayout = (SuperSwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        //设置RecylerView布局管理器为垂直垂直排布
        if (flag == 2) {
            mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        } else {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }

        datas = new ArrayList<>();
        gson = new Gson();
        adapter();
        loading(true);
        refresh();
        mRecyclerView.setAdapter(adapter);
        //设置item之间的间隔
        SpacesItemDecoration decoration = new SpacesItemDecoration(5);
        mRecyclerView.addItemDecoration(decoration);


    }

    private void refresh() {

        mRefreshLayout.setTargetScrollWithLayout(true);
        mRefreshLayout.setHeaderView(createHeaderView());
        mRefreshLayout.setFooterView(createFooterView());

        //下拉刷新
        mRefreshLayout.setOnPullRefreshListener(new SuperSwipeRefreshLayout.OnPullRefreshListener() {
            @Override
            public void onRefresh() {
                datas.clear();
                loading(true);
                headprogressView.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        mRefreshLayout.setRefreshing(false);
                        headprogressView.setVisibility(View.GONE);
                    }
                }, 2000);
            }

            @Override
            public void onPullDistance(int distance) {

            }

            @Override
            public void onPullEnable(boolean enable) {

            }
        });

        //上啦加载

        mRefreshLayout.setOnPushLoadMoreListener(new SuperSwipeRefreshLayout.OnPushLoadMoreListener() {
            @Override
            public void onLoadMore() {
                progressView.setVisibility(View.VISIBLE);
                moreLoading(true);
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        progressView.setVisibility(View.GONE);
                        mRefreshLayout.setLoadMore(false);
                    }
                }, 5000);
            }

            @Override
            public void onPushDistance(int distance) {

            }

            @Override
            public void onPushEnable(boolean enable) {
                mRefreshLayout.setLoadMore(true);
            }
        });

    }


    /**
     * 定义头部刷新布局
     *
     * @return
     */
    private View createHeaderView() {
        View headerView = LayoutInflater.from(mRefreshLayout.getContext())
                .inflate(R.layout.head_layout, null);
        headprogressView = (ProgressView) headerView.findViewById(R.id.pb_view);
        headprogressView.setVisibility(View.GONE);
        return headerView;
    }

    /**
     * 定义加载更多布局
     *
     * @return
     */
    private View createFooterView() {
        View footerView = LayoutInflater.from(mRefreshLayout.getContext())
                .inflate(R.layout.more_layout, null);
        progressView = (ProgressView) footerView.findViewById(R.id.Footer);
        progressView.setVisibility(View.GONE);
        return footerView;
    }

    private void loading(boolean isrefresh) {

        if (isrefresh == true) {

            switch (flag) {
                case 0:
                    RxVolley.get(HttpUrl.API + "/wxnew/" + HttpUrl.More, new HttpCallback() {
                        @Override
                        public void onSuccess(String t) {
                            DataBean dataBean = gson.fromJson(t, DataBean.class);
                            datas.addAll(dataBean.getNewslist());
                            adapter.notifyDataSetChanged();
                        }
                    });
                    break;
                case 1:
                    RxVolley.get(HttpUrl.API + "/social/" + HttpUrl.More, new HttpCallback() {
                        @Override
                        public void onSuccess(String t) {
                            DataBean dataBean = gson.fromJson(t, DataBean.class);
                            datas.addAll(dataBean.getNewslist());
                            adapter.notifyDataSetChanged();
                        }
                    });
                    break;
                case 2:
                    RxVolley.get(HttpUrl.API + "/meinv/" + HttpUrl.More, new HttpCallback() {
                        @Override
                        public void onSuccess(String t) {
                            DataBean dataBean = gson.fromJson(t, DataBean.class);
                            datas.addAll(dataBean.getNewslist());
                            adapter.notifyDataSetChanged();
                        }
                    });
                    break;
                case 3:
                    RxVolley.get(HttpUrl.API + "/qiwen/" + HttpUrl.More, new HttpCallback() {
                        @Override
                        public void onSuccess(String t) {
                            DataBean dataBean = gson.fromJson(t, DataBean.class);
                            datas.addAll(dataBean.getNewslist());
                            adapter.notifyDataSetChanged();
                        }
                    });
                    break;
                case 4:
                    RxVolley.get(HttpUrl.API + "/travel/" + HttpUrl.More, new HttpCallback() {
                        @Override
                        public void onSuccess(String t) {
                            DataBean dataBean = gson.fromJson(t, DataBean.class);
                            datas.addAll(dataBean.getNewslist());
                            adapter.notifyDataSetChanged();
                        }
                    });
                    break;
                case 5:
                    RxVolley.get(HttpUrl.API + "/keji/" + HttpUrl.More, new HttpCallback() {
                        @Override
                        public void onSuccess(String t) {
                            DataBean dataBean = gson.fromJson(t, DataBean.class);
                            datas.addAll(dataBean.getNewslist());
                            adapter.notifyDataSetChanged();
                        }
                    });
                    break;
                case 6:
                    RxVolley.get(HttpUrl.API + "/huabian/" + HttpUrl.More, new HttpCallback() {
                        @Override
                        public void onSuccess(String t) {
                            DataBean dataBean = gson.fromJson(t, DataBean.class);
                            datas.addAll(dataBean.getNewslist());
                            adapter.notifyDataSetChanged();
                        }
                    });
                    break;
                case 7:
                    RxVolley.get(HttpUrl.API + "/health/" + HttpUrl.More, new HttpCallback() {
                        @Override
                        public void onSuccess(String t) {
                            DataBean dataBean = gson.fromJson(t, DataBean.class);
                            datas.addAll(dataBean.getNewslist());
                            adapter.notifyDataSetChanged();
                        }
                    });
                    break;
            }
        }
    }

    private void moreLoading(boolean is) {
        if (is == true) {
            Page++;
            switch (flag) {
                case 0:
                    RxVolley.get(HttpUrl.API + "/wxnew/" + HttpUrl.More + Page, new HttpCallback() {
                        @Override
                        public void onSuccess(String t) {
                            DataBean dataBean = gson.fromJson(t, DataBean.class);
                            datas.addAll(dataBean.getNewslist());
                            adapter.notifyDataSetChanged();
                        }
                    });
                    break;
                case 1:
                    RxVolley.get(HttpUrl.API + "/social/" + HttpUrl.More + Page, new HttpCallback() {
                        @Override
                        public void onSuccess(String t) {
                            DataBean dataBean = gson.fromJson(t, DataBean.class);
                            datas.addAll(dataBean.getNewslist());
                            adapter.notifyDataSetChanged();
                        }
                    });
                    break;
                case 2:
                    RxVolley.get(HttpUrl.API + "/meinv/" + HttpUrl.More + Page, new HttpCallback() {
                        @Override
                        public void onSuccess(String t) {
                            DataBean dataBean = gson.fromJson(t, DataBean.class);
                            datas.addAll(dataBean.getNewslist());
                            adapter.notifyDataSetChanged();
                        }
                    });
                    break;
                case 3:
                    RxVolley.get(HttpUrl.API + "/qiwen/" + HttpUrl.More + Page, new HttpCallback() {
                        @Override
                        public void onSuccess(String t) {
                            DataBean dataBean = gson.fromJson(t, DataBean.class);
                            datas.addAll(dataBean.getNewslist());
                            adapter.notifyDataSetChanged();
                        }
                    });
                    break;
                case 4:
                    RxVolley.get(HttpUrl.API + "/travel/" + HttpUrl.More + Page, new HttpCallback() {
                        @Override
                        public void onSuccess(String t) {
                            DataBean dataBean = gson.fromJson(t, DataBean.class);
                            datas.addAll(dataBean.getNewslist());
                            adapter.notifyDataSetChanged();
                        }
                    });
                    break;
                case 5:
                    RxVolley.get(HttpUrl.API + "/keji/" + HttpUrl.More, new HttpCallback() {
                        @Override
                        public void onSuccess(String t) {
                            DataBean dataBean = gson.fromJson(t, DataBean.class);
                            datas.addAll(dataBean.getNewslist());
                            adapter.notifyDataSetChanged();
                        }
                    });
                    break;
                case 6:
                    RxVolley.get(HttpUrl.API + "/huabian/" + HttpUrl.More, new HttpCallback() {
                        @Override
                        public void onSuccess(String t) {
                            DataBean dataBean = gson.fromJson(t, DataBean.class);
                            datas.addAll(dataBean.getNewslist());
                            adapter.notifyDataSetChanged();
                        }
                    });
                    break;
                case 7:
                    RxVolley.get(HttpUrl.API + "/health/" + HttpUrl.More, new HttpCallback() {
                        @Override
                        public void onSuccess(String t) {
                            DataBean dataBean = gson.fromJson(t, DataBean.class);
                            datas.addAll(dataBean.getNewslist());
                            adapter.notifyDataSetChanged();
                        }
                    });
                    break;
            }
        }
    }

    private void adapter() {

        if (flag == 2) {
            adapter = new BaseRecyclerAdapter<DataBean.NewslistBean>(mRecyclerView, datas, R.layout.item_layout) {
                @Override
                public void convert(RecyclerHolder holder, final DataBean.NewslistBean item, int position, boolean isScrolling) {

                    ImageView imageView = holder.getView(R.id.img);

                    TextView title = holder.getView(R.id.img_title);

                    Glide.with(getActivity().getApplication())
                            .load(item.getPicUrl())
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(imageView);

                    holder.setText(R.id.img_title, item.getTitle());

                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            ImageDiaLog imageDiaLog = new ImageDiaLog();
                            Bundle bundle = new Bundle();
                            
                            bundle.putString("imgurl", item.getPicUrl());
                            imageDiaLog.setArguments(bundle);

                            imageDiaLog.show(getActivity().getFragmentManager(), "aaa");

                        }
                    });

                    title.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.putExtra("weburl", item.getUrl());
                            intent.setClass(getActivity(), DetailsActivity.class);
                            startActivity(intent);
                        }
                    });

                }
            };
        } else
            adapter = new BaseRecyclerAdapter<DataBean.NewslistBean>(mRecyclerView, datas, R.layout.recy_item_layout) {
                @Override
                public void convert(RecyclerHolder holder, final DataBean.NewslistBean item, int position, boolean isScrolling) {

                    ImageView imageView = holder.getView(R.id.picImg);

                    holder.setText(R.id.picImgTitle, item.getTitle());

                    holder.setText(R.id.tag_title, item.getDescription());

                    holder.setText(R.id.date, item.getCtime());

                    LinearLayout view = holder.getView(R.id.layout);

                    Glide.with(getActivity().getApplication())
                            .load(item.getPicUrl())
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(imageView);

                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            ImageDiaLog imageDiaLog = new ImageDiaLog();
                            Bundle bundle = new Bundle();
                            bundle.putString("imgurl", item.getPicUrl());
                            imageDiaLog.setArguments(bundle);
                            imageDiaLog.show(getActivity().getFragmentManager(), "aaa");

                        }
                    });

                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.putExtra("weburl", item.getUrl());
                            intent.setClass(getActivity(), DetailsActivity.class);
                            startActivity(intent);
                        }
                    });

                }
            };


    }
}
