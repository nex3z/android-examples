package com.nex3z.examples.tasteofreactive.rest.service;

import com.nex3z.examples.tasteofreactive.rest.model.User;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface UserService {

    @GET("/users")
    Observable<List<User>> getUsers();


    @GET("/users")
    Observable<List<User>> getUsers(@Query("since") long since);

}
