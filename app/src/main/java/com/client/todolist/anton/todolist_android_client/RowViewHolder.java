package com.client.todolist.anton.todolist_android_client;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class RowViewHolder extends RecyclerView.ViewHolder implements CheckBox.OnCheckedChangeListener {
    public CheckBox mCheckBox;
    private RecyclerViewClickListener mListener;

    RowViewHolder(View v, RecyclerViewClickListener listener) {
        super(v);
        mCheckBox = (CheckBox) v.findViewById(R.id.todo_item_box);
        mListener = listener;
        mCheckBox.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        mListener.onCheckedChanged(compoundButton, b, getAdapterPosition());
    }
}
