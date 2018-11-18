package com.retrofitsample.ui.activity;

import android.os.Bundle;

import com.retrofitsample.R;
import com.retrofitsample.api.service.API;
import com.retrofitsample.api.service.RetrofitClient;
import com.retrofitsample.databinding.ActivityMainBinding;
import com.retrofitsample.observer.MyObserver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        this.getLifecycle().addObserver(new MyObserver(binding));
    }
}
