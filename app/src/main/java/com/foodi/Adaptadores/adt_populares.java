package com.foodi.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.foodi.R;

public class adt_populares  extends RecyclerView.Adapter<adt_populares.ViewHolder> {

    private String[] datos;
    public adt_populares(){this.datos = new String[]{"a","a","a","a","a","a","a","a"};}

    @NonNull
    @Override
    public adt_populares.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_tarjetas,null,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adt_populares.ViewHolder holder, int position) {
        try {holder.asignar_datos(datos[position]);}catch (Exception e){}
    }

    @Override
    public int getItemCount() {
        return datos.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtNombreplatillo, txtNombrerestaurante;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombreplatillo = (TextView) itemView.findViewById(R.id.txtNombrePlatillo);
            txtNombrerestaurante = (TextView) itemView.findViewById(R.id.txtNombrerestaurante);
        }

        public void asignar_datos(String valor){
            txtNombreplatillo.setText("Sopa de pato con carne");
            txtNombrerestaurante.setText("Las papas de Fredo");
        }
    }
}
