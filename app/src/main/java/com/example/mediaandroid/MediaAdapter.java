package com.example.mediaandroid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.AudioViewHolder> {

    protected List<MediaModel> medias = new ArrayList<>();
    Context context;

    public MediaAdapter(Context context, List<MediaModel> audios){
        this.context = context;
        this.medias = audios;
    }

    @NonNull
    @Override
    public AudioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.audio_layout,parent,false);
        return new AudioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioViewHolder holder, @SuppressLint("RecyclerView") int position) {
        MediaModel mediaModel = medias.get(position);
        holder.titleText.setText(mediaModel.getTitle());
        holder.pathText.setText(mediaModel.getPath());

        ArrayList<String> audioPaths = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();
        for (MediaModel audio : medias) {
            audioPaths.add(audio.getPath());
            titles.add(audio.getTitle());
        }

        initCardView(holder, position, audioPaths, titles);

    }

    public void initCardView(@NotNull AudioViewHolder holder, int position, ArrayList<String> mediaPaths, ArrayList<String> titles) {
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AudioPlayer.class);
                // Pass the list of audio paths through the intent
                intent.putStringArrayListExtra("MediaPaths_list", mediaPaths);
                // Pass the selected position to start playing from that position
                intent.putExtra("SelectedPosition", position);
                intent.putStringArrayListExtra("Titles", titles);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return medias.size();
    }

    public void setAudioList(List<MediaModel> filteredList) {
        medias = filteredList;
    }

    static class AudioViewHolder extends RecyclerView.ViewHolder{

        TextView titleText,pathText;
        CardView cardView;

        public AudioViewHolder(@NonNull View itemView) {
            super(itemView);

            titleText = itemView.findViewById(R.id.title_tv);
            pathText = itemView.findViewById(R.id.path_tv);
            cardView = itemView.findViewById(R.id.card_view);

        }
    }
}