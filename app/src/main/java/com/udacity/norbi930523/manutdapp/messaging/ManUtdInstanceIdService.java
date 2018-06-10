package com.udacity.norbi930523.manutdapp.messaging;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.udacity.norbi930523.manutdapp.util.ApiUtils;

import java.io.IOException;

import timber.log.Timber;

public class ManUtdInstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        try {
            ApiUtils.getManutdApiService().registerToken(refreshedToken).execute();
        } catch (IOException e) {
            Timber.e(e, "Failed to register push notification token");
        }
    }
}
