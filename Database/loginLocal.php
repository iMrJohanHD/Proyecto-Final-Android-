<?php
    $con = mysqli_connect('localhost','root','','bbdd_cheaptransporte');
    
    if(!$con){
        echo'Error en conexión';
    }

    $email = $_POST['email'];
    $password = $_POST['password'];

    $query = "SELECT * FROM cuenta WHERE email = '$email' AND password ='$password'";
    $resultado = mysqli_query($con,$query);

    if($resultado->num_rows>0){
        echo "Ingreso correctamente";
    }else{
        echo "No se logro ingresar";
    }

    $con->close();
?>