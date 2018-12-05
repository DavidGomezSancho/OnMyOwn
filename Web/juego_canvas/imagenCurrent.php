<?php
  // read input stream
    $data = file_get_contents("php://input");
 
    // filtering and decoding code adapted from
    // Filter out the headers (data:,) part.
    $filteredData=substr($data, strpos($data, ",")+1);
    // Need to decode before saving since the data we received is already base64 encoded
    $decodedData=base64_decode($filteredData);
 
    // store in server
    $fic_name = 'current'.'.png';
    $fp = fopen('./upload/'.$fic_name, 'wb');
    fwrite( $fp, $decodedData);
    fclose( $fp );
    echo "./upload/current.png";
?>