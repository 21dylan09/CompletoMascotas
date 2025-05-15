package com.example.loginsignup.actividadesCuidador.adapters;



import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginsignup.R;
import com.example.loginsignup.baseDatos.entidades.UbicacionCuidador;

import java.util.List;

public class CuidadorAdapter extends RecyclerView.Adapter<CuidadorAdapter.ViewHolder> {

    private List<UbicacionCuidador> lista;
    private Context context;

    public CuidadorAdapter(List<UbicacionCuidador> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cuidador, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UbicacionCuidador cuidador = lista.get(position);

        holder.tvDireccion.setText("Dirección: " + cuidador.direccionTexto);
        holder.tvTelefono.setText("Teléfono: " + cuidador.telefono);

        holder.btnSMS.setOnClickListener(v -> {
            String numero = cuidador.telefono;
            String mensaje = "Hola, estoy interesado en tus servicios como cuidador de mascotas.";

            Intent smsIntent = new Intent(Intent.ACTION_VIEW);
            smsIntent.setData(Uri.parse("smsto:" + numero));
            smsIntent.putExtra("sms_body", mensaje);
            context.startActivity(smsIntent);
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDireccion, tvTelefono;
        Button btnSMS;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDireccion = itemView.findViewById(R.id.tvDireccion);
            tvTelefono = itemView.findViewById(R.id.tvTelefono);
            btnSMS = itemView.findViewById(R.id.btnSMS);
        }
    }
}

