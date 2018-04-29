package com.example.a15520.newdictionary_ver2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.auth.FirebaseAuth;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Locale;

public class TranslateActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private EditText _etTranslate;
    private TextView _tvTranslated,_tvTranslateLanguage , _tvTranslatedLanguage;
    private Button _btnTranslate,_btnSwap,_btnPhoto ,_btnClear,_btnSpeech;
    private String type = "";
    private TextView _name,_email;
    private ScrollView _svTranslate;
    private TextToSpeech voice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(!isInternetOn())
        {
            Toast.makeText(this,"Chức năng cần có kết nối internet",Toast.LENGTH_SHORT).show();
            finish();
        }

        Typeface helvetica = Typeface.createFromAsset(getAssets(), "fonts/HelveticaWorld-Regular.ttf");

        _etTranslate = (EditText) findViewById(R.id.etTranslate);
        _tvTranslated = (TextView) findViewById(R.id.tvTranslated);

        _tvTranslatedLanguage = (TextView) findViewById(R.id.tvTranslatedLanguage); //source language
        _tvTranslateLanguage = (TextView) findViewById(R.id.tvTranslateLanguage); // des language

        _btnTranslate = (Button) findViewById(R.id.btnTranslate);
        _btnSwap = (Button) findViewById(R.id.btnSwap);
        _btnPhoto = (Button) findViewById(R.id.btnPicture);
        _btnSpeech = (Button) findViewById(R.id.btnSpeech);
        _btnClear   = (Button) findViewById(R.id.btnClear);



        _svTranslate = (ScrollView) findViewById(R.id.svTranslated);
        type = "en|vi";

        _btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _etTranslate.setText("");
            }
        });

        _etTranslate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                _btnTranslate.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
               if(_etTranslate.getText().length() == 0)
                    _svTranslate.setVisibility(View.INVISIBLE);
            }
        });

//Speech
        voice = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener()
        {
            @Override
            public void onInit(int i) {
                if(i != TextToSpeech.ERROR)
                    voice.setLanguage(Locale.UK);
            }
        });
