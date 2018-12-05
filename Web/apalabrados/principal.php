<html>
<body>
<p>
<?php
$letras="holafgir";
$palabra="";
$lenght=strlen($letras);
$punt_palabra=0;
$mostrar=true;
$repetido=array_fill(0,$lenght,1);
$lemario=@fopen("./Diccionarios/lista-sin.txt", "r");
$matriz=file("./Diccionarios/lista-sin.txt");
fclose($lemario);
$longm=count($matriz);

function crear_palabra($palabra, $lenght, $punt_palabra, $n){
	global $letras, $repetido, $mostrar, $palabras, $longm, $matriz;
	$saltoLinea="\r\n";
	for($i=0; $i < $lenght; $i++){
	  set_time_limit(50);
	  $repetido[$i]--;
	  $palabra[$punt_palabra]=$letras[$i];
	  if($repetido[$i]>-1){
	  if($punt_palabra==$n){
		  $intervalo=calc_int($palabra);
		  $mostrado=implode($palabra);
		  for($j=$intervalo[0];$j<$intervalo[1];$j++){
			if(strcmp($matriz[$j],$mostrado."\r\n")==0) {
			  $j=$longm;
			  echo $mostrado;
			}
		  }
	  }else{
	    crear_palabra($palabra, $lenght, $punt_palabra+1, $n);
	  }
	  }
?></p><p><?php
	$repetido[$i]++;
	}
}

function calc_int($palabra){
	switch($palabra[0]){
		case 'a':
		  return array(0,10666);
		  break;
		case 'b':
		  return array(10666, 14307);
		  break;
		case 'c':
		  return array(14307,26387);
		  break;
		case 'd':
		  return array(26387,32087);
		  break;
		case 'e':
		  return array(32087,39160);
		  break;
		case 'f':
		  return array(39160,41953);
		  break;
		case 'g':
		  return array(41953,44770);
		  break;
		case 'h':
		  return array(44770,46905);
		  break;
		case 'i':
		  return array(46905,50034);
		  break;
		case 'j':
		  return array(50034,50958);
		  break;
		case 'k':
		  return array(50958,51057);
		  break;
		case 'l':
		  return array(51057,53439);
		  break;
		case 'm':
		  return array(53439,58584);
		  break;
		case 'n':
		  return array(58584,59932);
		  break;
		case 'ñ':
		  return array(58584,59932);
		  break;
		case 'o':
		  return array(59932,61296);
		  break;
		case 'p':
		  return array(61296,68495);
		  break;
		case 'q':
		  return array(68495,68988);
		  break;
		case 'r':
		  return array(68988, 73308);
		  break;
		case 's':
		  return array(73308,77782);
		  break;
		case 't':
		  return array(77782,82541);
		  break;
		case 'u':
		  return array(82541,82997);
		  break;
		case 'v':
		  return array(82997,84874);
		  break;
		case 'w':
		  return array(84874,84940);
		  break;
		case 'y':
		  return array(84940,84896);
		  break;
		case 'x':
		  return array(84896,85179);
		  break;
		case 'z':
		  return array(85179,85917);
		  break;
	}
	
}

for ($i=1; $i<$lenght; $i++) { 
crear_palabra($palabra, $lenght, $punt_palabra, $i); 
}

?>
</p>
</body>
</html>