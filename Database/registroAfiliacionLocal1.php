<?php
    $con = mysqli_connect('localhost','root','','bbdd_cheaptransporte');
    
    if(!$con){
        echo "Error en conexion";
    }

    $nombreCuenta = $_POST["nombreCuenta"];
    $cedulaCuenta = $_POST["cedulaCuenta"];
    $cedulaCuentaValor = intval($cedulaCuenta);
    $edadCuenta = $_POST["edadCuenta"];
    $edadCuentaValor = intval($edadCuenta);
    $placaCuenta = $_POST["placaCuenta"];
    $nombreEmpresa = $_POST["nombreEmpresa"];
    $nombreEmpresaValor = intval($nombreEmpresa);
    /*$img_soat_cuenta = $_POST["img_soat_cuenta"];
    $img_tarjeta_propiedad_cuenta = $_POST["img_tarjeta_propiedad_cuenta"];*/
    $img_cedula_cuenta = $_POST["img_cedula_cuenta"];
    /*$img_vehiculo_cuenta = $_POST["img_vehiculo_cuenta"];*/
    $email = $_POST["email"];
    $password = $_POST["password"];
    $rol = "tipo2";


    /*$pathSoat="imagenes/$nombreCuenta.jpg";
    $urlFoto = "http://localhost//bbdd/CheapTransporte/imagenes/$pathSoat";

    $pathTarjetaPropiedad="imagenes/$nombreCuenta.jpg";
    $urlFoto = "http://localhost//bbdd/CheapTransporte/imagenes/$pathTarjetaPropiedad";*/

    $pathCedula="imagenes/$nombreCuenta.jpg";
    $urlFoto = "http://localhost//bbdd/CheapTransporte/imagenes/$pathCedula";
    
    /*$pathVehiculo="imagenes/$nombreCuenta.jpg";
    $urlFoto = "http://localhost//bbdd/CheapTransporte/imagenes/$pathVehiculo";*/

    /*file_put_contents($pathSoat,base64_decode($img_soat_cuenta));
    $bytesArchivoSoat= file_get_contents($pathSoat);
    file_put_contents($pathTarjetaPropiedad,base64_decode($img_tarjeta_propiedad_cuenta));
    $bytesArchivoTarjetaPropiedad= file_get_contents($pathTarjetaPropiedad);*/
    file_put_contents($pathCedula,base64_decode($img_cedula_cuenta));
    $bytesArchivoCedula= file_get_contents($pathCedula);
    /*file_put_contents($pathVehiculo,base64_decode($img_vehiculo_cuenta));
    $bytesArchivoVehiculo= file_get_contents($pathVehiculo);*/
    
   
  

    $sql ="INSERT INTO cuenta (nombre_cuenta,cedula_cuenta,edad_cuenta,placa_cuenta,afiliado_cuenta,img_cedula_cuenta,email,password,rol_cuenta)VALUES(?,?,?,?,?,?,?,?,?)";
    $stm=$con->prepare($sql);
    $stm->bind_param('siisissss',$nombreCuenta,$cedulaCuentaValor,$edadCuentaValor,$placaCuenta,$nombreEmpresaValor,$bytesArchivoCedula,$email,$password,$rol);

    /*$sql ="INSERT INTO cuenta (nombre_cuenta,cedula_cuenta,edad_cuenta,placa_cuenta,afiliado_cuenta,email,password,rol_cuenta)VALUES(?,?,?,?,?,?,?,?)";
    $stm=$con->prepare($sql);
    $stm->bind_param('siisisss',$nombreCuenta,$cedulaCuentaValor,$edadCuentaValor,$placaCuenta,$nombreEmpresaValor,$email,$password,$rol);*/

    /*$query ="INSERT INTO cuenta (nombre_cuenta,cedula_cuenta,edad_cuenta,placa_cuenta,afiliado_cuenta,email,password,rol_cuenta) VALUES (?,?,?,?,?,?,?,?)";
    $stm=$con->prepare($query);
    $stm->bind_param('siisisss',$nombreCuenta,$cedulaCuentaValor,$edadCuentaValor,$placaCuenta,$nombreEmpresaValor,$email,$password,$rol);
    $resultado = mysqli_query($con,$query);*/


    if($stm->execute()){
        echo"Registro ingresado correctamente";
    }else{
        echo"Hubo un error al registrar al usuario, intentalo de nuevo";
    }
  
    $con->close();
?>