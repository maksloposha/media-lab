package com.example.mediaandroid;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.List;

public class VideoPlayer extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_players);

        List<String> videoPaths = getIntent().getStringArrayListExtra("MediaPaths_list");
        int currentPosition = getIntent().getIntExtra("SelectedPosition", 0);
        TextView title = findViewById(R.id.videoTitle);
        title.setText(getIntent().getStringArrayListExtra("Titles").get(currentPosition));

        mediaPlayer = new MediaPlayer();
        videoView = findViewById(R.id.video_view);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.e("MediaPlayerError", "MediaPlayer error occurred: " + what + ", Extra: " + extra);
                return false;
            }
        });

        initializeMediaPlayer(videoPaths.get(currentPosition));

    }


    private void initializeMediaPlayer(String path) {
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    // Set the media player for the video view
                    MediaController mediaController = new MediaController(VideoPlayer.this);
                    videoView.setMediaController(mediaController);

                    // Set the video URI for the VideoView
                    videoView.setVideoURI(Uri.parse(path));

                    // Start video playback
                    videoView.start();
                }
            });
        } catch (IOException e) {
            Log.e("VideoPlayerException", "Error initializing MediaPlayer: " + e.getMessage());
        }
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mediaPlayer.stop();
        finish();
    }

}
