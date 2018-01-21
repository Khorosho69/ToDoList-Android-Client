package com.client.todolist.anton.todolist_android_client;

import com.google.gson.annotations.SerializedName;

public class ToDoItem {
    @SerializedName("id")
    private long id;
    @SerializedName("text")
    private String text;
    @SerializedName("complete")
    private boolean isComplete;

    public ToDoItem(long id, String text, boolean isComplete) {
        this.id = id;
        this.text = text;
        this.isComplete = isComplete;
    }

    public long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public boolean isComplete() {
        return isComplete;
    }
}
