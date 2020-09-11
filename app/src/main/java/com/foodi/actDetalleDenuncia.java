package com.foodi;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareMediaContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.foodi.Clases.GlobalClass;
import com.foodi.WSSoap.SOAPWork;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.foodi.Clases.clsUtilitarios.IP_SERVIDOR;
import static com.foodi.Clases.clsUtilitarios.PUERTO;
import static com.foodi.Clases.clsUtilitarios.denuncia_selec;

import com.foodi.WebServices.Asynchtask;

import org.json.JSONException;

public class actDetalleDenuncia extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, Asynchtask {

    GoogleMap Mapa;
    TextView txtTituloDetalle, txtUsuarioDetalle, txtFechaDetalle, txtPuntajeDetalle, txtPuntajeDetalle2, txtDescripcionDetalle, txtAtendidaDetalle, txtTipoDetalle;
    ImageView ivFotoDetalle, ivPerfilDetalle;
    Button btnEliminarDetalle;

    int proceso;
    int id_denuncia;
    ShareDialog shareDialog;
    CallbackManager callbackManager;
    FloatingActionButton btnTwitter;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_detalle_denuncia);

        //ImprimirKeyHash();
       FacebookSdk.sdkInitialize(getApplicationContext());

        //Inicializar Facebook
        callbackManager=CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        txtTituloDetalle = findViewById(R.id.txtTituloDetalle);
        txtUsuarioDetalle = findViewById(R.id.txtUsuarioDetalle);
        txtFechaDetalle = findViewById(R.id.txtFechaDetalle);
        txtPuntajeDetalle = findViewById(R.id.txtPuntajeDetalle);
        txtPuntajeDetalle2 = findViewById(R.id.txtPuntajeDetalle2);
        txtDescripcionDetalle = findViewById(R.id.txtDescripcionDetalle);
        txtAtendidaDetalle = findViewById(R.id.txtAtentidaDetalle);
        txtTipoDetalle = findViewById(R.id.txtTipoDetalle);
        ivFotoDetalle = findViewById(R.id.ivFotoDetalle);
        ivPerfilDetalle = findViewById(R.id.ivPerfilDetalle);
        btnEliminarDetalle = findViewById(R.id.btnEliminarDetalle);
        String fecha = denuncia_selec.getIdDenuncia().getFecha();
        fecha = fecha.substring(0, 10);
        id_denuncia = Integer.parseInt(denuncia_selec.getIdDenuncia().getIdDenuncia());

        btnTwitter = findViewById(R.id.btnTwitterFlo);

        txtTipoDetalle.setText(denuncia_selec.getIdDenuncia().getTipo());
        txtUsuarioDetalle.setText(denuncia_selec.getIdDenuncia().getIdUsuario().getNombres() + " " + denuncia_selec.getIdDenuncia().getIdUsuario().getApellidos());
        txtFechaDetalle.setText(fecha);
        txtAtendidaDetalle.setText(denuncia_selec.getIdDenuncia().getAtendida());
        txtDescripcionDetalle.setText(denuncia_selec.getIdDenuncia().getDetalles());
        txtTituloDetalle.setText(denuncia_selec.getIdDenuncia().getTitulo());

        btnTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivFotoDetalle.buildDrawingCache();
                Bitmap bitmap = ivFotoDetalle.getDrawingCache();
                try {
                    File file = new File(getApplicationContext().getExternalCacheDir(), File.separator +"imagenejemplo.jpg");
                    FileOutputStream fOut = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                    fOut.flush();
                    fOut.close();
                    file.setReadable(true, false);
                    final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID +".provider", file);

                    // Intent intent = new Intent(Intent.ACTION_SEND);
                    //intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, "DenunciasQVD informa sobre la denuncia realizada por: "+txtUsuarioDetalle.getText()+"\n" +
                            "Titulo: "+txtTituloDetalle.getText()+"\n" +
                            "Descripción: "+txtDescripcionDetalle.getText()+"\n" +
                            "Fecha publicación: "+txtFechaDetalle.getText()+"\n" +
                            "@GadQuevedo #MunicipioQuevedo");
                    intent.putExtra(Intent.EXTRA_STREAM, photoURI);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setType("image/jpg");
                    intent.setPackage("com.twitter.android");
                    startActivity(intent);
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                }


            }
        });

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
        GlobalClass globalclass = (GlobalClass) getApplicationContext();

       // Toast.makeText(getApplicationContext(),globalclass.getId_usuario_actual()+" "+denuncia_selec.getIdDenuncia().getIdUsuario().getIdUsuario(),Toast.LENGTH_LONG).show();
        if(!globalclass.getId_usuario_actual().equals(denuncia_selec.getIdDenuncia().getIdUsuario().getIdUsuario())){
            btnEliminarDetalle.setVisibility(View.GONE);
         //   Toast.makeText(getApplicationContext(),"Borrar",Toast.LENGTH_LONG).show();
        }
    }
    private void ImprimirKeyHash() {
        try {
            PackageInfo info=getPackageManager().getPackageInfo("com.foodi",
                    PackageManager.GET_SIGNATURES);
            for(Signature signature: info.signatures){
                MessageDigest md= MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", android.util.Base64.encodeToString(md.digest(), android.util.Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
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
        //  Puntos seleccionados
        //Mapa.addMarker(new MarkerOptions().position(new LatLng(-1.012934, -79.469373)).title("Facultad de Ciencias Agrarias"));
    }

    public void cambiarVista(View view) {
        if (Mapa.getMapType() == GoogleMap.MAP_TYPE_NORMAL) {
            Mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        } else {
            Mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
    }

    public void votarReal(View view) {
        Map<String, String> map = new LinkedHashMap<>();
        try {
            GlobalClass globalclass = (GlobalClass) getApplicationContext();


            map.put("sentencia", "select ejecutar_puntaje('" + id_denuncia + "','" + globalclass.getId_usuario_actual() + "','SI')");
            SOAPWork dd = new SOAPWork("http://" + IP_SERVIDOR + ":8080/Denunciasqvd_srv/ws_Procesar?WSDL", map, this, this);
            dd.setMethod_name("procesar");
            dd.execute();
            proceso = 1;
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error, " + e.toString(), Toast.LENGTH_LONG).show();
        }
        proceso = 2;
    }

    public void votarFalso(View view) {

        Map<String, String> map = new LinkedHashMap<>();
        try {
            GlobalClass globalclass = (GlobalClass) getApplicationContext();
            map.put("sentencia", "select ejecutar_puntaje('" + id_denuncia + "','" + globalclass.getId_usuario_actual() + "','NO')");
            SOAPWork dd = new SOAPWork("http://" + IP_SERVIDOR + ":8080/Denunciasqvd_srv/ws_Procesar?WSDL", map, this, this);
            dd.setMethod_name("procesar");
            dd.execute();
            proceso = 1;
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error, " + e.toString(), Toast.LENGTH_LONG).show();
        }
        proceso = 2;
    }

    public void eliminarVoto(View view) {
        Map<String, String> map = new LinkedHashMap<>();
        try {
            GlobalClass globalclass = (GlobalClass) getApplicationContext();
            map.put("sentencia", "DELETE from ratificaciones where id_usuario = '" + globalclass.getId_usuario_actual() + "' and id_denuncia = '" + id_denuncia + "'");
            SOAPWork dd = new SOAPWork("http://" + IP_SERVIDOR + ":8080/Denunciasqvd_srv/ws_Procesar?WSDL", map, this, this);
            dd.setMethod_name("procesar");
            dd.execute();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error, " + e.toString(), Toast.LENGTH_LONG).show();
        }
        proceso = 2;
    }

    public void eliminarDenuncia(View view){
        Map<String, String> map = new LinkedHashMap<>();
        try {
            GlobalClass globalclass = (GlobalClass) getApplicationContext();
            map.put("sentencia", "select eliminar_denuncia('"+id_denuncia+"')");
            SOAPWork dd = new SOAPWork("http://" + IP_SERVIDOR + ":8080/Denunciasqvd_srv/ws_Procesar?WSDL", map, this, this);
            dd.setMethod_name("procesar");
            dd.execute();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error, " + e.toString(), Toast.LENGTH_LONG).show();
        }
        proceso = 3;
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
        if (proceso == 3) {
            finish();
        }
    }

    public void compartirFacebook(View view){
        try {
            ivFotoDetalle.buildDrawingCache();
            Bitmap bitmap = ivFotoDetalle.getDrawingCache();
            SharePhoto foto=new SharePhoto.Builder().setBitmap(bitmap).build();
           ShareContent todocontenido=new ShareMediaContent.Builder().addMedium(foto)
                    .setShareHashtag(new ShareHashtag.Builder().setHashtag("DenunciasQVD informa sobre la denuncia realizada por: "+txtUsuarioDetalle.getText()+"\n" +
                            "Titulo: "+txtTituloDetalle.getText()+"\n" +
                            "Descripción: "+txtDescripcionDetalle.getText()+"\n" +
                            "Fecha publicación: "+txtFechaDetalle.getText()+"\n" +
                            "#MunicipioQuevedo").build())
                    .build();
            /*ShareContent todocontenido=new ShareMediaContent.Builder().addMedium(foto)
                    .setShareHashtag(new ShareHashtag.Builder().setHashtag("DenunciasQVD informa #MunicipioQuevedo").build())
                    .build();*/
            if(ShareDialog.canShow(SharePhotoContent.class)){
                shareDialog.show(todocontenido);
            }
        }catch (Exception e){
            //
        }

    }
}