package com.foodi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.foodi.Clases.GlobalClass;
import com.foodi.Clases.clsConexionBd;
import com.foodi.WSSoap.SOAPWork;
import com.foodi.WebServices.Asynchtask;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;

import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.foodi.Clases.clsUtilitarios.IP_SERVIDOR;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link frgUsuario#newInstance} factory method to
 * create an instance of this fragment.
 */
public class frgUsuario extends Fragment implements OnMapReadyCallback, Asynchtask  {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public frgUsuario() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment frgUsuario.
     */
    // TODO: Rename and change types and number of parameters
    public static frgUsuario newInstance(String param1, String param2) {
        frgUsuario fragment = new frgUsuario();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    GoogleMap Mapa;
    Float latitud=(float)0, longitud=(float)0;
    LatLng pos = null;
    Integer ban=0;
    View view=null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        GlobalClass globalclass=(GlobalClass)getActivity().getApplicationContext();
        view=inflater.inflate(R.layout.fragment_frg_usuario, container, false);
        Map<String, String> map = new LinkedHashMap<>();
        try
        {
            map.put("sentencia", "select consultardatos_usuario("+ globalclass.getId_usuario_actual()+")");
            SOAPWork dd = new SOAPWork("http://"+IP_SERVIDOR+":18609/Denunciasqvd_srv/ws_Procesar?WSDL", map, getContext(), this);
            dd.setMethod_name("consultar");
            dd.execute();

        }
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error, "+e.toString(),Toast.LENGTH_LONG).show();
        }
        byte[] pruebina = null;
        clsConexionBd con = new clsConexionBd();
        ResultSet rs = con.consultar("select imagen from usuario where id_usuario = "+globalclass.getId_usuario_actual());
        try {
            while (rs.next()) {
                pruebina = rs.getBytes(1);
            }
            Bitmap bitm = BitmapFactory.decodeByteArray(pruebina, 0, pruebina.length);
            ImageView iv3 = (ImageView) view.findViewById(R.id.profile_image);
            iv3.setImageBitmap(bitm);
            }
        catch (Exception e) {
            Toast.makeText(getContext(), "Error, "+e.toString(),Toast.LENGTH_LONG).show();
        }


        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Mapa=googleMap;
        CameraUpdate camUpd1 = CameraUpdateFactory.newLatLngZoom(new LatLng(latitud, longitud), 15);
        Mapa.addMarker(new MarkerOptions().position(pos).title("Ubicación"));
        Mapa.moveCamera(camUpd1);
    }
    String[] resultados=null;
    @Override
    public void processFinish(String result) throws JSONException {
        resultados =result.split("&");
        TextView nombre= view.findViewById(R.id.txtNombre);
        TextView telefono= view.findViewById(R.id.txtTelefono);
        TextView correo= view.findViewById(R.id.txtEmail);
        TextView usuario= view.findViewById(R.id.txtUsuario);


        nombre.setText(resultados[0]+" "+resultados[1]);
        telefono.setText(resultados[2]);
        correo.setText(resultados[5]);
        usuario.setText(resultados[6]);


        latitud=Float. parseFloat( resultados[3]);
        longitud=Float. parseFloat(resultados[4]);
        pos = new LatLng(latitud, longitud);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_usuario);
        mapFragment.getMapAsync(this);
    }



}