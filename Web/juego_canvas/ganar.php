<?php 
$identidad=$_REQUEST['identidad'];
$respuesta=$_REQUEST['respuesta'];
if($respuesta=="si"){
  if($identidad=="1"){
    $juegocanvas=@fopen("./datosCanvas.txt", "r");
    $matriz=file("datosCanvas.txt");
    fclose($juegocanvas);
    
    $dividir=explode(":", $matriz[1]);
    $dividir2=explode(":", $matriz[0]); 

    $subir=$dividir[3]+1;
    $juegocanvas=@fopen("./datosCanvas.txt", "w");
    fwrite($juegocanvas, $matriz[0].$dividir[0].":".$dividir[1].":".$dividir[2].":".(string)$subir.":".$dividir[4].$matriz[2].$matriz[3]);
    fclose($juegocanvas);

    $marcador=$dividir2[3]."-".$subir;
  }else{
    $juegocanvas=@fopen("./datosCanvas.txt", "r");
    $matriz=file("datosCanvas.txt");
    fclose($juegocanvas);
    
    $dividir=explode(":", $matriz[0]);
    $dividir2=explode(":", $matriz[1]);  

    $subir=$dividir[3]+1;
    $juegocanvas=@fopen("./datosCanvas.txt", "w");
    fwrite($juegocanvas, $dividir[0].":".$dividir[1].":".$dividir[2].":".$subir.":".$dividir[4].$matriz[1].$matriz[2].$matriz[3]);
    fclose($juegocanvas);

    $marcador=$dividir2[3]."-".$subir;
  }
}else{
  if($identidad=="1"){
    $juegocanvas=@fopen("./datosCanvas.txt", "r");
    $matriz=file("datosCanvas.txt");
    fclose($juegocanvas);
    
    $dividir=explode(":", $matriz[0]); 
    $dividir2=explode(":", $matriz[1]); 
    $subir=$dividir[3]+1;
    $juegocanvas=@fopen("./datosCanvas.txt", "w");
    fwrite($juegocanvas, $dividir[0].":".$dividir[1].":".$dividir[2].":".$subir.":".$dividir[4].$matriz[1].$matriz[2].$matriz[3]);
    fclose($juegocanvas);

    $marcador=$subir."-".$dividir2[3];
  }else{
    $juegocanvas=@fopen("./datosCanvas.txt", "r");
    $matriz=file("datosCanvas.txt");
    fclose($juegocanvas);
    
    $dividir=explode(":", $matriz[1]); 
    $dividir2=explode(":", $matriz[0]); 

    $subir=$dividir[3]+1;
    $juegocanvas=@fopen("./datosCanvas.txt", "w");
    fwrite($juegocanvas, $matriz[0].$dividir[0].":".$dividir[1].":".$dividir[2].":".$subir.":".$dividir[4].$matriz[2].$matriz[3]);
    fclose($juegocanvas);

    $marcador=$subir."-".$dividir2[3];
  }
}
$saltoLinea="\r\n";
$juegocanvas=@fopen("./datosCanvas.txt", "r");
$matriz=file("datosCanvas.txt");
fclose($juegocanvas);
$juegocanvas=@fopen("./datosCanvas.txt", "w");
fwrite($juegocanvas, $matriz[0].$matriz[1].$matriz[2]."default:".$saltoLinea);
fclose($juegocanvas);
unlink("./upload/current.png");
echo $marcador;
?>