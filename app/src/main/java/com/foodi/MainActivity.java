package com.foodi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        BottomNavigationView menu_abajo = findViewById(R.id.menu_abajo);


        Fragment selectedFragment = new frgInicio();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragments,selectedFragment);
        fragmentTransaction.commit();

        menu_abajo.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.frgInicio:
                        selectedFragment = new frgInicio();
                        fragmentTransaction.replace(R.id.fragments,selectedFragment);
                        fragmentTransaction.commit();
                        break;

                    case R.id.frgFavoritos:
                        selectedFragment = new frgFavoritos();
                        fragmentTransaction.replace(R.id.fragments,selectedFragment);
                        fragmentTransaction.commit();
                        break;
                }
              return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_busqueda,menu);
        return true;
    }

    public void cambioFragment()
    {
        FragmentManager manager = getSupportFragmentManager();

    }
}