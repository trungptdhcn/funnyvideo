package com.xinzao.hotvideo.ui.model;

/**
 * Created by TRUNGPT on 6/1/16.
 */
public class Categories
{
    private int icon;
    private String title;

    public Categories(int icon, String title)
    {
        this.icon = icon;
        this.title = title;
    }

    public int getIcon()
    {
        return icon;
    }

    public void setIcon(int icon)
    {
        this.icon = icon;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }
}
