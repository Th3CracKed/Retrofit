package com.retrofitsample.api.service;

import android.util.Log;

import com.retrofitsample.api.model.RepositoriesModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RetrofitHelper {
    private static RetrofitHelper ourInstance;
    private RetrofitHelper(){}

    public static synchronized RetrofitHelper getInstance(){
        if(ourInstance == null){
            ourInstance = new RetrofitHelper();
        }
        return ourInstance;
    }
    /*
    return GET params
    Calculate today - 30 days and parse it to the correct date format
 */
    public Map<String,String> getParams(int requestedPage) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH,-30);//reduce 30 days of today's date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.US);
        String formattedDate = dateFormat.format(calendar.getTime());//format date to string with specific format
        Map<String,String> params = new HashMap<>();
        params.put("q","created:>"  + formattedDate);
        params.put("sort","stars");
        params.put("order","desc");
        params.put("page",String.valueOf(requestedPage));
        return params;
    }

    public void logList(List <RepositoriesModel.Item> items){
        for(RepositoriesModel.Item item : items){
            Log.e("MainActivity"," Name : "+item.getName()+" Description : "+item.getDescription()
                    +" \nLogin : "+item.getOwner().getLogin()+" Watchers : "+item.getWatchers()+" Image url : "+item.getOwner().getAvatar_url()+" Repo Url : "+item.getHtml_url()

            );
        }
    }
}
