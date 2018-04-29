package com.example.a15520.newdictionary_ver2;

import com.google.android.gms.nearby.connection.ConnectionInfo;

/**
 * Created by LÊHOÀNGNAM on 31-12-17.
 */

public interface MyNearbyConnectionListener {
    void onConnectionInitiated( Endpoint  endpoint, ConnectionInfo connectionInfo);
    void onConnectionFailed( Endpoint endpoint);
}
