<?php
header('Content-Type: application/json; charset=UTF-8');
include("conexao.php");

error_reporting(E_ALL);

$uid = $_REQUEST['uid'] ?? '';
$id_publicacao = $_REQUEST['id_publicacao'] ?? 0;
$titulo = $_REQUEST['titulo'] ?? '';
$descricao = $_REQUEST['descricao'] ?? '';

if (empty($uid) || empty($id_publicacao) || empty($titulo)) {
    echo json_encode(["status" => "error", "message" => "Dados incompletos (uid, id_publicacao, titulo)."]);
    exit;
}

if (isset($_FILES['imagem_publi']) && $_FILES['imagem_publi']['error'] == 0) {
    $fotoNome = $_FILES['imagem_publi']['name'];
    $fotoTmp = $_FILES['imagem_publi']['tmp_name'];
    $destino = "uploads/" . uniqid() . "_" . basename($fotoNome);
    
    if (move_uploaded_file($fotoTmp, $destino)) {
        $sql = "UPDATE publicacoes SET 
                    titulo = ?, 
                    descricao = ?, 
                    foto = ? 
                WHERE id_publicacoes = ? AND id_perfil_usuario = ?";
        
        $stmt = $conn->prepare($sql);
        $stmt->bind_param("sssis", $titulo, $descricao, $destino, $id_publicacao, $uid);
        
        if ($stmt->execute()) {
            echo json_encode(["status" => "success", "message" => "Publicação e foto atualizadas."]);
        } else {
            echo json_encode(["status" => "error", "message" => "Falha ao atualizar DB com foto."]);
        }
        $stmt->close();
        $conn->close();
        exit;

    } else {
        echo json_encode(["status" => "error", "message" => "Falha ao salvar nova imagem."]);
        exit;
    }

} else {
    $sql = "UPDATE publicacoes SET 
                titulo = ?, 
                descricao = ? 
            WHERE id_publicacoes = ? AND id_perfil_usuario = ?";
    
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("ssis", $titulo, $descricao, $id_publicacao, $uid);
}

if ($stmt->execute()) {
    if ($stmt->affected_rows > 0) {
        echo json_encode(["status" => "success", "message" => "Publicação atualizada com sucesso."]);
    } else {
        echo json_encode(["status" => "error", "message" => "Nenhuma publicação encontrada ou você não é o dono."]);
    }
} else {
    echo json_encode(["status" => "error", "message" => "Falha ao atualizar publicação: " . $stmt->error]);
}

$stmt->close();
$conn->close();
?>