package com.client.todolist.anton.todolist_android_client.activities;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.client.todolist.anton.todolist_android_client.R;
import com.client.todolist.anton.todolist_android_client.adapters.RecyclerViewAdapter;
import com.client.todolist.anton.todolist_android_client.api.ServiceGenerator;
import com.client.todolist.anton.todolist_android_client.api.ToDoItemsAPIInterface;
import com.client.todolist.anton.todolist_android_client.api.models.ToDoItem;
import com.client.todolist.anton.todolist_android_client.fragments.CreateItemDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListActivity extends AppCompatActivity implements CreateItemDialog.NoticeDialogListener {

    public ToDoItemsAPIInterface mResponseService;
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
        fab.setOnClickListener(view -> showNoticeDialog());

        mToDoItemsList = new ArrayList<>();

        mResponseService = ServiceGenerator.getClient().create(ToDoItemsAPIInterface.class);

        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this::updateAllToDoItems);

        mRecyclerView = findViewById(R.id.toDoItemsRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.Adapter mAdapter = new RecyclerViewAdapter(this, mToDoItemsList);
        mRecyclerView.setAdapter(mAdapter);

        updateAllToDoItems();
    }

    public void showNoticeDialog() {
        DialogFragment dialog = CreateItemDialog.newInstance(CreateItemDialog.EditMode.CREATE, "", 0);
        dialog.show(getFragmentManager(), "add_item");
    }

    public void showEditDialog(String itemText, int itemId) {
        DialogFragment dialog = CreateItemDialog.newInstance(CreateItemDialog.EditMode.EDIT, itemText, itemId);
        dialog.show(getFragmentManager(), "edit_item");
    }

    private void updateAllToDoItems() {
        Call<List<ToDoItem>> toDoCall = mResponseService.getAllToDoItems();
        toDoCall.enqueue(new Callback<List<ToDoItem>>() {
            @Override
            public void onResponse(Call<List<ToDoItem>> call, Response<List<ToDoItem>> response) {
                if (response.isSuccessful()) {
                    mSwipeRefreshLayout.setRefreshing(false);

                    mToDoItemsList.clear();
                    mToDoItemsList.addAll(response.body());

                    Toast.makeText(ListActivity.this, "Successful.", Toast.LENGTH_SHORT).show();

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

    @Override
    public void onCreateItem(String itemText) {
        Call<ToDoItem> toDoCall = mResponseService.postNewToDoItem(itemText);
        toDoCall.enqueue(new Callback<ToDoItem>() {
            @Override
            public void onResponse(Call<ToDoItem> call, Response<ToDoItem> response) {
                if (response.isSuccessful()) {
                    mToDoItemsList.add(response.body());
                    mRecyclerView.getAdapter().notifyItemInserted(mToDoItemsList.size());
                }
            }

            @Override
            public void onFailure(Call<ToDoItem> call, Throwable t) {
                Toast.makeText(ListActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });
    }

    @Override
    public void onEditItem(int elementId, String itemText) {
        Call<ToDoItem> toDoCall = mResponseService.updateToDoItem(elementId,
                new ToDoItem(itemText, mToDoItemsList.get(elementId).isComplete()));
        toDoCall.enqueue(new Callback<ToDoItem>() {
            @Override
            public void onResponse(Call<ToDoItem> call, Response<ToDoItem> response) {
                if (response.isSuccessful()) {
                    mToDoItemsList.set(elementId, response.body());
                    mRecyclerView.getAdapter().notifyItemChanged(elementId);
                }
            }

            @Override
            public void onFailure(Call<ToDoItem> call, Throwable t) {
                Toast.makeText(ListActivity.this, t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
