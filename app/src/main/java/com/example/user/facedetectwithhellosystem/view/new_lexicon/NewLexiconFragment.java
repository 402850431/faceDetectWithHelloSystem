package com.example.user.facedetectwithhellosystem.view.new_lexicon;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.facedetectwithhellosystem.R;
import com.example.user.facedetectwithhellosystem.database.MySQLite;
import com.example.user.facedetectwithhellosystem.tools.ReplaceFragment;
import com.example.user.facedetectwithhellosystem.view.MainActivity;
import com.example.user.facedetectwithhellosystem.view.ManageLexiconFragment;
import com.example.user.facedetectwithhellosystem.view.choose_lexicon.ChooseLexiconAdapter;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewLexiconFragment extends Fragment {

    MySQLite mySQLite;
    EditText newLexiconEt, newWordEt;
    RecyclerView recyclerView;
    ArrayList<String> arrayList;

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

        return inflater.inflate(R.layout.fragment_new_lexicon, container, false);

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
                String tableNameStr = newLexiconEt.getText().toString();
                if (!mySQLite.isTableExists(tableNameStr)){
                    mySQLite.createTable(tableNameStr);
                }
                for (int i = 0; i < arrayList.size(); i++) {
                    mySQLite.add(tableNameStr, arrayList.get(i));
                }
                mySQLite.close();
                Toast.makeText(getContext(), R.string.newSuccess, Toast.LENGTH_SHORT).show();
                new ReplaceFragment(getContext()).backToPreviousFragment(getFragmentManager());
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
        setUpRv();
    }

    private void setUpRv() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new NewLexiconAdapter(getContext(), arrayList));
    }


    private void setUpOnClicks(View view) {
        view.findViewById(R.id.newWordImg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrayList.size()<20) {
                    arrayList.add(newWordEt.getText().toString());
                    recyclerView.getAdapter().notifyItemInserted(arrayList.size());
                    newWordEt.getText().clear();
                } else {
                    new AlertDialog.Builder(getContext())
                            .setTitle(R.string.maximumIs20)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
            }
        });

        view.findViewById(R.id.clearTextImg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newLexiconEt.getText().clear();
                newWordEt.getText().clear();
            }
        });

    }

    private void findViewByIds(View view) {
        mySQLite = new MySQLite(getContext());
        newLexiconEt = view.findViewById(R.id.newLexiconEt);
        newWordEt = view.findViewById(R.id.newWordEt);
        recyclerView = view.findViewById(R.id.recyclerView);
        arrayList = new ArrayList<>();
    }
}
