package com.retrofitsample.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.retrofitsample.R;
import com.retrofitsample.api.model.RepositoryModel;
import com.retrofitsample.api.service.API;
import com.retrofitsample.api.service.RetrofitClient;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        API api = RetrofitClient.getInstance().create(API.class);
        fetchData(api,"1");
    }
    //it's even cooler to add this https://medium.com/mindorks/rxjava2-and-retrofit2-error-handling-on-a-single-place-8daf720d42d6
    private void fetchData(API api, String page) {
        api.getRepositories(getParams(page))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<RepositoryModel>() {
                    @Override
                    public void onNext(RepositoryModel repositoryModel) {
                        findViewById(R.id.progressBar).setVisibility(View.GONE);//hide Progress Bar
                        findViewById(R.id.txtview).setVisibility(View.VISIBLE);
                        printList(repositoryModel);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("Mainactivity","Error");
                        findViewById(R.id.progressBar).setVisibility(View.GONE);//hide Progress Bar
                        TextView txt = findViewById(R.id.txtview);
                        txt.setText("Error");
                        txt.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    /*
        return GET params
        Calculate today - 30 days and parse it to the correct date format
     */
    private Map<String,String> getParams(String page) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH,-30);//reduce 30 days of today's date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.US);
        String formattedDate = dateFormat.format(calendar.getTime());//format date to string with specific format
        Map<String,String> params = new HashMap<>();
        params.put("q","created:>"  + formattedDate);
        params.put("sort","stars");
        params.put("order","desc");
        params.put("page",page);
        return params;
    }

    private void printList(RepositoryModel reposModel){
        List<RepositoryModel.Item> items = reposModel.getItems();
        for(RepositoryModel.Item item : items){
            Log.d("MainActivity"," Name : "+item.getName()+" Description : "+item.getDescription()
                    +" \nLogin : "+item.getOwner().getLogin()+" Watchers : "+item.getWatchers()+" Image url : "+item.getOwner().getAvatar_url()+" Repo Url : "+item.getHtml_url()

            );
        }
    }
}
