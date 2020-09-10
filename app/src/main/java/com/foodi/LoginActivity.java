package com.foodi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.foodi.Clases.GlobalClass;
import com.foodi.WSSoap.SOAPWork;
import com.foodi.WebServices.Asynchtask;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.foodi.Clases.clsUtilitarios.IP_SERVIDOR;

public class LoginActivity extends AppCompatActivity implements Asynchtask {

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int PICK_IMAGE = 1;
    Uri imageUri;
    ImageView ivLetras;
    EditText txtNombreUsuario;
    EditText txtContrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtNombreUsuario = findViewById(R.id.txtNombreUsuario);
        txtContrasena = findViewById(R.id.txtContrasena);
        ivLetras = findViewById(R.id.ivLetras);

        ArrayList<String> permisos = new ArrayList<String>();
        permisos.add(Manifest.permission.CAMERA);
        permisos.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permisos.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        getPermission(permisos);

        Intent galeria = new Intent();
        galeria.setType("image/*");
        galeria.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(galeria, "Sellect Picture"), PICK_IMAGE);
    }

    public void getPermission(ArrayList<String> permisosSolicitados){

        ArrayList<String> listPermisosNOAprob = getPermisosNoAprobados(permisosSolicitados);
        if (listPermisosNOAprob.size()>0)
            if (Build.VERSION.SDK_INT >= 23)
                requestPermissions(listPermisosNOAprob.toArray(new String[listPermisosNOAprob.size()]), 1);

    }


    public ArrayList<String> getPermisosNoAprobados(ArrayList<String>  listaPermisos) {
        ArrayList<String> list = new ArrayList<String>();
        for(String permiso: listaPermisos) {
            if (Build.VERSION.SDK_INT >= 23)
                if(checkSelfPermission(permiso) != PackageManager.PERMISSION_GRANTED)
                    list.add(permiso);
        }
        return list;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        String s="";
        if(requestCode==1)    {
            for(int i =0; i<permissions.length;i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED)
                    s=s + "OK " + permissions[i] + "\n";
                else
                    s=s + "NO  " + permissions[i] + "\n";
            }
           // Toast.makeText(this.getApplicationContext(), s, Toast.LENGTH_LONG).show();
        }
    }

    public void registrarse(View view){
        Toast.makeText(getApplicationContext(),"Registrarse",Toast.LENGTH_LONG).show();
    }

    public void iniciarSesion(View view){
        Map<String, String> map = new LinkedHashMap<>();
        try
        {
            map.put("sentencia", "select consultar_usuario('"+txtNombreUsuario.getText()+"','"+txtContrasena.getText()+"')");
            SOAPWork dd = new SOAPWork("http://"+IP_SERVIDOR+":8080/Denunciasqvd_srv/ws_Procesar?WSDL", map, this, this);
            dd.setMethod_name("consultar");
            dd.execute();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error, "+e.toString(),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                ivLetras.setImageBitmap(bitmap);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public void processFinish(String result) throws JSONException {
        String[] resultados = result.split("_");
        if (resultados[0].equals("OK")) {
            Intent intent;
            if (resultados[1].equals("CIUDADANO")) {
                intent = new Intent(this, MainActivity.class);
            } else {
                intent = new Intent(this, actAdministrador.class);
            }
            GlobalClass globalclass=(GlobalClass)getApplicationContext();
            globalclass.setId_usuario_actual(resultados[2]);
            startActivity(intent);
            this.finish();
        }
        else {
            Toast.makeText(getApplicationContext(), "Usuario y/o contraseña incorrectos", Toast.LENGTH_LONG).show();
        }
    }
}