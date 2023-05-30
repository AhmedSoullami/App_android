package com.my_app_perso;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class NoteAdapter extends ArrayAdapter<Note> {
    public NoteAdapter(@NonNull Context context, @NonNull List objects) {
        super(context,0, objects);
    }

    @NonNull
    @Override
    public View getView(int i, @Nullable View convertView, @NonNull ViewGroup parent) {
        View newItem;
        Log.d(NoteAdapter.class.getSimpleName(),"Item N°"+i);
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        newItem = li.inflate(R.layout.notes_line,parent,false);

        ImageView icone = newItem.findViewById(R.id.Icone);
        TextView txtMatiere = newItem.findViewById(R.id.txtMatière);
        TextView txtScore = newItem.findViewById(R.id.txtScore);

        if(this.getItem(i).getScore()<10)
        icone.setImageResource(R.drawable.ic_dislike);
        else
            icone.setImageResource(R.drawable.ic_like);

        txtMatiere.setText(this.getItem(i).getLabel());
        txtScore.setText(this.getItem(i).getScore()+"");


        return newItem;
        
    }
}
