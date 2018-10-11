package com.example.user.facedetectwithhellosystem.view.choose_lexicon;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.facedetectwithhellosystem.R;
import com.example.user.facedetectwithhellosystem.database.MySQLite;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChooseLexiconFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<String> tableList;
    MySQLite mySQLite;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose_lexicon, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViewByIds(view);
        setUpOnClicks(view);
        setUpRv();
    }

    private void setUpRv() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        tableList = mySQLite.getTables();
        recyclerView.setAdapter(new ChooseLexiconAdapter(getContext(), tableList, mySQLite));
    }

    private void setUpOnClicks(View view) {

    }

    private void findViewByIds(View view) {
        mySQLite = new MySQLite(getContext());
        recyclerView = view.findViewById(R.id.recyclerView);
    }

}
