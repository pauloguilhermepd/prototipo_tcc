<?php
include("conexao.php");
header("Content-Type: application/json");

$id_publicacao = $_POST['id_publicacao'] ?? '';
$id_perfil_usuario = $_POST['id_perfil_usuario'] ?? '';

if (empty($id_publicacao) || empty($id_perfil_usuario)) {
    echo json_encode(["status" => "erro", "msg" => "Dados incompletos"]);
    exit;
}

$check = $conn->prepare("SELECT * FROM curtidas WHERE id_publicacao = ? AND id_perfil_usuario = ?");
$check->bind_param("is", $id_publicacao, $id_perfil_usuario);
$check->execute();
$result = $check->get_result();

if ($result->num_rows > 0) {
    $delete = $conn->prepare("DELETE FROM curtidas WHERE id_publicacao = ? AND id_perfil_usuario = ?");
    $delete->bind_param("is", $id_publicacao, $id_perfil_usuario);
    $delete->execute();
    echo json_encode(["status" => "removido"]);
} else {
    $insert = $conn->prepare("INSERT INTO curtidas (id_publicacao, id_perfil_usuario) VALUES (?, ?)");
    $insert->bind_param("is", $id_publicacao, $id_perfil_usuario);
    $insert->execute();
    echo json_encode(["status" => "curtido"]);
}
?>