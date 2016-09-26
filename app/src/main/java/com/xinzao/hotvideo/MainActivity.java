package com.xinzao.hotvideo;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import butterknife.OnItemClick;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.xinzao.hostvideo.R;
import com.xinzao.hotvideo.async.AsyncTaskListener;
import com.xinzao.hotvideo.async.AsyncTaskSearchData;
import com.xinzao.hotvideo.base.StringUtils;
import com.xinzao.hotvideo.sync.request.YoutubeRequestDTO;
import com.xinzao.hotvideo.ui.model.Item;
import com.xinzao.hotvideo.ui.model.PageModel;
import com.xinzao.hotvideo.ui.activity.YoutubeVideoDetailActivity;
import com.xinzao.hotvideo.ui.adapter.CommonAdapter;
import com.xinzao.hotvideo.ui.listener.EndlessScrollListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.xinzao.hotvideo.utils.Utils;

public class MainActivity extends AppCompatActivity implements AsyncTaskListener
{
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.adView)
    AdView adView;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.list_view)
    ListView listView;
    YoutubeRequestDTO requestDTO;
    String nextPage;
    CommonAdapter adapter;
    String keyword;
    InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        int postion = getIntent().getIntExtra("position", -1);
        String tempKey = getIntent().getStringExtra("keyword");
        Utils.loadBannerAds(adView);
        mInterstitialAd= new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.ads_fullscreen));
        mInterstitialAd.setAdListener(new AdListener()
        {
            @Override
            public void onAdClosed()
            {
                requestNewInterstitial();
            }
        });
        requestNewInterstitial();
        switch (postion)
        {
            case -1:
                keyword = tempKey;
                break;
            case 0:
                keyword = "comedy videos";
                break;
            case 1:
                keyword = "funny babies";
                break;
            case 2:
                keyword = "funny kids";
                break;
            case 3:
                keyword = "funny animal";
                break;
            case 4:
                keyword = "funny prank";
                break;
            case 5:
                keyword = "comedy movies";
                break;
            case 6:
                keyword = "FUNNY MOMENTS";
                break;

        }
        requestDTO = new YoutubeRequestDTO
                .YoutubeRequestBuilder(keyword)
                .type("video")
                .build();
        AsyncTaskSearchData asyncTask = new AsyncTaskSearchData(this, Constant.HOST_NAME.YOUTUBE, requestDTO);
        asyncTask.setListener(this);
        asyncTask.execute();
        EndlessScrollListener endlessScrollListener = new EndlessScrollListener()
        {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount)
            {
                if (StringUtils.isNotEmpty(nextPage))
                {
                    requestDTO.setPageToken(nextPage);
                    AsyncTaskSearchData asyncTask = new AsyncTaskSearchData(MainActivity.this, Constant.HOST_NAME.YOUTUBE, requestDTO);
                    asyncTask.setListener(MainActivity.this);
                    asyncTask.execute();
                    return true;
                }
                else
                {
                    return false;
                }

            }
        };
        listView.setOnScrollListener(endlessScrollListener);
    }

    private void requestNewInterstitial()
    {
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mInterstitialAd.loadAd(adRequest);
    }
    @Override
    public void prepare()
    {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void complete(Object obj)
    {
        PageModel page = (PageModel) obj;
        if (page != null)
        {
            List<Item> videos = page.getItems();
            nextPage = page.getNextPage();
            if (adapter == null)
            {
                adapter = new CommonAdapter(videos, this);
                listView.setAdapter(adapter);
            }
            else
            {
                adapter.getVideos().addAll(videos);
                adapter.notifyDataSetChanged();
            }

        }
        progressBar.setVisibility(View.GONE);
    }

    @OnItemClick(R.id.list_view)
    public void itemClick(int position)
    {
        if (position % 10 == 0)
        {
            mInterstitialAd.show();
        }
        Intent intent = new Intent(this, YoutubeVideoDetailActivity.class);
        intent.putExtra("video", (Parcelable) adapter.getItem(position));
        startActivity(intent);
    }
}
