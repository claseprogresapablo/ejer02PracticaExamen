package com.example.ejer02practicaexamen;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Toast;

import com.example.ejer02practicaexamen.adapters.ProductoAdapter;
import com.example.ejer02practicaexamen.databinding.ActivityMainBinding;
import com.example.ejer02practicaexamen.modelos.Producto;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private List<Producto> productosList;
    private ProductoAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    //ActivityResultLaunchers
    private ActivityResultLauncher<Intent> launcherCrearProducto;
    private NumberFormat numberFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);



        productosList = new ArrayList<>();
        numberFormat = NumberFormat.getCurrencyInstance();
        calculaaValoresFinales();

        int columnas;
        //HORIZONTAL -> 2
        //VERTICAL -> 1
        //DESDE LAS CONFIGURACIONES DE LA ACTIVIDAD -> orientation // PORTRAIT(V) / LANDSCAPE(H)
        columnas = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? 1 : 2; //OPERADOR TERNARIO
        //linkear main con adapter(no se dice así pero me aclaro)
        adapter = new ProductoAdapter(productosList,R.layout.productos_model_view,MainActivity.this);
        binding.contentMain.contenedor.setAdapter(adapter);
        layoutManager = new GridLayoutManager(MainActivity.this, columnas);
        binding.contentMain.contenedor.setLayoutManager(layoutManager);


        inicializaLaunchers();


        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(MainActivity.this, CrearProductoActivity.class);
                launcherCrearProducto.launch(intent);




            }
        });
    }

    private void inicializaLaunchers() {

        launcherCrearProducto = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK){
                            if (result.getData()!=null && result.getData().getExtras() != null){


                                Bundle bundle = result.getData().getExtras();
                                //sacar del bundle la info segun el tag
                                Producto p = (Producto) bundle.getSerializable("PRODUCT");
                                productosList.add(p);
                                adapter.notifyItemInserted(productosList.size()-1);
                                calculaaValoresFinales();



                            }
                        } else {
                            Toast.makeText(MainActivity.this, "ACCION CANCELADA", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }




    /**
     * Se dispara ANTES de que se elimine la actividad
     * @param outState -> guardo los datos
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("LISTA", (Serializable) productosList);


    }

    /**
     * Se dispara despues de crear la actividad nueva
     * @param savedInstanceState -> recupero los datos
     */
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ArrayList<Producto> tem = (ArrayList<Producto>) savedInstanceState.getSerializable("LISTA");
        calculaaValoresFinales();
        int size = savedInstanceState.getInt("NUMELE");
        Float total = savedInstanceState.getFloat("TOTAL");
        binding.contentMain.lblCantidadProductosMain.setText(String.valueOf("Cantidad Total: "+size));
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        binding.contentMain.lblDineroTotalMainActivity.setText("Dinero Total: "+numberFormat.format(total));
        productosList.addAll(tem);
        adapter.notifyItemRangeInserted(0,productosList.size());
    }


    public void calculaaValoresFinales(){

        int cantidadTotal = 0;
        float importeTotal = 0;

        for (Producto p:productosList) {

            cantidadTotal+= p.getCantidad();
            importeTotal+= p.getPrecio() * p.getCantidad();

        }


        binding.contentMain.lblCantidadProductosMain.setText(String.valueOf("Cantidad Total: "+cantidadTotal));

        binding.contentMain.lblDineroTotalMainActivity.setText("Dinero Total: "+numberFormat.format(importeTotal));

    }


}