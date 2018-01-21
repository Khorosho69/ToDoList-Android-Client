package com.client.todolist.anton.todolist_android_client;

import android.widget.CompoundButton;

public interface RecyclerViewClickListener {
    void onCheckedChanged(CompoundButton compoundButton, boolean b, int position);
}
