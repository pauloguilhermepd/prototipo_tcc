<?php
include("conexao.php");
header("Content-Type: application/json; charset=UTF-8");

$uid = $_GET['uid'] ?? '';

if (empty($uid)) {
    echo json_encode([]); 
    exit;
}

$sql = "SELECT 
    p.id_publicacoes,
    p.titulo,
    p.descricao,
    p.foto,
    u.nome_completo AS autor_nome,
    u.foto_perfil AS autor_foto,
    (SELECT COUNT(*) FROM curtidas c WHERE c.id_publicacao = p.id_publicacoes) AS curtidas
FROM publicacoes p
JOIN perfil_usuario u ON p.id_perfil_usuario = u.id_perfil_usuario
WHERE p.id_perfil_usuario = ? 
ORDER BY p.id_publicacoes DESC";

$stmt = $conn->prepare($sql);
$stmt->bind_param("s", $uid);
$stmt->execute();
$result = $stmt->get_result();

$publicacoes = [];

while ($row = $result->fetch_assoc()) {
    if (!empty($row['foto'])) {
        $row['foto'] = "https://abby-unbeaued-tyron.ngrok-free.dev/api_php/" . $row['foto'];
    }
    if (!empty($row['autor_foto'])) {
        $row['autor_foto'] = "https://abby-unbeaued-tyron.ngrok-free.dev/api_php/" . $row['autor_foto'];
    }
    $publicacoes[] = $row;
}

echo json_encode($publicacoes);

$stmt->close();
$conn->close();
?>