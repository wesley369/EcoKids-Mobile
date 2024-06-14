package com.example.ecokids;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder> {

    private int[] characterImages;

    public CharacterAdapter(int[] characterImages) {
        this.characterImages = characterImages;
    }

    @NonNull
    @Override
    public CharacterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_character, parent, false);
        return new CharacterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CharacterViewHolder holder, int position) {
        int imageRes = characterImages[position];
        holder.imageView.setImageResource(imageRes);
    }

    @Override
    public int getItemCount() {
        return characterImages.length;
    }

    static class CharacterViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public CharacterViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);

        }
    }
}
