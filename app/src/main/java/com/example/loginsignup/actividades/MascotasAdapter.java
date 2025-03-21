package com.example.loginsignup.actividades;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import com.example.loginsignup.R;
import com.example.loginsignup.baseDatos.entidades.Mascota;

import java.util.List;

public class MascotasAdapter extends RecyclerView.Adapter<MascotasAdapter.MascotaViewHolder> {

    private List<Mascota> mascotas;
    private OnMascotaClickListener listener;

    public MascotasAdapter(List<Mascota> mascotas, OnMascotaClickListener listener) {
        this.mascotas = mascotas;
        this.listener = listener;
    }

    @Override
    public MascotaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mascota, parent, false);
        return new MascotaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MascotaViewHolder holder, int position) {
        Mascota mascota = mascotas.get(position);
        holder.buttonMascota.setText(mascota.getNombre());
        holder.buttonMascota.setOnClickListener(v -> listener.onMascotaClick(mascota));
    }

    @Override
    public int getItemCount() {
        return mascotas.size();
    }

    public static class MascotaViewHolder extends RecyclerView.ViewHolder {
        Button buttonMascota;

        public MascotaViewHolder(View itemView) {
            super(itemView);
            buttonMascota = itemView.findViewById(R.id.buttonMascota);
        }
    }

    public interface OnMascotaClickListener {
        void onMascotaClick(Mascota mascota);
    }
}
