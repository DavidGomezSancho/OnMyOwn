<?php
 $respuesta=$_REQUEST['respuesta'];
 $identidad=$_REQUEST['identidad'];
 $saltoLinea="\r\n";
 $juegocanvas=@fopen("./datosCanvas.txt", "r");
 $matriz=file("datosCanvas.txt");
 fclose($juegocanvas);
 $juegocanvas=@fopen("./datosCanvas.txt", "w");
 fwrite($juegocanvas, $matriz[0].$matriz[1].$matriz[2].$respuesta.":".$saltoLinea);
 fclose($juegocanvas);
 $respondido[0]="";
 while($respondido[0]!="default"){
  set_time_limit (30);
  usleep(300000);
  $juegocanvas=@fopen("./datosCanvas.txt", "r");
  $matriz=file("datosCanvas.txt");
  fclose($juegocanvas);
  $respondido=explode(":",$matriz[3]);
 }

$dividir1=explode(":",$matriz[0]);
$dividir2=explode(":",$matriz[1]);
if($identidad=="1"){
 $marcador=$dividir1[3]."-".$dividir2[3];
}else{
 $marcador=$dividir2[3]."-".$dividir1[3];
}
echo $marcador;
?>