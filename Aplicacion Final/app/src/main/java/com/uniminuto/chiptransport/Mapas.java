package com.uniminuto.chiptransport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.uniminuto.chiptransport.pojo.Viajes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Mapas extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap map;
    JSONObject jsonObject;
    Double longitudOrigen, latitudOrigen, longitudDestino, latitudDestino;
    String origen,destino;
    String API_KEY="AIzaSyCh1PVXj-85HTqCk9uBGwqZPZcxzqyjKoQ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapas);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapaMapas);
        origen = getIntent().getStringExtra("origen");
        destino = getIntent().getStringExtra("destino");
        establecerViaje(origen,destino);
        //Toast.makeText(Mapas.this,String.valueOf(latitudOrigen),Toast.LENGTH_SHORT).show();
        //Toast.makeText(Mapas.this,String.valueOf(longitudOrigen),Toast.LENGTH_SHORT).show();
        mapFragment.getMapAsync(this);

    }

    public void establecerViaje(String origen,String destino){


        if (origen.equals("Bogota")&& destino.equals("Cali")){
            latitudOrigen = 4.672236856824588;
            longitudOrigen =-74.09340051981445;
            latitudDestino = 3.488955004176121;
            longitudDestino =  -76.50919755473855;
        }else if(origen.equals("Bogota")&& destino.equals("Barranquilla")){
            latitudOrigen = 4.672236856824588;
            longitudOrigen =-74.09340051981445;
            latitudDestino = 10.941334675881318;
            longitudDestino = -74.78368847592543;
        }else if(origen.equals("Cali")&&destino.equals("Bogota")){
            latitudOrigen =3.488955004176121;
            longitudOrigen = -76.50919755473855;
            latitudDestino = 4.672236856824588;
            longitudDestino = -74.09340051981445;
        }else if(origen.equals("Cali")&&destino.equals("Barranquilla")){
            latitudOrigen =3.488955004176121;
            longitudOrigen = -76.50919755473855;
            latitudDestino = 10.941334675881318;
            longitudDestino = -74.78368847592543;
        }else if(origen.equals("Barranquilla")&&destino.equals("Bogota")){
            latitudOrigen = 10.941334675881318;
            longitudOrigen = -74.78368847592543;
            latitudDestino = 4.672236856824588;
            longitudDestino = -74.09340051981445;
        }else if(origen.equals("Barranquilla")&&destino.equals("Cali")){
            latitudOrigen = 10.941334675881318;
            longitudOrigen = -74.78368847592543;
            latitudDestino = 3.488955004176121;
            longitudDestino = -76.50919755473855;
        }else{
            Toast.makeText(Mapas.this,"No se encontro origen o destino",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        map = googleMap;
        //map.setMyLocationEnabled(false);
        map.addMarker(new MarkerOptions().position(new LatLng(latitudOrigen,longitudOrigen)).title(origen));
        map.addMarker(new MarkerOptions().position(new LatLng(latitudDestino,longitudDestino)).title(destino));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitudOrigen,longitudOrigen))
                .zoom(14)
                .build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        String url = "https://maps.googleapis.com/maps/api/directions/json?origin="+latitudOrigen+","+longitudOrigen+"&destination="+latitudDestino+","+longitudDestino+"&key="+API_KEY;

        RequestQueue queue = Volley.newRequestQueue(Mapas.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    jsonObject = new JSONObject(response);
                    trazarRuta(jsonObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);
    }

    private void trazarRuta(JSONObject jsonObject) {
        JSONArray jRoutes;
        JSONArray jLegs;
        JSONArray jSteps;
        try {
            jRoutes = jsonObject.getJSONArray("routes");
            for (int i=0;i<jRoutes.length();i++){
                jLegs = ((JSONObject)(jRoutes.get(i))).getJSONArray("legs");
                for (int j=0;j<jLegs.length();j++){
                    jSteps = ((JSONObject)jLegs.get(i)).getJSONArray("steps");
                    for (int k=0;k<jSteps.length();k++){
                        String polyline = ""+((JSONObject)((JSONObject)jSteps.get(k)).get("polyline")).get("points");
                        Log.i("end",""+polyline);
                        List<LatLng> list= PolyUtil.decode(polyline);
                        map.addPolyline(new PolylineOptions().addAll(list).color(Color.GREEN).width(5));
                    }
                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}