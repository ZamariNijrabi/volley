package com.example.hamid.volley.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.hamid.volley.datamodel.Note;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hamid on 3/12/2018.
 */

public class NoteServices {

    Context context;

    public NoteServices(Context context) {
        this.context = context;
    }

    // Note request urls
    final String BASE_URL = "http://192.168.56.1/istores/public/api/";
    final String GET_NOTE = BASE_URL + "notes/";

    /**
     * Get note by id
     *
     * @param id
     * @param onNoteReceived
     */
    public void getNote(final String id, final OnNoteReceived onNoteReceived) {
        JsonObjectRequest getNote = new JsonObjectRequest(Request.Method.GET, GET_NOTE + id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Note note = new Note();
                try {
                    note.setId(response.getString("id"));
                    note.setTitle(response.getString("title"));
                    note.setDescription(response.getString("description"));
                    note.setImagePath(response.getString("image_path"));
                    note.setCreatedAt(response.getString("created_at"));
                    note.setDeletedAt(response.getString("deleted_at"));
                    note.setUpdatedAt(response.getString("updated_at"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                onNoteReceived.onNoteReceived(note);
                Log.d("note", "onResponse: " + note);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("On get note error", "onErrorResponse: " + error);
            }
        });

        // retry policy
        getNote.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(getNote);
    }

    public interface OnNoteReceived {
        void onNoteReceived(Note note);
    }


    /**
     * Get all notes
     *
     * @param onNotesReceived
     */
    public void getNotes(final OnNotesReceived onNotesReceived) {
        JsonArrayRequest getNotes = new JsonArrayRequest(Request.Method.GET, GET_NOTE, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                List<Note> notes = new ArrayList<>();

                for (int i = 0; i < response.length(); i++) {
                    try {
                        Note note = new Note();
                        JSONObject jsonObject = response.getJSONObject(i);
                        note.setId(jsonObject.getString("id"));
                        note.setTitle(jsonObject.getString("title"));
                        note.setDescription(jsonObject.getString("description"));
                        note.setImagePath(jsonObject.getString("image_path"));
                        note.setDeletedAt(jsonObject.getString("deleted_at"));
                        note.setCreatedAt(jsonObject.getString("created_at"));
                        note.setUpdatedAt(jsonObject.getString("updated_at"));
                        notes.add(note);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    onNotesReceived.onNotesReceived(notes);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("on receive note", "onErrorResponse: " + error);
            }
        });

        // retry policy
        getNotes.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(getNotes);

    }

    public interface OnNotesReceived {
        void onNotesReceived(List<Note> notes);
    }

}
