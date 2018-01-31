package com.client.todolist.anton.todolist_android_client;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface ToDoItemsAPIInterface {
    @GET("/toDoItems")
    Call<List<ToDoItem>> getAllToDoItems();

    @FormUrlEncoded
    @POST("/toDoItems?")
    Call<ToDoItem> postNewToDoItem(@Field("itemText") String itemText);

    @FormUrlEncoded
    @PUT("/toDoItems?")
    Call<ToDoItem> changeToDoItemStatus(@Field("id") Integer id, @Field("status") Boolean status);
}
