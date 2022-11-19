package com.uniminuto.chiptransport.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uniminuto.chiptransport.R;
import com.uniminuto.chiptransport.pojo.Viajes;

import java.util.List;

public class HistorialViajesAdapter extends RecyclerView.Adapter<HistorialViajesAdapter.HistorialViajesHolder> {

    List<Viajes> listaHistorialViajes;

    public HistorialViajesAdapter(List<Viajes>listaHistorialViajes){
        this.listaHistorialViajes = listaHistorialViajes;
    }

    @NonNull
    @Override
    public HistorialViajesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.historial_viajes_list,parent,false);
        RecyclerView.LayoutParams layoutParams =  new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        return new HistorialViajesHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull HistorialViajesAdapter.HistorialViajesHolder holder, int position) {
        holder.txtFrHistorialViajesOrigen.setText(listaHistorialViajes.get(position).getOrigen().toString());
        holder.txtFrHistorialViajesDestino.setText(listaHistorialViajes.get(position).getDestino().toString());
        holder.txtFrHistorialViajesCupo.setText(listaHistorialViajes.get(position).getCupo().toString());
        holder.txtFrHistorialViajesFechaSalida.setText(listaHistorialViajes.get(position).getFechaInicio().toString());
        holder.txtFrHistorialViajesHoraSalida.setText(listaHistorialViajes.get(position).getHoraInicio().toString());
        holder.txtFrHistorialViajesFechaLlegada.setText(listaHistorialViajes.get(position).getFechaFin().toString());
        holder.txtFrHistorialViajesHoraLlegada.setText(listaHistorialViajes.get(position).getHoraFin().toString());
        holder.txtFrHistorialViajesPrecio.setText(listaHistorialViajes.get(position).getPrecio().toString());
    }

    @Override
    public int getItemCount() {
        return listaHistorialViajes.size();
    }

    public class HistorialViajesHolder extends RecyclerView.ViewHolder{

        TextView txtFrHistorialViajesOrigen,txtFrHistorialViajesDestino,txtFrHistorialViajesCupo,txtFrHistorialViajesFechaSalida,txtFrHistorialViajesHoraSalida,txtFrHistorialViajesFechaLlegada,txtFrHistorialViajesHoraLlegada,txtFrHistorialViajesPrecio;
        public HistorialViajesHolder(View itemView){
            super(itemView);
            txtFrHistorialViajesOrigen =  itemView.findViewById(R.id.txtFrHistorialViajesOrigen);
            txtFrHistorialViajesDestino = itemView.findViewById(R.id.txtFrHistorialViajesDestino);
            txtFrHistorialViajesCupo =  itemView.findViewById(R.id.txtFrHistorialViajesCupo);
            txtFrHistorialViajesFechaSalida = itemView.findViewById(R.id.txtFrHistorialViajesFechaSalida);
            txtFrHistorialViajesHoraSalida = itemView.findViewById(R.id.txtFrHistorialViajesHoraSalida);
            txtFrHistorialViajesFechaLlegada = itemView.findViewById(R.id.txtFrHistorialViajesFechaLlegada);
            txtFrHistorialViajesHoraLlegada = itemView.findViewById(R.id.txtFrHistorialViajesHoraLlegada);
            txtFrHistorialViajesPrecio = itemView.findViewById(R.id.txtFrHistorialViajesPrecio);

        }
    }
}
