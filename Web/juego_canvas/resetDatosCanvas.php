 <?php
   $saltoLinea="\r\n";
   $doslineas="default:0:0:0:";
   $juegocanvas=@fopen("./datosCanvas.txt", "w");
    fwrite($juegocanvas, $doslineas.$saltoLinea.$doslineas.$saltoLinea."0:".$saltoLinea."default:".$saltoLinea);
    fclose($juegocanvas);
 ?>