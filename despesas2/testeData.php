<?php
 echo date("d/m/Y H:i:s", 
   mktime(1, 2, 3, 3, 23, 13));
   
   echo "<p>" . date("d/m/Y H:i:s", mktime(1, 2, 3, 3, 23, 13));
   echo "<p>" . date("Ymd", mktime(1, 2, 3, 3, 23, 13));
   
   echo "<p>x=" . x("23/02/2014");

function fmtYMD($data) {
  $d = substr($data, 0,2);
  $m = substr($data, 3,2);
  $a = substr($data, 6,4);
  
  echo "<br>d=$d, m=$m, a=$a<br>";
  
  return $a . $m . $d;
}   
   
?>
