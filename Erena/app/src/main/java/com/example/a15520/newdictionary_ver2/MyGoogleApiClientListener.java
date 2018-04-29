package com.example.a15520.newdictionary_ver2;


/**
 * Created by LÊHOÀNGNAM on 31-12-17.
 */

public interface MyGoogleApiClientListener {
    void onConnected();
    void onConnectionFailed();
    void onConnectionSuspended(int reason);
}
