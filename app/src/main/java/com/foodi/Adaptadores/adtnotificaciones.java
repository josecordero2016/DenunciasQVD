package com.foodi.Adaptadores;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.foodi.Modelos.DetalleDenuncia;
import com.foodi.R;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.List;

public class adtnotificaciones extends RecyclerView.Adapter<adtnotificaciones.ViewHolder> {

    private List<DetalleDenuncia> datos;

    public adtnotificaciones(List<DetalleDenuncia> datos) {
        this.datos = datos;
    }

    @NonNull
    @Override
    public adtnotificaciones.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_notificacion,null,false);
        return new adtnotificaciones.ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull adtnotificaciones.ViewHolder holder, int position) {
        try {holder.asignar_datos(datos.get(position));}catch (Exception e){}
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView txtNombreUserDenunciaNotifi, txtTipoDenunciaNotifi, txtFechaDenunciaNotifi;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombreUserDenunciaNotifi = (TextView) itemView.findViewById(R.id.txtNombreUserDenunciaNotifi);
            txtFechaDenunciaNotifi = (TextView) itemView.findViewById(R.id.txtFechaDenunciaNotifi);
            txtTipoDenunciaNotifi = (TextView) itemView.findViewById(R.id.txtTipoDenunciaNotifi);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void asignar_datos(DetalleDenuncia detalle) throws UnsupportedEncodingException {

        }
        @Override
        public void onClick(View view) {

        }
    }

}