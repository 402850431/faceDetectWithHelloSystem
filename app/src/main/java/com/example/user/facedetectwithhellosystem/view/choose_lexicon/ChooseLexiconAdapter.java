package com.example.user.facedetectwithhellosystem.view.choose_lexicon;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.facedetectwithhellosystem.view.choose_lexicon.choose_lexicon_word.ChooseLexiconWordsFragment;
import com.example.user.facedetectwithhellosystem.R;
import com.example.user.facedetectwithhellosystem.view.RenameLexiconFragment;
import com.example.user.facedetectwithhellosystem.database.MySQLite;
import com.example.user.facedetectwithhellosystem.tools.ReplaceFragment;

import java.util.ArrayList;

public class ChooseLexiconAdapter extends RecyclerView.Adapter<ChooseLexiconAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> arrayList;
    private ArrayList<Boolean> booleanArrayList;
    private MySQLite mySQLite;
    private int lastCheckedPosition = -1;
    private int storedCheckedPosition;

    public int getLastCheckedPosition() {
        return lastCheckedPosition;
    }

    public void setLastCheckedPosition(int lastCheckedPosition) {
        this.lastCheckedPosition = lastCheckedPosition;
    }

    ChooseLexiconAdapter(Context context, ArrayList<String> arrayList, MySQLite mySQLite) {
        this.context = context;
        this.arrayList = arrayList;
        this.mySQLite = mySQLite;
        SharedPreferences spf = PreferenceManager.getDefaultSharedPreferences(context);
        storedCheckedPosition = spf.getInt("selectedLexicon", 0);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lexicon, null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(lp);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.wordTv.setText(arrayList.get(position));

        if (storedCheckedPosition != -1) {
//            holder.checkBox.setChecked(true);
        }

        holder.checkBox.setChecked(holder.getAdapterPosition() == lastCheckedPosition);

        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("oldLexiconName", arrayList.get(holder.getAdapterPosition()));
                new ReplaceFragment(context).replaceWithBundle(((AppCompatActivity) context).getSupportFragmentManager(),
                        R.id.mainFrame, new RenameLexiconFragment(), bundle);
            }
        });

        holder.wordTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("lexiconName", arrayList.get(holder.getAdapterPosition()));
                new ReplaceFragment(context).replaceWithBundle(((AppCompatActivity) context).getSupportFragmentManager(),
                        R.id.mainFrame, new ChooseLexiconWordsFragment(), bundle);
            }
        });

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(context)
                        .setTitle(R.string.areYouSureToDelete)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (holder.getAdapterPosition() == lastCheckedPosition) {
                                    Toast.makeText(context, R.string.canNotDeleteSelectedTable, Toast.LENGTH_SHORT).show();
                                } else if (arrayList.size() < 2) {
                                        Toast.makeText(context, R.string.tableSizeCanNotBeZero, Toast.LENGTH_SHORT).show();
                                } else {
                                    mySQLite.deleteTable(arrayList.get(holder.getAdapterPosition()));
                                    Toast.makeText(context, R.string.deleteSuccess, Toast.LENGTH_SHORT).show();
                                    arrayList.remove(holder.getAdapterPosition());
                                    notifyItemRemoved(holder.getAdapterPosition());

                                }
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView wordTv;
        Button editBtn, chooseBtn, deleteBtn;
        CheckBox checkBox;

        ViewHolder(View itemView) {
            super(itemView);
            wordTv = itemView.findViewById(R.id.wordTv);
            editBtn = itemView.findViewById(R.id.editBtn);
            chooseBtn = itemView.findViewById(R.id.chooseBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
            checkBox = itemView.findViewById(R.id.checkbox);
            final ArrayList<String> tableList = mySQLite.getTables();
            chooseBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int copyOfLastCheckedPosition = lastCheckedPosition;
                    lastCheckedPosition = getAdapterPosition();
                    notifyItemChanged(copyOfLastCheckedPosition);
                    notifyItemChanged(lastCheckedPosition);

                    PreferenceManager.getDefaultSharedPreferences(context).edit()
                            .putInt("selectedLexicon", getAdapterPosition())
                            .putString("selectedLexiconName", tableList.get(getAdapterPosition()))
                            .apply();
                }
            });

        }
    }
}