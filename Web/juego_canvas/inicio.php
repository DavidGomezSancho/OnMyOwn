<?php 
  $usuario=$_REQUEST["usuario"];
  $juegocanvas=@fopen("./datosCanvas.txt", "r");
  $matriz=file("datosCanvas.txt");
  fclose($juegocanvas);

  session_start();
  if (!isset($_SESSION['count'])) {
   $_SESSION['count'] = 0;
  } else {
   $_SESSION['count']++;
  }
  $dividir1=explode(":", $matriz[0]);
  $dividir2=explode(":", $matriz[1]);
  if($dividir1[1]==0){
    $juegocanvas=@fopen("./datosCanvas.txt", "w+");
    $saltoLinea="\r\n";
    fwrite($juegocanvas, $usuario.":1:1:0:".$saltoLinea.$matriz[1]."0:".$saltoLinea.$matriz[3]);
    fclose($juegocanvas);
    echo "1:1".$_SESSION['count'];
  }else{
  if($dividir2[1]==0){
    $juegocanvas=@fopen("./datosCanvas.txt", "w+");
    $saltoLinea="\r\n";
    fwrite($juegocanvas, $matriz[0].$usuario.":2:2:0:".$saltoLinea."0:".$saltoLinea.$matriz[3]);
    fclose($juegocanvas);
    echo "2:2".$_SESSION['count'];
  }else{
	echo "3:3".$_SESSION['count'];
	}
  } 
?>