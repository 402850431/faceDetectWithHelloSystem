package com.example.user.facedetectwithhellosystem.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.facedetectwithhellosystem.R;
import com.example.user.facedetectwithhellosystem.view.choose_lexicon.ChooseLexiconFragment;
import com.example.user.facedetectwithhellosystem.tools.ReplaceFragment;
import com.example.user.facedetectwithhellosystem.view.new_lexicon.NewLexiconFragment;

public class ManageLexiconFragment extends Fragment {

    ActionBar actionBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        if (getActivity()!=null) {
            actionBar = ((MainActivity)getActivity()).getSupportActionBar();
            if (actionBar!=null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.show();
            }
        }
        return inflater.inflate(R.layout.fragment_manage_lexicon, container, false);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (actionBar!=null) {
                actionBar.setDisplayHomeAsUpEnabled(false);
                }
                new ReplaceFragment(getContext()).backToPreviousFragment(getFragmentManager());

                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViewByIds(view);
        setUpOnClicks(view);
    }

    private void setUpOnClicks(View view) {
        view.findViewById(R.id.newLexiconImg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ReplaceFragment(getContext()).replace(getFragmentManager(), R.id.mainFrame, new NewLexiconFragment());
            }
        });
        view.findViewById(R.id.chooseLexiconImg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ReplaceFragment(getContext()).replace(getFragmentManager(), R.id.mainFrame, new ChooseLexiconFragment());
            }
        });
    }

    private void findViewByIds(View view) {
    }

}
