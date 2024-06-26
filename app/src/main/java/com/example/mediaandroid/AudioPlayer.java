package com.example.mediaandroid;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AudioPlayer extends AppCompatActivity {

    TextView playerpos, playerdur;

    SeekBar seekBar;

    private List<String> audioPaths;
    private List<String> titles;
    private int currentPosition;

    ImageView rewind, play, pause, forward, play_next, play_previous;

    MediaPlayer mediaPlayer;
    Handler handler;
    Runnable runnable;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);

        // Retrieve the list of audio paths and selected position from the intent
        audioPaths = getIntent().getStringArrayListExtra("MediaPaths_list");
        currentPosition = getIntent().getIntExtra("SelectedPosition", 0);
        title = findViewById(R.id.titleAudio);
        titles = getIntent().getStringArrayListExtra("Titles");
        title.setText(titles.get(currentPosition));

        playerpos = findViewById(R.id.playerPosition);
        playerdur = findViewById(R.id.playerDuration);
        seekBar = findViewById(R.id.seek_bar);
        rewind = findViewById(R.id.btn_rew);
        pause = findViewById(R.id.btn_pause);
        play = findViewById(R.id.btn_ply);
        forward = findViewById(R.id.btn_frwd);
        play_next = findViewById(R.id.btn_next);
        play_previous = findViewById(R.id.btn_previous);

        mediaPlayer = new MediaPlayer();

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    int duration = mediaPlayer.getDuration();
                    playerpos.setText(convertFormat(currentPosition));
                    playerdur.setText(convertFormat(duration));
                    seekBar.setProgress(currentPosition);
                }
                handler.postDelayed(this, 500);
            }
        };


        // Initialize MediaPlayer
        mediaPlayer = new MediaPlayer();

        // Initialize and start playback using the first audio path
        initializeMediaPlayer(audioPaths.get(currentPosition));
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play.setVisibility(View.GONE);
                pause.setVisibility(View.VISIBLE);
                mediaPlayer.start();
                seekBar.setMax(mediaPlayer.getDuration());
                handler.postDelayed(runnable, 0);
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pause.setVisibility(View.GONE);
                play.setVisibility(View.VISIBLE);
                mediaPlayer.pause();
                handler.removeCallbacks(runnable);
            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPos = mediaPlayer.getCurrentPosition();
                int duration = mediaPlayer.getDuration();
                if (mediaPlayer.isPlaying() && duration != currentPos) ;
                currentPos = currentPos + 5000;
                playerpos.setText(convertFormat(currentPos));
                mediaPlayer.seekTo(currentPos);

            }
        });

        rewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPos = mediaPlayer.getCurrentPosition();

                if (mediaPlayer.isPlaying() && currentPos > 5000) {
                    currentPos = currentPos - 5000;
                    playerpos.setText(convertFormat(currentPos));
                    mediaPlayer.seekTo(currentPos);
                }

            }
        });

        play_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playNextAudio();
            }
        });

        play_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playPreviousAudio();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    mediaPlayer.seekTo(i);
                }
                playerpos.setText(convertFormat(mediaPlayer.getCurrentPosition()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    // Method to play the next audio
    private void playNextAudio() {
        if (currentPosition < audioPaths.size() - 1) {
            currentPosition++;
            stopAudio();
            initializeMediaPlayer(audioPaths.get(currentPosition));
            playAudio();
            title.setText(titles.get(currentPosition));
        } else {
            currentPosition = 0;
            if (!audioPaths.isEmpty()) {
                stopAudio();
                initializeMediaPlayer(audioPaths.get(currentPosition));
                playAudio();
                title.setText(titles.get(currentPosition));
            }
        }
    }

    // Method to play the previous audio
    private void playPreviousAudio() {
        if (currentPosition > 0) {
            currentPosition--;
        }else{
            currentPosition = audioPaths.size() - 1;
        }
        title.setText(titles.get(currentPosition));
        stopAudio();
        initializeMediaPlayer(audioPaths.get(currentPosition));
        playAudio();
    }

    // Method to stop the currently playing audio
    private void stopAudio() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
    }

    private void initializeMediaPlayer(String path) {
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepareAsync(); // Use prepareAsync for asynchronous preparation

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    int duration = mediaPlayer.getDuration();
                    String sDuration = convertFormat(duration);
                    playerdur.setText(sDuration);
                    playAudio(); // Start playback after successful preparation
                }
            });
        } catch (IOException e) {
            Log.e("AudioPlayerException", "Error initializing MediaPlayer: " + e.getMessage());
        }
    }


    // Method to start playing audio
    private void playAudio() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
            play.setVisibility(View.GONE);
            pause.setVisibility(View.VISIBLE);
            int duration = mediaPlayer.getDuration();
            playerdur.setText(convertFormat(duration));
            seekBar.setMax(mediaPlayer.getDuration());
            handler.postDelayed(runnable, 0);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mediaPlayer.stop();
        finish();
    }

    @SuppressLint("DefaultLocale")
    private String convertFormat(int duration) {
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(minutes);
        return String.format("%02d:%02d", minutes, seconds);
    }

}