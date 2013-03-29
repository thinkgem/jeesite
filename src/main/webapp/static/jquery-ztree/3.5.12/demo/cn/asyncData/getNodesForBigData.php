<?php ?>
[<?php
$pId = "-1";
if(array_key_exists( 'id',$_REQUEST)) {
	$pId=$_REQUEST['id'];
}
$pCount = "10";
if(array_key_exists( 'count',$_REQUEST)) {
	$pCount=$_REQUEST['count'];
}
if ($pId==null || $pId=="") $pId = "0";
if ($pCount==null || $pCount=="") $pCount = "10";

$max = (int)$pCount;
for ($i=1; $i<=$max; $i++) {
	$nId = $pId."_".$i;
	$nName = "tree".$nId;
	echo "{ id:'".$nId."',	name:'".$nName."'}";
	if ($i<$max) {
		echo ",";
	}
	
}
?>]