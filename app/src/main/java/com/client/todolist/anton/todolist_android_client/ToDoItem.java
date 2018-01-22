package com.client.todolist.anton.todolist_android_client;

import com.google.gson.annotations.SerializedName;

public class ToDoItem {
    @SerializedName("text")
    private String text;
    @SerializedName("complete")
    private boolean isComplete;

    public ToDoItem(String text, boolean isComplete) {
        this.text = text;
        this.isComplete = isComplete;
    }

    public String getText() {
        return text;
    }

    public boolean isComplete() {
        return isComplete;
    }
}
