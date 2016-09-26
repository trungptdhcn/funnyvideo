package com.xinzao.hotvideo.ui.adapter;

import android.app.Activity;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.squareup.picasso.Picasso;
import com.xinzao.hostvideo.R;
import com.xinzao.hotvideo.ui.model.Categories;
import com.xinzao.hotvideo.utils.Utils;

import java.util.List;

/**
 * Created by TRUNGPT on 6/1/16.
 */
public class GridAdapter extends BaseAdapter
{
    private Activity activity;
    private List<Categories> categories;
    public GridAdapter(Activity activity)
    {
        this.activity = activity;
        categories = Utils.getCategories();
    }

    @Override
    public int getCount()
    {
        return categories.size();
    }

    @Override
    public Object getItem(int position)
    {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Categories category = categories.get(position);
        LayoutInflater inflater = activity.getLayoutInflater();
        final ViewHolder holder;
        if (convertView != null)
        {
            holder = (ViewHolder) convertView.getTag();
        }
        else
        {
            convertView = inflater.inflate(R.layout.grid_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        Picasso.with(activity)
                .load(category.getIcon())
                .resize(width / 2, height / 5)
                .centerCrop()
                .into(holder.ivThumbnail)
        ;
        holder.tvTitle.setText(category.getTitle());
        return convertView;
    }

    static class ViewHolder
    {
        @Bind(R.id.grid_item_ivThumbnail)
        ImageView ivThumbnail;
        @Bind(R.id.grid_item_tvTitle)
        TextView tvTitle;

        public ViewHolder(View view)
        {
            ButterKnife.bind(this, view);
        }
    }
}
