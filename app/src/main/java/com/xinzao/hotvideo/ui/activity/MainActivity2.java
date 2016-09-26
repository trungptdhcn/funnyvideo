package com.xinzao.hotvideo.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.analytics.Tracker;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.xinzao.hotvideo.MainActivity;
import com.xinzao.hotvideo.MyApplication;
import com.xinzao.hostvideo.R;
import com.xinzao.hotvideo.ui.adapter.GridAdapter;
import com.xinzao.hotvideo.utils.Utils;

public class MainActivity2 extends AppCompatActivity
{

    @Bind(R.id.gridview)
    GridView gridView;
    @Bind(R.id.search_view)
    MaterialSearchView searchView;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    GridAdapter adapter;

    @Bind(R.id.adView)
    AdView adView;
    InterstitialAd mInterstitialAd;

    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
        MyApplication application = (MyApplication) getApplication();
        mTracker = application.getDefaultTracker();
        setSupportActionBar(toolbar);
        searchView.setVoiceSearch(false);
        searchView.setCursorDrawable(R.drawable.custom_cursor);
        searchView.setEllipsize(true);
        searchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));
        adapter = new GridAdapter(this);
        gridView.setAdapter(adapter);
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
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {

                Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                intent.putExtra("keyword", query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                //Do some magic
                return false;
            }
        });
    }

    private void requestNewInterstitial()
    {
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mInterstitialAd.loadAd(adRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

    @Override
    public void onBackPressed()
    {
        if (searchView.isSearchOpen())
        {
            searchView.closeSearch();
        }
        else
        {
            super.onBackPressed();
        }
    }

    @OnItemClick(R.id.gridview)
    public void clickItem(int position)
    {
        if (position == 6)
        {
            mInterstitialAd.show();
        }
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("position", position);
        startActivity(intent);
    }
}
