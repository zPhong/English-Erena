package com.example.a15520.newdictionary_ver2;


/**
 * Created by LÊHOÀNGNAM on 31-12-17.
 */

public interface MyNearbyEndpointListener {
    void onEndpointDiscovered( Endpoint endpoint);
    void onEndpointConnected( Endpoint endpoint);
    void onEndpointDisconnected( Endpoint endpoint);
}
