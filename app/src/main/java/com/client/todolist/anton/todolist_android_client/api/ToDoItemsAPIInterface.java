package com.client.todolist.anton.todolist_android_client.api;

import com.client.todolist.anton.todolist_android_client.api.models.ToDoItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ToDoItemsAPIInterface {
    @GET("/toDoItems")
    Call<List<ToDoItem>> getAllToDoItems();

    @FormUrlEncoded
    @POST("/toDoItems?")
    Call<ToDoItem> postNewToDoItem(@Field("itemText") String itemText);

    @PUT("/toDoItems/{id}")
    Call<ToDoItem> updateToDoItem(@Path("id") int id, @Body ToDoItem body);

}
