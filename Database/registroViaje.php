<?php
    $con = mysqli_connect('localhost','root','','bbdd_cheaptransporte');
    
    if(!$con){
        echo "Error en conexion";
    }
    
    $origenViaje = $_POST["origenViaje"];
    $destinoViaje = $_POST["destinoViaje"];
    $fechaInicioViaje = $_POST["fechaInicioViaje"];
    $fechaFinViaje = $_POST["fechaFinViaje"];
    $horaInicioViaje = $_POST["horaInicioViaje"];
    $horaFinViaje = $_POST["horaFinViaje"];
    $cupoViaje = $_POST["cupoViaje"];
    $precioViaje = "A convenir";
    $categoriaViaje = $_POST["categoriaViaje"];
    $categoriaViajeValor = intval($categoriaViaje);
    
    
    $idCuenta = $_POST["id_cuenta"];
    $idCuentaValor = intval($idCuenta);

    date_default_timezone_set('America/Bogota');
    $fechaActual = date('Y-m-d H:i:s');

    $idViaje;
    $estadoRegistro = 1;

    $sql ="INSERT INTO viaje (origen_viaje,destino_viaje,fecha_inicio_viaje,fecha_fin_viaje,hora_inicio_viaje,hora_fin_viaje,precio_viaje,cupo_viaje,categoria_viaje)VALUES(?,?,?,?,?,?,?,?,?)";
    $stm=$con->prepare($sql);
    $stm->bind_param('ssssssssi',$origenViaje,$destinoViaje,$fechaInicioViaje,$fechaFinViaje,$horaInicioViaje,$horaFinViaje,$cupoViaje,$precioViaje,$categoriaViajeValor);


    
  

    if($stm->execute()){
        $query = "SELECT * FROM viaje WHERE hora_inicio_viaje='$horaInicioViaje' AND hora_fin_viaje='$horaFinViaje'";

        $resultado = $con->query($query);
        while($row = $resultado->fetch_array()){
            $idViaje= $row['id_viaje'];
        }

        if($resultado->num_rows>0){
            $query1 ="INSERT INTO registro(id_registro_cuenta,id_registro_viaje,estado,hora_registro)VALUES(?,?,?,?)";
            $stm1 = $con->prepare($query1);
            $stm1->bind_param('iiis',$idCuentaValor,$idViaje,$estadoRegistro,$fechaActual);
            $resultado->close();

            if($stm1->execute()){
                echo"Viaje creado correctamente";
            }else{
                echo "Viaje no creado";
            }

        }else{
            echo "Viaje no creado";
        }

       
    }else{
        echo "Viaje no creado";
    }
    $con->close();
   
?>