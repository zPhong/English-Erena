package com.example.a15520.newdictionary_ver2;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by 15520 on 12/22/2017.
 */

public class FragmentRanking2 extends Fragment {

    ListView _lvRanking;
    RankingAdapter _adapterRanking;
    private DatabaseReference mUserRef;

    public FragmentRanking2() {
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


        return rootView;
    }

    private void SortData(ArrayList<UserModel> listID ) {

            Collections.sort(listID,new UserChainedComparator(new UserScore2Comparator(),new UserRankComparator() ));



    }


    @Override
    public void onStart() {
        super.onStart();
        ArrayList<UserModel> _listID = GlobalVariable.getInstance().get_arrayUser();
        SortData(_listID);

        _adapterRanking = new RankingAdapter(_listID, getActivity(),2);
        _lvRanking.setAdapter(_adapterRanking);
    }
}