package com.example.a15520.newdictionary_ver2;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.mikhaellopez.circularimageview.CircularImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    //region user info
    private TextView _name, _email;
    //endregion


    private ViewPager pager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pager = (ViewPager) findViewById(R.id.view_pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        FragmentManager manager = getSupportFragmentManager();
        PagerAdapter adapter = new PagerAdapter(manager);
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setupWithViewPager(pager);


        Typeface helvetica = Typeface.createFromAsset(getAssets(), "fonts/HelveticaWorld-Regular.ttf");
        Typeface inconsolata = Typeface.createFromAsset(getAssets(), "fonts/Inconsolata.otf");


        //region user info
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        View header = navigationView.getHeaderView(0);

        _name = (TextView) header.findViewById(R.id.name);
        _email = (TextView) header.findViewById(R.id.email);
        _name.setTypeface(helvetica);
        _email.setTypeface(inconsolata);

        _name.setText(GlobalVariable.getInstance().getUser().getDisplayName());
        _email.setText(GlobalVariable.getInstance().getUser().getEmail());

        //endregion
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Intent myIntent = new Intent(MainActivity.this, MyConnectionService.class);
        startService(myIntent);
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
            Intent t = new Intent(MainActivity.this, Game1GuideActivity.class);
            startActivity(t);


        } else if (id == R.id.nav_game2) {
            Intent t = new Intent(MainActivity.this, Game2GuideActivity.class);
            startActivity(t);

        } else if (id == R.id.nav_game3) {
            Intent t = new Intent(MainActivity.this, Game3GuideActivity.class);
            startActivity(t);


        } else if (id == R.id.nav_ranking) {
            Intent t = new Intent(MainActivity.this, RankingActivity.class);
            startActivity(t);


        } else if (id == R.id.nav_feedback) {
            Intent t = new Intent(MainActivity.this, SendMailActivity.class);
            startActivity(t);


        } else if (id == R.id.nav_idioms) {
            Intent t = new Intent(MainActivity.this, IdiomsActivity.class);
            startActivity(t);

        } else if (id == R.id.nav_document) {
            Intent t = new Intent(MainActivity.this, ListDocumentsActivity.class);
            startActivity(t);

        } else if (id == R.id.nav_Sign_out) {
            signout();

        } else if (id == R.id.nav_dict) {
//          if(host.getCurrentTab() == 0)
//              host.setCurrentTab(1);
//          else
//              host.setCurrentTab(0);
        } else if (id == R.id.nav_advanced) {
            Intent t = new Intent(MainActivity.this, TranslateActivity.class);
            startActivity(t);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);


        return true;
    }

    private void signout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);

        startActivity(intent);
        GoogleSignInClient mGoogleSignInClient;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.signOut();
        finish();
//        Auth.GoogleSignInApi.signOut(GlobalVariable.getInstance().getmGoogleSignInClient()).setResultCallback(new ResultCallback<Status>() {
//
//            public void onResult(@NonNull Status status) {
//
//                Toast.makeText(MainActivity.this, "Logout", Toast.LENGTH_SHORT).show();
//
//            }
//        });

    }


}
