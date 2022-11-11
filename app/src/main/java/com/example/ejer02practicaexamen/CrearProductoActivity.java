package com.example.ejer02practicaexamen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ejer02practicaexamen.R;
import com.example.ejer02practicaexamen.databinding.ActivityCrearProductoBinding;
import com.example.ejer02practicaexamen.databinding.ActivityMainBinding;
import com.example.ejer02practicaexamen.modelos.Producto;

public class CrearProductoActivity extends AppCompatActivity {

    private ActivityCrearProductoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCrearProductoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        //RECUOERAR EL INTENT
        Intent intent = getIntent();
        //crear bundle
        Bundle bundle = new Bundle();

        Producto p = new Producto();



        binding.btnCancelarCrearProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        binding.btnCrearCrearProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createProducto(p);

                bundle.putSerializable("PRODUCT",p);
                intent.putExtras(bundle);

                setResult(RESULT_OK,intent);
                finish();


            }
        });

    }



    private void createProducto(Producto p) {


        p.setNombre(binding.txtNombreCrearProducto.getText().toString());
        p.setCantidad(Integer.parseInt(binding.txtCantidadCrearProducto.getText().toString()));
        p.setPrecio(Float.parseFloat(binding.txtPrecioCrearProducto.getText().toString()));
        p.setPrecioTotal(p.getCantidad()*p.getPrecio());


    }
}