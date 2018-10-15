package com.example.user.facedetectwithhellosystem.view.choose_lexicon.choose_lexicon_word;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.user.facedetectwithhellosystem.R;
import com.example.user.facedetectwithhellosystem.database.MySQLite;
import com.example.user.facedetectwithhellosystem.tools.ReplaceFragment;
import com.example.user.facedetectwithhellosystem.view.RenameWordFragment;

import java.util.ArrayList;

public class ChooseLexiconWordsAdapter extends RecyclerView.Adapter<ChooseLexiconWordsAdapter.HomeViewHolder> {

    private Context context;
    private ArrayList<Word> arrayList;
    private MySQLite mySQLite;
    String lexiconName;

    ChooseLexiconWordsAdapter(Context context, ArrayList<Word> arrayList, String lexiconName) {
        this.context = context;
        this.arrayList = arrayList;
        this.mySQLite = new MySQLite(context);
        this.lexiconName = lexiconName;
    }

    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lexicon_words, null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(lp);

        return new HomeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final HomeViewHolder holder, final int position) {
        holder.wordTv.setText(arrayList.get(position).getWord());

        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("id", arrayList.get(holder.getAdapterPosition()).getId());
                bundle.putString("lexiconName", lexiconName);
                bundle.putString("oldWord", arrayList.get(holder.getAdapterPosition()).getWord());
                new ReplaceFragment(context).replaceWithBundle(((AppCompatActivity) context).getSupportFragmentManager(),
                        R.id.mainFrame, new RenameWordFragment(), bundle);
            }
        });

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(">>>delete", ":)");

                new AlertDialog.Builder(context)
                        .setTitle(R.string.areYouSureToDelete)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mySQLite.delete(lexiconName, arrayList.get(holder.getAdapterPosition()).getId());
                                arrayList.remove(position);
                                notifyItemRemoved(position);
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
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


    class HomeViewHolder extends RecyclerView.ViewHolder {

        TextView wordTv;
        Button editBtn, deleteBtn;

        HomeViewHolder(View itemView) {
            super(itemView);
            wordTv = itemView.findViewById(R.id.wordTv);
            editBtn = itemView.findViewById(R.id.editBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }
}