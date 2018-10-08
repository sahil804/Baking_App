package com.example.suris.baking_app.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.suris.baking_app.data.models.Step;

import java.util.List;

/**
 * Created by sahil on 10/7/2018.
 */
public class StepsViewPagerAdapter extends FragmentPagerAdapter {

    private List<Step> stepList;

    public StepsViewPagerAdapter(List<Step> stepList, FragmentManager fm) {
        super(fm);
        this.stepList = stepList;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(RecipeStepDetailFragment.STEP_KEY, stepList.get(position));
        RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public int getCount() {
        return stepList.size();
    }

    /*@Override
    public CharSequence getPageTitle(int position) {
        return "STEP: " + stepList.get(position).get_id();
    }*/
}
