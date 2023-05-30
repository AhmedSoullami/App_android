package com.my_app_perso;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class NoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        this.setTitle(getIntent().getStringExtra(MainActivity.EXTRA_STD_NAME));
    }

    public void ClickHandler(View view) {
        EditText t1= findViewById(R.id.newMat);
        EditText t2= findViewById(R.id.newScore);
        Note n = new Note(t1.getText().toString(),Double.parseDouble(t2.getText().toString()));
        Intent iRetour= new Intent();
        iRetour.putExtra(MainActivity.EXTRA_NEW_NOTE,n);
        setResult(RESULT_OK,iRetour);
        finish();
    }
}