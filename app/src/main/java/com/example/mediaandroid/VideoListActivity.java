package com.example.mediaandroid;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class VideoListActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_ADD_ONLINE_VIDEO = 1001;
    public static final int REQUEST_MEDIA_PERMISSION = 3;

    List<MediaModel> videoList = new ArrayList<>();
    List<MediaModel> filteredList = new ArrayList<>();
    RecyclerView recyclerView;
    MediaAdapter adapter;
    EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_list_activity);
        checkPermission();

        recyclerView = findViewById(R.id.recyclerView);
        searchEditText = findViewById(R.id.searchEditText);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new VideoAdapter(this, videoList);
        recyclerView.setAdapter(adapter);


        findViewById(R.id.buttonAddOnlineAudio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideoListActivity.this, AddOnlineAudioActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ADD_ONLINE_VIDEO);
            }
        });

        // Search functionality
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String query = editable.toString().toLowerCase();
                filterVideoList(query);
            }
        });
    }

    private void getVideoFiles() {
        ContentResolver contentResolver = getContentResolver();
        Uri videoUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Video.Media.TITLE, MediaStore.Video.Media.DATA};
        Cursor cursor = contentResolver.query(videoUri, projection, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                MediaModel mediaModel = new MediaModel(title, path);
                videoList.add(mediaModel);
            }
            cursor.close();
        }
    }

    private void filterVideoList(String query) {
        filteredList.clear();
        for (MediaModel audio : videoList) {
            if (audio.getTitle().toLowerCase().contains(query)) {
                filteredList.add(audio);
            }
        }
        adapter.setAudioList(filteredList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ADD_ONLINE_VIDEO && resultCode == RESULT_OK) {
            // Retrieve added online audio from the result intent
            String title = data.getStringExtra("title");
            String url = data.getStringExtra("url");
            videoList.add(new MediaModel(title, url));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_MEDIA_VIDEO) ==
                PackageManager.PERMISSION_GRANTED) {
            getVideoFiles();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_MEDIA_VIDEO},
                    REQUEST_MEDIA_PERMISSION);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_MEDIA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getVideoFiles();
            } else {
                getVideoFiles();
            }
        }

    }
}
