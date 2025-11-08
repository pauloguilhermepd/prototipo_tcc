<?php 
include("conexao.php");
header("Content-Type: application/json; charset=UTF-8");

$id_perfil_usuario = $_POST['id_perfil_usuario'] ?? '';


if (empty($id_perfil_usuario) || empty($estilo)) {
    echo json_encode(["status" => "erro", "mensagem" => "Dados incompletos."]);
    exit;
}

?>