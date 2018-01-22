package com.client.todolist.anton.todolist_android_client;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private List<ToDoItem> mToDoItemsList;

    private ClientRequestInterface mResponceService;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        mToDoItemsList = new ArrayList<>();

        mResponceService = getClient().create(ClientRequestInterface.class);

        mRecyclerView = findViewById(R.id.toDoItemsRecyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mSwipeRefreshLayout.setOnRefreshListener(getSwipeRefreshLayoutListener());

        mAdapter = new RecyclerViewAdapter(mToDoItemsList, getOnCheckedChangesListener(), getCreateToDoItemListener());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.getAdapter().notifyDataSetChanged();

        getAllToDoItems();
    }

    private SwipeRefreshLayout.OnRefreshListener getSwipeRefreshLayoutListener(){
        SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllToDoItems();
            }
        };

        return onRefreshListener;
    }

    private RecyclerViewClickListener getOnCheckedChangesListener(){
        RecyclerViewClickListener onCheckedChangeListener = (compoundButton, b, position) -> {
            Call<ToDoItem> toDoCall = mResponceService.changeToDoItemStatus(position, b);
            toDoCall.enqueue(new Callback<ToDoItem>() {
                @Override
                public void onResponse(Call<ToDoItem> call, Response<ToDoItem> response) {
                    if(response.isSuccessful()){
                        mToDoItemsList.set(position, new ToDoItem(mToDoItemsList.get(position).getText(), b));

                        mRecyclerView.getAdapter().notifyDataSetChanged();
                    }

                }

                @Override
                public void onFailure(Call<ToDoItem> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
                    call.cancel();
                }
            });
        };

        return onCheckedChangeListener;
    }

    private View.OnClickListener getCreateToDoItemListener(){
        String itemText = "Post from android";

        View.OnClickListener createToDoItemListener = (View v) ->{
            Call<ToDoItem> toDoCall = mResponceService.postNewToDoItem(itemText);
            toDoCall.enqueue(new Callback<ToDoItem>() {
                @Override
                public void onResponse(Call<ToDoItem> call, Response<ToDoItem> response) {
                    if(response.isSuccessful()){
                        mToDoItemsList.add(new ToDoItem(itemText, false));

                        mRecyclerView.getAdapter().notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<ToDoItem> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
                    call.cancel();
                }
            });
        };

        return createToDoItemListener;
    }

    private void getAllToDoItems(){
        Call <List<ToDoItem>> toDoCall = mResponceService.getAllToDoItems();
        toDoCall.enqueue(new Callback<List<ToDoItem>>() {
            @Override
            public void onResponse(Call<List<ToDoItem>> call, Response<List<ToDoItem>> response) {
                if(response.isSuccessful()){
                    mSwipeRefreshLayout.setRefreshing(false);

                    mToDoItemsList.clear();
                    mToDoItemsList.addAll(response.body());

                    Toast.makeText(getApplicationContext(), "Successful. List size: " + mToDoItemsList.size(), Toast.LENGTH_SHORT).show();

                    mRecyclerView.getAdapter().notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<ToDoItem>> call, Throwable t) {
                mSwipeRefreshLayout.setRefreshing(false);

                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    private Retrofit getClient(){
        Retrofit retrofit;

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.101:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }
}
