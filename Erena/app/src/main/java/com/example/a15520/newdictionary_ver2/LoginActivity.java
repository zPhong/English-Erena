package com.example.a15520.newdictionary_ver2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.internal.zzdwg;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by 15520 on 12/02/2017.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private SignInButton _signin;
    private Button _signGuest;
    private GoogleSignInClient mGoogleSignInClient;
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;

    private FirebaseUser user;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mUserRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);


        _signin = (SignInButton) findViewById(R.id.sign_in_button);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
//        mGoogleSignInClient = new GoogleApiClient.Builder(this).addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();
//        GlobalVariable.getInstance().setmGoogleSignInClient(mGoogleSignInClient);
        _signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        _signGuest = (Button)findViewById(R.id.btnGuest);
        _signGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUI(null);
            }
        });


//region Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mUserRef = mDatabase.getReference("UserInfo");


        //endregion
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();

        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser !=null) {
            GlobalVariable.getInstance().setUser(currentUser);
            updateUI(currentUser);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            // Google Sign In was successful, authenticate with Firebase
            GoogleSignInAccount account = task.getResult(ApiException.class);
            firebaseAuthWithGoogle(account);
        } catch (ApiException e) {
            // Google Sign In failed, update UI appropriately
            Log.w(TAG, "Google sign in failed", e);
            // [START_EXCLUDE]
            updateUI(null);
            // [END_EXCLUDE]
        }
    }


    void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Thành công
                            Toast.makeText(LoginActivity.this, "Đã đăng nhập.", Toast.LENGTH_SHORT).show();
                            user = mAuth.getCurrentUser();
                            GlobalVariable.getInstance().setUser(user);
                            SaveDataUser();
                            updateUI(user);
                        } else {
                            // Thất bại
                            updateUI(null);
                        }
                    }
                });

    }

    private void SaveDataUser() {


        mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(user.getUid())) {
                    mUserRef.updateChildren(createObj());
                    mUserRef.removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //mGoogleSignInClient.signOut();
    }

    private Map<String,Object> createObj() {
        String key = mAuth.getUid();
        HashMap<String, Object> result = new HashMap<>();
//        if(user.getPhotoUrl() != null)
         result.put(key + "/Url",user.getPhotoUrl().toString());
//        else
//            result.put(key + "/Url","");
        result.put( key + "/Name", user.getDisplayName());
        result.put(key + "/Email",user.getEmail());
        result.put(key + "/Score1","0");
        result.put(key + "/Score2","0");
        result.put(key + "/Score3","0");
        result.put(key + "/Rank","1");

        return result;
    }

    private void updateUI(FirebaseUser user)
    {

        if(user== null) {
            GlobalVariable.getInstance().setUser(new FirebaseUser() {
                @NonNull
                @Override
                public String getUid() {
                    return null;
                }

                @NonNull
                @Override
                public String getProviderId() {
                    return null;
                }

                @Override
                public boolean isAnonymous() {
                    return true;
                }

                @Nullable
                @Override
                public List<String> getProviders() {
                    return null;
                }

                @NonNull
                @Override
                public List<? extends UserInfo> getProviderData() {
                    return null;
                }

                @NonNull
                @Override
                public FirebaseUser zzap(@NonNull List<? extends UserInfo> list) {
                    return null;
                }

                @Override
                public FirebaseUser zzcc(boolean b) {
                    return null;
                }

                @NonNull
                @Override
                public FirebaseApp zzbpm() {
                    return null;
                }

                @Nullable
                @Override
                public String getDisplayName() {
                    return "GUEST";
                }

                @Nullable
                @Override
                public Uri getPhotoUrl() {
                    return null;
                }

                @Nullable
                @Override
                public String getEmail() {
                    return null;
                }

                @Nullable
                @Override
                public String getPhoneNumber() {
                    return null;
                }

                @NonNull
                @Override
                public zzdwg zzbpn() {
                    return null;
                }

                @Override
                public void zza(@NonNull zzdwg zzdwg) {

                }

                @NonNull
                @Override
                public String zzbpo() {
                    return null;
                }

                @NonNull
                @Override
                public String zzbpp() {
                    return null;
                }

                @Nullable
                @Override
                public FirebaseUserMetadata getMetadata() {
                    return null;
                }

                @Override
                public boolean isEmailVerified() {
                    return false;
                }
            });
        }

        Intent intent=new Intent(LoginActivity.this,MainActivity.class);

        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(LoginActivity.this, "No Internet Connection!!!", Toast.LENGTH_SHORT).show();
    }
}
