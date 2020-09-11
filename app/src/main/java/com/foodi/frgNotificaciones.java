package com.foodi;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.foodi.Adaptadores.adtdenuncias;
import com.foodi.Interfaces.itfRetrofit;
import com.foodi.Modelos.DetalleDenuncia;
import com.foodi.Modelos.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.foodi.Clases.clsUtilitarios.IP_SERVIDOR;
import static com.foodi.Clases.clsUtilitarios.PUERTO;

public class frgNotificaciones extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    List<Usuario> lista;


    public frgNotificaciones() {
        // Required empty public constructor
    }


    public static frgNotificaciones newInstance(String param1, String param2) {
        frgNotificaciones fragment = new frgNotificaciones();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_frg_inicio, container, false);

        try {
            //rclPopulares = view.findViewById(R.id.rclDenunciasAdmin);
            final LinearLayoutManager linear = new LinearLayoutManager(getActivity().getApplicationContext());
            linear.setOrientation(LinearLayoutManager.VERTICAL);
            Retrofit rf = new Retrofit.Builder().baseUrl("http://" + IP_SERVIDOR + ":"+PUERTO+"/").addConverterFactory(GsonConverterFactory.create()).build();
            itfRetrofit retrofit_interfaz = rf.create(itfRetrofit.class);
            Call<List<Usuario>> call = retrofit_interfaz.getUsuario();
            call.enqueue(new Callback<List<Usuario>>() {
                @Override
                public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                    //Codigo de respuesta a la petición realizada
                    String cod_respuesta = "Código " + response.code();
                    //Definiendo donde se guardaran los valores obtenidos
                    String valores = "";
                    try {
                        lista = response.body();
                        final List<Usuario> finalLista = lista;
                        //adtdenuncias adtdenuncias = new adtdenuncias(finalLista, frg);
                        //rclPopulares.setLayoutManager(linear);
                        //rclPopulares.setAdapter(adtdenuncias);
                        String valor = "prueba";
                    } catch (Exception e) {
                        Toast.makeText(getActivity().getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Usuario>> call, Throwable t) {
                    Toast.makeText(getActivity().getApplicationContext(), "No se ha podido establecer conexión con el servidor " + t.toString(), Toast.LENGTH_LONG).show();
                }
            });

        } catch (Exception e) {
            Toast.makeText(getActivity().getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
        return view;
    }
}