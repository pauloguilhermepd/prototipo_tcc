<?php
header("Content-Type: application/json; charset=UTF-8");
include("conexao.php");

$id_comentario = $_POST['id_comentario'] ?? 0;
$id_usuario = $_POST['id_usuario'] ?? '';

if (empty($id_usuario) || empty($id_comentario)) {
    http_response_code(400); 
    echo json_encode(["status" => "error", "message" => "Dados incompletos"]);
    exit;
}

$sql = "DELETE FROM comentario 
        WHERE id_comentario = ? AND id_usuario = ?";

$stmt = $conn->prepare($sql);
$stmt->bind_param("is", $id_comentario, $id_usuario);

if ($stmt->execute()) {
    if ($stmt->affected_rows > 0) {
        http_response_code(200);
        echo json_encode(["status" => "success", "message" => "Comentário excluído"]);
    } else {
        http_response_code(403); 
        echo json_encode(["status" => "error", "message" => "Não autorizado ou não encontrado"]);
    }
} else {
    http_response_code(500); 
    echo json_encode(["status" => "error", "message" => "Falha no banco de dados"]);
}

$stmt->close();
$conn->close();
?>