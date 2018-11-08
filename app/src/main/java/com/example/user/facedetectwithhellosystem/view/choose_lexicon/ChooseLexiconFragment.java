package com.example.user.facedetectwithhellosystem.view.choose_lexicon;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.user.facedetectwithhellosystem.R;
import com.example.user.facedetectwithhellosystem.database.MySQLite;
import com.example.user.facedetectwithhellosystem.tools.ReplaceFragment;
import com.example.user.facedetectwithhellosystem.view.MainActivity;
import com.example.user.facedetectwithhellosystem.view.ManageLexiconFragment;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChooseLexiconFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<String> tableList;
    MySQLite mySQLite;
    ChooseLexiconAdapter chooseLexiconAdapter;
    SharedPreferences spf;

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
        return inflater.inflate(R.layout.fragment_choose_lexicon, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_complete, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuComplete:
                spf.edit()
                        .putInt("selectedLexicon", chooseLexiconAdapter.getLastCheckedPosition())
                        .putString("selectedLexiconName", tableList.get(chooseLexiconAdapter.getLastCheckedPosition()))
                        .apply();
                Toast.makeText(getContext(), R.string.editSuccess, Toast.LENGTH_SHORT).show();
                return true;
            case android.R.id.home:
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
        if (mySQLite.isHasTable()) {
            setUpRv();
        }
    }

    private void setUpRv() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(chooseLexiconAdapter);
        final int storedPosition = spf.getInt("selectedLexicon", 0);
        Log.e(">>>storedPosition", storedPosition +"");
//        RecyclerView.ViewHolder childViewHolder = recyclerView.findViewHolderForAdapterPosition(storedPosition);
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                View viewItem = recyclerView.getLayoutManager().findViewByPosition(storedPosition);
                CheckBox checkBox = viewItem.findViewById(R.id.checkbox);
                checkBox.setChecked(true);
                chooseLexiconAdapter.setLastCheckedPosition(storedPosition);
            }
        });
        /*
        View viewItem = mLayoutManager.findViewByPosition(storedPosition);
        if (storedPosition != -1 && viewItem != null) {
//            View childView = childViewHolder.itemView;
            CheckBox checkBox = viewItem.findViewById(R.id.checkbox);
            checkBox.setChecked(true);
            chooseLexiconAdapter.setLastCheckedPosition(storedPosition);
            Log.e(">>>setLastCh", storedPosition +"");
        } else {
            Log.e(">>>viewItem", "is null");
        }
        */
    }

    private void setUpOnClicks(View view) {

    }

    private void findViewByIds(View view) {
        mySQLite = new MySQLite(getContext());
        recyclerView = view.findViewById(R.id.recyclerView);
        tableList = mySQLite.getTables();
        chooseLexiconAdapter = new ChooseLexiconAdapter(getContext(), tableList, mySQLite);
        spf = PreferenceManager.getDefaultSharedPreferences(getContext());
    }

}
