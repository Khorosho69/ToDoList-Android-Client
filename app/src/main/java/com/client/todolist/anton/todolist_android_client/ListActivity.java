package com.client.todolist.anton.todolist_android_client;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

public class ListActivity extends AppCompatActivity {

    public ClientRequestInterface mResponceService;
    private List<ToDoItem> mToDoItemsList;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> createItem());

        mToDoItemsList = new ArrayList<>();

        mResponceService = getClient().create(ClientRequestInterface.class);

        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this::updateAllToDoItems);

        mRecyclerView = findViewById(R.id.toDoItemsRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.Adapter mAdapter = new RecyclerViewAdapter(this, mToDoItemsList);
        mRecyclerView.setAdapter(mAdapter);

        updateAllToDoItems();
    }

    private void createItem() {
        String itemText = "Post from android";

        String itemText1 = itemText + " " + mToDoItemsList.size();
        Call<ToDoItem> toDoCall = mResponceService.postNewToDoItem(itemText1);
        toDoCall.enqueue(new Callback<ToDoItem>() {
            @Override
            public void onResponse(Call<ToDoItem> call, Response<ToDoItem> response) {
                if (response.isSuccessful()) {
                    mToDoItemsList.add(new ToDoItem(itemText1, false));
                    mRecyclerView.getAdapter().notifyItemInserted(mToDoItemsList.size());
                }
            }

            @Override
            public void onFailure(Call<ToDoItem> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });
    }

    private void updateAllToDoItems() {
        Call<List<ToDoItem>> toDoCall = mResponceService.getAllToDoItems();
        toDoCall.enqueue(new Callback<List<ToDoItem>>() {
            @Override
            public void onResponse(Call<List<ToDoItem>> call, Response<List<ToDoItem>> response) {
                if (response.isSuccessful()) {
                    mSwipeRefreshLayout.setRefreshing(false);

                    mToDoItemsList.clear();
                    mToDoItemsList.addAll(response.body());

                    Toast.makeText(ListActivity.this, "Successful. List size: " + mToDoItemsList.size(), Toast.LENGTH_SHORT).show();

                    mRecyclerView.getAdapter().notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<ToDoItem>> call, Throwable t) {
                mSwipeRefreshLayout.setRefreshing(false);

                Toast.makeText(ListActivity.this, t.toString(), Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }


    private Retrofit getClient() {
        Retrofit retrofit;

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }
}
