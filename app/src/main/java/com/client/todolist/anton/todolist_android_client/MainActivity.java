package com.client.todolist.anton.todolist_android_client;

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
    private List<ToDoItem> toDoItemsList;

    private ClientRequestInterface service;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toDoItemsList = new ArrayList<>();

        service = getClient().create(ClientRequestInterface.class);

        mRecyclerView = findViewById(R.id.my_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        RecyclerViewClickListener onChekedChangeListener = (compoundButton, b, position) -> {
            Call<ToDoItem> toDoCall = service.changeToDoItemStatus(position, b);
            toDoCall.enqueue(new Callback<ToDoItem>() {
                @Override
                public void onResponse(Call<ToDoItem> call, Response<ToDoItem> response) {
                    if(response.isSuccessful()){
                        toDoItemsList.set(position, new ToDoItem(toDoItemsList.get(position).getId(), toDoItemsList.get(position).getText(), b));

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

        View.OnClickListener createToDoItemListener = (View v) ->{
            Call<ToDoItem> toDoCall = service.postNewToDoItem("Post from android");
            toDoCall.enqueue(new Callback<ToDoItem>() {
                @Override
                public void onResponse(Call<ToDoItem> call, Response<ToDoItem> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "Item added.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ToDoItem> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
                    call.cancel();
                }
            });
        };

        mAdapter = new RecyclerViewAdapter(toDoItemsList, onChekedChangeListener, createToDoItemListener);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }



    public void refreshToDoItems(View view) {
        Call <List<ToDoItem>> toDoCall = service.getAllToDoItems();
        toDoCall.enqueue(new Callback<List<ToDoItem>>() {
            @Override
            public void onResponse(Call<List<ToDoItem>> call, Response<List<ToDoItem>> response) {
                if(response.isSuccessful()){
                    toDoItemsList.clear();
                    toDoItemsList.addAll(response.body());

                    Toast.makeText(getApplicationContext(), "Successful. List size: " + toDoItemsList.size(), Toast.LENGTH_SHORT).show();

                    mRecyclerView.getAdapter().notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<ToDoItem>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    public void postNewToDoItem(View view) {
        Call<ToDoItem> toDoCall = service.postNewToDoItem("Post from android");
        toDoCall.enqueue(new Callback<ToDoItem>() {
            @Override
            public void onResponse(Call<ToDoItem> call, Response<ToDoItem> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Item added.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ToDoItem> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
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
