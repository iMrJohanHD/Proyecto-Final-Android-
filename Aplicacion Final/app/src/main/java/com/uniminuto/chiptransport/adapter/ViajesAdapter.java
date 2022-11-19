package com.uniminuto.chiptransport.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.uniminuto.chiptransport.MainActivity;
import com.uniminuto.chiptransport.Mapas;
import com.uniminuto.chiptransport.R;
import com.uniminuto.chiptransport.pojo.Viajes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViajesAdapter extends RecyclerView.Adapter<ViajesAdapter.ViajesHolder>{
    RequestQueue requestQueue;
    List<Viajes> listaViajes;
 Context context;
 String srtIdRegistro;
    public ViajesAdapter(List<Viajes>listaViajes){
        this.listaViajes = listaViajes;
    }

    @NonNull
    @Override
    public ViajesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.viajes_list,parent,false);
        RecyclerView.LayoutParams layoutParams =  new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        return new ViajesHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViajesHolder holder, int position) {
        holder.txtFrViajesOrigen.setText(listaViajes.get(position).getOrigen().toString());
        holder.txtFrViajesDestino.setText(listaViajes.get(position).getDestino().toString());
        holder.txtFrViajesIdViaje.setText(String.valueOf(listaViajes.get(position).getId()));
        holder.txtFrViajesCupo.setText(listaViajes.get(position).getCupo().toString());
        holder.txtFrViajesIdRegistro.setText(String.valueOf(listaViajes.get(position).getIdRegistro()));
        holder.txtFrViajesFechaSalida.setText(listaViajes.get(position).getFechaInicio().toString());
        holder.txtFrViajesFechaLlegada.setText(listaViajes.get(position).getFechaFin().toString());
        holder.btnFragmentViajesMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Mapas.class);
                intent.putExtra("origen",holder.txtFrViajesOrigen.getText().toString());
                intent.putExtra("destino",holder.txtFrViajesDestino.getText().toString());
                v.getContext().startActivity(intent);
            }
        });
        holder.btnFragmentViajesEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                srtIdRegistro = holder.txtFrViajesIdRegistro.getText().toString().trim();
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Disponibilidad de viaje");
                builder.setMessage("¿Desea cambiar la disponibilidad del viaje?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final ProgressDialog progressDialog = new ProgressDialog(v.getContext());
                                progressDialog.setMessage("Por favor espere");
                                progressDialog.show();

                                StringRequest request = new StringRequest(Request.Method.POST, "http://10.168.0.103/bbdd/CheapTransporte/actualizarDispoViaje.php", new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        progressDialog.dismiss();
                                        if (response.equalsIgnoreCase("Disponibilidad cambiada correctamente")) {

                                        } else {
                                            Toast.makeText(v.getContext(),"La disponibilidad del viaje ha sido cambiada",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        progressDialog.dismiss();
                                        Toast.makeText(v.getContext(),"No se ha podidio conectar", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                }){
                                    @Nullable
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String,String> parametros = new HashMap<>();
                                        parametros.put("idRegistro",srtIdRegistro);
                                        return parametros;
                                    }
                                };
                                requestQueue=  Volley.newRequestQueue(v.getContext());
                                requestQueue.add(request);
                            }
                        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(v.getContext(), "Cancelar...",Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        })
                        .setCancelable(false)
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaViajes.size();
    }

    public class ViajesHolder extends RecyclerView.ViewHolder{

        TextView txtFrViajesOrigen,txtFrViajesDestino,txtFrViajesIdViaje,txtFrViajesCupo,txtFrViajesIdRegistro,txtFrViajesFechaSalida,txtFrViajesFechaLlegada;
        Button btnFragmentViajesEliminar,btnFragmentViajesMapa;
        public ViajesHolder(View itemView){
            super(itemView);
            txtFrViajesOrigen =  itemView.findViewById(R.id.txtFrViajesOrigen);
            txtFrViajesDestino = itemView.findViewById(R.id.txtFrViajesDestino);
            txtFrViajesIdViaje =  itemView.findViewById(R.id.txtFrViajesIdViaje);
            txtFrViajesCupo = itemView.findViewById(R.id.txtFrViajesCupo);
            txtFrViajesIdRegistro = itemView.findViewById(R.id.txtFrViajesIdRegistro);
            txtFrViajesFechaSalida = itemView.findViewById(R.id.txtFrViajesFechaSalida);
            txtFrViajesFechaLlegada = itemView.findViewById(R.id.txtFrViajesFechaLlegada);
            btnFragmentViajesEliminar = itemView.findViewById(R.id.btnFragmentViajesEliminar);
            btnFragmentViajesMapa = itemView.findViewById(R.id.btnFragmentViajesMapa);
        }
    }
}
