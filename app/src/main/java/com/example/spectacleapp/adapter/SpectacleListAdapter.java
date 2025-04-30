package com.example.spectacleapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spectacleapp.R;
import com.example.spectacleapp.activities.DetailSpectacleActivity;
import com.example.spectacleapp.dtos.SpectacleDTO;

import java.util.List;

public class SpectacleListAdapter extends RecyclerView.Adapter<SpectacleListAdapter.SpectacleViewHolder> {

    private final LayoutInflater mInflater;
    private List<SpectacleDTO> mSpectacles;
    private final Context context;
    private final int defaultImageResId;

    public SpectacleListAdapter(Context context, List<SpectacleDTO> spectacles) {
        this.mInflater = LayoutInflater.from(context);
        this.mSpectacles = spectacles;
        this.context = context;
        this.defaultImageResId = R.drawable.default_image;
    }

    public void setSpectacles(List<SpectacleDTO> spectacles) {
        this.mSpectacles = spectacles;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SpectacleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.spectacle_item, parent, false);
        return new SpectacleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SpectacleViewHolder holder, int position) {
        if (mSpectacles != null && position >= 0 && position < mSpectacles.size()) {
            holder.bind(mSpectacles.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mSpectacles != null ? mSpectacles.size() : 0;
    }

    class SpectacleViewHolder extends RecyclerView.ViewHolder {
        private final TextView titreItemView;
        private final ImageView imageItemView;
        private final TextView nbDatesView;

        SpectacleViewHolder(View itemView) {
            super(itemView);
            titreItemView = itemView.findViewById(R.id.titre);
            imageItemView = itemView.findViewById(R.id.image);
            nbDatesView = itemView.findViewById(R.id.nb_dates);
        }

        void bind(SpectacleDTO spectacle) {
            // Titre
            titreItemView.setText(spectacle.getTitre());

            // Image
            loadImage(spectacle.getImageUrl());

            // Dates disponibles
            if (spectacle.getDatesLieux() != null && !spectacle.getDatesLieux().isEmpty()) {
                nbDatesView.setText(spectacle.getDatesLieux().size() + " dates disponibles");
            } else {
                nbDatesView.setText("Aucune date programmée");
            }

            // Gestion du clic
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && mSpectacles != null) {
                    SpectacleDTO selectedSpectacle = mSpectacles.get(position);
                    Intent intent = new Intent(context, DetailSpectacleActivity.class);
                    intent.putExtra("SPECTACLE_ID", selectedSpectacle.getId());
                    intent.putExtra("SPECTACLE_TITRE", selectedSpectacle.getTitre());
                    context.startActivity(intent);
                }
            });
        }

        private void loadImage(String imageUrl) {
            try {
                if (imageUrl == null || imageUrl.isEmpty()) {
                    imageItemView.setImageResource(defaultImageResId);
                    return;
                }

                String imageName = getImageNameFromUrl(imageUrl);
                int resId = context.getResources().getIdentifier(
                        imageName, "drawable", context.getPackageName());

                imageItemView.setImageResource(resId != 0 ? resId : defaultImageResId);
            } catch (Exception e) {
                imageItemView.setImageResource(defaultImageResId);
            }
        }

        private String getImageNameFromUrl(String url) {
            try {
                String filename = url.substring(url.lastIndexOf('/') + 1);
                return filename.split("\\.")[0].toLowerCase().replace("-", "_");
            } catch (Exception e) {
                return "";
            }
        }
    }
}