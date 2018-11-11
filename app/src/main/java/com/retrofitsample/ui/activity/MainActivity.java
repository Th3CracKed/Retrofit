package com.retrofitsample.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.retrofitsample.R;
import com.retrofitsample.api.model.RepositoryModel;
import com.retrofitsample.api.service.RepositoryClient;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.retrofitsample.api.service.RepositoryClient.BASE_URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RepositoryClient client = retrofit.create(RepositoryClient.class);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH,-30);//reduce 30 days of today's date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.US);
        String formattedDate = dateFormat.format(calendar.getTime());//format date to string with specific format
        Map<String,String> params = new HashMap<>();
        params.put("q","created:>"  + formattedDate);
        params.put("sort","stars");
        params.put("order","desc");
        params.put("page","1");
        Call<RepositoryModel> call = client.getRepositories(params);
        call.enqueue(new Callback<RepositoryModel>() {
            @Override
            public void onResponse(Call<RepositoryModel> call, Response<RepositoryModel> response) {
                RepositoryModel reposModel = response.body();
                findViewById(R.id.progressBar).setVisibility(View.GONE);//hide Progress Bar
                findViewById(R.id.txtview).setVisibility(View.VISIBLE);
                printList(reposModel);
            }

            @Override
            public void onFailure(Call<RepositoryModel> call, Throwable t) {
                Log.e("Mainactivity","Error");
                findViewById(R.id.progressBar).setVisibility(View.GONE);//hide Progress Bar
                TextView txt = findViewById(R.id.txtview);
                txt.setText("Error");
                txt.setVisibility(View.VISIBLE);
            }
        });
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
