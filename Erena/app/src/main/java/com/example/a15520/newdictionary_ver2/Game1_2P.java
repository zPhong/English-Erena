package com.example.a15520.newdictionary_ver2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.Payload;

import java.util.ArrayList;

import static android.view.inputmethod.EditorInfo.IME_ACTION_DONE;
import static android.view.inputmethod.EditorInfo.IME_ACTION_NEXT;
import static android.view.inputmethod.EditorInfo.IME_ACTION_PREVIOUS;

public class Game1_2P extends AppCompatActivity implements
        MyNearbyDataListener, MyGoogleApiClientListener, MyNearbyEndpointListener, MyNearbyConnectionListener,
        MyNearbyAdvertisingListener, MyNearbyDiscoveringListener {
    private EditText _etPlayer;
    private TextView _tvMyLife, _tvHisLife;
    private String _endWord = "a";
    private ArrayList<String> _wordUsed;
    private int iMyLife, iHisLife;

    private GoogleApiClient _googleApiClient = MyConnectionService.getInstance().getGoogleApiClient();

    private ListView _lvWord;
    private Game1Adapter adapter;
    private ConstraintLayout _layoutCity_2P;
    private ConstraintLayout _layoutTree_2P;

    private MyNearbyDataListener nearbyData;
    private MyNearbyEndpointListener nearbyEndpoint;
    private MyGoogleApiClientListener googleApi;
    private MyNearbyConnectionListener nearbyListener;
    private MyNearbyAdvertisingListener nearbyAdvertise;
    private MyNearbyDiscoveringListener nearbyDiscover;


    @Override
    protected void onStart() {
        super.onStart();
        _googleApiClient.connect();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game1_2_p);


        Typeface courierBd = Typeface.createFromAsset(getAssets(), "fonts/courbd.ttf");
        _etPlayer = (EditText) findViewById(R.id.edtPlayer2P);
        _lvWord = (ListView) findViewById(R.id.lvWordGame1_2P);

        _layoutCity_2P = (ConstraintLayout) findViewById(R.id.layoutCity2P);
        _layoutTree_2P = (ConstraintLayout) findViewById(R.id.layoutTree2P);

        _tvMyLife = (TextView) findViewById(R.id.txtMyLife);
        _tvMyLife.setText("My life\n" + String.valueOf(iMyLife));
        _tvMyLife.setTypeface(courierBd);
        iMyLife = 3;

        _tvHisLife = (TextView) findViewById(R.id.txtHisLife);
        _tvHisLife.setText("Enemy Life\n" + String.valueOf(iHisLife));
        _tvHisLife.setTypeface(courierBd);
        iHisLife = 3;

        _etPlayer.setHint("Write your word here...");

        _wordUsed = new ArrayList<>();

        adapter = new Game1Adapter(_wordUsed, this);

        _lvWord.setAdapter(adapter);

        // random first word

        googleApi = this;
        nearbyData = this;
        nearbyAdvertise = this;
        nearbyDiscover = this;
        nearbyEndpoint = this;
        nearbyListener = this;

        MyConnectionService.getInstance().setNearbyDataListener(nearbyData);
        MyConnectionService.getInstance().setNearbyEndpointListener(nearbyEndpoint);
        MyConnectionService.getInstance().setGoogleApiClientListener(googleApi);
        MyConnectionService.getInstance().setNearbyConnectionListener(nearbyListener);
        MyConnectionService.getInstance().setNearbyAdvertisingListener(nearbyAdvertise);
        MyConnectionService.getInstance().setNearbyDiscoveringListener(nearbyDiscover);

        _etPlayer.addTextChangedListener(new TextWatcher() {
            int mPreviousLength;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mPreviousLength = charSequence.length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                Boolean mBackSpace = mPreviousLength > editable.length();
                if (mBackSpace == true && editable.length() == 0) {
                    _etPlayer.setText(_endWord);
                    if (!_endWord.equals(""))
                        _etPlayer.setSelection(1);
                }
            }
        });

        _etPlayer.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == IME_ACTION_DONE || i == IME_ACTION_NEXT) {

                  /* Write your logic here that will be executed when user taps next button */

                    _etPlayer.setEnabled(false);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(_etPlayer.getWindowToken(), 0);
                    doClick(_etPlayer.getText().toString(), true);

                    handled = true;
                }
                if (i == IME_ACTION_PREVIOUS) {
                    finish();
                    handled = true;
                }
                return handled;

            }
        });

    }

    private void sendMessage(String message) {

        MyConnectionService.getInstance().setNearbyEndpointListener(nearbyEndpoint);
        MyConnectionService.getInstance().setNearbyDataListener(nearbyData);
        MyConnectionService.getInstance().setGoogleApiClientListener(googleApi);
        MyConnectionService.getInstance().setNearbyConnectionListener(nearbyListener);

        if (_googleApiClient != null && _googleApiClient.isConnected()) {
            //  sendMessage();

            if (MyConnectionService.getInstance().getCurrentConnectedEndpoint() == null) {
                Toast.makeText(this, "Not Connected yet", Toast.LENGTH_SHORT).show();
                return;
            }
            if (message.equals(""))
                return;

            if (message.matches(""))
                return;
            // Toast.makeText(this, "Send message to " + MyConnectionService.getInstance().getCurrentConnectedEndpoint().toString(), Toast.LENGTH_LONG).show();
            MyConnectionService.getInstance().sendMessage(message, MyConnectionService.getInstance().getCurrentConnectedEndpoint());

        } else
            Toast.makeText(this, "Connection error", Toast.LENGTH_SHORT).show();
    }

    private void doClick(String word, boolean _isUser) {
        //  String text = _etPlayer.getText().toString().trim();
        String text = word.trim();
        if (GlobalVariable.getInstance().get_listEV().contains(text) == true
                && _wordUsed.contains(text) == false && text.length() > 1) {

            _endWord = text.substring(text.length() - 1);
            _wordUsed.add(text);
            adapter.notifyDataSetChanged();

            ////
            _lvWord.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink));

            //update previous action
            _tvMyLife.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink));
            //_score += 5 + (_wordUsed.size() / 12); // 3 word will increase bonus 1

            if (_wordUsed.size() > 8) {
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) _layoutCity_2P.getLayoutParams();
                layoutParams.topMargin += 100;
                layoutParams.bottomMargin -= 100;

                _layoutCity_2P.setLayoutParams(layoutParams);

                ConstraintLayout.LayoutParams layoutParams2 = (ConstraintLayout.LayoutParams) _layoutTree_2P.getLayoutParams();
                layoutParams.topMargin += 100;
                layoutParams.bottomMargin -= 100;
                _layoutTree_2P.setLayoutParams(layoutParams2);
            }

            _etPlayer.setText(_endWord);

            if (!_endWord.equals(""))
                _etPlayer.setSelection(1);

        } else {
            checkWin(_isUser);
            _etPlayer.setText(_endWord);
            if (!_endWord.equals(""))
                _etPlayer.setSelection(1);
        }

        if (_isUser == true) {
            if (text.equals("")) {
                checkWin(_isUser);
            }
            sendMessage(text);
            //_etPlayer.setEnabled(false);
            //  _etPlayer.setFocusable(false);
        }
