package com.example.mediaandroid;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<AudioModel> audioList = new ArrayList<>();
    List<AudioModel> filteredList = new ArrayList<>();
    RecyclerView recyclerView;
    AudioAdapter adapter;
    EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        searchEditText = findViewById(R.id.searchEditText);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AudioAdapter(this, audioList);
        recyclerView.setAdapter(adapter);

        //To Get All Audio Files from device
        getAudioFiles();

        // Search functionality
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String query = editable.toString().toLowerCase();
                filterAudioList(query);
            }
        });
    }

    private void getAudioFiles() {
        ContentResolver contentResolver = getContentResolver();
        Uri externalUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA};
        Cursor cursor = contentResolver.query(externalUri, projection, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                AudioModel audioModel = new AudioModel(title, path);
                audioList.add(audioModel);
            }
            cursor.close();
        }

        Uri internalUri = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
        cursor = contentResolver.query(internalUri, projection, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                AudioModel audioModel = new AudioModel(title, path);
                audioList.add(audioModel);
            }
            cursor.close();
        }
        audioList.add(new AudioModel("Online Audio 1", "https://file-examples.com/index.php/sample-audio-files/sample-mp3-download/"));

        filteredList.addAll(audioList);
        adapter.notifyDataSetChanged();
    }

    private void filterAudioList(String query) {
        filteredList.clear();
        for (AudioModel audio : audioList) {
            if (audio.getTitle().toLowerCase().contains(query)) {
                filteredList.add(audio);
            }
        }
        adapter.setAudioList(filteredList);
        adapter.notifyDataSetChanged();
    }
}
