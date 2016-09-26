package com.xinzao.hotvideo.ui.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.google.android.gms.ads.AdView;
import com.poliveira.parallaxrecyclerview.ParallaxRecyclerAdapter;
import com.squareup.picasso.Picasso;
import com.xinzao.hotvideo.Constant;
import com.xinzao.hostvideo.R;
import com.xinzao.hotvideo.async.AsyncTaskListener;
import com.xinzao.hotvideo.async.AsyncTaskSearchData;
import com.xinzao.hotvideo.base.StringUtils;
import com.xinzao.hotvideo.sync.RequestDTO;
import com.xinzao.hotvideo.sync.request.YoutubeRequestDTO;
import com.xinzao.hotvideo.ui.model.Item;
import com.xinzao.hotvideo.ui.model.PageModel;
import com.xinzao.hotvideo.ui.model.UserModel;
import com.xinzao.hotvideo.ui.adapter.RecycleAdapter;
import com.xinzao.hotvideo.ui.customview.SimpleDividerItemDecoration;
import com.xinzao.hotvideo.ui.listener.EndlessRecyclerOnScrollListener;
import com.xinzao.hotvideo.utils.Utils;

import java.text.DecimalFormat;
import java.util.List;

public class UserDetailActivity extends AppCompatActivity implements AsyncTaskListener, ParallaxRecyclerAdapter.OnParallaxScroll
{
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.recyclerview)
    RecyclerView recyclerView;
    @Bind(R.id.activity_user_detail_toolbar)
    Toolbar toolbar;
    @Bind(R.id.adView)
    AdView adView;
    String nextPage;
    RecycleAdapter adapter;
    RequestDTO requestDTO;
    UserModel userModel;
    EndlessRecyclerOnScrollListener scrollListener;
    View header;
    private Constant.HOST_NAME host_name;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        ButterKnife.bind(this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getResources()));
        Intent i = getIntent();
        userModel = (UserModel) i.getSerializableExtra("video");
        host_name = (Constant.HOST_NAME) i.getSerializableExtra("host_name");
        Utils.loadBannerAds(adView);
        if (toolbar != null)
        {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(userModel.getName());
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    onBackPressed();
                }
            });
        }
        header = getLayoutInflater().inflate(R.layout.cover_layout, recyclerView, false);
        ImageView ivCover = (ImageView) header.findViewById(R.id.cover_layout_ivCover);
        ImageView ivUserAvatar = (ImageView) header.findViewById(R.id.cover_layout_ivUserAvatar);
        TextView tvUserName = (TextView) header.findViewById(R.id.cover_layout_tvUserName);
        TextView tvLikeOfUser = (TextView) header.findViewById(R.id.cover_layout_tvLikes);
        TextView tvFollowsOfUser = (TextView) header.findViewById(R.id.cover_layout_tvFollows);
        DecimalFormat df = new DecimalFormat("#,###");
        tvLikeOfUser.setText(userModel.getSubscrible() != null ? (df.format(userModel.getSubscrible()) + " likes") : "0 likes");
        tvFollowsOfUser.setText(userModel.getSubscrible() != null ? (df.format(userModel.getSubscrible()) + " subscribes") : "0 subscribes");
        Picasso.with(this)
                .load(userModel.getUrlAvatar())
                .placeholder(R.drawable.ic_default)
                .into(ivUserAvatar);
        Picasso.with(this)
                .load(userModel.getUrlCover())
                .placeholder(R.drawable.ic_default)
                .into(ivCover);
        requestDTO = new YoutubeRequestDTO
                .YoutubeRequestBuilder("")
                .id(userModel.getId())
                .build();
        AsyncTaskSearchData asyncTask = new AsyncTaskSearchData(this, Constant.HOST_NAME.YOUTUBE_VIDEO_BY_CHANNEL_ID, requestDTO);
        asyncTask.setListener(this);
        asyncTask.execute();
        tvUserName.setText(userModel.getName());

        scrollListener = new EndlessRecyclerOnScrollListener(mLayoutManager)
        {
            @Override
            public void onLoadMore(final int current_page)
            {
                if (StringUtils.isNotEmpty(nextPage))
                {
                    if (host_name.equals(Constant.HOST_NAME.YOUTUBE))
                    {
                        ((YoutubeRequestDTO) requestDTO).setPageToken(nextPage);
                        AsyncTaskSearchData asyncTask = new AsyncTaskSearchData(UserDetailActivity.this
                                , Constant.HOST_NAME.YOUTUBE_VIDEO_BY_CHANNEL_ID, requestDTO);
                        asyncTask.setListener(UserDetailActivity.this);
                        asyncTask.execute();
                    }
                }

            }
        };
        recyclerView.addOnScrollListener(scrollListener);
    }

    @Override
    public void prepare()
    {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void complete(Object obj)
    {
        PageModel pageModel = (PageModel) obj;
        if (pageModel != null)
        {
            List<Item> items = pageModel.getItems();
            nextPage = pageModel.getNextPage();
            if (adapter == null)
            {
                adapter = new RecycleAdapter(items, this, host_name);
                adapter.setParallaxHeader(header, recyclerView);
                adapter.setOnParallaxScroll(this);
                recyclerView.setAdapter(adapter);
            }
            else
            {
                adapter.getData().addAll(items);
                adapter.notifyItemInserted(adapter.getItemCount());
            }
        }
        progressBar.setVisibility(View.GONE);
    }


    @Override
    public void onParallaxScroll(float percentage, float offset, View parallax)
    {
        Drawable c = toolbar.getBackground();
        c.setAlpha(Math.round(percentage * 255));
        toolbar.setBackgroundDrawable(c);
    }
}
