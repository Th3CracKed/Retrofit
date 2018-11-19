package com.retrofitsample.observer;

import android.util.Log;
import android.view.View;

import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.retrofitsample.api.model.RepositoriesModel;
import com.retrofitsample.api.service.API;
import com.retrofitsample.api.service.RetrofitClient;
import com.retrofitsample.api.service.RetrofitHelper;
import com.retrofitsample.databinding.ActivityMainBinding;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MyObserver implements LifecycleObserver {

    private Disposable internetDisposable;
    private API api;
    private int requestedPage = 1;//counter
    private Boolean isRequested = true;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ActivityMainBinding binding;

    public MyObserver(ActivityMainBinding mBinding){
        binding = mBinding;
        api = RetrofitClient.getInstance().create(API.class);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume(){
        onInternetAvailabilityChange();
        //TODO Deviendra pull to recharge
        binding.newPage.setOnClickListener(v -> fetchData());
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause(){
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
                    //TODO changera en callback parameter
                    Log.e("Main","onInternetAvailabilityChange");
                    binding.internetStatus.setText(isConnected.toString());
                    //if internet became available, and user requested a page
                    if (isConnected && isRequested) {
                        binding.progressBar.setVisibility(View.VISIBLE);//show Progress Bar
                        binding.result.setVisibility(View.INVISIBLE);
                        fetchData();
                    }
                });
    }


    private void fetchData(){
        compositeDisposable.add(api.getRepositories(RetrofitHelper.getInstance().getParams(requestedPage))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onFetchSuccess, throwable -> onFetchFailed()));
    }

    private void onFetchSuccess(RepositoriesModel repositoriesModel) {
        binding.progressBar.setVisibility(View.INVISIBLE);//hide Progress Bar
        binding.result.setVisibility(View.VISIBLE);
        RetrofitHelper.getInstance().logList(repositoriesModel.getItems());
        requestedPage += 1;//increment
        isRequested = false;//request delivered
    }

    private void onFetchFailed() {
        Log.e("MainActivity","Error");
        isRequested = true; //queue a request that will be launched when internet is available again
        binding.progressBar.setVisibility(View.INVISIBLE);//hide Progress Bar
        binding.result.setText("Error");
        binding.result.setVisibility(View.VISIBLE);
    }

}
