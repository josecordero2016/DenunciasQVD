package com.foodi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.foodi.Clases.GlobalClass;
import com.foodi.WSSoap.SOAPWork;
import com.foodi.WebServices.Asynchtask;
import org.json.JSONException;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.foodi.Clases.clsUtilitarios.IP_SERVIDOR;

public class LoginActivity extends AppCompatActivity implements Asynchtask {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText txtNombreUsuario = findViewById(R.id.txtNombreUsuario);
        EditText txtContrasena = findViewById(R.id.txtContrasena);
       /* Map<String, String> map = new LinkedHashMap<>();
        try
        {
            map.put("sentencia", "select consultar_usuario('"+txtNombreUsuario.getText()+"','"+txtContrasena.getText()+"')");
            SOAPWork dd = new SOAPWork("http://"+IP_SERVIDOR+":8080/Foodi_srv/ws_Procesar?WSDL", map, this, this);
            dd.setMethod_name("consultar");
            dd.execute();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error, "+e.toString(),Toast.LENGTH_LONG).show();
        }*/

    }

    public void registrarse(View view){
        Toast.makeText(getApplicationContext(),"Registrarse",Toast.LENGTH_LONG).show();
    }

    public void iniciarSesion(View view){

        Intent intent = new Intent(this, actAdministrador.class);
        GlobalClass globalclass=(GlobalClass) getApplicationContext();
        globalclass.setId_usuario_actual("1");
        startActivity(intent);
        this.finish();
    }

    @Override
    public void processFinish(String result) throws JSONException {

        String[] resultados = result.toString().split("_");
        if (resultados[0].equals("OK")) {
            Intent intent;
            if (resultados[1].equals("C")) {
                intent = new Intent(this, MainActivity.class);
            } else {
                intent = new Intent(this, actAdministrador.class);
            }
            startActivity(intent);
            this.finish();
        } else {
            Toast.makeText(getApplicationContext(), "Usuario y/o contraseña incorrectos", Toast.LENGTH_LONG).show();
        }
    }
}