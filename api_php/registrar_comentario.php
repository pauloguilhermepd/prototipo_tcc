<?php
include("conexao.php");
header("Content-Type: application/json");

$id_publicacoes = $_POST['id_publicacoes'] ?? '';
$id_usuario = $_POST['id_usuario'] ?? '';
$comentarios = $_POST['comentarios'] ?? '';

if (empty($id_publicacoes) || empty($id_usuario) || empty($comentarios)) {
    echo json_encode(["status" => "erro", "msg" => "Dados incompletos"]);
    exit;
}

$stmt = $conn->prepare("INSERT INTO comentario (comentarios, data_comentario, id_publicacoes, id_usuario)
                        VALUES (?, NOW(), ?, ?)");
$stmt->bind_param("sis", $comentarios, $id_publicacoes, $id_usuario);

if ($stmt->execute()) {
    echo json_encode(["status" => "sucesso"]);
} else {
    echo json_encode(["status" => "erro", "msg" => $stmt->error]);
}
?>
