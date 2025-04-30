package com.example.spectacleapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spectacleapp.R;
import com.example.spectacleapp.dtos.LieuDTO;
import com.example.spectacleapp.dtos.SpectacleDateLieuDTO;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class DateLieuAdapter extends RecyclerView.Adapter<DateLieuAdapter.DateLieuViewHolder> {

    private List<SpectacleDateLieuDTO> datesLieux;
    private OnDateLieuClickListener listener;
    private int selectedPosition = -1;

    public List<SpectacleDateLieuDTO> getCurrentList() {
        return datesLieux;
    }

    public interface OnDateLieuClickListener {
        void onReservationClick(SpectacleDateLieuDTO dateLieu);
        void onLieuClick(String adresse); // Modifié pour recevoir directement l'adresse
        void onItemClick(SpectacleDateLieuDTO dateLieu);
    }

    public DateLieuAdapter(List<SpectacleDateLieuDTO> datesLieux, OnDateLieuClickListener listener) {
        this.datesLieux = datesLieux;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DateLieuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_date_lieu, parent, false);
        return new DateLieuViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull DateLieuViewHolder holder, int position) {
        SpectacleDateLieuDTO dateLieu = datesLieux.get(position);
        holder.bind(dateLieu);

        // Highlight selected item
        holder.itemView.setSelected(selectedPosition == position);

        // Handle item click
        holder.itemView.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION && listener != null) {
                listener.onItemClick(datesLieux.get(adapterPosition));
                selectedPosition = adapterPosition;
                notifyDataSetChanged();
            }
        });
    }

    @Override

    public int getItemCount() {
        return datesLieux != null ? datesLieux.size() : 0;
    }

    public void updateData(List<SpectacleDateLieuDTO> newDatesLieux) {
        this.datesLieux = newDatesLieux;
        notifyDataSetChanged();
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
        notifyDataSetChanged();
    }

    static class DateLieuViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvDateHeure;
        private final TextView tvLieu;
        private final TextView tvAdresse;
        private final TextView tvPrix;
        private final TextView tvPlacesRestantes;
        private final Button btnReserver;
        private final OnDateLieuClickListener listener;

        public DateLieuViewHolder(@NonNull View itemView, OnDateLieuClickListener listener) {
            super(itemView);
            this.listener = listener;
            tvDateHeure = itemView.findViewById(R.id.tv_date_heure);
            tvLieu = itemView.findViewById(R.id.tv_lieu);
            tvAdresse = itemView.findViewById(R.id.tv_adresse);
            tvPrix = itemView.findViewById(R.id.tv_prix);
            tvPlacesRestantes = itemView.findViewById(R.id.tv_places_restantes);
            btnReserver = itemView.findViewById(R.id.btn_reserver);
        }


        public void bind(SpectacleDateLieuDTO dateLieu) {
            // Formatage date et heure
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE d MMM yyyy", Locale.getDefault());
            String dateStr = dateFormat.format(dateLieu.getDate());
            String timeStr = formatHeure(dateLieu.getHeureDebut());
            tvDateHeure.setText(String.format("%s - %s", dateStr, timeStr));


            // Lieu et adresse
            if (dateLieu.getLieu() != null) {
                LieuDTO lieu = dateLieu.getLieu();
                tvLieu.setText(lieu.getNom());
                tvAdresse.setText(lieu.getAdresse());

                View.OnClickListener mapClickListener = v -> {
                    if (listener != null && lieu.getAdresse() != null) {
                        listener.onLieuClick(lieu.getAdresse());
                    }
                };

                tvLieu.setOnClickListener(mapClickListener);
                tvAdresse.setOnClickListener(mapClickListener);
            }

            // Prix et places
            tvPrix.setText(String.format("%d DT", dateLieu.getPrix()));
            tvPlacesRestantes.setText(String.format("%d places restantes", dateLieu.getPlacesDisponibles()));

            // Bouton Réserver
            btnReserver.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onReservationClick(dateLieu);
                }
            });
        }

        private String formatHeure(double heure) {
            int heures = (int) heure;
            int minutes = (int) ((heure - heures) * 60);
            return String.format(Locale.getDefault(), "%02dh%02d", heures, minutes);
        }
    }


}