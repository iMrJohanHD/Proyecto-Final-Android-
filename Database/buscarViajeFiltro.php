<?php
$con = mysqli_connect('localhost','root','','bbdd_cheaptransporte');
    
if(!$con){
       echo "Error en conexion";
}

$idCuenta = $_GET["idCuenta"];

$query = "SELECT * FROM registro r JOIN viaje v ON r.id_registro_viaje = v.id_viaje WHERE r.estado=1 AND r.id_registro_cuenta='$idCuenta'";

$resultado = $con->query($query);
    while($row = $resultado->fetch_array()){
        $viajeFiltro['viajeFiltro'][] = array_map('utf8_encode',$row);
    }

    if($resultado->num_rows>0){
      echo json_encode($viajeFiltro);
        $resultado->close();
    }else{
        
    }

$con->close();

?>