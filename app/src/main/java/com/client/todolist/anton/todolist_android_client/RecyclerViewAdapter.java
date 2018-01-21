package com.client.todolist.anton.todolist_android_client;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ToDoItem> mDataset;

    private RecyclerViewClickListener mListener;
    private View.OnClickListener mCreateNewItemListener;

    public RecyclerViewAdapter(List<ToDoItem> myDataset, RecyclerViewClickListener listener, View.OnClickListener createNewItemListener) {
        mDataset = myDataset;
        mListener = listener;
        mCreateNewItemListener = createNewItemListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;

        if(viewType == R.layout.todo_item_layout){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item_layout, parent, false);
            return new RowViewHolder(v, mListener);
        }
        else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_todo_item_layout, parent, false);
            return new HeaderViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RowViewHolder) {
            RowViewHolder rowHolder = (RowViewHolder) holder;

            ToDoItem toDoItem = mDataset.get(position);
            rowHolder.mCheckBox.setText(toDoItem.getText());
            rowHolder.mCheckBox.setChecked(toDoItem.isComplete());
        }
        else {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;

            if(position == mDataset.size()) {
                headerHolder.mButton.setOnClickListener(mCreateNewItemListener);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == mDataset.size()){
            return R.layout.add_todo_item_layout;
        }else {
            return R.layout.todo_item_layout;
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size() + 1;
    }
}
