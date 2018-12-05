<?php
 while(file_exists("upload/current.png")==false){
    $juegocanvas=@fopen("./datosCanvas.txt", "r");
    $matriz=file("datosCanvas.txt");
    fclose($juegocanvas);
    $dividir=explode(":", $matriz[2]);
    if($dividir=="1"){
     echo "logout";
     exit;
    }
 set_time_limit (30);
 usleep(300000);
 }
 echo "El rival ha pintado";
?>