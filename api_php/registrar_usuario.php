<?php
header('Content-Type: application/json; charset=UTF-8');
include("conexao.php");

error_reporting(E_ALL); // evita quebrar JSON

$uid = $_REQUEST['uid'] ?? '';
$nome = $_REQUEST['nome'] ?? '';
$data_aniver = $_REQUEST['data_aniver'] ?? '';
$bio = $_REQUEST['bio'] ?? '';
$pronome = $_REQUEST['pronome'] ?? '';

if (isset($_FILES['foto_perfil'])) {
    $fotoNome = $_FILES['foto_perfil']['name'];
    $fotoTmp = $_FILES['foto_perfil']['tmp_name'];
    $destino = "uploads/" . uniqid() . "_" . basename($fotoNome);
    move_uploaded_file($fotoTmp, $destino);
} else {
    $destino = "uploads/default.png";
}

$stmt = $conn->prepare("INSERT INTO perfil_usuario (id_perfil_usuario, nome_completo, data_nascimento, biografia, pronomes, foto_perfil)
                        VALUES (?, ?, ?, ?, ?, ?)");
$stmt->bind_param("ssssss", $uid, $nome, $data_aniver, $bio, $pronome, $destino);

if ($stmt->execute()) {
    echo json_encode(["status" => "success", "id_perfil_usuario" => $conn->$uid]);
} else {
    echo json_encode(["status" => "error", "message" => $stmt->error]);
}

?>

