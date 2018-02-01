package com.client.todolist.anton.todolist_android_client.api.models;

import com.google.gson.annotations.SerializedName;

public class ToDoItem {

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

    public void setText(String text) {
        this.text = text;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }
}
