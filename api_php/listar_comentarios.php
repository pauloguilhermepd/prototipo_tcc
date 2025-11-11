<?php
include("conexao.php");
header("Content-Type: application/json; charset=UTF-8");

$id_publicacoes = $_GET['id_publicacoes'] ?? '';

if (empty($id_publicacoes)) {
    echo json_encode(["status" => "erro", "msg" => "ID da publicação não informado"]);
    exit;
}

$sql = "SELECT c.id_comentario, c.comentarios, c.data_comentario, 
               c.id_usuario,
               u.nome_completo AS autor_nome, u.foto_perfil AS autor_foto
        FROM comentario c
        JOIN perfil_usuario u ON c.id_usuario = u.id_perfil_usuario
        WHERE c.id_publicacoes = ?
        ORDER BY c.data_comentario DESC";

$stmt = $conn->prepare($sql);
$stmt->bind_param("i", $id_publicacoes);
$stmt->execute();
$result = $stmt->get_result();

$comentarios = [];
while ($row = $result->fetch_assoc()) {
    if (!empty($row['autor_foto'])) {
        $row['autor_foto'] = "https://interword-everleigh-coordinately.ngrok-free.dev/api_php/" . $row['autor_foto'];
    }
    $comentarios[] = $row;
}

echo json_encode($comentarios);
?>
