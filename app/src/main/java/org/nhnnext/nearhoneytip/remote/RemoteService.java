package org.nhnnext.nearhoneytip.remote;

import org.nhnnext.nearhoneytip.item.TipItem;
import org.nhnnext.nearhoneytip.item.User;
import org.nhnnext.nearhoneytip.item.UserResult;

import java.io.File;
import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;

/**
 * Created by eunjooim on 2015. 11. 10.
 */
public interface RemoteService {
    String BASE_URL = "http://54.64.250.239:3000";

    //profile
    @GET("/tip/all")
    void getAllTips(Callback<List<TipItem>> callback);

    @GET("/tip/_id={id}")
    TipItem getTip(Callback<TipItem> callback,
                   @Path("id") String id);

    //http://54.64.250.239:3000/image/icon=icon1.png
    @GET("/image/icon={iconPath}")
    File getIcon(@Path("iconPath") String iconPath,
                 Callback<File> callback);

    @Multipart
    @POST("/tip")
    void postTip(@Part("storename") String storename,
                 @Part("tipdetail") String tipdetail,
                 @Part("uid") String uid,
                 @Part("nickname") String nickname,
                 @Part("profilephoto") String profilephoto,
                 @Part("upload") TypedFile file,
                 Callback<TipItem> callback);


    @GET("/users/_id={id}")
    void getUser(@Path("id") String id,
                 Callback<List<User>> callback);

    @Headers( "Content-Type: application/json" )
    @POST("/users")
    void postUser(@Body TypedString jsonBody,
                  Callback<User> callback);
}
