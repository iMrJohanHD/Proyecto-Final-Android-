package com.uniminuto.chiptransport;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uniminuto.chiptransport.fragment.ViajesFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    //Creacion de la variable de tipo boton
    EditText txtEmail,txtPassword;
    String strEmail, strPassword,strIdCuenta;
    RequestQueue requestQueue;
    TextView txtLoginIdCuenta;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Implementacion del metodo loginRegister para pasar a la ventana de RegistroCuentaAfiliado
        loginRegister();
        verManualUser();

        txtEmail =(EditText) findViewById(R.id.editTxtLoginCorreo);
        txtPassword =(EditText) findViewById(R.id.editTxtLoginContraseña);
        txtLoginIdCuenta = (TextView) findViewById(R.id.txtLoginIdCuenta);
    }

    public void login(View view){
        if(txtEmail.getText().toString().equals("")){
            Toast.makeText(this, "Ingrese un correo", Toast.LENGTH_SHORT).show();
        }else if(txtPassword.getText().toString().equals("")){
            Toast.makeText(this, "Ingrese una contraseña", Toast.LENGTH_SHORT).show();
        }else{
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Por favor espere");
            progressDialog.show();

            strEmail=txtEmail.getText().toString().trim();
            strPassword=txtPassword.getText().toString().trim();

            StringRequest request = new StringRequest(Request.Method.POST, "http://10.168.0.103/bbdd/CheapTransporte/loginLocal.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();

                    if (response.equalsIgnoreCase("Ingreso correctamente")) {
                        buscarDatosCuenta("http://10.168.0.103/bbdd/CheapTransporte/buscarCuenta.php?email="+strEmail+"&password="+strPassword+"");
                    } else {
                        Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this,"No se ha podidio conectar", Toast.LENGTH_SHORT).show();
                }

        }){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> parametros = new HashMap<>();
                    parametros.put("email",strEmail);
                    parametros.put("password",strPassword);
                    return parametros;
                }
            };
            requestQueue=  Volley.newRequestQueue(MainActivity.this);
            requestQueue.add(request);
        }
    }


    public void buscarDatosCuenta(String URL){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        txtLoginIdCuenta.setText(jsonObject.getString("id_cuenta"));
                        strIdCuenta = txtLoginIdCuenta.getText().toString().trim();
                        Intent intent = new Intent(getApplicationContext(), InicioCheapTransporte.class);
                        intent.putExtra("idCuenta",strIdCuenta);
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
                Toast.makeText(getApplicationContext(),"No",Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue =  Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(jsonArrayRequest);
    }
    //Creacion de un metodo para acceder a RegistroCuentaAfiliado
    public void loginRegister(){
        Button btnLoginRegister = (Button) findViewById(R.id.btnLoginRegister);
        btnLoginRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(MainActivity.this, RegistroCuentaAfiliado.class);
                startActivity(i);
            }
        });
    }

    public void verManualUser(){
        FloatingActionButton fabMainManualUser = (FloatingActionButton) findViewById(R.id.fabMainManualUser);
        fabMainManualUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ManualUser.class);
                startActivity(intent);
            }
        });
    }
}