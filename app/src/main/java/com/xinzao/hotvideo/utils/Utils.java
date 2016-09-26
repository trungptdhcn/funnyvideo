package com.xinzao.hotvideo.utils;

import android.content.Context;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.xinzao.hostvideo.R;
import com.xinzao.hotvideo.ui.model.Categories;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TRUNGPT on 6/1/16.
 */
public class Utils
{
    public static List<Categories> getCategories()
    {
        List<Categories> categories = new ArrayList<>();
        Categories categories1 = new Categories(R.drawable.comedy,"COMEDY VIDEOS");
        Categories categories2 = new Categories(R.drawable.funnybabies,"FUNNY BABIES VIDEOS");
        Categories categories3 = new Categories(R.drawable.funnykids,"FUNNY KIDS");
        Categories categories4 = new Categories(R.drawable.funnyaninal,"FUNNY ANIMAL VIDEOS");
        Categories categories5 = new Categories(R.drawable.funnyprank,"FUNNY PRANK VIDEOS");
        Categories categories6 = new Categories(R.drawable.comedymovies,"COMEDY MOVIES VIDEOS");
        Categories categories7 = new Categories(R.drawable.funnymoment,"FUNNY MOMENTS VIDEOS");
        categories.add(categories1);
        categories.add(categories2);
        categories.add(categories3);
        categories.add(categories4);
        categories.add(categories5);
        categories.add(categories6);
        categories.add(categories7);
        return categories;
    }

    private static AdRequest adRequest = new AdRequest.Builder()
            .addTestDevice("")
            .build();
    public static void loadBannerAds(AdView adView)
    {
        adView.loadAd(adRequest);
    }

    public static void loadFullScreenAds(Context context)
    {
        final InterstitialAd mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId(context.getString(R.string.ads_fullscreen));
        mInterstitialAd.setAdListener(new AdListener()
        {
            @Override
            public void onAdClosed()
            {
                mInterstitialAd.loadAd(adRequest);
            }
        });
        mInterstitialAd.loadAd(adRequest);
        mInterstitialAd.show();
    }
}
