package com.example.user.facedetectwithhellosystem.view.choose_lexicon.choose_lexicon_word;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.facedetectwithhellosystem.R;
import com.example.user.facedetectwithhellosystem.database.MySQLite;
import com.example.user.facedetectwithhellosystem.tools.ReplaceFragment;
import com.example.user.facedetectwithhellosystem.view.MainActivity;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChooseLexiconWordsFragment extends Fragment {

    RecyclerView recyclerView;
//    ArrayList<String> wordsList;
    ArrayList<Word> wordsList;
    MySQLite mySQLite;
    String lexiconName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        if (getActivity()!=null) {
            ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
            if (actionBar!=null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.show();
            }
        }
        return inflater.inflate(R.layout.fragment_choose_lexicon_words, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViewByIds(view);
        setUpOnClicks(view);
        if (getArguments()!=null) {
            lexiconName = getArguments().getString("lexiconName");
            setUpRv();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                new ReplaceFragment(getContext()).backToPreviousFragment(getFragmentManager());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpRv() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        wordsList = mySQLite.getRows(lexiconName);
        recyclerView.setAdapter(new ChooseLexiconWordsAdapter(getContext(), wordsList, lexiconName));
        mySQLite.close();
    }
    private void setUpOnClicks(View view) {

    }

    private void findViewByIds(View view) {
        mySQLite = new MySQLite(getContext());
        recyclerView = view.findViewById(R.id.recyclerView);
    }

}
