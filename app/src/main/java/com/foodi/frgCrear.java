package com.foodi;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
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

import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.foodi.Clases.clsUtilitarios.IP_SERVIDOR;
import static com.foodi.Clases.clsUtilitarios.PUERTO;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link frgCrear#newInstance} factory method to
 * create an instance of this fragment.
 */
public class frgCrear extends Fragment implements Asynchtask, OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMyLocationButtonClickListener  {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    //Variables
    private Spinner spTipoDenuncia;
    private Button btnRegistrarDenuncia;
    ImageView btnBuscarImagen;

    String[] resultados = null;
    TextView txtLatitud = null;
    TextView txtLongitud = null;

    GoogleMap Mapa;
    Float latitud = (float) 0, longitud = (float) 0;
    LatLng pos = null;

    private static final int PICK_IMAGE = 2;
    Uri imageUri;
    ImageView imageviewU;


    public frgCrear() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment frgCrear.
     */
    // TODO: Rename and change types and number of parameters
    public static frgCrear newInstance(String param1, String param2) {
        frgCrear fragment = new frgCrear();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_frg_crear, container, false);
        final String[] lista= new String[]{"Seleccione..","Luminaria defectuosa", "Bienes sustraidos", "Alcantarilla problematica","Agua potable insuficiente","Otro"};

        //web service
        GlobalClass globalclass = (GlobalClass) getActivity().getApplicationContext();
        Map<String, String> map = new LinkedHashMap<>();
        try {
            map.put("sentencia", "select consultardatos_usuario(" + globalclass.getId_usuario_actual() + ")");
            SOAPWork dd = new SOAPWork("http://" + IP_SERVIDOR + ":"+PUERTO+"/Denunciasqvd_srv/ws_Procesar?WSDL", map, getContext(), this);
            dd.setMethod_name("consultar");
            dd.execute();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error, " + e.toString(), Toast.LENGTH_LONG).show();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),R.layout.support_simple_spinner_dropdown_item,lista);
        spTipoDenuncia=(Spinner) view.findViewById(R.id.spTipoDenuncia);
        spTipoDenuncia.setAdapter(adapter);
        return view;
    }
    @Override
    public void processFinish(String result) throws JSONException {
        resultados = result.split("&");
        txtLatitud = getActivity().findViewById(R.id.txtLat);
        txtLongitud = getActivity().findViewById(R.id.txtLon);

        txtLatitud.setText(resultados[3]);
        txtLongitud.setText(resultados[4]);

        latitud = Float.parseFloat(resultados[3]);
        longitud = Float.parseFloat(resultados[4]);

        pos = new LatLng(latitud, longitud);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_denuncia);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Mapa = googleMap;
        Mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        Mapa.getUiSettings().setZoomControlsEnabled(true);
        CameraUpdate camUpd1 = CameraUpdateFactory.newLatLngZoom(new LatLng(latitud, longitud), 17);
        Mapa.addMarker(new MarkerOptions().position(pos).title("Ubicación"));
        Mapa.moveCamera(camUpd1);
        Mapa.setOnMapClickListener(this);
        Mapa.setOnMyLocationButtonClickListener(this);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;

        Mapa.setMyLocationEnabled(true);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Mapa.clear();
        Mapa.addMarker(new MarkerOptions().position(latLng).title("Hola"));
        latitud = (float) latLng.latitude;
        longitud = (float) latLng.longitude;
        DecimalFormat df = new DecimalFormat("#.00000");
        txtLatitud.setText(String.valueOf(df.format(latLng.latitude)));
        txtLongitud.setText(String.valueOf(df.format(latLng.longitude)));
    }

    @Override
    public boolean onMyLocationButtonClick() {
        DecimalFormat df = new DecimalFormat("#.00000");
        Location location = Mapa.getMyLocation();
        txtLatitud.setText(String.valueOf(df.format(location.getLatitude())));
        txtLongitud.setText(String.valueOf(df.format(location.getLongitude())));
        return false;
    }

}