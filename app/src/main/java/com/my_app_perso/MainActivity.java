package com.my_app_perso;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private static final String HTTP_URL = "https://belatar.name/rest/profile.php?login=test&passwd=test&id=9998";
    private static final String HTTP_IMAGES = "https://belatar.name/images/";
    public static final String EXTRA_STD_NAME = "stdName";
    public static final String EXTRA_NEW_NOTE = "stdNewNote";
    private static final int ACT_NEW_NOTE = 101;

    private Etudiant etu;
    private ListView ListNote = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(MainActivity.class.getSimpleName(),"On est dans OnResume");


        ListNote = findViewById(R.id.notes);
        String noteparametre;
        if (ListNote==null)
        {  noteparametre="";}
        else
        {
             noteparametre="&notes=true";
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, HTTP_URL+noteparametre, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(MainActivity.class.getSimpleName(), response.toString());
                        try {
                            if (response.has("error"))
                                Toast.makeText(MainActivity.this, "Erreur", Toast.LENGTH_LONG).show();
                            else {
                                etu = new Etudiant(response.getInt("id"), response.getString("nom"),
                                        response.getString("prenom"), response.getString("classe"),
                                        response.getString("phone"), null);

                                if (response.has("notes"))
                                {
                                    JSONArray ja = response.getJSONArray("notes");
                                    for (int i=0;i<ja.length();i++)
                                        etu.addNotes(new Note(
                                                ja.getJSONObject(i).getString("label")
                                                ,ja.getJSONObject(i).getDouble("score"))
                                        );
                                }



                                VolleySingleton.getInstance(getApplicationContext()).getImageLoader()
                                        .get(HTTP_IMAGES + response.getString("photo"),
                                                new ImageLoader.ImageListener() {
                                                    @Override
                                                    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                                                        etu.setPhoto(response.getBitmap());
                                                        ImageView img = findViewById(R.id.imgProfile);
                                                        img.setImageBitmap(etu.getPhoto());
                                                    }

                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Log.e(MainActivity.class.getSimpleName(),error.getMessage());

                                                    }
                                                });

                                if(etu.getNotes()!=null) {
                                    NoteAdapter na = new NoteAdapter(MainActivity.this, etu.getNotes());
                                    ListNote.setAdapter(na);
                                }

                                EditText txtnom = findViewById(R.id.txtNom);
                                EditText txtprenom = findViewById(R.id.txtPrenom);
                                EditText txtclasse = findViewById(R.id.txtClasse);

                                txtnom.setText(etu.getNom());
                                txtprenom.setText(etu.getPrenom());
                                txtclasse.setText(etu.getClasse());
                            }

                            } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(MainActivity.class.getSimpleName(),error.getMessage());
                    }
                }


        );

        if(etu==null)
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    public void ClickHandler(View view) {
        switch(view.getId())
        {
            case R.id.btnSave:
                Toast.makeText(this, R.string.clickmsg, Toast.LENGTH_LONG).show();
                break;
            case R.id.btnCall:
                if(etu!=null)
                {
                    Intent iCall = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+etu.getPhone()));
                    startActivity(iCall);
                }
                break;

            case R.id.btnAddNote:
                if(etu!=null) {
                    Intent iNote = new Intent(getApplicationContext(), NoteActivity.class);
                    iNote.putExtra(EXTRA_STD_NAME, etu.getNom() + " " + etu.getPrenom());
                    startActivityForResult(iNote, ACT_NEW_NOTE);
                }
                break;
            default :
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ACT_NEW_NOTE && resultCode==RESULT_OK && data!=null)
        {
            Note newNote = (Note) data.getSerializableExtra(EXTRA_NEW_NOTE);
            ListNote = findViewById(R.id.notes);
            ((NoteAdapter) ListNote.getAdapter()).add(newNote);
            Log.w(MainActivity.class.getSimpleName(),
                    "On a réccupéré la note de"+newNote.getLabel());
        }
    }
}