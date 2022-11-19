package com.uniminuto.chiptransport.ui.main;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.uniminuto.chiptransport.InicioCheapTransporte;
import com.uniminuto.chiptransport.fragment.HistorialFragment;
import com.uniminuto.chiptransport.fragment.PagoFragment;
import com.uniminuto.chiptransport.fragment.PerfilFragment;
import com.uniminuto.chiptransport.fragment.ViajesFragment;
import com.uniminuto.chiptransport.databinding.FragmentInicioCheapTransporteBinding;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    public static String idCuenta =  InicioCheapTransporte.getIdCuenta();
    public static String idViaje = InicioCheapTransporte.getIdViaje();
    private PageViewModel pageViewModel;
    private FragmentInicioCheapTransporteBinding binding;

    public static Fragment newInstance(int index) {
        /*PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);*/
        Fragment fragment = null;

        switch (index){
            case 1:fragment= new ViajesFragment();
                Bundle datos =  new Bundle();
                datos.putString("idCuenta",idCuenta);
                datos.putString("idViaje",idViaje);
                fragment.setArguments(datos);
                break;
            case 2:fragment= new HistorialFragment();
                Bundle datos1 =  new Bundle();
                datos1.putString("idCuenta",idCuenta);
                fragment.setArguments(datos1);
                break;
            case 3:fragment= new PerfilFragment();
                break;
            case 4:fragment= new PagoFragment();
                break;
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = FragmentInicioCheapTransporteBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        final TextView textView = binding.sectionLabel;
        pageViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}