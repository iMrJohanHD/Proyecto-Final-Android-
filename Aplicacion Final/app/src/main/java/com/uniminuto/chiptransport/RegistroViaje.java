package com.uniminuto.chiptransport;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RegistroViaje extends AppCompatActivity implements View.OnClickListener {
Spinner spinnerOrigenViaje,spinnerLlegadaViaje,spinnerCategoriaVehiculo,spinnerCupoVehiculo;
String strIdViaje;
String [] listaCiudades = {"Bogota", "Cali","Barranquilla"};
String [] listaCategoriasVehiculo = {"Furgon","Contenedor 20'","Contenedor 40'"};
String [] listaCuposVehiculo = {"Medio","Cuarto","Tres cuartos"};
Button btnRegisterFechaInicioViaje, btnRegisterHoraInicioViaje, btnRegisterFechaLlegadaViaje,btnRegisterHoraLlegadaViaje,btnRegisterViaje;
TextView txtRegisterFechaInicioViaje, txtRegisterHoraInicioViaje, txtRegisterFechaLlegadaViaje,txtRegisterHoraLlegadaViaje, txtIdCuentaViaje,txtValorSpinnerCategoriaViaje, txtRegisterIdViaje;
RequestQueue requestQueue;
static String idCuentaViaje;

private int diaInicio, mesInicio,anoInicio,horaInicio,minutosInicio, diaLlegada, mesLlegada,anoLlegada,horaLlegada,minutosLlegada;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_viaje);
        initUI();
        requestQueue = Volley.newRequestQueue(this);

        btnRegisterFechaInicioViaje.setOnClickListener(this);
        btnRegisterHoraInicioViaje.setOnClickListener(this);
        btnRegisterHoraLlegadaViaje.setOnClickListener(this);
        btnRegisterFechaLlegadaViaje.setOnClickListener(this);
        btnRegisterViaje.setOnClickListener(this);

        idCuentaViaje =getIntent().getStringExtra("idCuentaViaje");
        txtIdCuentaViaje.setText(idCuentaViaje);
    }

    public void initUI(){
        spinnerOrigenViaje = (Spinner) findViewById(R.id.spinnerOrigenViaje);
        ArrayAdapter<String> adapterCiudades =  new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,listaCiudades);
        spinnerOrigenViaje.setAdapter(adapterCiudades);
        spinnerLlegadaViaje = (Spinner) findViewById(R.id.spinnerLlegadaViaje);
        spinnerLlegadaViaje.setAdapter(adapterCiudades);
        spinnerCategoriaVehiculo = (Spinner) findViewById(R.id.spinnerCategoriaVehiculo);
        ArrayAdapter<String> adapterCategoriasVehiculo =  new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,listaCategoriasVehiculo);
        spinnerCategoriaVehiculo.setAdapter(adapterCategoriasVehiculo);
        spinnerCupoVehiculo = (Spinner) findViewById(R.id.spinnerCupoVehiculo);
        ArrayAdapter<String> adapterCupoVehiculo = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,listaCuposVehiculo);
        spinnerCupoVehiculo.setAdapter(adapterCupoVehiculo);
        btnRegisterFechaInicioViaje = (Button) findViewById(R.id.btnRegisterFechaInicioViaje);
        btnRegisterHoraInicioViaje = (Button) findViewById(R.id.btnRegisterHoraInicioViaje);
        btnRegisterFechaLlegadaViaje = (Button) findViewById(R.id.btnRegisterFechaLlegadaViaje);
        btnRegisterHoraLlegadaViaje = (Button) findViewById(R.id.btnRegisterHoraLlegadaViaje);
        btnRegisterViaje = (Button) findViewById(R.id.btnRegisterViaje);
        txtRegisterFechaInicioViaje = (TextView) findViewById(R.id.txtRegisterFechaInicioViaje);
        txtRegisterHoraInicioViaje = (TextView) findViewById(R.id.txtRegisterHoraInicioViaje);
        txtRegisterFechaLlegadaViaje = (TextView) findViewById(R.id.txtRegisterFechaLlegadaViaje);
        txtRegisterHoraLlegadaViaje = (TextView) findViewById(R.id.txtRegisterHoraLlegadaViaje);
        txtIdCuentaViaje = (TextView) findViewById(R.id.txtIdCuentaViaje);
        txtValorSpinnerCategoriaViaje = (TextView) findViewById(R.id.txtValorSpinnerCategoriaViaje);
        txtRegisterIdViaje = (TextView) findViewById(R.id.txtRegisterIdViaje);
    }

    public void valorSpinnerCategoriaVehiculo(){
        String nombreSpinner = spinnerCategoriaVehiculo.getSelectedItem().toString();
        if(nombreSpinner.equals("Furgon")){
            txtValorSpinnerCategoriaViaje.setText("1");
        }else if(nombreSpinner.equals("Contenedor 40'")){
            txtValorSpinnerCategoriaViaje.setText("2");
        }else if(nombreSpinner.equals("Contenedor 20'")){
            txtValorSpinnerCategoriaViaje.setText("3");
        }else{
        }
    }


    public void insertarViaje(){
        final String origenViaje = spinnerOrigenViaje.getSelectedItem().toString();
        final String destinoViaje = spinnerLlegadaViaje.getSelectedItem().toString();
        final String fechaInicioViaje = txtRegisterFechaInicioViaje.getText().toString();
        final String fechaFinViaje = txtRegisterFechaLlegadaViaje.getText().toString();
        final String horaInicioViaje = txtRegisterHoraInicioViaje.getText().toString();
        final String horaFinViaje = txtRegisterHoraLlegadaViaje.getText().toString();
        final String cupoViaje = spinnerCupoVehiculo.getSelectedItem().toString();
        final String categoriaViaje = txtValorSpinnerCategoriaViaje.getText().toString();
        final String id_cuenta = txtIdCuentaViaje.getText().toString();
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando");
        if (origenViaje==destinoViaje){
            Toast.makeText(getApplicationContext(),"Por favor seleccione origen o destino diferentes",Toast.LENGTH_SHORT).show();
            spinnerOrigenViaje.requestFocus();
            spinnerLlegadaViaje.requestFocus();
        }else if(fechaInicioViaje.isEmpty()){
            txtRegisterFechaInicioViaje.setError("Complete los datos");
            txtRegisterFechaInicioViaje.requestFocus();
        }else if(fechaFinViaje.isEmpty()){
            txtRegisterFechaLlegadaViaje.setError("Complete los datos");
            txtRegisterFechaLlegadaViaje.requestFocus();
        }else if(horaInicioViaje.isEmpty()){
            txtRegisterHoraInicioViaje.setError("Complete los datos");
            txtRegisterHoraInicioViaje.requestFocus();
        }else if(horaFinViaje.isEmpty()){
            txtRegisterHoraLlegadaViaje.setError("Complete los datos");
            txtRegisterHoraLlegadaViaje.requestFocus();
        }else{
            progressDialog.show();
            StringRequest request= new StringRequest(Request.Method.POST, "http://10.168.0.103/bbdd/CheapTransporte/registroViaje.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.trim().equalsIgnoreCase("Viaje creado correctamente")) {
                        Toast.makeText(RegistroViaje.this, "Viaje creado", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        buscarDatosViaje("http://10.168.0.103/bbdd/CheapTransporte/buscarViaje.php?horaInicioViaje="+horaInicioViaje+"&horaFinViaje="+horaFinViaje+"");
                    } else {
                        Toast.makeText(RegistroViaje.this, response, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        Toast.makeText(RegistroViaje.this, "No se logro registrar", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(RegistroViaje.this,"No se ha podidio conectar",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> parametros= new HashMap<>();
                    parametros.put("origenViaje",origenViaje);
                    parametros.put("destinoViaje",destinoViaje);
                    parametros.put("fechaInicioViaje",fechaInicioViaje);
                    parametros.put("fechaFinViaje",fechaFinViaje);
                    parametros.put("horaInicioViaje",horaInicioViaje);
                    parametros.put("horaFinViaje",horaFinViaje);
                    parametros.put("cupoViaje",cupoViaje);
                    parametros.put("categoriaViaje",categoriaViaje);
                    parametros.put("id_cuenta",id_cuenta);


                    return parametros;
                }
            };

            RequestQueue requestQueue=Volley.newRequestQueue(RegistroViaje.this);
            requestQueue.add(request);
        }
    }

    public void buscarDatosViaje(String URL){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        txtRegisterIdViaje.setText(jsonObject.getString("id_viaje"));
                        strIdViaje = txtRegisterIdViaje.getText().toString().trim();
                        Intent intent = new Intent(getApplicationContext(), InicioCheapTransporte.class);
                        intent.putExtra("idViaje",strIdViaje);
                        startActivity(intent);
                        finish();
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"No se ha conectado para encontrar el viaje",Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue =  Volley.newRequestQueue(RegistroViaje.this);
        requestQueue.add(jsonArrayRequest);
    }

    public void onClick(View v){
        int id = v.getId();
        if(id==R.id.btnRegisterFechaInicioViaje){

            final Calendar calendar= Calendar.getInstance();
            diaInicio= calendar.get(Calendar.DAY_OF_MONTH);
            mesInicio= calendar.get(Calendar.MONTH);
            anoInicio=calendar.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    txtRegisterFechaInicioViaje.setText(year+"-"+(month+1)+"-"+dayOfMonth);
                }
            }, diaInicio,mesInicio,anoInicio);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
            datePickerDialog.show();
        }else if(id==R.id.btnRegisterHoraInicioViaje){

            final Calendar calendar = Calendar.getInstance();
            horaInicio = calendar.get(Calendar.HOUR_OF_DAY);
            minutosInicio = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    txtRegisterHoraInicioViaje.setText(hourOfDay+":"+minute+":00");
                }
            },horaInicio,minutosInicio,true);
            timePickerDialog.show();
        }else if(id==R.id.btnRegisterFechaLlegadaViaje){
            final Calendar calendar= Calendar.getInstance();
            diaLlegada= calendar.get(Calendar.DAY_OF_MONTH);
            mesLlegada= calendar.get(Calendar.MONTH);
            anoLlegada= calendar.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    txtRegisterFechaLlegadaViaje.setText(year+"-"+(month+1)+"-"+dayOfMonth);
                }
            }, diaLlegada,mesLlegada,anoLlegada);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
            datePickerDialog.show();
        }else if(id==R.id.btnRegisterHoraLlegadaViaje){
            final Calendar calendar = Calendar.getInstance();
            horaLlegada = calendar.get(Calendar.HOUR_OF_DAY);
            minutosLlegada = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    txtRegisterHoraLlegadaViaje.setText(hourOfDay+":"+minute+":00");
                }
            },horaLlegada,minutosLlegada,true);
            timePickerDialog.show();
        }else if(id==R.id.btnRegisterViaje){
            valorSpinnerCategoriaVehiculo();
            insertarViaje();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}