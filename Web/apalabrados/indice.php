<html>
<body>
<p>

<?php
$lemario=@fopen("./Diccionarios/lista-sin.txt", "r");
$matriz=file("./Diccionarios/lista-sin.txt");
fclose($lemario);
$alfabeto=array("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "y", "x", "z");
$posicion=array(0, 10666, 14307, 26387, 32087, 39160, 41953, 44770, 46905, 50034, 
50958, 51057, 53439, 58584, 59932, 61296,68495, 68988, 73308, 77782, 82541, 82997,
 84874, 84940, 84896, 85179);

function a(){

}
$i=count($alfabeto);
return $i;
}
echo a()."||";
echo count($matriz);

?>

</p>
</body>
</html>