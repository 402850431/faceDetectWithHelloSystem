package com.example.user.facedetectwithhellosystem.tools;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

/**
 * Created by Innooz on 2017/12/18.
 */

public class ReplaceFragment {

    private Context context;
    FragmentTransaction fragmentTransaction;

    public ReplaceFragment(Context context) {
        this.context = context;
    }

    public void replace(FragmentManager fragmentManager, int layout, Fragment newFragment){
        fragmentTransaction = fragmentManager.beginTransaction();
        try {
            fragmentTransaction.replace(layout, newFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } catch (Exception e) {
            Log.e(">>>Exception", e.toString());
        }
    }


    public void replaceWithoutBackStack(FragmentManager fragmentManager, int layout, Fragment newFragment){
        fragmentTransaction = fragmentManager.beginTransaction();
        try {
            fragmentTransaction.replace(layout, newFragment);
            fragmentTransaction.commit();
        } catch (Exception e) {
            Log.e(">>>Exception", e.toString());
        }
    }

    public void replaceWithBundle(FragmentManager fragmentManager, int layout, Fragment newFragment, Bundle bundle){

        fragmentTransaction = fragmentManager.beginTransaction();
        try {
            newFragment.setArguments(bundle);
            fragmentTransaction.replace(layout, newFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } catch (Exception e) {
            Log.e(">>>Exception", e.toString());
        }
    }

    public void add(FragmentManager fragmentManager, int layout, Fragment newFragment){
        fragmentTransaction = fragmentManager.beginTransaction();
        try {
            fragmentTransaction.add(layout, newFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } catch (Exception e) {
            Log.e(">>>Exception", e.toString());
        }
    }

    public void addWithBundle(FragmentManager fragmentManager, int layout, Fragment newFragment, Bundle bundle){

        fragmentTransaction = fragmentManager.beginTransaction();
        try {
            newFragment.setArguments(bundle);
            fragmentTransaction.add(layout, newFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } catch (Exception e) {
            Log.e(">>>Exception", e.toString());
        }
    }


    public void clearBackStack(FragmentManager fragmentManager){
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public void backToPreviousFragment(FragmentManager fragmentManager){
        fragmentManager.popBackStack();
    }

}
