<?php 
header("Content-Type: application/json; charset=UTF-8");
$host = "localhost";
$user = "root";
$pass = "";
$dbname = "fitmatch";

$conn = new mysqli($host, $user, $pass, $dbname);

if($conn->connect_error) {
    die("Falha na conexão: " . $conn->connect_error);
}
?>