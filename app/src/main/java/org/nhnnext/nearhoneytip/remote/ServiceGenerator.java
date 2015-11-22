package org.nhnnext.nearhoneytip.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.nhnnext.nearhoneytip.BuildConfig;
import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by eunjooim on 2015. 11. 10.
 */
public class ServiceGenerator {
    public static <S> S createService(Class<S> serviceClass, String baseUrl) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
                .create();

        RestAdapter.LogLevel logLevel;
        if (BuildConfig.DEBUG) {
            logLevel = RestAdapter.LogLevel.FULL;
        } else {
            logLevel = RestAdapter.LogLevel.NONE;
        }

        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setLogLevel(logLevel)
                .setEndpoint(baseUrl)
                .setConverter(new LenientGsonConverter(gson))
                .setClient(new OkClient(new OkHttpClient()));

        RestAdapter adapter = builder.build();

        return adapter.create(serviceClass);
    }
}
