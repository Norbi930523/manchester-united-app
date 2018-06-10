package com.udacity.norbi930523.manutdapp.util;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.udacity.norbi930523.manutdapp.BuildConfig;
import com.udacity.norbi930523.manutdapp.backend.manutd.Manutd;

import java.io.IOException;

public class ApiUtils {

    private static final Manutd MANUTD_API_SERVICE;

    static {
        Manutd.Builder builder = new Manutd.Builder(
                AndroidHttp.newCompatibleTransport(),
                new AndroidJsonFactory(),
                new HttpRequestInitializer() {
                    @Override
                    public void initialize(HttpRequest request) throws IOException {
                        request.setConnectTimeout(5000);
                    }
                });

        builder.setApplicationName("Manchester United App");

        // options for running against local devappserver
        // - 10.0.2.2 is localhost's IP address in Android emulator
        // - turn off compression when running against local devappserver
        builder.setRootUrl(BuildConfig.API_ENDPOINT)
                .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                    @Override
                    public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                        abstractGoogleClientRequest.setDisableGZipContent(BuildConfig.DEBUG);
                    }
                });
        // end options for devappserver

        MANUTD_API_SERVICE = builder.build();
    }

    private ApiUtils(){ }

    public static Manutd getManutdApiService(){
        return MANUTD_API_SERVICE;
    }

}
