package com.example.kun.lovelier.view;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.Address;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.example.kun.lovelier.R;
import com.example.kun.lovelier.URL.HttpUrl;
import com.example.kun.lovelier.dialog.WeatherDialog;
import com.example.kun.lovelier.moudle.WeatherBean;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.rey.material.widget.ProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 天气Activity
 */
public class WeatherActivity extends BaseActivity {

    private static final String TAG = "WeatherActivity";

    //百度定位
    public LocationClient mLocationClient = null;

    private Gson gson;

    private SuperSwipeRefreshLayout mRefreshLayout;//刷新控件

    private ProgressView progressView;  //底部刷新圈

    private ProgressView headprogressView;//头部刷新圈

    private LinearLayout linearLayout;//填充布局

    private LayoutInflater inflater;


    private TextView now_tmp, now_cond, ip_aqi, cityText, locText;

    @Override
    protected void initContentView() {
        setContentView(R.layout.weather_activity);
        initbaidu();
        gson = new Gson();

        initView();

        init();

        refresh();

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

    private void refresh() {
        mRefreshLayout.setTargetScrollWithLayout(true);
        mRefreshLayout.setHeaderView(createHeaderView());
        mRefreshLayout.setFooterView(createFooterView());

        //下拉刷新
        mRefreshLayout.setOnPullRefreshListener(new SuperSwipeRefreshLayout.OnPullRefreshListener() {
            @Override
            public void onRefresh() {
                initbaidu();
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

    }

    private void init() {
        inflater = getLayoutInflater();
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

    private void initView() {

        linearLayout = (LinearLayout) findViewById(R.id.linerlayout);


        mRefreshLayout = (SuperSwipeRefreshLayout) findViewById(R.id.swipe_container);

        now_tmp = (TextView) findViewById(R.id.now_tmp);
        now_cond = (TextView) findViewById(R.id.now_cond);
        ip_aqi = (TextView) findViewById(R.id.ip_qlty);
        cityText = (TextView) findViewById(R.id.city);
        locText = (TextView) findViewById(R.id.loc);
    }

    private void loading(String string, boolean isfalg) {
        if (isfalg == true) {
            RxVolley.get(HttpUrl.WEATH + string, new HttpCallback() {
                @Override
                public void onSuccess(String t) {
                    try {
                        JSONObject jsonObject = new JSONObject(t);
                        JSONArray array = jsonObject.getJSONArray("HeWeather data service 3.0");
                        JSONObject object = array.getJSONObject(0);
                        JSONArray hourly_forecast = object.getJSONArray("hourly_forecast");
                        JSONObject basic = object.getJSONObject("basic");
                        JSONObject update = basic.getJSONObject("update");
                        JSONObject api = object.getJSONObject("aqi");
                        final JSONObject city = api.getJSONObject("city");
                        JSONObject now = object.getJSONObject("now");
                        JSONObject code = now.getJSONObject("cond");
                        ip_aqi.setText(city.optString("qlty"));  //空气质量类别
                        now_tmp.setText(now.optString("tmp"));  //当前温度
                        now_cond.setText(code.optString("txt")); // 天气状况
                        cityText.setText(basic.optString("city"));//当前城市
                        locText.setText(update.optString("loc")); //当前更新时间

                        ip_aqi.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                WeatherDialog dialog = new WeatherDialog();
                                Bundle bundle = new Bundle();
                                bundle.putString("api", city.optString("api"));
                                bundle.putString("no2", city.optString("no2"));
                                bundle.putString("pm10", city.optString("pm10"));
                                bundle.putString("pm25", city.optString("pm25"));
                                bundle.putString("so2", city.optString("so2"));
                                dialog.setArguments(bundle);
                                dialog.show(getFragmentManager(), "");
                            }
                        });


                        for (int i = 0; i < hourly_forecast.length(); i++) {
                            View inflate = inflater.inflate(R.layout.item_linear, null);
                            TextView time = (TextView) inflate.findViewById(R.id.time);
                            TextView tmp = (TextView) inflate.findViewById(R.id.tmp);
                            String nowtime = hourly_forecast.getJSONObject(i).optString("date");
                            String str = nowtime.replace(" ", "");
                            time.setText(str.substring(str.length()-5));
                            tmp.setText(hourly_forecast.getJSONObject(i).optString("tmp"));
                            linearLayout.addView(inflate);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }

    }

    private void initbaidu() {
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                String city = bdLocation.getCity();
                String s = city.substring(0, city.length() - 1);
                loading(s, true);
            }
        });    //注册监听函数
        initLocation();
        mLocationClient.start();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        option.setScanSpan(0);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
    }
}
