<html>
<body>
<p>
<?php
$letras="hola";
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
		//for($e=0; $e < $lenght; $e++){if($repetido[$e]<0) {$mostrar=false;$e=$lenght;}}
		//if($mostrar==true){
		  $mostrado=implode($palabra);
		  echo $mostrado;
		  for($j=0;$j<$longm;$j++){
			if(strcmp($matriz[$j],$mostrado."\r\n")==0) {
			  $j=$longm;
			  echo $mostrado;
			}
		  }
		//}else{$mostrar=true;}
	  }else{
	    crear_palabra($palabra, $lenght, $punt_palabra+1, $n);
	  }
	  }
?></p><p><?php
	$repetido[$i]++;
	}
}

for ($i=1; $i<$lenght; $i++) { 
crear_palabra($palabra, $lenght, $punt_palabra, $i); 
}

?>
</p>
</body>
</html>