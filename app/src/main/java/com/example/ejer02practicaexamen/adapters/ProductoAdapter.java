package com.example.ejer02practicaexamen.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsetsController;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ejer02practicaexamen.MainActivity;
import com.example.ejer02practicaexamen.R;
import com.example.ejer02practicaexamen.modelos.Producto;

import java.text.NumberFormat;
import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoVH> {

    private List<Producto> objects;
    private int resource;
    private Context context;
    private MainActivity mainActivity;

    private NumberFormat nf;

    public ProductoAdapter(List<Producto> objects, int resource, Context context) {
        this.objects = objects;
        this.resource = resource;
        this.context = context;
        this.nf = NumberFormat.getCurrencyInstance();
        mainActivity = (MainActivity) context;

        mainActivity.calculaaValoresFinales();
    }

    @NonNull
    @Override
    public ProductoVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //ESTE METODO SE LLAMA CADA VEZ QUE HAY QUE CREAR UNA NUEVA FILA
        //Dar valores
        View productoView = LayoutInflater.from(context).inflate(resource, null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        productoView.setLayoutParams(lp);
        return new ProductoVH(productoView);

    }

    @Override
    public void onBindViewHolder(@NonNull ProductoVH holder, int position) {


        Producto producto = objects.get(position); //obtener objto que tengo que monstrar en este momento
        holder.lblProducto.setText(producto.getNombre());
        holder.lblcantidad.setText(String.valueOf(producto.getCantidad()));
        holder.lblPrecio.setText(nf.format(producto.getPrecio()));


        //BORRAR
        holder.btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(context, ""+producto.getPrecioTotal(), Toast.LENGTH_SHORT).show();
                objects.remove(producto);
                notifyItemRemoved(holder.getAdapterPosition());
                mainActivity.calculaaValoresFinales();

                //MainActivity.actualizardatosLabel(producto);
            }
        });

        //LANZAR Alert Dialog EDITAR
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editarProducto(producto,holder.getAdapterPosition()).show();

            }
        });
    }

    private AlertDialog editarProducto( Producto producto, int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(producto.getNombre());
        builder.setCancelable(false);

        View contenido = LayoutInflater.from(context).inflate(R.layout.edit_producto_alert_dialog, null);
        builder.setView(contenido);

        TextView txtnombre = contenido.findViewById(R.id.txtNombreEditProducto);
        TextView txtPrecio = contenido.findViewById(R.id.txtPrecioEditProducto);
        TextView txtCantidad = contenido.findViewById(R.id.txtCantidadEditProducto);

        txtnombre.setText(producto.getNombre());
        txtPrecio.setText(String.valueOf(producto.getPrecio()));
        txtCantidad.setText(String.valueOf(producto.getCantidad()));

        builder.setNegativeButton("CANCEL",null);
        builder.setPositiveButton("EDIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                producto.setNombre(txtnombre.getText().toString());
                producto.setCantidad(Integer.parseInt(txtCantidad.getText().toString()));
                producto.setPrecio(Float.parseFloat(txtPrecio.getText().toString()));
                producto.setPrecioTotal(Integer.parseInt(txtCantidad.getText().toString())*Float.parseFloat(txtPrecio.getText().toString()));
                notifyItemChanged(position);
                mainActivity.calculaaValoresFinales();
                //ainActivity.actualizardatosLabel(producto);
            }
        });

        return builder.create();

    }

    @Override
    public int getItemCount() {
        return objects.size();
    }


    public class ProductoVH extends RecyclerView.ViewHolder{

        TextView lblProducto,lblcantidad,lblPrecio;
        ImageButton btnBorrar;
        public ProductoVH(@NonNull View itemView) {
            super(itemView);

            lblProducto = itemView.findViewById(R.id.txtProductoModelView);
            lblcantidad = itemView.findViewById(R.id.txtCantidadModelView);
            lblPrecio = itemView.findViewById(R.id.txtPrecioModelView);
            btnBorrar = itemView.findViewById(R.id.btnDeleteModelView);

        }
    }
}
