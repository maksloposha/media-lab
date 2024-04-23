package com.example.mediaandroid;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class VideoAdapter extends MediaAdapter {
    public VideoAdapter(Context context, List<MediaModel> audios) {
        super(context, audios);
    }

    @Override
    public void initCardView(@NotNull AudioViewHolder holder, int position, ArrayList<String> mediaPaths, ArrayList<String> titles) {
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, VideoPlayer.class);
                // Pass the list of audio paths through the intent
                intent.putStringArrayListExtra("MediaPaths_list", mediaPaths);
                // Pass the selected position to start playing from that position
                intent.putExtra("SelectedPosition", position);
                intent.putStringArrayListExtra("Titles", titles);
                context.startActivity(intent);
            }
        });
    }


    @NonNull
    @NotNull
    @Override
    public AudioViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_layout, parent, false);
        return new AudioViewHolder(view);
    }
}
