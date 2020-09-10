package com.foodi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.foodi.WebServices.Asynchtask;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;

import java.text.DecimalFormat;

public class RegistroActivity extends AppCompatActivity implements OnMapReadyCallback, Asynchtask,  GoogleMap.OnMapClickListener {

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
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Mapa.clear();
        Mapa.addMarker(new MarkerOptions().position(latLng).title("Ubicación"));
        latitud=(float)latLng.latitude;
        longitud=(float)latLng.longitude;
        DecimalFormat df = new DecimalFormat("#.00000");
       // latitudtxt.setText(String.valueOf(df.format(latLng.latitude)));
       // longitudtxt.setText(String.valueOf(df.format(latLng.longitude)));
    }

    @Override
    public void processFinish(String result) throws JSONException {

    }

    public void registrarse(View view){
        EditText nombres= findViewById(R.id.txtNombreReg);
        EditText apellidos= findViewById(R.id.txtApellidoReg);
        EditText telefono= findViewById(R.id.txtTelefonoReg);
        EditText correo= findViewById(R.id.txtCorreoReg);
        EditText nomb_usuario= findViewById(R.id.NombreUsuarioReg);
        EditText clave1= findViewById(R.id.txtContraseReg);
        EditText clave2= findViewById(R.id.txtContraseReg2);
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
}