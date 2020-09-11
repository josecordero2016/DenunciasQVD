package com.foodi;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.foodi.Adaptadores.adtdenuncias;
import com.foodi.Clases.clsConexionBd;
import com.foodi.Interfaces.itfRetrofit;
import com.foodi.Modelos.DetalleDenuncia;

import java.sql.ResultSet;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.foodi.Clases.clsUtilitarios.IP_SERVIDOR;
import static com.foodi.Clases.clsUtilitarios.PUERTO;
import static com.foodi.Clases.clsUtilitarios.denuncia_selec;


public class frgInicioAdmin extends Fragment implements adtdenuncias.OnNoteListener{


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    frgInicioAdmin frg = this;

    private String mParam1;
    private String mParam2;
    RecyclerView rclDenunciasAdmin;
    List<DetalleDenuncia> lista;

    public frgInicioAdmin() {
        // Required empty public constructor
    }


    public static frgInicioAdmin newInstance(String param1, String param2) {
        frgInicioAdmin fragment = new frgInicioAdmin();
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
        final View view = inflater.inflate(R.layout.fragment_frg_inicio, container, false);
        clsConexionBd con = new clsConexionBd();
        ResultSet rs= con.consultar("select * from usuario");
        try {
            rclDenunciasAdmin = view.findViewById(R.id.rclDenunciasAdmin);
            final LinearLayoutManager linear = new LinearLayoutManager(getActivity().getApplicationContext());
            linear.setOrientation(LinearLayoutManager.VERTICAL);
            Retrofit rf = new Retrofit.Builder().baseUrl("http://" + IP_SERVIDOR + ":"+PUERTO+"/").addConverterFactory(GsonConverterFactory.create()).build();
            itfRetrofit retrofit_interfaz = rf.create(itfRetrofit.class);
            Call<List<DetalleDenuncia>> call = retrofit_interfaz.getDetalleDenuncia();
            call.enqueue(new Callback<List<DetalleDenuncia>>() {
                @Override
                public void onResponse(Call<List<DetalleDenuncia>> call, Response<List<DetalleDenuncia>> response) {
                    //Codigo de respuesta a la petición realizada
                    String cod_respuesta = "Código " + response.code();
                    //Definiendo donde se guardaran los valores obtenidos
                    String valores = "";
                    try {
                        lista = response.body();
                        final List<DetalleDenuncia> finalLista = lista;
                        adtdenuncias adtdenuncias = new adtdenuncias(finalLista, frg);
                        rclDenunciasAdmin.setLayoutManager(linear);
                        rclDenunciasAdmin.setAdapter(adtdenuncias);
                    } catch (Exception e) {
                        Toast.makeText(getActivity().getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<List<DetalleDenuncia>> call, Throwable t) {
                    Toast.makeText(getActivity().getApplicationContext(), "No se ha podido establecer conexión con el servidor " + t.toString(), Toast.LENGTH_LONG).show();
                }
            });

        } catch (Exception e) {
            Toast.makeText(getActivity().getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }

        return view;
    }

    @Override
    public void onNoteClick(int position) {
        Intent intent = new Intent(getActivity().getApplicationContext(), actDetalleAdmin.class);
        denuncia_selec = lista.get(position);
        //   Toast.makeText(getActivity().getApplicationContext(), String.valueOf(position)+"d"+denuncia_selec.toString(), Toast.LENGTH_LONG).show();
        startActivity(intent);
    }
}