package com.uniminuto.chiptransport.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uniminuto.chiptransport.R;
import com.uniminuto.chiptransport.RegistroViaje;
import com.uniminuto.chiptransport.adapter.ViajesAdapter;
import com.uniminuto.chiptransport.pojo.Viajes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViajesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViajesFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "idCuenta";
    private static final String ARG_PARAM2 = "idViaje";
    //varaibles y conexiones con UI
    TextView txtIdCuenta, txtIdViaje;
    FloatingActionButton fabRegisterViaje;
    // TODO: Rename and change types of parameters
    private String idCuenta, idViaje;
    String idCuentaViaje, idBusquedaViaje;

    RecyclerView recyclerViewViajes;
    ArrayList<Viajes> listaViajes;

    ProgressDialog progressDialog;

    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;

    public ViajesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param idCuenta Parameter 1.
     * @return A new instance of fragment ViajesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViajesFragment newInstance(String idCuenta, String idViaje) {
        ViajesFragment fragment = new ViajesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, idCuenta);
        args.putString(ARG_PARAM2, idViaje);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idCuenta = getArguments().getString(ARG_PARAM1);
            idViaje = getArguments().getString(ARG_PARAM2);
        }


    }



        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {




        // Inflate the layout for this fragment
        View main = inflater.inflate(R.layout.fragment_viajes, container, false);

        txtIdCuenta = (TextView) main.findViewById(R.id.txtIdCuenta);
        txtIdViaje = (TextView) main.findViewById(R.id.txtIdViaje);
        txtIdCuenta.setText(idCuenta);
        txtIdViaje.setText(idViaje);
        idCuentaViaje = txtIdCuenta.getText().toString().trim();
        idBusquedaViaje = txtIdViaje.getText().toString().trim();
        fabRegisterViaje = (FloatingActionButton) main.findViewById(R.id.fabRegisterViaje);
            fabRegisterViaje.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent =  new Intent(getContext(), RegistroViaje.class);
                    intent.putExtra("idCuentaViaje",idCuentaViaje);
                    startActivity(intent);
                }
            });

        listaViajes =  new ArrayList<>();
        recyclerViewViajes = (RecyclerView) main.findViewById(R.id.rvViajes);
        recyclerViewViajes.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerViewViajes.setHasFixedSize(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this.getContext(),DividerItemDecoration.HORIZONTAL);
        recyclerViewViajes.addItemDecoration(dividerItemDecoration);
        requestQueue = Volley.newRequestQueue(getContext());

        cargarViajes();

        return main;
    }

    private void cargarViajes() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Buscando viajes...");
        progressDialog.show();
        String url = "http://10.168.0.103/bbdd/CheapTransporte/buscarViajeFiltro.php?idCuenta="+idCuenta+"";

        jsonObjectRequest =  new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        requestQueue.add(jsonObjectRequest);

    }
    @Override
    public void onResponse(JSONObject response) {
        Viajes viaje = null;
        JSONArray json = response.optJSONArray("viajeFiltro");
        try {
        for (int i=0;i<json.length();i++){
            viaje = new Viajes();
            JSONObject jsonObject =  null;
            jsonObject = json.getJSONObject(i);
            viaje.setId(jsonObject.optInt("id_viaje"));
            viaje.setIdRegistro(jsonObject.optInt("id_registro"));
            viaje.setOrigen(jsonObject.optString("origen_viaje"));
            viaje.setDestino(jsonObject.optString("destino_viaje"));
            viaje.setCupo(jsonObject.optString("precio_viaje"));
            viaje.setFechaInicio(jsonObject.optString("fecha_inicio_viaje"));
            viaje.setFechaFin(jsonObject.optString("fecha_fin_viaje"));
            listaViajes.add(viaje);
        }
           progressDialog.hide();
            ViajesAdapter adapter = new ViajesAdapter(listaViajes);
            recyclerViewViajes.setAdapter(adapter);
        }catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(),"No se logro conexion con el servidor",Toast.LENGTH_SHORT).show();
            progressDialog.hide();
        }
    }
    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(),"No se puede conectar",Toast.LENGTH_SHORT).show();
        progressDialog.hide();
    }


}