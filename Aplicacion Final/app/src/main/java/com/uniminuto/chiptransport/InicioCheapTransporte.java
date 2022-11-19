package com.uniminuto.chiptransport;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.uniminuto.chiptransport.fragment.ViajesFragment;
import com.uniminuto.chiptransport.ui.main.PlaceholderFragment;
import com.uniminuto.chiptransport.ui.main.SectionsPagerAdapter;
import com.uniminuto.chiptransport.databinding.ActivityInicioCheapTransporteBinding;

public class InicioCheapTransporte extends AppCompatActivity {

    private ActivityInicioCheapTransporteBinding binding;
    static String idCuenta,idViaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityInicioCheapTransporteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);

        idCuenta =getIntent().getStringExtra("idCuenta");
        idViaje = getIntent().getStringExtra("idViaje");


    }

    public static String getIdCuenta() {
        return idCuenta;
    }
    public static String getIdViaje(){return idViaje;}
}