package com.example.android_client;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @POST("insert_user.php")
    Call<Void> insertUser(@Body User user);

    @GET("get_users.php")
    Call<List<User>> getUsers();
}
