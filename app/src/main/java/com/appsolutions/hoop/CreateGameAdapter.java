package com.appsolutions.hoop;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class CreateGameAdapter extends FragmentStatePagerAdapter {

    public CreateGameAdapter(FragmentManager fragmentManager){
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return CreateStepOne.getInstance();
            case 1:
                return CreateStepTwo.getInstance();
            case 2:
                return CreateStepThree.getInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
