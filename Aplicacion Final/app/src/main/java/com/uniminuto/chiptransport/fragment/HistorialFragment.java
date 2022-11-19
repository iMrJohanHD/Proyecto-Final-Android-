package com.uniminuto.chiptransport.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.uniminuto.chiptransport.R;
import com.uniminuto.chiptransport.adapter.HistorialViajesAdapter;
import com.uniminuto.chiptransport.adapter.ViajesAdapter;
import com.uniminuto.chiptransport.pojo.Viajes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistorialFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistorialFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "idCuenta";

    TextView txtHistorialIdCuenta;
    // TODO: Rename and change types of parameters
    private String idCuenta;
    String idCuentaViaje;

    RecyclerView recyclerViewHistorialViajes;
    ArrayList<Viajes> listaHitorialViajes;

    ProgressDialog progressDialog;

    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;

    public HistorialFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param idCuenta Parameter 1.
     * @return A new instance of fragment HistorialFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistorialFragment newInstance(String idCuenta) {
        HistorialFragment fragment = new HistorialFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, idCuenta);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idCuenta = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View main =inflater.inflate(R.layout.fragment_historial, container, false);

        txtHistorialIdCuenta = (TextView) main.findViewById(R.id.txtHistorialIdCuenta);
        txtHistorialIdCuenta.setText(idCuenta);
        idCuentaViaje = txtHistorialIdCuenta.getText().toString().trim();

        listaHitorialViajes =  new ArrayList<>();
        recyclerViewHistorialViajes = (RecyclerView) main.findViewById(R.id.rvHistorialViajes);
        recyclerViewHistorialViajes.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerViewHistorialViajes.setHasFixedSize(true);

        requestQueue = Volley.newRequestQueue(getContext());

        cargarHistorialViajes();

        return main;
    }

    private void cargarHistorialViajes() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Buscando viajes...");
        progressDialog.show();
        String url = "http://10.168.0.103/bbdd/CheapTransporte/buscarHistorialViajes.php?idCuenta="+idCuenta+"";

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
                viaje.setOrigen(jsonObject.optString("origen_viaje"));
                viaje.setDestino(jsonObject.optString("destino_viaje"));
                viaje.setCupo(jsonObject.optString("precio_viaje"));
                viaje.setFechaInicio(jsonObject.optString("fecha_inicio_viaje"));
                viaje.setHoraInicio(jsonObject.optString("hora_inicio_viaje"));
                viaje.setFechaFin(jsonObject.optString("fecha_fin_viaje"));
                viaje.setHoraFin(jsonObject.optString("hora_fin_viaje"));
                viaje.setPrecio(jsonObject.optString("cupo_viaje"));
                listaHitorialViajes.add(viaje);
            }
            progressDialog.hide();
            HistorialViajesAdapter adapter = new HistorialViajesAdapter(listaHitorialViajes);
            recyclerViewHistorialViajes.setAdapter(adapter);
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