<?php
include("conexao.php");

$uid = $_POST['uid'];
$nome = $_POST['nome'];
$data_nascimento = $_POST['data_nascimento'];
$biografia = $_POST['biografia'];
$pronomes = $_POST['pronomes'];


$sql = "INSERT INTO perfil_usuario (firebase_uid, nome_completo, data_nascimento, biografia, pronomes) 
        VALUES ('$uid', '$nome', '$data_nascimento', '$biografia', '$pronomes')";

if ($conn->query($sql) === TRUE) {
    echo json_encode(["status" => "ok", "mensagem" => "Usuário registrado"]);
} else {
    echo json_encode(["status" => "erro", "mensagem" => $conn->error]);
}
?>
