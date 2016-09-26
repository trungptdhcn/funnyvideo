package com.xinzao.hotvideo;

import android.app.Application;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.xinzao.hostvideo.R;

/**
 * Created by TRUNGPT on 6/1/16.
 */
public class MyApplication extends Application
{
    private Tracker mTracker;

    synchronized public Tracker getDefaultTracker()
    {
        if (mTracker == null)
        {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        FontsOverride.setDefaultFont(this, "SERIF", "fonts/myfont.ttf");
    }
}
