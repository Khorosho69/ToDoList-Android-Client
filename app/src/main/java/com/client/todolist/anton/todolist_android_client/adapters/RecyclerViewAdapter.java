package com.client.todolist.anton.todolist_android_client.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.client.todolist.anton.todolist_android_client.R;
import com.client.todolist.anton.todolist_android_client.activities.ListActivity;
import com.client.todolist.anton.todolist_android_client.api.models.ToDoItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RowViewHolder> {
    private final Activity activity;
    private List<ToDoItem> mDataset;

    public RecyclerViewAdapter(Activity activity, List<ToDoItem> myDataset) {
        this.activity = activity;
        mDataset = myDataset;
    }

    @Override
    public RecyclerViewAdapter.RowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item_layout, parent, false);
        return new RowViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.RowViewHolder holder, int position) {
        ToDoItem toDoItem = mDataset.get(position);
        holder.mCheckBox.setText(toDoItem.getText());
        holder.mCheckBox.setOnCheckedChangeListener(null);
        holder.mCheckBox.setOnLongClickListener(null);
        holder.mCheckBox.setChecked(toDoItem.isComplete());
        holder.mCheckBox.setOnCheckedChangeListener((compoundButton, b) -> updateItemStatus(holder, position, b));

        holder.mCheckBox.setOnLongClickListener(v -> {
            ((ListActivity) activity).showEditDialog(holder.mCheckBox.getText().toString(), holder.getAdapterPosition());
            return true;
        });
    }

    private void updateItemStatus(RecyclerViewAdapter.RowViewHolder holder, int position, Boolean status) {
        Call<ToDoItem> toDoCall = ((ListActivity) activity).mResponseService.updateToDoItem(holder.getAdapterPosition(),
                new ToDoItem(mDataset.get(holder.getAdapterPosition()).getText(), status));
        toDoCall.enqueue(new Callback<ToDoItem>() {
            @Override
            public void onResponse(Call<ToDoItem> call, Response<ToDoItem> response) {
                if (response.isSuccessful()) {
                    mDataset.set(position, response.body());
                    notifyItemChanged(position);
                }
            }

            @Override
            public void onFailure(Call<ToDoItem> call, Throwable t) {
                Toast.makeText(activity, t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    class RowViewHolder extends RecyclerView.ViewHolder {
        CheckBox mCheckBox;

        RowViewHolder(View v) {
            super(v);
            mCheckBox = v.findViewById(R.id.todo_item_box);
        }
    }
}


