<html>
<body>
<p>
<?php
$palabras[0]="n";
$letras=$_REQUEST['respuesta'];
$lenght=strlen($letras);
$punt_palabra=0;
$mostrar=true;
$repetido=array_fill(0,$lenght,1);
$lemario=@fopen("./Diccionarios/lista-sin.txt", "r");
$matriz=file("./Diccionarios/lista-sin.txt");
fclose($lemario);
$longm=count($matriz);

//echo "<a target='_blank' HREF='http://lema.rae.es/drae/?val=".$ensenar."'>".$ensenar."</a> ||";

function busqueda(){
  global $letras, $lenght, $matriz, $repetido, $palabras;
  $punt_palabra=0;
  $tam_max=0;
  for($i=0; $i<$lenght; $i++){
    $repetido[$i]--;
    $intervalo=calc_int($letras[$i]);
    for($punt_mtz=$intervalo[0]; $punt_mtz<$intervalo[1]; $punt_mtz++){
	if($lenght+2 >= strlen($matriz[$punt_mtz])){
	  $palabra=compr_palabra($punt_mtz, 1, strlen($matriz[$punt_mtz]));
	  $ensenar=substr ( $matriz[$punt_mtz], 0, strlen($matriz[$punt_mtz])-2 );
	  if($palabra==true) { 
		$palabras[$punt_palabra]=$ensenar; 
		$tamanyo=strlen($palabras[$punt_palabra]);
		$punt_palabra++;
		if($tamanyo>$tam_max){$tam_max=$tamanyo;}
	  }
	}
    }
    $repetido[$i]++;
  }
//$o tamaño palabra
//$e palabra dentro de la matriz
for($o=$tam_max; $o>1; $o--){
 echo "<p>Palabras de longitud ".$o." :</p>";
 for($e=0; $e<$punt_palabra; $e++){
  $mostrar=true;
  if(strlen($palabras[$e])==$o){
	for($u=$e-1;$u>-1;$u--){
	  if(strcmp($palabras[$e], $palabras[$u])==0){
		$mostrar=false;
	  }
	}
	if($mostrar==true){
	  echo " ||<a target='_blank' HREF='http://lema.rae.es/drae/?val=".$palabras[$e]."' style='text-decoration:none;color:blue;'>".$palabras[$e]."</a>";
	}
  }
 }
}
}

function compr_palabra($punt_mtz, $punt_palabra, $long_palabra){
  global $letras, $lenght, $matriz, $repetido;
  
  $respuesta=false;
  $palabra=$matriz[$punt_mtz];
  for($letra=0; $letra<$lenght; $letra++){
    if($repetido[$letra]!=0){
	if(strcmp($palabra[$punt_palabra], $letras[$letra])==0){
	  if($long_palabra-2==$punt_palabra+1){
	    $respuesta=true;
	    $letra=$lenght;

	  }else{
	    $repetido[$letra]--;
	    $respuesta=compr_palabra($punt_mtz, $punt_palabra+1, $long_palabra);
	    $repetido[$letra]++;
	    if($respuesta==true){$letra=$lenght;}
	  }
	}
    }
  }
  return $respuesta;
}

function ordenar($palabras){
  $num_max;
  $lenght=count($palabras);
  for($i=0; $i<$lenght; $i++){
    $palabra=$palabras[$i];
  }
}

function calc_int($palabra){
	switch($palabra){
		case 'a':
		  return array(0,10666);
		  break;
		case 'b':
		  return array(10665, 14307);
		  break;
		case 'c':
		  return array(14306,26387);
		  break;
		case 'd':
		  return array(26386,32087);
		  break;
		case 'e':
		  return array(32086,39160);
		  break;
		case 'f':
		  return array(39159,41953);
		  break;
		case 'g':
		  return array(41952,44770);
		  break;
		case 'h':
		  return array(44769,46905);
		  break;
		case 'i':
		  return array(46904,50034);
		  break;
		case 'j':
		  return array(50033,50958);
		  break;
		case 'k':
		  return array(50957,51057);
		  break;
		case 'l':
		  return array(51056,53439);
		  break;
		case 'm':
		  return array(53438,58584);
		  break;
		case 'n':
		  return array(58583,59932);
		  break;
		case 'ñ':
		  return array(58583,59932);
		  break;
		case 'o':
		  return array(59931,61296);
		  break;
		case 'p':
		  return array(61295,68495);
		  break;
		case 'q':
		  return array(68494,68988);
		  break;
		case 'r':
		  return array(68987, 73308);
		  break;
		case 's':
		  return array(73307,77782);
		  break;
		case 't':
		  return array(77781,82541);
		  break;
		case 'u':
		  return array(82540,82997);
		  break;
		case 'v':
		  return array(82996,84874);
		  break;
		case 'w':
		  return array(84873,84940);
		  break;
		case 'y':
		  return array(84939,84896);
		  break;
		case 'x':
		  return array(84895,85179);
		  break;
		case 'z':
		  return array(85178,85917);
		  break;
	}
	
}

busqueda();
?>
</p>
</body>
</html>