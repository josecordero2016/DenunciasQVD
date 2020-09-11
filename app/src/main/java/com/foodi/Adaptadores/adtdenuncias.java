package com.foodi.Adaptadores;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.foodi.Modelos.DetalleDenuncia;
import com.foodi.R;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

public class adtdenuncias extends RecyclerView.Adapter<adtdenuncias.ViewHolder> {

    private List<DetalleDenuncia> datos;
    private OnNoteListener onNoteListenera;

    public adtdenuncias(List<DetalleDenuncia> datos, OnNoteListener onNoteListener) {
        this.datos = datos;
        this.onNoteListenera=onNoteListener;
    }

    @NonNull
    @Override
    public adtdenuncias.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_tarjetas,null,false);
        return new ViewHolder(view,onNoteListenera);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull adtdenuncias.ViewHolder holder, int position) {
        try {holder.asignar_datos(datos.get(position));}catch (Exception e){}
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView txtTitulo, txtEstado, txtNombre, txtFecha,txt;
        ImageView ivImagen;
        OnNoteListener onNoteListener;

        public ViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            txtTitulo = (TextView) itemView.findViewById(R.id.txtTitulo);
            txtEstado = (TextView) itemView.findViewById(R.id.txtTipo);
            txtNombre = (TextView) itemView.findViewById(R.id.txtNombre);
            txtFecha = (TextView) itemView.findViewById(R.id.txtFecha);
            ivImagen = (ImageView) itemView.findViewById(R.id.ivImagen);
            this.onNoteListener=onNoteListener;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void asignar_datos(DetalleDenuncia detalle) throws UnsupportedEncodingException {
            txtTitulo.setText(detalle.getIdDenuncia().getTitulo());
            txtEstado.setText(detalle.getIdDenuncia().getTipo());
            txtNombre.setText(detalle.getIdDenuncia().getIdUsuario().getNombres()+" "+detalle.getIdDenuncia().getIdUsuario().getApellidos());
            txtFecha.setText(detalle.getIdDenuncia().getFecha());
            try{
            byte[] byteArrray = Base64.getDecoder().decode(new String(detalle.getImagen()).getBytes("UTF-8"));
            Bitmap bitm = BitmapFactory.decodeByteArray(byteArrray, 0, byteArrray.length);
            ivImagen.setImageBitmap(bitm);
            }
            catch (Exception e){}
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }
    public interface OnNoteListener{
        void onNoteClick(int position);
    }
}
