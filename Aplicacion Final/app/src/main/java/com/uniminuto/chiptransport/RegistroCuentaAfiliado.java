package com.uniminuto.chiptransport;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.loopj.android.http.*;

import cz.msebera.android.httpclient.Header;

public class RegistroCuentaAfiliado extends AppCompatActivity implements View.OnClickListener {
    EditText editTxtRegisterNombre, editTxtRegisterEdad,editTxtRegisterCedula,editTxtRegisterPlacaVehiculo,
            editTxtRegisterCorreo,editTxtRegisterContraseña,editTxtRegisterContraseñaConfirmación;
    TextView txtValorSpinner;
    ImageView imgFotoCedulaCuenta,imgSoatCuenta,imgTarjetaPropiedadCuenta,imgVehiculoCuenta;
    String currentPhotoPatchCedula,currentPhotoPatchSoat,currentPhotoPatchTarjetaPropieda,currentPhotoPatchVehiculo;
    Bitmap decodedCedula,decodedSoat,decodedTarjetaPropiedad,decodedVehiculo;
    RequestQueue requestQueue;
    static final int REQUEST_PERMISSION_CAMERA_CEDULA=100;
    static final int REQUEST_TAKE_PHOTO_CEDULA=101;
    static final int REQUEST_PERMISSION_CAMERA_SOAT=102;
    static final int REQUEST_TAKE_PHOTO_SOAT=103;
    static final int REQUEST_PERMISSION_CAMERA_TARJETA=104;
    static final int REQUEST_TAKE_PHOTO_TARJETA=105;
    static final int REQUEST_PERMISSION_CAMERA_VEHICULO=106;
    static final int REQUEST_TAKE_PHOTO_VEHICULO=107;
    Spinner spinnerNombreAfiliado;
    String [] listaFiliados ={"Transporte Bonda", "Transporte Alegria", "Transporte Honestidad"};
    CheckBox checkBoxTerminos;
    Button btnRegisterRegistrarSocio,btnRegisterLoadCedula,btnRegisterLoadSoat,btnRegisterLoadTarjetaPropiedad,btnRegisterLoadVehiculo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_cuenta_afiliado);

        initUI();
        requestQueue = Volley.newRequestQueue(this);

        btnRegisterLoadCedula.setOnClickListener(this);
        btnRegisterRegistrarSocio.setOnClickListener(this);
        //btnRegisterLoadSoat.setOnClickListener(this);
       // btnRegisterLoadTarjetaPropiedad.setOnClickListener(this);
       // btnRegisterLoadVehiculo.setOnClickListener(this);

    }

    //Metodo para inicializar todos los obejtos de la interfaz
    private void initUI(){
        //Enlace de objetos de la interfaz
        editTxtRegisterNombre = (EditText)findViewById(R.id.editTxtRegisterNombre);
        editTxtRegisterEdad = (EditText)findViewById(R.id.editTxtRegisterEdad);
        editTxtRegisterCedula = (EditText)findViewById(R.id.editTxtRegisterCedula);
        imgFotoCedulaCuenta = (ImageView) findViewById(R.id.imgFotoCedulaCuenta);
        btnRegisterLoadCedula = (Button) findViewById(R.id.btnRegisterLoadCedula);
        spinnerNombreAfiliado = (Spinner)findViewById(R.id.spinnerNombreAfiliado);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,listaFiliados);
        spinnerNombreAfiliado.setAdapter(adapter);
        txtValorSpinner = (TextView) findViewById(R.id.txtValorSpinner);
        editTxtRegisterPlacaVehiculo = (EditText)findViewById(R.id.editTxtRegisterPlacaVehiculo);
        //imgSoatCuenta = (ImageView) findViewById(R.id.imgSoatCuenta);
        //btnRegisterLoadSoat = (Button) findViewById(R.id.btnRegisterLoadSoat);
       // imgTarjetaPropiedadCuenta = (ImageView) findViewById(R.id.imgTarjetaPropiedadCuenta);
        //btnRegisterLoadTarjetaPropiedad = (Button) findViewById(R.id.btnRegisterLoadTarjetaPropiedad);
        //imgVehiculoCuenta = (ImageView) findViewById(R.id.imgVehiculoCuenta);
        //btnRegisterLoadVehiculo = (Button) findViewById(R.id.btnRegisterLoadVehiculo);
        editTxtRegisterCorreo = (EditText)findViewById(R.id.editTxtRegisterCorreo);
        editTxtRegisterContraseña = (EditText)findViewById(R.id.editTxtRegisterContraseña);
        //editTxtRegisterContraseñaConfirmación = (EditText)findViewById(R.id.editTxtRegisterContraseñaConfirmación);
        checkBoxTerminos =(CheckBox) findViewById(R.id.checkBoxTerminos);
        btnRegisterRegistrarSocio =(Button) findViewById(R.id.btnRegisterRegistrarSocio);
    }


    //Metodo para guardar la foto de la cedula con sus preferencias, nombre de acuerdo a la fecha de la toma y la terminacion en este caso .jpg
    private File createImgFileCedula() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imgFileName="JPEG_"+timeStamp+"_";
        File storageDir= getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File img = File.createTempFile(
                imgFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        );
        //Este currentPhotoPatch es la ruta de la foto usada como variable global
        currentPhotoPatchCedula = img.getAbsolutePath();
        return img;
    }

    //Metodo para direccionar a la aplicacion a la camara para tomar la foto
    private void takePictureFullSizeCedula(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager())!=null){
            File photoFile= null;
            try {
                photoFile = createImgFileCedula();
            }catch (IOException e){
                e.getMessage();
            }
            if(photoFile!=null){
                Uri photoUri = FileProvider.getUriForFile(
                        this,
                        "com.uniminuto.chiptransport",//Este es el paquete al que se diriguira la foto que se tome
                        photoFile
                );

                intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
                startActivityForResult(intent,REQUEST_TAKE_PHOTO_CEDULA);
            }
        }
    }

    //Metodo para guardar la foto del Soat con sus preferencias, nombre de acuerdo a la fecha de la toma y la terminacion en este caso .jpg
    private File createImgFileSoat() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imgFileName="JPEG_"+timeStamp+"_";
        File storageDir= getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File img = File.createTempFile(
                imgFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        );
        //Este currentPhotoPatch es la ruta de la foto usada como variable global
        currentPhotoPatchSoat = img.getAbsolutePath();
        return img;
    }

    //Metodo para direccionar a la aplicacion a la camara para tomar la foto
    private void takePictureFullSizeSoat(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager())!=null){
            File photoFile= null;
            try {
                photoFile = createImgFileSoat();
            }catch (IOException e){
                e.getMessage();
            }
            if(photoFile!=null){
                Uri photoUri = FileProvider.getUriForFile(
                        this,
                        "com.uniminuto.chiptransport",//Este es el paquete al que se diriguira la foto que se tome
                        photoFile
                );

                intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
                startActivityForResult(intent,REQUEST_TAKE_PHOTO_SOAT);
            }
        }
    }

    //Metodo para guardar la foto de la tarjeta de propiedad con sus preferencias, nombre de acuerdo a la fecha de la toma y la terminacion en este caso .jpg
    private File createImgFileTarjetaPropiedad() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imgFileName="JPEG_"+timeStamp+"_";
        File storageDir= getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File img = File.createTempFile(
                imgFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        );
        //Este currentPhotoPatch es la ruta de la foto usada como variable global
        currentPhotoPatchTarjetaPropieda = img.getAbsolutePath();
        return img;
    }

    //Metodo para direccionar a la aplicacion a la camara para tomar la foto
    private void takePictureFullSizeTarjetaPropiedad(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager())!=null){
            File photoFile= null;
            try {
                photoFile = createImgFileTarjetaPropiedad();
            }catch (IOException e){
                e.getMessage();
            }
            if(photoFile!=null){
                Uri photoUri = FileProvider.getUriForFile(
                        this,
                        "com.uniminuto.chiptransport",//Este es el paquete al que se diriguira la foto que se tome
                        photoFile
                );

                intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
                startActivityForResult(intent,REQUEST_TAKE_PHOTO_TARJETA);
            }
        }
    }

    //Metodo para guardar la foto del vehiculo con sus preferencias, nombre de acuerdo a la fecha de la toma y la terminacion en este caso .jpg
    private File createImgFileVehiculo() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imgFileName="JPEG_"+timeStamp+"_";
        File storageDir= getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File img = File.createTempFile(
                imgFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        );
        //Este currentPhotoPatch es la ruta de la foto usada como variable global
        currentPhotoPatchVehiculo = img.getAbsolutePath();
        return img;
    }

    //Metodo para direccionar a la aplicacion a la camara para tomar la foto
    private void takePictureFullSizeVehiculo(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager())!=null){
            File photoFile= null;
            try {
                photoFile = createImgFileVehiculo();
            }catch (IOException e){
                e.getMessage();
            }
            if(photoFile!=null){
                Uri photoUri = FileProvider.getUriForFile(
                        this,
                        "com.uniminuto.chiptransport",//Este es el paquete al que se diriguira la foto que se tome
                        photoFile
                );

                intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
                startActivityForResult(intent,REQUEST_TAKE_PHOTO_VEHICULO);
            }
        }
    }

    //Verificar permisos de camara para Cedula
    private void checkPermissionCedula(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){
                takePictureFullSizeCedula();
            }else{
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.CAMERA},
                        REQUEST_PERMISSION_CAMERA_CEDULA
                );
            }
        }else{
            takePictureFullSizeCedula();
        }
    }

    //Metodos para redimencionar la imagen y enviarla a la base de datos en formato de 64
    private void setToImmageViewCedula(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,bytes);
        decodedCedula = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

        imgFotoCedulaCuenta.setImageBitmap(decodedCedula);

    }

    //Verificar permisos de camara para Soat
    private void checkPermissionSoat(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){
                takePictureFullSizeSoat();
            }else{
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.CAMERA},
                        REQUEST_PERMISSION_CAMERA_SOAT
                );
            }
        }else{
            takePictureFullSizeSoat();
        }
    }

    //Metodos para redimencionar la imagen y enviarla a la base de datos en formato de 64
    private void setToImmageViewSoat(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,bytes);
        decodedSoat = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

        imgSoatCuenta.setImageBitmap(decodedSoat);

    }

    //Verificar permisos de camara para Tarjeta de propiedad
    private void checkPermissionTarjetaPropiedad(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){
                takePictureFullSizeTarjetaPropiedad();
            }else{
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.CAMERA},
                        REQUEST_PERMISSION_CAMERA_TARJETA
                );
            }
        }else{
            takePictureFullSizeTarjetaPropiedad();
        }
    }

    //Metodos para redimencionar la imagen y enviarla a la base de datos en formato de 64
    private void setToImmageViewTarjetaPropiedad(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,bytes);
        decodedTarjetaPropiedad = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

        imgTarjetaPropiedadCuenta.setImageBitmap(decodedTarjetaPropiedad);

    }

    //Verificar permisos de camara para Vehiculo
    private void checkPermissionVehiculo(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){
                takePictureFullSizeVehiculo();
            }else{
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.CAMERA},
                        REQUEST_PERMISSION_CAMERA_VEHICULO
                );
            }
        }else{
            takePictureFullSizeVehiculo();
        }
    }

    //Metodos para redimencionar la imagen y enviarla a la base de datos en formato de 64
    private void setToImmageViewVehiculo(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,bytes);
        decodedVehiculo = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

        imgVehiculoCuenta.setImageBitmap(decodedVehiculo);

    }

    private Bitmap getResizedBitmap(Bitmap bitmap, int maxSize) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        //Si las medidas de la imaguen son menores a 1024 no se hara ninguna configuracion y solo se retornara la imaguen tal cual esta
        if(width<=maxSize && height<=maxSize){
            return bitmap;
        }

        float bitmapRatio = (float) width/(float) height;
        if(bitmapRatio>1){
            width = maxSize;
            height = (int) (width/bitmapRatio);
        }else{
            height = maxSize;
            width = (int) (height*bitmapRatio);
        }


        return Bitmap.createScaledBitmap(bitmap,width,height,true);
    }

    private String getStringImage(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] imageBytes = baos.toByteArray();

        return Base64.encodeToString(imageBytes,Base64.DEFAULT);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==REQUEST_PERMISSION_CAMERA_CEDULA){
            if(permissions.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                takePictureFullSizeCedula();
            }
        }
        if(requestCode==REQUEST_PERMISSION_CAMERA_SOAT){
            if(permissions.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                takePictureFullSizeSoat();
            }
        }
        if(requestCode==REQUEST_PERMISSION_CAMERA_TARJETA){
            if(permissions.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                takePictureFullSizeTarjetaPropiedad();
            }
        }
        if(requestCode==REQUEST_PERMISSION_CAMERA_VEHICULO){
            if(permissions.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                takePictureFullSizeVehiculo();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==REQUEST_TAKE_PHOTO_CEDULA) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    File file = new File(currentPhotoPatchCedula);
                    Uri uri = Uri.fromFile(file);
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                            this.getContentResolver(),
                            uri
                    );
                    setToImmageViewCedula(getResizedBitmap(bitmap, 1024));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else if(requestCode==REQUEST_TAKE_PHOTO_SOAT){
            if (resultCode == Activity.RESULT_OK) {
                try {
                    File file = new File(currentPhotoPatchSoat);
                    Uri uri = Uri.fromFile(file);
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                            this.getContentResolver(),
                            uri
                    );
                    setToImmageViewSoat(getResizedBitmap(bitmap, 1024));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else if(requestCode==REQUEST_TAKE_PHOTO_TARJETA){
            if (resultCode == Activity.RESULT_OK) {
                try {
                    File file = new File(currentPhotoPatchTarjetaPropieda);
                    Uri uri = Uri.fromFile(file);
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                            this.getContentResolver(),
                            uri
                    );
                    setToImmageViewTarjetaPropiedad(getResizedBitmap(bitmap, 1024));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else if(requestCode==REQUEST_TAKE_PHOTO_VEHICULO){
            if (resultCode == Activity.RESULT_OK) {
                try {
                    File file = new File(currentPhotoPatchVehiculo);
                    Uri uri = Uri.fromFile(file);
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                            this.getContentResolver(),
                            uri
                    );
                    setToImmageViewVehiculo(getResizedBitmap(bitmap, 1024));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    //Metodo para tomar valores del spinner
    public void valorSpinner(){
       String nombreSpinner = spinnerNombreAfiliado.getSelectedItem().toString();
        if(nombreSpinner.equals("Transporte Bonda")){
            txtValorSpinner.setText("1");
        }else if(nombreSpinner.equals("Transporte Alegria")){
            txtValorSpinner.setText("2");
        }else if(nombreSpinner.equals("Transporte Honestidad")){
            txtValorSpinner.setText("3");
        }else{

        }
    }

    //Metodo para insentar socio
    public void insertarSocio(){
        final String nombreCuenta = editTxtRegisterNombre.getText().toString().trim();
        final String cedulaCuenta = editTxtRegisterCedula.getText().toString().trim();
        final String edadCuenta = editTxtRegisterEdad.getText().toString().trim();
        final String nombreEmpresa = txtValorSpinner.getText().toString().trim();
        final String placaCuenta =editTxtRegisterPlacaVehiculo.getText().toString().trim();
        final String email = editTxtRegisterCorreo.getText().toString().trim();
        final String password = editTxtRegisterContraseña.getText().toString().trim();
        //final String passwordConfirmation = editTxtRegisterContraseñaConfirmación.getText().toString().trim();
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando");
        if(nombreCuenta.isEmpty()){
            editTxtRegisterNombre.setError("Complete los campos");
            editTxtRegisterNombre.requestFocus();
            return;
        }else if(cedulaCuenta.isEmpty()){
            editTxtRegisterCedula.setError("Complete los campos");
            editTxtRegisterCedula.requestFocus();
            return;
        }else if(edadCuenta.isEmpty()){
            editTxtRegisterEdad.setError("Complete los campos");
            editTxtRegisterEdad.requestFocus();
            return;
        }else if(Integer.parseInt(edadCuenta)<18||Integer.parseInt(edadCuenta)>70){
            editTxtRegisterEdad.setError("Edad ingresada no soportada");
            editTxtRegisterEdad.requestFocus();
        }else if(nombreEmpresa==null){
            Toast.makeText(getApplicationContext(),"No has seleccionado ninguna empresa afiliada",Toast.LENGTH_SHORT).show();
            spinnerNombreAfiliado.requestFocus();
            return;
        }else if(placaCuenta.isEmpty()){
            editTxtRegisterPlacaVehiculo.setError("Complete los campos");
            editTxtRegisterPlacaVehiculo.requestFocus();
            return;
        }else if(email.isEmpty()){
            editTxtRegisterCorreo.setError("Complete los campos");
            editTxtRegisterCorreo.requestFocus();
            return;
        }else if(password.isEmpty()){
            editTxtRegisterContraseña.setError("Complete los campos");
            editTxtRegisterContraseña.requestFocus();
            return;
        }/*else if(passwordConfirmation.isEmpty()){
            editTxtRegisterContraseñaConfirmación.setError("Complete los campos");
            editTxtRegisterContraseñaConfirmación.requestFocus();
            return;
        }else if(password!=passwordConfirmation){
            editTxtRegisterContraseñaConfirmación.setError("Las contraseñas ingresadas no coinciden");
            editTxtRegisterContraseñaConfirmación.requestFocus();
            return;
        }*/else if(checkBoxTerminos.isChecked()!=true){
            checkBoxTerminos.setError("Debes aceptar los terminos y condiciones para continuar");
            checkBoxTerminos.requestFocus();
            return;
        }else{
            progressDialog.show();
            StringRequest request= new StringRequest(Request.Method.POST, "http://10.168.0.103/bbdd/CheapTransporte/registroAfiliacionLocal1.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.trim().equalsIgnoreCase("Registro ingresado correctamente")) {
                        Toast.makeText(RegistroCuentaAfiliado.this, "Datos insertados", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        Intent intent = new Intent(RegistroCuentaAfiliado.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(RegistroCuentaAfiliado.this, response, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        Toast.makeText(RegistroCuentaAfiliado.this, "No se logro registrar", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(RegistroCuentaAfiliado.this,"No se ha podidio conectar",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> parametros= new HashMap<>();
                    parametros.put("nombreCuenta",nombreCuenta);
                    parametros.put("cedulaCuenta",cedulaCuenta);
                    parametros.put("edadCuenta",edadCuenta);
                    parametros.put("placaCuenta",placaCuenta);
                    parametros.put("nombreEmpresa",nombreEmpresa);
                    //Aca se hace la codificacion base 64 para el alamacenamiento de la imagen
                    //parametros.put("img_soat_cuenta",getStringImage(decodedSoat));
                    //parametros.put("img_tarjeta_propiedad_cuenta",getStringImage(decodedTarjetaPropiedad));
                    parametros.put("img_cedula_cuenta",getStringImage(decodedCedula));
                   // parametros.put("img_vehiculo_cuenta",getStringImage(decodedVehiculo));
                    parametros.put("email",email);
                    parametros.put("password",password);


                    return parametros;
                }
            };

            RequestQueue requestQueue=Volley.newRequestQueue(RegistroCuentaAfiliado.this);
            requestQueue.add(request);
        }
    }
    @Override
    public void onClick(View v){
        int id = v.getId();
        if(id==R.id.btnRegisterLoadCedula){
            checkPermissionCedula();
        }/*else if(id==R.id.btnRegisterLoadSoat){
            checkPermissionSoat();
        }else if(id==R.id.btnRegisterLoadTarjetaPropiedad){
            checkPermissionTarjetaPropiedad();
        }else if(id==R.id.btnRegisterLoadVehiculo){
            checkPermissionVehiculo();
        }*/else if(id==R.id.btnRegisterRegistrarSocio){
            valorSpinner();
            insertarSocio();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}