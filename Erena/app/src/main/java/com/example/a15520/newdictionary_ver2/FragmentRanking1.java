package com.example.a15520.newdictionary_ver2;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static android.view.inputmethod.EditorInfo.IME_ACTION_DONE;

/**
 * Created by 15520 on 12/22/2017.
 */

public class FragmentRanking1 extends Fragment {

    ListView _lvRanking;
    RankingAdapter _adapterRanking;
    private DatabaseReference mUserRef;

    public FragmentRanking1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ranking, container, false);

        Typeface helvetica = Typeface.createFromAsset(getActivity().getAssets(),"fonts/HelveticaWorld-Regular.ttf");
        Typeface inconsolata = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Inconsolata.otf");

        mUserRef = FirebaseDatabase.getInstance().getReference("UserInfo");
        _lvRanking = (ListView) rootView.findViewById(R.id.lv_ranking);
        final ArrayList<UserModel> _listID = new ArrayList<>();
        mUserRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // _listID.add(dataSnapshot.getValue(UserModel.class));
                UserModel pos = dataSnapshot.getValue(UserModel.class);
                _listID.add(pos);
                SortData(_listID,1);

                _adapterRanking = new RankingAdapter(_listID, getActivity(),1);
                _lvRanking.setAdapter(_adapterRanking);
                GlobalVariable.getInstance().set_arrayUser(_listID);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //UserModel pos = new UserModel("","phong","100",1);
        //_listID.add(pos);


        return rootView;
    }

    private void SortData(ArrayList<UserModel> listID , int Type) {

            Collections.sort(listID,new UserChainedComparator(new UserScore1Comparator() ,new UserRankComparator()));


    }


    @Override
    public void onStart() {
        super.onStart();

    }
}