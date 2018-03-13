package com.example.hamid.volley;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.hamid.volley.api.NoteServices;
import com.example.hamid.volley.datamodel.Note;

public class NotesActivity extends AppCompatActivity {
    NoteServices services;
    TextView title, description, createdAt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        services = new NoteServices(this);
        title = (TextView)findViewById(R.id.noteTitle);
        description = (TextView)findViewById(R.id.description);
        createdAt = (TextView)findViewById(R.id.createdAt);


        // get the 4th note
        services.getNote("5", new NoteServices.OnNoteReceived() {
            @Override
            public void onNoteReceived(Note note) {
                title.setText(note.getTitle());
                description.setText(note.getDescription());
                createdAt.setText(note.getCreatedAt());
            }
        });
    }
}
