<?php
include("conexao.php");
header("Content-Type: application/json; charset=UTF-8");

$id_publicacoes = $_POST['id_publicacoes'] ?? '';
$id_usuario = $_POST['id_usuario'] ?? '';
$comentarios = $_POST['comentarios'] ?? '';

if (empty($id_publicacoes) || empty($id_usuario) || empty($comentarios)) {
    echo json_encode(["status" => "erro", "msg" => "Dados incompletos"]);
    exit;
}

$sql = "INSERT INTO comentario (comentarios, data_comentario, id_publicacoes, id_usuario)
        VALUES (?, NOW(), ?, ?)";
$stmt = $conn->prepare($sql);
$stmt->bind_param("sis", $comentarios, $id_publicacoes, $id_usuario);

if ($stmt->execute()) {
    echo json_encode(["status" => "sucesso", "msg" => "Comentário registrado com sucesso"]);
} else {
    echo json_encode(["status" => "erro", "msg" => "Falha ao registrar comentário"]);
}
?>
