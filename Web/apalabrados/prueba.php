<html>
<head></head>
<body>
<p>
<?php 
  $lemario=@fopen("./Diccionarios/lista-corta.txt", "r");
  $matriz=file("./Diccionarios/lista-corta.txt");
  fclose($lemario);
  $long=count($matriz);
  echo $long;
?></p>
</body>
</html>