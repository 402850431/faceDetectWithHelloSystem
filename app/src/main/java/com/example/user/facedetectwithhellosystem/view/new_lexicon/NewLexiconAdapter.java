package com.example.user.facedetectwithhellosystem.view.new_lexicon;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.facedetectwithhellosystem.R;
import com.example.user.facedetectwithhellosystem.database.MySQLite;
import com.example.user.facedetectwithhellosystem.tools.ReplaceFragment;
import com.example.user.facedetectwithhellosystem.view.RenameLexiconFragment;
import com.example.user.facedetectwithhellosystem.view.choose_lexicon.choose_lexicon_word.ChooseLexiconWordsFragment;

import java.util.ArrayList;

public class NewLexiconAdapter extends RecyclerView.Adapter<NewLexiconAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> arrayList;

    NewLexiconAdapter(Context context, ArrayList<String> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_lexicon, null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(lp);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.wordTv.setText(arrayList.get(position));

        holder.deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayList.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView wordTv;
        ImageView deleteImg;

        ViewHolder(View itemView) {
            super(itemView);
            wordTv = itemView.findViewById(R.id.wordTv);
            deleteImg = itemView.findViewById(R.id.deleteImg);
        }
    }
}