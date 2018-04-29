package com.example.a15520.newdictionary_ver2;


import com.google.android.gms.nearby.connection.Payload;

/**
 * Created by LÊHOÀNGNAM on 31-12-17.
 */

public interface MyNearbyDataListener {
    void onReceive( Endpoint endpoint, Payload payload);
}
