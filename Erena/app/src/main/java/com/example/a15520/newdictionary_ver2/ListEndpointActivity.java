package com.example.a15520.newdictionary_ver2;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.Payload;

import java.util.ArrayList;

public class ListEndpointActivity extends AppCompatActivity
        implements MyNearbyDiscoveringListener, MyNearbyEndpointListener, MyNearbyAdvertisingListener
        , MyNearbyConnectionListener, MyGoogleApiClientListener, MyNearbyDataListener {

    private ListView listviewEndpoint;
    ArrayList<Endpoint> listEndpoints;
    EndpointAdapter endpointAdapter;
    Switch onOffSwitch;


    private MyNearbyAdvertisingListener nearbyAdvertise;
    private MyNearbyDiscoveringListener nearbyDiscover;
    private MyNearbyEndpointListener nearbyEndpoint;
    private MyNearbyConnectionListener nearbyListener;
    private MyNearbyDataListener nearbyData;
    private MyGoogleApiClientListener nearbyClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_endpoint);
        listviewEndpoint = (ListView) findViewById(R.id.lvdEndpoints);
        listEndpoints = new ArrayList<>();

        endpointAdapter = new EndpointAdapter(listEndpoints, this);
        listviewEndpoint.setAdapter(endpointAdapter);

        onOffSwitch = (Switch) findViewById(R.id.switchNearby);
        onOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    MyConnectionService.getInstance().setNearbyEndpointListener(nearbyEndpoint);
                    MyConnectionService.getInstance().setNearbyAdvertisingListener(nearbyAdvertise);
                    MyConnectionService.getInstance().setNearbyConnectionListener(nearbyListener);
                    MyConnectionService.getInstance().setNearbyDiscoveringListener(nearbyDiscover);
                    MyConnectionService.getInstance().setNearbyDataListener(nearbyData);
                    MyConnectionService.getInstance().setGoogleApiClientListener(nearbyClient);
                    MyConnectionService.getInstance().startAdvertising();
                    MyConnectionService.getInstance().startDiscovering();
                } else {
                    endpointAdapter.Clear();
                    listviewEndpoint.setAdapter(endpointAdapter);
                    MyConnectionService.getInstance().stopAdvertising();
                    MyConnectionService.getInstance().stopDiscovering();
                    MyConnectionService.getInstance().setCurrentConnectedEndpoint(null);
                }
            }

        });

        listviewEndpoint.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyConnectionService.getInstance().setCurrentConnectedEndpoint(null);
                MyConnectionService.getInstance().connectToEndpoint(endpointAdapter.getEndpointbyPosition(position));
            }
        });

    }

    public void checkPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                Toast.makeText(this, "We need to use your location to play this mode", Toast.LENGTH_SHORT).show();
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    this.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
            }
        } else {
            Toast.makeText(this, "Location permissions already granted", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        MyConnectionService.getInstance().setCurrentConnectedEndpoint(null);

        nearbyAdvertise = this;
        nearbyDiscover = this;
        nearbyEndpoint = this;
        nearbyListener = this;
        nearbyData = this;
        nearbyClient = this;


        checkPermission();
        onOffSwitch.setChecked(false);
    }

    @Override
    public void onEndpointDiscovered(Endpoint endpoint) {
        endpointAdapter.Add(endpoint);
        listviewEndpoint.setAdapter(endpointAdapter);
    }

    @Override
    public void onEndpointConnected(Endpoint endpoint) {
        Intent t = new Intent(ListEndpointActivity.this, Game1_2P.class);
        startActivity(t);
        finish();
    }

    @Override
    public void onEndpointDisconnected(Endpoint endpoint) {
        endpointAdapter.removeEndpoint(endpoint);
        listviewEndpoint.setAdapter(endpointAdapter);
    }

    @Override
    public void onAdvertisingStarted() {
    }

    @Override
    public void onAdvertisingFailed() {
    }

    @Override
    public void onDiscoveryStarted() {
    }

    @Override
    public void onDiscoveryFailed() {
    }

    @Override
    public void onConnectionInitiated(final Endpoint endpoint, ConnectionInfo connectionInfo) {
        if (MyConnectionService.getInstance().getCurrentConnectedEndpoint() != null) {
            Toast.makeText(this, connectionInfo.getEndpointName() + "had just invited you to play", Toast.LENGTH_SHORT).show();
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ListEndpointActivity.this);
        alertDialogBuilder.setTitle("Start a match with  \n" + GlobalVariable.getInstance().getUser().getDisplayName() + "\n" + connectionInfo.getEndpointName())
                .setMessage("Accept code :  " + connectionInfo.getAuthenticationToken() + " to start a match")
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Nếu ấn cái nút này thì đồng ý

                        MyConnectionService.getInstance().setCurrentConnectedEndpoint(endpoint);
                        MyConnectionService.getInstance().acceptConnection(endpoint);
                        //MyConnectionService.getInstance().stopAdvertising();
                    }
                })
                .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        MyConnectionService.getInstance().rejectConnection(endpoint);
                    }
                }).show();
    }

    @Override
    public void onConnectionFailed(Endpoint endpoint) {

    }

    @Override
    public void onConnected() {

    }

    @Override
    public void onConnectionFailed() {

    }

    @Override
    public void onConnectionSuspended(int reason) {

    }

    @Override
    public void onReceive(Endpoint endpoint, Payload payload) {

    }
}
