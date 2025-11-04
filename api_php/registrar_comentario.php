<?php
include("conexao.php");

$id_publicacoes = $_POST['id_publicacoes'];
$id_usuario = $_POST['id_usuario'];
$comentarios = $_POST['comentarios'];

$sql = "INSERT INTO comentario (comentarios, data_comentario, id_publicacoes, id_usuario)
        VALUES ('$comentarios', NOW(), '$id_publicacoes', '$id_usuario')";

if ($conn->query($sql) === TRUE) {
    echo json_encode(["status" => "ok", "mensagem" => "Comentário registrado"]);
} else {
    echo json_encode(["status" => "erro", "mensagem" => $conn->error]);
}
?>