package org.nhnnext.nearhoneytip.remote;

import org.nhnnext.nearhoneytip.item.NearTipItem;
import org.nhnnext.nearhoneytip.item.ResponseResult;
import org.nhnnext.nearhoneytip.item.TipItem;
import org.nhnnext.nearhoneytip.item.User;

import java.io.File;
import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;

/**
 * Created by eunjooim on 2015. 11. 10.
 */
public interface RemoteService {
    String BASE_URL = "http://54.64.250.239:3000";

    //tips
    @GET("/tip/all")
    void getAllTips(Callback<List<TipItem>> callback);

    @GET("/tip/_id={id}")
    TipItem getTip(Callback<TipItem> callback,
                   @Path("id") String id);

    @GET("/tip/lat={latPath}&long={longPath}&sid={uid}")
    void getNearTips(@Path("latPath") double latPath,
                     @Path("longPath") double longPath,
                     @Path("uid") String uid,
                     Callback<List<NearTipItem>> callback);

    @Multipart
    @POST("/tip")
    void postTip(@Part("storename") String storename,
                 @Part("tipdetail") String tipdetail,
                 @Part("uid") String uid,
                 @Part("longitude") String longitude,
                 @Part("latitude") String latitude,
                 @Part("upload") TypedFile file,
                 Callback<TipItem> callback);


    @GET("/users/_id={id}")
    void getUser(@Path("id") String id,
                 Callback<User> callback);

    @Headers("Content-Type: application/json")
    @POST("/users")
    void postUser(@Body TypedString jsonBody,
                  Callback<User> callback);

    @Headers("Content-Type: application/json")
    @POST("/like/_id={id}")
    void putLike(@Path("id") String idPath,
                 @Body TypedString jsonBody,
                 Callback<ResponseResult> callback);

    @Headers("Content-Type: application/json")
    @PUT("/like/_id={id}")
    void deleteLike(@Path("id") String idPath,
                    @Body TypedString jsonBody,
                    Callback<ResponseResult> callback);


}
