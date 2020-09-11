package com.foodi;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.foodi.Clases.GlobalClass;
import com.foodi.WSSoap.SOAPWork;
import com.foodi.WebServices.Asynchtask;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.foodi.Clases.clsUtilitarios.IP_SERVIDOR;
import static com.foodi.Clases.clsUtilitarios.PUERTO;
import static com.foodi.Clases.clsUtilitarios.denuncia_selec;

public class actDetalleAdmin extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, Asynchtask {

    GoogleMap Mapa;
    TextView txtTituloDetalle, txtUsuarioDetalle, txtFechaDetalle, txtPuntajeDetalle, txtPuntajeDetalle2, txtDescripcionDetalle, txtAtendidaDetalle, txtTipoDetalle;
    ImageView ivFotoDetalle, ivPerfilDetalle;
    int proceso;
    int id_denuncia;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_detalle_admin);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapA);
        mapFragment.getMapAsync(this);
        txtTituloDetalle = findViewById(R.id.txtTituloDetalleA);
        txtUsuarioDetalle = findViewById(R.id.txtUsuarioDetalleA);
        txtFechaDetalle = findViewById(R.id.txtFechaDetalleA);
        txtPuntajeDetalle = findViewById(R.id.txtPuntajeDetalleA);
        txtPuntajeDetalle2 = findViewById(R.id.txtPuntajeDetalle2A);
        txtDescripcionDetalle = findViewById(R.id.txtDescripcionDetalleA);
        txtAtendidaDetalle = findViewById(R.id.txtAtentidaDetalleA);
        txtTipoDetalle = findViewById(R.id.txtTipoDetalleA);
        ivFotoDetalle = findViewById(R.id.ivFotoDetalleA);
        ivPerfilDetalle = findViewById(R.id.ivPerfilDetalleA);
        String fecha = denuncia_selec.getIdDenuncia().getFecha();
        fecha = fecha.substring(0, 10);
        id_denuncia = Integer.parseInt(denuncia_selec.getIdDenuncia().getIdDenuncia());

        txtTipoDetalle.setText(denuncia_selec.getIdDenuncia().getTipo());
        txtUsuarioDetalle.setText(denuncia_selec.getIdDenuncia().getIdUsuario().getNombres() + " " + denuncia_selec.getIdDenuncia().getIdUsuario().getApellidos());
        txtFechaDetalle.setText(fecha);
        txtAtendidaDetalle.setText(denuncia_selec.getIdDenuncia().getAtendida());
        txtDescripcionDetalle.setText(denuncia_selec.getIdDenuncia().getDetalles());
        txtTituloDetalle.setText(denuncia_selec.getIdDenuncia().getTitulo());
        if (denuncia_selec.getImagen() != null) {
            try {
                byte[] byteArrray = new byte[0];
                byteArrray = Base64.getDecoder().decode(new String(denuncia_selec.getImagen()).getBytes("UTF-8"));
                Bitmap bitm = BitmapFactory.decodeByteArray(byteArrray, 0, byteArrray.length);
                ivFotoDetalle.setImageBitmap(bitm);
                if (denuncia_selec.getIdDenuncia().getIdUsuario().getImagen() != null) {
                    byteArrray = new byte[0];
                    byteArrray = Base64.getDecoder().decode(new String(denuncia_selec.getIdDenuncia().getIdUsuario().getImagen()).getBytes("UTF-8"));
                    bitm = BitmapFactory.decodeByteArray(byteArrray, 0, byteArrray.length);
                    ivPerfilDetalle.setImageBitmap(bitm);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }


        //Carga de los puntajes
        Map<String, String> map = new LinkedHashMap<>();
        try {
            map.put("sentencia", "select consultar_puntos('" + id_denuncia + "')");
            SOAPWork dd = new SOAPWork("http://" + IP_SERVIDOR + ":" + PUERTO + "/Denunciasqvd_srv/ws_Procesar?WSDL", map, this, this);
            dd.setMethod_name("consultar");
            dd.execute();
            proceso = 1;
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error, " + e.toString(), Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Mapa = googleMap;
        Mapa.getUiSettings().setZoomControlsEnabled(true);
        Mapa.setOnMapClickListener(this);
        //CameraUpdate camUpd1 = CameraUpdateFactory.newLatLngZoom(new LatLng(-1.012542, -79.469177), 17);
        //Mapa.moveCamera(camUpd1);
        CameraUpdate camUpd1 = CameraUpdateFactory.newLatLngZoom(new LatLng(Float.parseFloat(denuncia_selec.getIdDenuncia().getLatitud()), Float.parseFloat(denuncia_selec.getIdDenuncia().getLongitud())), 17);
        Mapa.moveCamera(camUpd1);
        float latitud = Float.parseFloat(denuncia_selec.getIdDenuncia().getLatitud());
        float longitud = Float.parseFloat(denuncia_selec.getIdDenuncia().getLongitud());
        Mapa.addMarker(new MarkerOptions().position(new LatLng(Float.parseFloat(denuncia_selec.getIdDenuncia().getLatitud()), Float.parseFloat(denuncia_selec.getIdDenuncia().getLongitud()))).title(denuncia_selec.getIdDenuncia().getTitulo()));
    }

    public void cambiarVista(View view) {
        if (Mapa.getMapType() == GoogleMap.MAP_TYPE_NORMAL) {
            Mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        } else {
            Mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
    }

    public void aceptarDenuncia(View view) {

        Map<String, String> map = new LinkedHashMap<>();
        try {
            GlobalClass globalclass = (GlobalClass) getApplicationContext();
            map.put("sentencia", "update denuncia set atendida = 'Atendida' where id_denuncia = '" + id_denuncia + "'");
            SOAPWork dd = new SOAPWork("http://" + IP_SERVIDOR + ":8080/Denunciasqvd_srv/ws_Procesar?WSDL", map, this, this);
            dd.setMethod_name("procesar");
            dd.execute();
            denuncia_selec.getIdDenuncia().setAtendida("Atendida");
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error, " + e.toString(), Toast.LENGTH_LONG).show();
        }
        proceso = 2;
    }

    public void rechazarDenuncia(View view) {

        Map<String, String> map = new LinkedHashMap<>();
        try {
            GlobalClass globalclass = (GlobalClass) getApplicationContext();
            map.put("sentencia", "update denuncia set atendida = 'No atendida' where id_denuncia = '" + id_denuncia + "'");
            SOAPWork dd = new SOAPWork("http://" + IP_SERVIDOR + ":8080/Denunciasqvd_srv/ws_Procesar?WSDL", map, this, this);
            dd.setMethod_name("procesar");
            dd.execute();
            denuncia_selec.getIdDenuncia().setAtendida("No atendida");
            proceso = 2;
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error, " + e.toString(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void processFinish(String result) throws JSONException {
        if (proceso == 1) {
            String[] resultados = result.split("_");
            txtPuntajeDetalle.setText(resultados[0]);
            txtPuntajeDetalle2.setText(resultados[1]);
        }
        if (proceso == 2) {
            finish();
            startActivity(getIntent());
        }
    }
}