//        hide keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(_etPlayer.getWindowToken(), 0);
        //_etPlayer.requestFocus();
        //imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }


    private void checkWin(boolean isMe) {
        if (isMe) {
            iMyLife -= 1;
        } else {
            iHisLife -= 1;
        }

        _tvHisLife.setText("My life\n" + String.valueOf(iHisLife));
        _tvMyLife.setText("Enemy life\n" + String.valueOf(iMyLife));
        if (iMyLife == 0 || iHisLife == 0) {

            if (iMyLife == 0) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Game1_2P.this);
                alertDialogBuilder.setTitle("You  lose !!!!")
                        .setMessage("Press OK to come back")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent t = new Intent(Game1_2P.this, ListEndpointActivity.class);
                                startActivity(t);
                            }
                        }).show();
            }
            if (iHisLife == 0) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Game1_2P.this);
                alertDialogBuilder.setTitle("You  Win !!!!")
                        .setMessage("Press OK to come back")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent t = new Intent(Game1_2P.this, ListEndpointActivity.class);
                                startActivity(t);
                            }
                        }).show();
            }

            // _etPlayer.setFocusable(false);
        }
    }

    @Override
    public void onEndpointDiscovered(Endpoint endpoint) {

    }

    @Override
    public void onEndpointConnected(Endpoint endpoint) {

    }

    @Override
    public void onEndpointDisconnected(Endpoint endpoint) {

    }

    @Override
    public void onConnected() {

    }

    @Override
    public void onConnectionFailed() {

    }

    @Override
    public void onConnectionSuspended(int reason) {

        Toast.makeText(this, "Other player has been disconnected", Toast.LENGTH_LONG).show();
        Intent t = new Intent(Game1_2P.this, ListEndpointActivity.class);
        startActivity(t);
    }

    @Override
    public void onReceive(Endpoint endpoint, Payload payload) {
        //  Toast.makeText(this, "Receive message ", Toast.LENGTH_SHORT).show();
        String strExtrasPayload = new String(payload.asBytes());
        _etPlayer.setEnabled(true);
        _etPlayer.requestFocus();
        doClick(strExtrasPayload, false);
    }

    @Override
    public void onConnectionInitiated(Endpoint endpoint, ConnectionInfo connectionInfo) {
        if (MyConnectionService.getInstance().getCurrentConnectedEndpoint() != null) {
            Toast.makeText(this, connectionInfo.getEndpointName() + "had just invited you to play", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionFailed(Endpoint endpoint) {

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
}