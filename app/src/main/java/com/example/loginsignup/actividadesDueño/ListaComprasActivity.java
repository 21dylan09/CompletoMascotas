package com.example.loginsignup.actividadesDueño;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import com.example.loginsignup.R;

public class ListaComprasActivity extends AppCompatActivity {

    private EditText etProducto;
    private Button btnAgregar;
    private ListView listViewProductos;
    private ArrayList<String> listaProductos;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_compras);

        etProducto = findViewById(R.id.etProducto);
        btnAgregar = findViewById(R.id.btnAgregar);
        listViewProductos = findViewById(R.id.listViewProductos);

        // Inicializar la lista de productos
        listaProductos = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaProductos);
        listViewProductos.setAdapter(adapter);

        // Evento para agregar producto
        btnAgregar.setOnClickListener(v -> agregarProducto());

        // Evento para eliminar producto al hacer clic en él
        listViewProductos.setOnItemClickListener((parent, view, position, id) -> eliminarProducto(position));
    }

    private void agregarProducto() {
        String producto = etProducto.getText().toString().trim();

        if (producto.isEmpty()) {
            Toast.makeText(this, "Ingrese un producto", Toast.LENGTH_SHORT).show();
            return;
        }

        listaProductos.add(producto);
        adapter.notifyDataSetChanged();
        etProducto.setText("");
    }

    private void eliminarProducto(int position) {
        listaProductos.remove(position);
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Producto eliminado", Toast.LENGTH_SHORT).show();
    }
}