//font
        _tvTranslateLanguage.setTypeface(helvetica);
        _tvTranslatedLanguage.setTypeface(helvetica);
        _tvTranslated.setTypeface(helvetica);
        _etTranslate.setTypeface(helvetica);

        _btnTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TranslateGoogle(_etTranslate.getText().toString(), type);
                _svTranslate.setVisibility(View.VISIBLE);
                _btnTranslate.setVisibility(View.INVISIBLE);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(_etTranslate .getWindowToken(), 0);

            }
        });

        _btnSwap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SwapLanguage();

            }
        });

        _btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextRecognize();

            }
        });

        _btnSpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(type.equals("en|vi")== true)
                    voice.speak(_etTranslate.getText().toString(),TextToSpeech.QUEUE_FLUSH,null);
                else
                    Toast.makeText(TranslateActivity.this,"Not Supported",Toast.LENGTH_SHORT).show();
            }
        });
        Typeface inconsolata = Typeface.createFromAsset(getAssets(),"fonts/Inconsolata.otf");

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        View header=navigationView.getHeaderView(0);

        _name = (TextView)header.findViewById(R.id.name);
        _email= (TextView)header.findViewById(R.id.email);
        _name.setTypeface(helvetica);
        _email.setTypeface(inconsolata);

        _name.setText(GlobalVariable.getInstance().getUser().getDisplayName());
        _email.setText(GlobalVariable.getInstance().getUser().getEmail());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_game1) {
            Intent t = new Intent(TranslateActivity.this,Game1GuideActivity.class);
            startActivity(t);

        } else if (id == R.id.nav_game2) {
            Intent t = new Intent(TranslateActivity.this,Game2Activity.class);
            startActivity(t);
        } else if (id == R.id.nav_game3) {
            Intent t = new Intent(TranslateActivity.this,PreGame3Activity.class);
            startActivity(t);
        } else if (id == R.id.nav_ranking) {
            Intent t = new Intent(TranslateActivity.this,RankingActivity.class);
            startActivity(t);

        }else if (id == R.id.nav_feedback) {
            Intent t = new Intent(TranslateActivity.this,SendMailActivity.class);
            startActivity(t);
        }else if (id == R.id.nav_idioms) {
            Intent t = new Intent(TranslateActivity.this,IdiomsActivity.class);
            startActivity(t);
        }else if (id == R.id.nav_document) {
            Intent t = new Intent(TranslateActivity.this,ListDocumentsActivity.class);
            startActivity(t);
        }else if (id == R.id.nav_Sign_out) {
            signout();
        }
        else if(id == R.id.nav_dict)
        {
            Intent t = new Intent(TranslateActivity.this,MainActivity.class);
            startActivity(t);
        }
        else if(id == R.id.nav_advanced)
        {

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void signout()
    {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(TranslateActivity.this, LoginActivity.class);

        startActivity(intent);
        GoogleSignInClient mGoogleSignInClient;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.signOut();

    }

    //region Text Recognize
    public void TextRecognize() {
        Intent intentCam = null;

        try {
            intentCam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        }catch (SecurityException e)
        {
        }
        startActivityForResult(intentCam, 69);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 69 && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");


            TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
            if (!textRecognizer.isOperational()) {
                Log.w("TranslateActivity", "false");
                Toast.makeText(TranslateActivity.this,"error",Toast.LENGTH_SHORT).show();
            } else {
                Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                SparseArray<TextBlock> items = textRecognizer.detect(frame);
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < items.size(); i++) {
                    TextBlock item = items.valueAt(i);
                    stringBuilder.append(item.getValue());
                    stringBuilder.append("\n");
                }
                _etTranslate.setText(stringBuilder.toString());

                if(_etTranslate.getText().length() < 1)
                    Toast.makeText(TranslateActivity.this,"Try Again!",Toast.LENGTH_SHORT).show();


            }
        }
    }

    //endregion


    private void SwapLanguage() {
        if(type.equals("en|vi") == true)
        {
            type = "vi|en";
            _tvTranslateLanguage.setText("Vietnamese");
            _tvTranslatedLanguage.setText("English");
            String t = _tvTranslated.getText().toString();
            _tvTranslated.setText(_etTranslate.getText().toString());
            _etTranslate.setText(t);

            return;
        }
        if(type.equals("vi|en") == true)
        {
            type = "en|vi";
            _tvTranslatedLanguage.setText("Vietnamese");
            _tvTranslateLanguage.setText("English");
            String t = _tvTranslated.getText().toString();
            _tvTranslated.setText(_etTranslate.getText().toString());
            _etTranslate.setText(t);
            return;
        }

    }

    private void TranslateGoogle(String sentence , String type) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //"https://translate.google.com/#en/vi/" + sentence;

        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, "utf-8");
        params.setBooleanParameter("http.protocol.expect-continue", false);


        String encodeSentence = "";
        String encodeType = "";
        HttpClient httpClient = new DefaultHttpClient(params);
        try {

            encodeSentence = URLEncoder.encode(sentence, "UTF-8");
            encodeType = URLEncoder.encode(type, "UTF-8");


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error Encode", Toast.LENGTH_LONG).show();
        }
        HttpGet httpGet = new HttpGet("http://www.google.com/translate_t?hl=en&ie=UTF8&text=" + encodeSentence + "&langpair=" + encodeType);
        try {
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
//
                String result = EntityUtils.toString(entity, HTTP.UTF_8);
                result = result.substring(result.indexOf("<span title=\"") + "<span title=\"".length());
                result = result.substring(result.indexOf(">") + 1);
                result = result.substring(0, result.indexOf("</span>"));

                _tvTranslated.setText(Html.fromHtml(result));

            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error Load HTML", Toast.LENGTH_LONG).show();
        }
    }
    public final boolean isInternetOn() {

            // get Connectivity Manager object to check connection
            ConnectivityManager connec =
                    (ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

            // Check for network connections
            if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                    connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                    connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                    connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {

                // if connected with internet

                //Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
                return true;

            } else if (
                    connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                            connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {

                //Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
                return false;
            }
            return false;
        }





}
