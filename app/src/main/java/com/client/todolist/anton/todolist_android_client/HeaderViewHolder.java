package com.client.todolist.anton.todolist_android_client;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

public class HeaderViewHolder extends RecyclerView.ViewHolder {
    Button mButton;

    public HeaderViewHolder(View itemView) {
        super(itemView);
        mButton = (Button) itemView.findViewById(R.id.add_item_button);
    }
}
