package com.example.mediaandroid;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AudioListActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_ADD_ONLINE_AUDIO = 1001;
    List<MediaModel> audioList = new ArrayList<>();
    List<MediaModel> filteredList = new ArrayList<>();
    RecyclerView recyclerView;
    MediaAdapter adapter;
    EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_list_activity);

        recyclerView = findViewById(R.id.recyclerView);
        searchEditText = findViewById(R.id.searchEditText);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MediaAdapter(this, audioList);
        recyclerView.setAdapter(adapter);

        getAudioFiles();

        findViewById(R.id.buttonAddOnlineAudio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AudioListActivity.this, AddOnlineAudioActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ADD_ONLINE_AUDIO);
            }
        });

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
                MediaModel mediaModel = new MediaModel(title, path);
                audioList.add(mediaModel);
            }
            cursor.close();
        }

        Uri internalUri = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
        cursor = contentResolver.query(internalUri, projection, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                MediaModel mediaModel = new MediaModel(title, path);
                audioList.add(mediaModel);
            }
            cursor.close();
        }

        filteredList.addAll(audioList);
        adapter.notifyDataSetChanged();
    }

    private void filterAudioList(String query) {
        filteredList.clear();
        for (MediaModel audio : audioList) {
            if (audio.getTitle().toLowerCase().contains(query)) {
                filteredList.add(audio);
            }
        }
        adapter.setAudioList(filteredList);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ADD_ONLINE_AUDIO && resultCode == RESULT_OK) {
            // Retrieve added online audio from the result intent
            String title = data.getStringExtra("title");
            String url = data.getStringExtra("url");
            audioList.add(new MediaModel(title,url));
        }
    }
}
