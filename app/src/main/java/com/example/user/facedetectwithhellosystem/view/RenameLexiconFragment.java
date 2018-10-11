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
public class RenameLexiconFragment extends Fragment {

    MySQLite mySQLite;
    EditText newLexiconEt, oldLexiconEt;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rename_lexicon, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViewByIds(view);
        setUpOnClicks(view);
        if (getArguments()!=null) {
            oldLexiconEt.setText(getArguments().getString("oldLexiconName"));
        }
    }

    private void setUpOnClicks(View view) {
        view.findViewById(R.id.renameImg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newLexiconStr = newLexiconEt.getText().toString();
                String oldLexiconStr = oldLexiconEt.getText().toString();
                if (!newLexiconStr.isEmpty()) {
                    if (mySQLite.isTableExists(oldLexiconStr)) {
                        mySQLite.changeTableName(oldLexiconStr, newLexiconStr);
                        mySQLite.close();
                        Toast.makeText(getContext(), R.string.editSuccess, Toast.LENGTH_SHORT).show();
                        new ReplaceFragment(getContext()).backToPreviousFragment(getFragmentManager());
//                    new ReplaceFragment(getContext()).replaceWithoutBackStack(getFragmentManager(), R.id.mainFrame, new ChooseLexiconFragment());
                    } else {
                        Toast.makeText(getContext(), R.string.lexiconNotExist, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), R.string.columnCanNotBeEmpty, Toast.LENGTH_SHORT).show();
                }
            }
        });

        view.findViewById(R.id.clearTextImg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newLexiconEt.getText().clear();
            }
        });

    }

    private void findViewByIds(View view) {
        mySQLite = new MySQLite(getContext());
        newLexiconEt = view.findViewById(R.id.newLexiconEt);
        oldLexiconEt = view.findViewById(R.id.oldLexiconEt);

    }
}
