package com.foodi.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.foodi.Modelos.DetalleDenuncia;
import com.foodi.R;

import java.util.List;

public class adt_populares  extends RecyclerView.Adapter<adt_populares.ViewHolder> {

    private List<DetalleDenuncia> datos;

    public adt_populares(List<DetalleDenuncia> datos) {
        this.datos = datos;
    }

    @NonNull
    @Override
    public adt_populares.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_tarjetas,null,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adt_populares.ViewHolder holder, int position) {
        try {holder.asignar_datos(datos.get(position));}catch (Exception e){}
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtTitulo, txtEstado, txtNombre, txtFecha;

        ImageView ivImagen;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitulo = (TextView) itemView.findViewById(R.id.txtTitulo);
            txtEstado = (TextView) itemView.findViewById(R.id.txtTipo);
            txtNombre = (TextView) itemView.findViewById(R.id.txtNombre);
            txtFecha = (TextView) itemView.findViewById(R.id.txtFecha);

            ivImagen = (ImageView) itemView.findViewById(R.id.ivImagen);
        }

        public void asignar_datos(DetalleDenuncia detalle){
        //    txtTitulo.setText(detalle.getIdDenuncia().getTitulo());
           // txtEstado.setText(detalle.getIdDenuncia().get);
        }
    }
}
