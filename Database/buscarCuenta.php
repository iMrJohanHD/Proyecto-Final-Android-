<?php
    $con = mysqli_connect('localhost','root','','bbdd_cheaptransporte');
    
    if(!$con){
           echo "Error en conexion";
    }
   
    $email = $_GET['email'];
    $password = $_GET['password'];

    $query = "SELECT * FROM cuenta WHERE email='$email' AND password='$password'";

    $resultado = $con->query($query);
    while($row = $resultado->fetch_array()){
        $cuenta[] = array_map('utf8_encode',$row);
    }
    
    /*$query = "SELECT * FROM cuenta WHERE email = '$email' AND password ='$password'";
    $resultado = mysqli_query($con,$query);*/

    if($resultado->num_rows>0){
        echo json_encode($cuenta);
        $resultado->close();
    }else{
        echo "No se logro ingresar";
    }
   $con->close();
?>