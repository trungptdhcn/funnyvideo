package com.xinzao.hotvideo.async;

import android.content.Context;
import android.os.AsyncTask;

import com.xinzao.hotvideo.sync.RequestDTO;
import com.xinzao.hotvideo.sync.YoutubeConnector;

/**
 * Created by TRUNGPT on 5/30/16.
 */
public class AsyncTaskMostPopular extends AsyncTask<Void, Void, Object>
{
    AsyncTaskListener listener;
    YoutubeConnector ytbConnect;
    private RequestDTO requestDTO;

    public AsyncTaskMostPopular(Context context, RequestDTO requestDTO)
    {
        this.requestDTO = requestDTO;
        ytbConnect = new YoutubeConnector(context);
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
        listener.prepare();
    }

    @Override
    protected Object doInBackground(Void... params)
    {
        Object o  = ytbConnect.getVideoByCategoriesId(requestDTO);
        return o;
    }

    @Override
    protected void onPostExecute(Object o)
    {
        super.onPostExecute(o);
        listener.complete(o);
    }

    public AsyncTaskListener getListener()
    {
        return listener;

    }

    public void setListener(AsyncTaskListener listener)
    {
        this.listener = listener;
    }
}
