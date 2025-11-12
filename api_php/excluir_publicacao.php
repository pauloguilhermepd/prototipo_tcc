<?php
header("Content-Type: application/json; charset=UTF-8");
include("conexao.php");

$id_publicacao = $_POST['id_publicacao'] ?? 0;
$id_usuario = $_POST['id_usuario'] ?? ''; 

if (empty($id_usuario) || empty($id_publicacao)) {
    http_response_code(400); 
    echo json_encode(["status" => "erro", "msg" => "Dados incompletos"]);
    exit;
}

$sql = "DELETE FROM publicacoes 
        WHERE id_publicacoes = ? AND id_perfil_usuario = ?";

$stmt = $conn->prepare($sql);
$stmt->bind_param("is", $id_publicacao, $id_usuario);

if ($stmt->execute()) {
    if ($stmt->affected_rows > 0) {
        http_response_code(200);
        echo json_encode(["status" => "sucesso", "msg" => "Publicacao excluida"]);
    } else {
        http_response_code(403); 
        echo json_encode(["status" => "erro", "msg" => "Usuario nao autorizado ou publicacao nao encontrada"]);
    }
} else {
    http_response_code(500); 
    echo json_encode(["status" => "erro", "msg" => "Falha no banco de dados"]);
}

$stmt->close();
$conn->close();
?>