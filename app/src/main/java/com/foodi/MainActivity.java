package com.foodi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.foodi.Clases.GlobalClass;
import com.foodi.Clases.clsConexionBd;
import com.foodi.Clases.clsUtilitarios;
import com.foodi.Interfaces.itfRetrofit;
import com.foodi.Modelos.Usuario;
import com.foodi.WSSoap.SOAPWork;
import com.foodi.WebServices.Asynchtask;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.foodi.Clases.clsUtilitarios.IP_SERVIDOR;
import static com.foodi.Clases.clsUtilitarios.PUERTO;


public class MainActivity extends AppCompatActivity implements Asynchtask {


    private static final int PICK_IMAGE = 1;
    private int banSubirImgDecuncia = 0;
    Uri imageUri;
    ImageView imageviewU;
    List<Usuario> lista;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        BottomNavigationView menu_abajo = findViewById(R.id.menu_abajo);

//nada
        Fragment selectedFragment = new frgInicio();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragments, selectedFragment);
        fragmentTransaction.commit();

        menu_abajo.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.frgDenuncias:
                        selectedFragment = new frgInicio();
                        fragmentTransaction.replace(R.id.fragments, selectedFragment);
                        fragmentTransaction.commit();
                        break;

                    case R.id.frgCrear:
                        selectedFragment = new frgCrear();
                        fragmentTransaction.replace(R.id.fragments, selectedFragment);
                        fragmentTransaction.commit();
                        break;

                    case R.id.frgNotificaciones:
                        selectedFragment = new frgNotificaciones();
                        fragmentTransaction.replace(R.id.fragments, selectedFragment);
                        fragmentTransaction.commit();
                        break;

                    case R.id.frgUsuario:
                        selectedFragment = new frgUsuario();
                        fragmentTransaction.replace(R.id.fragments, selectedFragment);
                        fragmentTransaction.commit();
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void cambioFragment() {
        FragmentManager manager = getSupportFragmentManager();

    }

    public void EditarUsuario(View view) {
        Fragment nuevoFragmento = new frgEdicionUsuario();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragments, nuevoFragmento);
        transaction.addToBackStack(null);
        // Commit a la transacción
        transaction.commit();
    }

    public void registrarDenuncia(View view) {
        final GlobalClass globalclass = (GlobalClass) getApplicationContext();
        EditText txtTituloDenun = findViewById(R.id.txtTituloDenuncia);
        EditText txtDetalleDenun = findViewById(R.id.txtDetalleDenuncia);
        Spinner spTipoDenuncia = findViewById(R.id.spTipoDenuncia);
        final EditText txtLatDenun = findViewById(R.id.txtLat);
        final EditText txtLonDenun = findViewById(R.id.txtLon);
        ImageView imgDenuncia = findViewById(R.id.btnBuscarImagen);

        clsUtilitarios utili = new clsUtilitarios();
        if (!txtTituloDenun.getText().toString().equals("") && !txtDetalleDenun.getText().toString().equals("") && banSubirImgDecuncia == 1 && spTipoDenuncia.getSelectedItemId() > 0) {
            //A ingresado todos los datos
            String query = "select sp_registrar_denuncia(?,?,?,?,?,?,?)";
            byte[] imagenDenuncia = utili.imagenAbyte(imgDenuncia);
            clsConexionBd con = new clsConexionBd();
            PreparedStatement ps = null;
            try {
                ps = con.connection.prepareStatement(query);
                ps.setInt(1, Integer.parseInt(globalclass.getId_usuario_actual()));
                ps.setString(2, txtTituloDenun.getText().toString());
                ps.setString(3, txtDetalleDenun.getText().toString());
                ps.setString(4, txtLatDenun.getText().toString());
                ps.setString(5, txtLonDenun.getText().toString());
                ps.setString(6, spTipoDenuncia.getSelectedItem().toString());
                ps.setBytes(7, imagenDenuncia);
                final String mensaje = con.ejecutarPs_mensaje(ps);

                ps.close();
                // con.cerrarConexion();
                Toast.makeText(getApplicationContext(), "Datos Actualizados", Toast.LENGTH_LONG).show();
                txtTituloDenun.setText("");
                txtDetalleDenun.setText("");
                imgDenuncia.setImageDrawable(getDrawable(R.drawable.ic_add_photo));
                banSubirImgDecuncia = 0;

                //realizar notificaciones
                try {
                    //con = new clsConexionBd();
                    ResultSet rs = con.consultar("select id_usuario,latitud_actual,longitud_actual, latitud_hogar,longitud_hogar from usuario where tipo!='ADMIN'  and id_usuario!="+globalclass.getId_usuario_actual());
                    List<Usuario> finalLista = new ArrayList<>();
                    while (rs.next()) {
                        finalLista.add(new Usuario(rs.getInt(1)+"", rs.getString(2).toString(), rs.getString(3), rs.getString(4), rs.getString(5)));
                    }
                    rs.close();
                    // con.cerrarConexion();

                    query = "select sp_registrar_notificaion(?,?)";
                    //con = new clsConexionBd();
                    for (int i = 0; i < finalLista.size(); i++) {
                        if (ditanceBetwen(Double.parseDouble(txtLatDenun.getText().toString()), Double.parseDouble(txtLonDenun.getText().toString()), finalLista.get(i))) {
                            ps = con.connection.prepareStatement(query);
                            ps.setInt(1, Integer.parseInt(finalLista.get(i).getIdUsuario()));
                            ps.setInt(2, Integer.parseInt(mensaje));
                            con.ejecutarPs(ps);
                            ps.close();
                        }
                    }
                    con.cerrarConexion();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            //falta ingresar datos
            Toast.makeText(this, "Ingresa todos los datos", Toast.LENGTH_LONG).show();
        }
    }

    public void GuardarDatos(View view) {
        GlobalClass globalclass = (GlobalClass) getApplicationContext();
        EditText nombre = findViewById(R.id.txtNombreEdit);
        EditText apelllido = findViewById(R.id.txtApellidoEdit);
        EditText telefono = findViewById(R.id.txtTelefonoEdit);
        EditText correo = findViewById(R.id.txtCorreoEdit);
        EditText clave = findViewById(R.id.txtContraseEdit);
        EditText clave2 = findViewById(R.id.txtContraseEdit2);
        TextView latitud = findViewById(R.id.txtlatitud);
        TextView longitud = findViewById(R.id.txtlongitud);
        ImageView perfil = findViewById(R.id.perfil_usuarioEdit);

        clsUtilitarios utili = new clsUtilitarios();
        int ban = 0;
        String cons = clave.getText().toString();
        if (!nombre.getText().toString().equals("") && !apelllido.getText().toString().equals("") && !telefono.getText().toString().equals("") && !correo.getText().equals("")) {

            if (clave.getText().toString().equals("") || clave2.getText().toString().equals("") || clave.getText().toString().equals(null) || clave2.getText().toString().equals(null)) {
                cons = "select actualizardatos_usuario_sin_contra(?,?,?,?,?,?,?,?)";
                ban = 0;


            } else if (clave.getText().toString().equals(clave2.getText().toString())) {
                cons = "select actualizardatos_usuario(?,?,?,?,?,?,?,?,?)";
                ban = 1;
            } else {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_LONG).show();
            }
            try {
                byte[] imagenPerfil = utili.imagenAbyte(perfil);
                clsConexionBd con = new clsConexionBd();
                PreparedStatement ps = con.connection.prepareStatement(cons);
                ps.setInt(1, Integer.parseInt(globalclass.getId_usuario_actual()));
                ps.setString(2, nombre.getText().toString());
                ps.setString(3, apelllido.getText().toString());
                ps.setString(4, telefono.getText().toString());
                ps.setString(5, latitud.getText().toString());
                ps.setString(6, longitud.getText().toString());
                ps.setString(7, correo.getText().toString());


                if (ban == 0)
                    ps.setBytes(8, imagenPerfil);
                else {
                    ps.setString(8, clave.getText().toString());
                    ps.setBytes(9, imagenPerfil);
                }
                con.ejecutarPs(ps);
                Toast.makeText(getApplicationContext(), "Datos Actualizados", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error, " + e.toString(), Toast.LENGTH_LONG).show();
            }
        } else
            Toast.makeText(this, "Datos incompletos", Toast.LENGTH_LONG).show();

    }


    public void BuscarImagen(View view) {
        Intent galeria = new Intent();
        galeria.setType("image/*");
        galeria.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(galeria, "Sellect Picture"), PICK_IMAGE);
    }


    public void BuscarImagenDenuncia(View view) {
        Intent galeria = new Intent();
        galeria.setType("image/*");
        galeria.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galeria, "Sellect Picture"), 2);
    }

    public Boolean ditanceBetwen(Double latitudDenunciaNotifi, Double longitudDenunciaNotifi, Usuario userNotificar) {
        float[] results = new float[5];
        //cerca de casa
        Location.distanceBetween(Double.parseDouble(userNotificar.getLatitudHogar()), Double.parseDouble(userNotificar.getLongitudHogar()), latitudDenunciaNotifi, longitudDenunciaNotifi, results);
        if (results[0] <= 1000)
            return true;
        //cerca de ubicacion actual
        Location.distanceBetween(Double.parseDouble(userNotificar.getLatitudActual()), Double.parseDouble(userNotificar.getLongitudActual()), latitudDenunciaNotifi, longitudDenunciaNotifi, results);
        if (results[0] <= 1000)
            return true;
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageviewU = findViewById(R.id.perfil_usuarioEdit);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imageviewU.setImageBitmap(bitmap);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            banSubirImgDecuncia = 0;
            imageUri = data.getData();
            imageviewU = findViewById(R.id.btnBuscarImagen);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imageviewU.setImageBitmap(bitmap);
                banSubirImgDecuncia = 1;
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public void processFinish(String result) throws JSONException {
        Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_LONG).show();
    }
}


