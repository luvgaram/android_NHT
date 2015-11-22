package org.nhnnext.nearhoneytip.remote;

import org.nhnnext.nearhoneytip.item.TipItem;

import java.io.File;
import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by eunjooim on 2015. 11. 10.
 */
public interface RemoteService {
    String BASE_URL = "http://54.64.250.239:3000";

    //profile
    @GET("/tip/all")
    void getAllTips(Callback<List<TipItem>> callback);

    @GET("/tip/_id={_id}")
    TipItem getTip(Callback<TipItem> callback, @Path("_id") String _id);

    //http://54.64.250.239:3000/image/icon=icon1.png
    @GET("/image/icon={iconPath}")
    File getIcon(Callback<File> callback, @Path("iconPath") String _iconPath);
}
