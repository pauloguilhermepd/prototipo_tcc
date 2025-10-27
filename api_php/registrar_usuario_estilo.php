<?php
header('Content-Type: application/json');
include 'conexao.php';

error_reporting(0); // evita quebrar JSON

$uid = $_REQUEST['uid'] ?? '';
$nome = $_REQUEST['nome'] ?? '';
$data = $_REQUEST['data_aniver'] ?? '';
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

$stmt = $conn->prepare("INSERT INTO usu_estilo (id_estilo, id_perfil_usuario)
                        VALUES (?, ?, ?, ?, ?, ?)");
$stmt->bind_param("ssssss", $uid, $nome, $data, $bio, $pronome, $destino);

if ($stmt->execute()) {
    echo json_encode(["status" => "success", "id_perfil_usuario" => $conn->insert_id]);
} else {
    echo json_encode(["status" => "error", "message" => $stmt->error]);
}

?>

