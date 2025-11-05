<?php
include("conexao.php");
header("Content-Type: application/json");

$id_publicacoes = $_GET['id_publicacoes'] ?? '';

if (empty($id_publicacoes)) {
    echo json_encode([]);
    exit;
}

$sql = "SELECT c.id_comentario, c.comentarios, c.data_comentario,
        u.nome_completo AS autor_nome, u.foto_perfil AS autor_foto
        FROM comentario c
        JOIN perfil_usuario u ON u.id_perfil_usuario = c.id_usuario
        WHERE c.id_publicacoes = ?
        ORDER BY c.id_comentario DESC";

$stmt = $conn->prepare($sql);
$stmt->bind_param("i", $id_publicacoes);
$stmt->execute();
$result = $stmt->get_result();

$comentarios = [];

while ($row = $result->fetch_assoc()) {
    $comentarios[] = $row;
}

echo json_encode($comentarios);
?>