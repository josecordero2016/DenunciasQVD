package com.foodi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.foodi.Clases.GlobalClass;
import com.foodi.Clases.clsConexionBd;
import com.foodi.Clases.clsUtilitarios;
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

import java.sql.PreparedStatement;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.Map;


public class RegistroActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener , GoogleMap.OnMyLocationButtonClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map_usuarioReg);
        mapFragment.getMapAsync(this);
    }


    GoogleMap Mapa;

    Float latitud=(float)-1.022820, longitud=(float)-79.463368;
    LatLng pos = new LatLng(latitud, longitud);
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Mapa=googleMap;
        CameraUpdate camUpd1 = CameraUpdateFactory.newLatLngZoom(new LatLng(latitud, longitud), 13);
        Mapa.addMarker(new MarkerOptions().position(pos).title("Ubicación"));
        Mapa.moveCamera(camUpd1);
        Mapa.setOnMapClickListener(this);
        Mapa.setOnMyLocationButtonClickListener(this);
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
            return;

        Mapa.setMyLocationEnabled(true);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Mapa.clear();
        Mapa.addMarker(new MarkerOptions().position(latLng).title("Ubicación"));
        DecimalFormat df = new DecimalFormat("#.00000");

        latitud=Float.parseFloat(df.format(latLng.latitude));
        longitud=Float.parseFloat(df.format(latLng.longitude));
    }



    public void registrarse(View view){
        EditText nombres= findViewById(R.id.txtNombreReg);
        EditText apellidos= findViewById(R.id.txtApellidoReg);
        EditText telefono= findViewById(R.id.txtTelefonoReg);
        EditText correo= findViewById(R.id.txtCorreoReg);
        EditText nomb_usuario= findViewById(R.id.NombreUsuarioReg);
        EditText clave1= findViewById(R.id.txtContraseReg);
        EditText clave2= findViewById(R.id.txtContraseReg2);
        ImageView perfil=findViewById(R.id.perfil_usuarioReg);

        clsUtilitarios utili = new clsUtilitarios();

        String cons="";

        if(!nombres.getText().toString().equals("") && !apellidos.getText().toString().equals("") && !telefono.getText().toString().equals("") && !correo.getText().equals("") && !latitud.equals("") && !longitud.equals("") && !clave1.getText().toString().equals("")&& !clave2.getText().toString().equals("") && !nomb_usuario.getText().toString().equals("")){

            if(clave1.getText().toString().equals(clave2.getText().toString()))
                cons="select registrar_ciudadano(?,?,?,?,?,?,?,?,?)";
            else
            {
                Toast.makeText(this, "Las contraseñas no coinciden",Toast.LENGTH_LONG).show();
            }
            try
            {
                byte[] imagenPerfil=utili.imagenAbyte(perfil);
                clsConexionBd con = new clsConexionBd();
                PreparedStatement ps = con.connection.prepareStatement(cons);
                ps.setString(1,nombres.getText().toString());
                ps.setString(2, apellidos.getText().toString());
                ps.setString(3, telefono.getText().toString());
                ps.setString(4, String.valueOf(latitud));
                ps.setString(5, String.valueOf(longitud));
                ps.setString(6, nomb_usuario.getText().toString());
                ps.setString(7, correo.getText().toString());
                ps.setString(8, clave1.getText().toString());
                ps.setBytes(9, imagenPerfil);

                String mensaje=con.ejecutarPs_mensaje(ps);
                Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_LONG).show();
            }catch (Exception e){
                Toast.makeText(getApplicationContext(),"Datos incompletos",Toast.LENGTH_LONG).show();
            }
        }
        else
            Toast.makeText(this, "Datos incompletos",Toast.LENGTH_LONG).show();

    }

    private static final int PICK_IMAGE = 1;
    Uri imageUri;
    ImageView imageviewU;

    public void BuscarImagen(View view){
        Intent galeria = new Intent();
        galeria.setType("image/*");
        galeria.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(galeria, "Sellect Picture"), PICK_IMAGE);
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageviewU= findViewById(R.id.perfil_usuarioReg);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imageviewU.setImageBitmap(bitmap);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
            }
        }

    }



    @Override
    public boolean onMyLocationButtonClick() {
        DecimalFormat df = new DecimalFormat("#.00000");
        Location location=Mapa.getMyLocation();
        latitud=Float.parseFloat(df.format(location.getLatitude()));
        longitud=Float.parseFloat(df.format(location.getLongitude()));
        return false;
    }
}