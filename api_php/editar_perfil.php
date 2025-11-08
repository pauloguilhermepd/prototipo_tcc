<?php
header('Content-Type: application/json; charset=UTF-8');
include("conexao.php");

error_reporting(E_ALL);

$uid = $_REQUEST['uid'] ?? '';
$nome = $_REQUEST['nome'] ?? '';
$data_aniver = $_REQUEST['data_aniver'] ?? '';
$bio = $_REQUEST['bio'] ?? '';
$pronome = $_REQUEST['pronome'] ?? '';

if (empty($uid) || empty($nome) || empty($data_aniver) || empty($bio) || empty($pronome)) {
    echo json_encode(["status" => "error", "message" => "Dados de texto incompletos."]);
    exit;
}

if (isset($_FILES['foto_perfil']) && $_FILES['foto_perfil']['error'] == 0) {
    $fotoNome = $_FILES['foto_perfil']['name'];
    $fotoTmp = $_FILES['foto_perfil']['tmp_name'];
    $destino = "uploads/" . uniqid() . "_" . basename($fotoNome);
    
    if (move_uploaded_file($fotoTmp, $destino)) {
        $sql = "UPDATE perfil_usuario SET 
                    nome_completo = ?, 
                    data_nascimento = ?, 
                    biografia = ?, 
                    pronomes = ?, 
                    foto_perfil = ? 
                WHERE id_perfil_usuario = ?";
        
        $stmt = $conn->prepare($sql);
        $stmt->bind_param("ssssss", $nome, $data_aniver, $bio, $pronome, $destino, $uid);

    } else {
        echo json_encode(["status" => "error", "message" => "Falha ao salvar nova imagem."]);
        exit;
    }

} else {
    $sql = "UPDATE perfil_usuario SET 
                nome_completo = ?, 
                data_nascimento = ?, 
                biografia = ?, 
                pronomes = ? 
            WHERE id_perfil_usuario = ?";
    
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("sssss", $nome, $data_aniver, $bio, $pronome, $uid);
}

if ($stmt->execute()) {
    echo json_encode(["status" => "success", "message" => "Perfil atualizado com sucesso."]);
} else {
    echo json_encode(["status" => "error", "message" => "Falha ao atualizar perfil: " . $stmt->error]);
}

$stmt->close();
$conn->close();
?>