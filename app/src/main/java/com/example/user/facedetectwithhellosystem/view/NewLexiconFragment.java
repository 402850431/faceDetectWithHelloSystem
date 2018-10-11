package com.example.user.facedetectwithhellosystem.view;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.facedetectwithhellosystem.R;
import com.example.user.facedetectwithhellosystem.database.MySQLite;
import com.example.user.facedetectwithhellosystem.tools.ReplaceFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewLexiconFragment extends Fragment {

    MySQLite mySQLite;
    EditText newLexiconEt, newWordEt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_lexicon, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViewByIds(view);
        setUpOnClicks(view);
    }

    private void setUpOnClicks(View view) {
        view.findViewById(R.id.newWordImg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tableNameStr = newLexiconEt.getText().toString();
                String wordStr = newWordEt.getText().toString();
                if (!tableNameStr.isEmpty() && !wordStr.isEmpty()){
                    if (mySQLite.isTableExists(tableNameStr)){
                        mySQLite.add(tableNameStr, wordStr);
                    } else {
                        mySQLite.createTable(tableNameStr);
                        mySQLite.add(tableNameStr, wordStr);
                    }
                    mySQLite.close();
                    Toast.makeText(getContext(), R.string.newSuccess, Toast.LENGTH_SHORT).show();
                    new ReplaceFragment(getContext()).replace(getFragmentManager(), R.id.mainFrame, new ManageLexiconFragment());
                } else {
                    Toast.makeText(getContext(), R.string.columnCanNotBeEmpty, Toast.LENGTH_SHORT).show();
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

    }
}
