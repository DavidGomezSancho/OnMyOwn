<?php 
  $juegocanvas=@fopen("./datosCanvas.txt", "r");
  $matriz=file("datosCanvas.txt");
  fclose($juegocanvas);
  $dividir=explode(":", $matriz[3]);
  while($dividir[0]=="default"){
    set_time_limit (30);
    usleep(300000);
    $juegocanvas=@fopen("./datosCanvas.txt", "r");
    $matriz=file("datosCanvas.txt");
    fclose($juegocanvas);
    $dividir=explode(":", $matriz[3]);
//Mirar, el logout debe depender de $matriz[2]
    $dividirc=explode(":", $matriz[2]);
    if($dividirc=="1"){
     echo "logout";
     exit;
    }
  }
  echo $dividir[0];
?>