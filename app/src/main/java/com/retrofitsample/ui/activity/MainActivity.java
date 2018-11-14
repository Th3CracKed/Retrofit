package com.retrofitsample.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
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

import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private TextView tvInternetStatus;
    private TextView result;
    private ProgressBar progressBar;
    private Disposable internetDisposable;
    private API api;
    private String requestedPage = "1";//counter
    private Boolean isRequested = true;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        api = RetrofitClient.getInstance().create(API.class);
        tvInternetStatus = findViewById(R.id.internet_status);
        result = findViewById(R.id.txtview);
        progressBar = findViewById(R.id.progressBar);
    }

    @Override protected void onResume() {
        super.onResume();
        onInternetAvailabilityChange();
    }

    @Override protected void onPause() {
        super.onPause();
        safelyDispose(internetDisposable,compositeDisposable);
    }

    private void safelyDispose(Disposable... disposables) {
        for (Disposable subscription : disposables) {
            if (subscription != null && !subscription.isDisposed()) {
                subscription.dispose();
            }
        }
    }

    private void onInternetAvailabilityChange() {
        internetDisposable = ReactiveNetwork
                .observeInternetConnectivity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isConnected ->{
                    Log.e("Main","onInternetAvailabilityChange");
                    tvInternetStatus.setText(isConnected.toString());
                    //if internet became available, and user requested a page
                    if (isConnected && isRequested) {
                        progressBar.setVisibility(View.VISIBLE);//show Progress Bar
                        result.setVisibility(View.INVISIBLE);
                        fetchData();
                    }
                });
    }

    private void fetchData(){
        compositeDisposable.add(api.getRepositories(getParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onFetchSuccess, throwable -> onFetchFailed()));
    }

    private void onFetchSuccess(RepositoryModel repositoryModel) {
        progressBar.setVisibility(View.INVISIBLE);//hide Progress Bar
        result.setVisibility(View.VISIBLE);
        printList(repositoryModel);
        requestedPage = String.valueOf(Integer.parseInt(requestedPage)+1);//increment
        isRequested = false;//request delivered
    }

    private void onFetchFailed() {
        Log.e("Mainactivity","Error");
        progressBar.setVisibility(View.INVISIBLE);//hide Progress Bar
        result.setText("Error");
        result.setVisibility(View.VISIBLE);
    }

    /*
        return GET params
        Calculate today - 30 days and parse it to the correct date format
     */
    private Map<String,String> getParams() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH,-30);//reduce 30 days of today's date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.US);
        String formattedDate = dateFormat.format(calendar.getTime());//format date to string with specific format
        Map<String,String> params = new HashMap<>();
        params.put("q","created:>"  + formattedDate);
        params.put("sort","stars");
        params.put("order","desc");
        params.put("page",requestedPage);
        return params;
    }

    private void printList(RepositoryModel reposModel){
        List<RepositoryModel.Item> items = reposModel.getItems();
        for(RepositoryModel.Item item : items){
            Log.e("MainActivity"," Name : "+item.getName()+" Description : "+item.getDescription()
                    +" \nLogin : "+item.getOwner().getLogin()+" Watchers : "+item.getWatchers()+" Image url : "+item.getOwner().getAvatar_url()+" Repo Url : "+item.getHtml_url()

            );
        }
    }
}
