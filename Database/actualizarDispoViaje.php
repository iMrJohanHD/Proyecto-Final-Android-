<?php
$con = mysqli_connect('localhost','root','','bbdd_cheaptransporte');
    
if(!$con){
       echo "Error en conexion";
}

$idRegistro = $_POST["idRegistro"];
$idRegistroValor = intval($idRegistro);

$query = "UPDATE registro SET estado=0 WHERE id_registro = '$idRegistroValor'";
    $resultado = mysqli_query($con,$query);

    if($resultado->num_rows>0){
        echo "Disponibilidad cambiada correctamente";
    }else{
        echo "No se logro cambiar la disponibilidad del viaje";
    }


$con->close();

?>