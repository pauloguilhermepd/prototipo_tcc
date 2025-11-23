<?php
header('Content-Type: application/json; charset=UTF-8');
include("conexao.php");

error_reporting(E_ALL);

$uid = $_REQUEST['uid'] ?? '';
$descricao = $_REQUEST['descricao'] ?? '';
$titulo = $_REQUEST['titulo'] ?? '';


if (isset($_FILES['imagem_publi'])) {
    $fotoNome = $_FILES['imagem_publi']['name'];
    $fotoTmp = $_FILES['imagem_publi']['tmp_name'];
    $destino = "uploads/" . uniqid() . "_" . basename($fotoNome);
    move_uploaded_file($fotoTmp, $destino);
} else {
    $destino = "uploads/default.png";
}

$stmt = $conn->prepare("INSERT INTO publicacoes (descricao, foto, titulo, id_perfil_usuario)
                        VALUES (?, ?, ?, ?)");
$stmt->bind_param("ssss", $descricao, $destino, $titulo, $uid);

if ($stmt->execute()) {
    echo json_encode(["status" => "success", "id_perfil_usuario" => $conn->$uid]);
} else {
    echo json_encode(["status" => "error", "message" => $stmt->error]);
}

?>