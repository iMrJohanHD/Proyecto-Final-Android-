<?php
$con = mysqli_connect('localhost','root','','bbdd_cheaptransporte');
    
    if(!$con){
           echo "Error en conexion";
    }
    
    $horaInicioViaje = $_GET["horaInicioViaje"];
    $horaFinViaje = $_GET["horaFinViaje"];

    $query = "SELECT * FROM viaje WHERE hora_inicio_viaje='$horaInicioViaje' AND hora_fin_viaje='$horaFinViaje'";

    $resultado = $con->query($query);
        while($row = $resultado->fetch_array()){
            $viaje[] = array_map('utf8_encode',$row);
        }
    
        if($resultado->num_rows>0){
          echo json_encode($viaje);
            $resultado->close();
        }else{
            echo "No se logro ingresar";
        }
    $con->close();
?>   