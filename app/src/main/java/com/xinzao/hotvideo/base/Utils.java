package com.xinzao.hotvideo.base;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Trung on 11/26/2015.
 */
public class Utils
{
    public static String readFromFile(String fileName, Context context)
    {
        StringBuilder returnString = new StringBuilder();
        InputStream fIn = null;
        InputStreamReader isr = null;
        BufferedReader input = null;
        try
        {
            fIn = context.getResources().getAssets()
                    .open(fileName, Context.MODE_PRIVATE);
            isr = new InputStreamReader(fIn);
            input = new BufferedReader(isr);
            String line = "";
            while ((line = input.readLine()) != null)
            {
                returnString.append(line);
            }
        }
        catch (Exception e)
        {
            e.getMessage();
        }
        finally
        {
            try
            {
                if (isr != null)
                {
                    isr.close();
                }
                if (fIn != null)
                {
                    fIn.close();
                }
                if (input != null)
                {
                    input.close();
                }
            }
            catch (Exception e2)
            {
                e2.getMessage();
            }
        }
        return returnString.toString();
    }

    public static String calculateTime(long before, long after)
    {
        long time = after - before;
        long hours = time / (1000 * 60 * 60);
        long day = hours / 24;
        long week = day / 7;
        long month = day / 30;
        long year = day / 365;
        if (hours < 24)
        {
            if (hours == 0)
            {
                return "one hours ago";
            }
            else
            {
                return hours + " hours ago";
            }
        }
        else if (day > 0 && week < 1)
        {
            return day + " days ago";
        }
        if (week >= 1 && month < 1)
        {
            return week + " weeks ago";
        }
//        else if (day > 7 && day < 30)
//        {
//            return week + " weeks ago";
//        }
//        else if (day==30)
//        {
//            return "a months ago";
//        }
        else if (month >= 1 && year < 1)
        {
            return month + " months ago";
        }
        else
        {
            return year + " years ago";
        }
    }
}
