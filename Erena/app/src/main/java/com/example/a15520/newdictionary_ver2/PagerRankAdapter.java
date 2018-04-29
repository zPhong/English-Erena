package com.example.a15520.newdictionary_ver2;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by 15520 on 12/22/2017.
 */

public class PagerRankAdapter extends FragmentStatePagerAdapter {

    PagerRankAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }
    @Override
    public Fragment getItem(int position) {
        Fragment frag=null;
        switch (position){
            case 0:
                frag = new FragmentRanking1();
                break;
            case 1:
                frag = new FragmentRanking2();
                break;
            case 2:
                frag = new FragmentRanking3();
                break;
        }
        return frag;
    }

    @Override
    public int getCount() {
        return 3;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position){
            case 1:
                title = "Fill-Cup";
                break;
            case 0:
                title = "Babel-Tower";
                break;

            case 2:
                title = "ChooChoo-Train";
                break;
        }
        return title;
    }
}