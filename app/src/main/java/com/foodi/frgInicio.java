package com.foodi;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.foodi.Adaptadores.adt_populares;

public class frgInicio extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    RecyclerView rclPopulares;

    public frgInicio() {

    }

    public static frgInicio newInstance(String param1, String param2) {
        frgInicio fragment = new frgInicio();
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
        View view = inflater.inflate(R.layout.fragment_frg_inicio, container, false);
        rclPopulares = (RecyclerView) view.findViewById(R.id.rclPopulares);
        LinearLayoutManager linear = new LinearLayoutManager(getActivity().getApplicationContext());
        linear.setOrientation(LinearLayoutManager.VERTICAL);
        rclPopulares.setLayoutManager(linear);
        adt_populares adt_populares = new adt_populares();
        rclPopulares.setAdapter(adt_populares);
        return view;
    }
}