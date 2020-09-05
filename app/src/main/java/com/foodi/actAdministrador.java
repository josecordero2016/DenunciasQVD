package com.foodi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class actAdministrador extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_administrador);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        BottomNavigationView menu = findViewById(R.id.menu_admin);
        Fragment selectedFragment = new frgInicioAdmin();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragments,selectedFragment);
        fragmentTransaction.commit();

        menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.frgDenunciasA:
                        selectedFragment = new frgInicioAdmin();
                        fragmentTransaction.replace(R.id.fragments,selectedFragment);
                        fragmentTransaction.commit();
                        break;

                    case R.id.frgUsuarioA:
                        selectedFragment = new frgUsuario();
                        fragmentTransaction.replace(R.id.fragments,selectedFragment);
                        fragmentTransaction.commit();
                        break;
                }
                return true;
            }
        });
    }
}