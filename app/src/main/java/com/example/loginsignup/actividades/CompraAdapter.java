package com.example.loginsignup.actividades;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CompraAdapter extends RecyclerView.Adapter<CompraAdapter.ViewHolder> {
    private List<String> listaCompras;

    public CompraAdapter(List<String> listaCompras) {
        this.listaCompras = listaCompras;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String item = listaCompras.get(position);
        holder.tvItem.setText(item);

        // Eliminar elemento al hacer clic en el botón
        holder.btnEliminar.setOnClickListener(v -> {
            listaCompras.remove(position);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return listaCompras.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvItem;
        Button btnEliminar;

        public ViewHolder(View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(android.R.id.text1);
            btnEliminar = new Button(itemView.getContext());
            btnEliminar.setText("Eliminar");
            ((ViewGroup) itemView).addView(btnEliminar);
        }
    }
}
