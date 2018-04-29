package com.example.a15520.newdictionary_ver2;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.Connections;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.nearby.connection.Strategy;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class MyConnectionService extends Service
        implements
        MyGoogleApiClientListener, MyNearbyAdvertisingListener, MyNearbyDiscoveringListener,
        MyNearbyDataListener, MyNearbyEndpointListener, MyNearbyConnectionListener,
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    ///////////////////////////
    //Singleton Design Patter//
    ///////////////////////////


    public static final int REQUEST_CODE_REQUIRED_PERMISSIONS = 1;

    private static final String[] REQUIRED_PERMISSIONS =
            new String[]{
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.CHANGE_WIFI_STATE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
            };


    private static MyConnectionService _instance = new MyConnectionService();
    private GoogleApiClient _googleApiClient;

    private static final Strategy STRATEGY = Strategy.P2P_STAR;
    private static String _clientName = BluetoothAdapter.getDefaultAdapter().getName();
    private static String SERVICE_ID = "EnglishAttackNearbyConnection";


    private final Map<String, Endpoint> _discoveredEndpoints = new HashMap<>();
    private final Map<String, Endpoint> _pendingConnections = new HashMap<>();
    private final Map<String, Endpoint> _establishedConnections = new HashMap<>();

    private boolean _isConnecting = false;
    private boolean _isDiscovering = false;
    private boolean _isAdvertising = false;

    private Endpoint _currentConnectedEndpoint;

    public MyConnectionService() {
    }


    //////////////////////////////////////////////////////
    //implement all interface for nearby connection///////
    //////////////////////////////////////////////////////

    private MyGoogleApiClientListener _googleApiClientListener;
    private MyNearbyAdvertisingListener _nearbyAdvertisingListener;
    private MyNearbyDiscoveringListener _nearbyDiscoveringListener;
    private MyNearbyDataListener _nearbyDataListener;
    private MyNearbyEndpointListener _nearbyEndpointListener;
    private MyNearbyConnectionListener _nearbyConnectionListener;

    ///////////////////////////////////////
    //getter for all interface instance////
    ///////////////////////////////////////

    public static MyConnectionService getInstance() {
        return _instance;
    }

    protected GoogleApiClient getGoogleApiClient() {
        return _googleApiClient;
    }

    protected String getClietName() {
        return _clientName;
    }

    protected String getServiceId() {
        return SERVICE_ID;
    }

    protected String[] getRequiredPermissions() {
        return REQUIRED_PERMISSIONS;
    }


    protected boolean isAdvertising() {
        return _isAdvertising;
    }

    protected boolean isDiscovering() {
        return _isDiscovering;
    }

    protected boolean isConnecting() {
        return _isConnecting;
    }


    protected Endpoint getCurrentConnectedEndpoint() {
        return _currentConnectedEndpoint;
    }
    ///////////////////////////////////////
    //setter for all interface instance////
    ///////////////////////////////////////

    public void setContext(Context context) {
        _context = context;
    }

    public void setGoogleApiClient(GoogleApiClient googleApiClient) {
        _googleApiClient = googleApiClient;
    }

    public void setCurrentConnectedEndpoint(Endpoint newEnpointConnected) {
        _currentConnectedEndpoint = newEnpointConnected;
    }


    public void setClientName(String clientName) {
        _clientName = clientName;
    }

    public void setGoogleApiClientListener(MyGoogleApiClientListener _googleApiClientListener) {
        this._googleApiClientListener = _googleApiClientListener;

    }

    public void setNearbyAdvertisingListener(MyNearbyAdvertisingListener _nearbyAdvertisingListener) {
        this._nearbyAdvertisingListener = _nearbyAdvertisingListener;
    }

    public void setNearbyDiscoveringListener(MyNearbyDiscoveringListener _nearbyDiscoveringListener) {
        this._nearbyDiscoveringListener = _nearbyDiscoveringListener;
    }

    public void setNearbyDataListener(MyNearbyDataListener _nearbyDataListener) {
        this._nearbyDataListener = _nearbyDataListener;
    }

    public void setNearbyEndpointListener(MyNearbyEndpointListener _nearbyEndpointListener) {
        this._nearbyEndpointListener = _nearbyEndpointListener;
    }

    public void setNearbyConnectionListener(MyNearbyConnectionListener _nearbyConnectionListener) {
        this._nearbyConnectionListener = _nearbyConnectionListener;
    }


    //////////////////////////////////////////////////////////
    /// NearbyConnection ovveride similar method
    //////////////////////////////////////////////////////////

    ////////////////////////For GoogleApiClientListener////////////////
    @Override
    public void onConnected() {
        _googleApiClientListener.onConnected();
    }

    @Override
    public void onConnectionFailed() {
        _googleApiClientListener.onConnectionFailed();
    }


    @CallSuper
    @Override
    public void onConnectionSuspended(int reason) {
        resetState();
       // Toast.makeText(_context, "On Connection Suspended : "+String.valueOf(reason), Toast.LENGTH_SHORT).show();
        _googleApiClientListener.onConnectionSuspended(reason);
    }

    /////////////////For NearbyAdvertsisingListener////////////////
    @Override
    public void onAdvertisingStarted() {
        _nearbyAdvertisingListener.onAdvertisingStarted();
    }

    @Override
    public void onAdvertisingFailed() {
        _nearbyAdvertisingListener.onAdvertisingFailed();
    }

    ///////////////For Nearby Connection Listener////////////////
    @Override
    public void onConnectionInitiated(Endpoint endpoint, ConnectionInfo connectionInfo) {
        _nearbyConnectionListener.onConnectionInitiated(endpoint, connectionInfo);
    }


    @Override
    public void onConnectionFailed(Endpoint endpoint) {
        _nearbyConnectionListener.onConnectionFailed(endpoint);
    }


    ///////////////For NearbyData Listener////////////////
    @Override
    public void onReceive(Endpoint endpoint, Payload payload) {
        _nearbyDataListener.onReceive(endpoint, payload);
    }

    ////////////////For Nearby Discovering Listener////////////////
    @Override
    public void onDiscoveryStarted() {
        _nearbyDiscoveringListener.onDiscoveryStarted();
    }

    @Override
    public void onDiscoveryFailed() {
        _nearbyDiscoveringListener.onDiscoveryFailed();
    }

    /////////////////// For Nearby Endpoint Listener////////////////
    @Override
    public void onEndpointDiscovered(Endpoint endpoint) {
        _nearbyEndpointListener.onEndpointDiscovered(endpoint);
    }

    @Override
    public void onEndpointConnected(Endpoint endpoint) {
        _nearbyEndpointListener.onEndpointConnected(endpoint);
    }

    @Override
    public void onEndpointDisconnected(Endpoint endpoint) {
        _nearbyEndpointListener.onEndpointDisconnected(endpoint);
    }

    /////////////////////////////////Dont know things
    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    ///////////////////////

    private static Context _context;

    public void createGoogleApiClient() {
        _context = this;
        if (_googleApiClient == null) {
            _googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Nearby.CONNECTIONS_API)
                    .addConnectionCallbacks(this)
                    .build();
        }


    }


    public void resetState() {
        _discoveredEndpoints.clear();
        _pendingConnections.clear();
        _establishedConnections.clear();
        _isConnecting = false;
        _isDiscovering = false;
        _isAdvertising = false;
        _currentConnectedEndpoint = null;
    }


    protected void acceptConnection(final Endpoint endpoint) {
        Nearby.Connections.acceptConnection(_googleApiClient, endpoint.getId(), _payloadCallback)
                .setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(@NonNull Status status) {
                                if (!status.isSuccess()) {
                                }
                            }
                        });
    }

    protected void rejectConnection(Endpoint endpoint) {
        Nearby.Connections.rejectConnection(_googleApiClient, endpoint.getId())
                .setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(@NonNull Status status) {
                                if (!status.isSuccess()) {
                                    //Log(String.format("rejectConnection failed. %s", MyConnectionService.toString(status)));
                                }
                            }
                        });
    }


    protected void startAdvertising() {
        _isAdvertising = true;
        //if (!_googleApiClient.isConnected())
        _googleApiClient.connect();

        Nearby.Connections.startAdvertising(
                _googleApiClient,
                _clientName,
                SERVICE_ID,
                _connectionLifecycleCallback,
                new AdvertisingOptions(STRATEGY))
                .setResultCallback(
                        new ResultCallback<Connections.StartAdvertisingResult>() {
                            @Override
                            public void onResult(@NonNull Connections.StartAdvertisingResult startAdvertisingResult) {
                                if (startAdvertisingResult.getStatus().isSuccess())
                                    MyConnectionService.this.onAdvertisingStarted();
                                else {
                                    MyConnectionService.this.onAdvertisingFailed();
                                }
                            }
                        }
                );
    }

    protected void stopAdvertising() {
        _isAdvertising = false;
        Nearby.Connections.stopAdvertising(_googleApiClient);

    }


    protected void startDiscovering() {
        _isDiscovering = true;
        if (!_googleApiClient.isConnected()) {
            _googleApiClient.connect();
        }
        _discoveredEndpoints.clear();


        Nearby.Connections.startDiscovery(
                _googleApiClient,
                SERVICE_ID,
                new EndpointDiscoveryCallback() {
                    @Override
                    public void onEndpointFound(String endpointId, DiscoveredEndpointInfo info) {
                        //log(String.format("onEndpointFound(endpointId=%s, serviceId=%s, endpointName=%s)", endpointId, info.getServiceId(), info.getEndpointName()));
                        //Toast.makeText(_context, String.format("onEndpointFound endpointId=%s, serviceId=%s, endpointName=%s)", endpointId, info.getServiceId(), info.getEndpointName()), Toast.LENGTH_SHORT).show();
                        if (SERVICE_ID.equals(info.getServiceId())) {
                            Endpoint endpoint = new Endpoint(endpointId, info.getEndpointName());
                            _discoveredEndpoints.put(endpointId, endpoint);
                            MyConnectionService.this.onEndpointDiscovered(endpoint);
                        }
                    }

                    @Override
                    public void onEndpointLost(String endpointId) {
                        //  log(String.format("onEndpointLost(endpointId=%s)", endpointId));
                        //Toast.makeText(_context, String.format("Lose Disconnect wit ", endpointId), Toast.LENGTH_SHORT).show();
                        Endpoint newEndpoint = null;

                        for (Endpoint endpoint : _discoveredEndpoints.values()) {
                            if (endpoint.getId().equals(endpointId)) {
                                newEndpoint = endpoint;
                            }
                        }
                        if (newEndpoint != null)
                            MyConnectionService.this.onEndpointDisconnected(newEndpoint);
                    }
                },
                new DiscoveryOptions(STRATEGY))
                .setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(@NonNull Status status) {
                                if (status.isSuccess()) {
                                    MyConnectionService.this.onDiscoveryStarted();
                                } else {
                                    _isDiscovering = false;
                                    //          log(String.format("Discovering failed. Received status %s.", MyConnectionService.this.toString(status)));
                                    Toast.makeText(_context, String.format("Discovering failed. Because %s.", MyConnectionService.this.toString(status)), Toast.LENGTH_SHORT).show();
                                    MyConnectionService.this.onDiscoveryFailed();
                                }
                            }
                        });
    }

    /**
     * Stops discovery.
     */
    protected void stopDiscovering() {
        _isDiscovering = false;
        Nearby.Connections.stopDiscovery(_googleApiClient);
    }


    protected void disconnect(Endpoint endpoint) {
        Nearby.Connections.disconnectFromEndpoint(_googleApiClient, endpoint.getId());
        _establishedConnections.remove(endpoint.getId());
    }

    protected void disconnectFromAllEndpoints() {
        for (Endpoint endpoint : _establishedConnections.values()) {
            Nearby.Connections.disconnectFromEndpoint(_googleApiClient, endpoint.getId());
        }
        _establishedConnections.clear();
    }

    protected void connectToEndpoint(final Endpoint endpoint) {
        // If we already sent out a connection request, wait for it to return
        // before we do anything else. P2P_STAR only allows 1 outgoing connection.
        if (_isConnecting) {
            //log("Already connecting, so ignoring this endpoint: " + endpoint.toString());
            //Toast.makeText(_context, "Already connecting, so ignoring this endpoint: " + endpoint.toString(), Toast.LENGTH_SHORT).show();
            return;
        }

        //log("Sending a connection request to endpoint " + endpoint.toString());
        Toast.makeText(_context, "Sending a connection request to endpoint " + endpoint.toString(), Toast.LENGTH_SHORT).show();
        // Mark ourselves as connecting so we don't connect multiple times
        _isConnecting = true;

        // Ask to connect

        Nearby.Connections.requestConnection(
                _googleApiClient,
                _clientName,
                endpoint.getId(),
                _connectionLifecycleCallback)
                .setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(@NonNull Status status) {
                                if (!status.isSuccess()) {
                                    // log(String.format("requestConnection failed. %s", MyConnectionService.toString(status)));
                                    _isConnecting = false;
                                    onConnectionFailed(endpoint);
                                }
                            }
                        });
    }


    private void connectedToEndpoint(Endpoint endpoint) {
        //log(String.format("connectedToEndpoint(endpoint=%s)", endpoint));
        Toast.makeText(_context, String.format("Alrealdy connect with %s", endpoint.getName()), Toast.LENGTH_SHORT).show();
        _establishedConnections.put(endpoint.getId(), endpoint);
        MyConnectionService.this.onEndpointConnected(endpoint);

    }

    private void disconnectedFromEndpoint(Endpoint endpoint) {
        //  log(String.format("disconnectedFromEndpoint(endpoint=%s)", endpoint));
        Toast.makeText(_context, String.format("Disconnected from %s", endpoint.getName()), Toast.LENGTH_SHORT);
        _establishedConnections.remove(endpoint.getId());
        MyConnectionService.this.onEndpointDisconnected(endpoint);
    }

    private final ConnectionLifecycleCallback _connectionLifecycleCallback = new ConnectionLifecycleCallback() {
        @Override
        public void onConnectionInitiated(final String endpointId, ConnectionInfo connectionInfo) {
            //  log(String.format("onConnectionInitiated(endpointId=%s, endpointName=%s)", endpointId, connectionInfo.getEndpointName()));
            //Toast.makeText(_context, String.format("onConnectionInitiated(endpointId=%s, endpointName=%s)", endpointId, connectionInfo.getEndpointName()), Toast.LENGTH_SHORT).show();
            Endpoint endpoint = new Endpoint(endpointId, connectionInfo.getEndpointName());
            _pendingConnections.put(endpointId, endpoint);

            MyConnectionService.this.onConnectionInitiated(new Endpoint(endpointId, connectionInfo.getEndpointName()), connectionInfo);

        }

        @Override
        public void onConnectionResult(String endpointId, ConnectionResolution result) {
            //   log(String.format("onConnectionResponse(endpointId=%s, result=%s)", endpointId, result));
            Toast.makeText(_context, String.format("onConnectionResponse(endpointId=%s, result=%s)", endpointId, result), Toast.LENGTH_SHORT).show();
            _isConnecting = false;
            switch (result.getStatus().getStatusCode()) {
                case ConnectionsStatusCodes.STATUS_OK:
                    //Đã kết nối được, bắt đầu có thể gửi nhận
                    Toast.makeText(_context, "Connect to other player succeed", Toast.LENGTH_SHORT).show();
                    MyConnectionService.this.connectedToEndpoint(_pendingConnections.remove(endpointId));
                    break;
                case ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED:
                    // Khi kết nối bị từ chối từ một phía
                    Toast.makeText(_context, String.format("Connection failed. Received status %s.", MyConnectionService.this.toString(result.getStatus())), Toast.LENGTH_SHORT).show();
                    onConnectionFailed(_pendingConnections.remove(endpointId));
                    break;
                //     log(String.format("Connection failed. Received status %s.", MyConnectionService.this.toString(result.getStatus())));

            }


        }

        @Override
        public void onDisconnected(String endpointId) {
            if (!_establishedConnections.containsKey(endpointId)) {

                //  log("Unexpected disconnection from endpoint " + endpointId);
                Toast.makeText(_context, "Unexpected disconnection from endpoint " + endpointId, Toast.LENGTH_LONG).show();
                return;
            }
            _currentConnectedEndpoint = null;
            disconnectedFromEndpoint(_establishedConnections.get(endpointId));
        }
    };

    ////////////////////////////////////////////////////////////////////////////
    //* Callbacks for payloads (bytes of data) sent from another device to us.
    ////////////////////////////////////////////////////////////////////////////
    private final PayloadCallback _payloadCallback =
            new PayloadCallback() {
                @Override
                public void onPayloadReceived(String endpointId, Payload payload) {
                    //  log(String.format("onPayloadReceived(endpointId=%s, payload=%s)", endpointId, payload));
                    onReceive(_establishedConnections.get(endpointId), payload);
                }

                @Override
                public void onPayloadTransferUpdate(String endpointId, PayloadTransferUpdate update) {
                    //  log(String.format("onPayloadTransferUpdate(endpointId=%s, update=%s)", endpointId, update));
                }
            };


    public static boolean hasPermissions(Context context, String... permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


    private static String toString(Status status) {
        return String.format(
                Locale.US,
                " (d) %s",
                status.getStatusCode(),
                status.getStatusMessage() != null
                        ? status.getStatusMessage()
                        : ConnectionsStatusCodes.getStatusCodeString(status.getStatusCode()));
    }

    protected void sendMessage(String strMsg, Endpoint endpoint) {
        Payload _msgPayload = Payload.fromBytes(strMsg.getBytes());
        Nearby.Connections.sendPayload(_googleApiClient, endpoint.getId(), _msgPayload);
    }

    public void connectGoogleApiClient() {
        _googleApiClient.connect();
    }


    //////////////////////////////////////////////
    ///////Service Code////////////////////////////
    //////////////////////////////////////////////

    private int NOTIFICATION = 1; // Unique identifier for our notification

    public static boolean isRunning = false;


    private NotificationManager notificationManager = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        _instance = this;
        isRunning = true;
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        super.onCreate();
        createGoogleApiClient();
        _googleApiClient.connect();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);

        // Set the info for the views that show in the notification panel.
        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logoapp)        // the status icon
                .setTicker("Click here to come back English Erena")           // the status text
                .setWhen(System.currentTimeMillis())       // the time stamp
                .setContentTitle("Erena")                 // the label of the entry
                .setContentText("Fast translate...")      // the content of the entry
                .setContentIntent(contentIntent)           // the intent to send when the entry is clicked
                .setOngoing(true)                          // make persistent (disable swipe-away)
                .build();

        // Start service in foreground mode
        startForeground(NOTIFICATION, notification);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        isRunning = false;
        _instance = null;
        _googleApiClient.disconnect();
        notificationManager.cancel(NOTIFICATION); // Remove notification
        super.onDestroy();
    }
}