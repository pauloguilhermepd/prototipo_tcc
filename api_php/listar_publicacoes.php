<?php
include("conexao.php");
header("Content-Type: application/json; charset=UTF-8");

$uid_logado = $_GET['uid'] ?? '0';

$sql = "SELECT 
    p.id_publicacoes,
    p.titulo,
    p.descricao,
    p.foto,
    p.id_perfil_usuario,
    u.nome_completo AS autor_nome,
    u.foto_perfil AS autor_foto,
    (SELECT COUNT(*) FROM curtidas c WHERE c.id_publicacao = p.id_publicacoes) AS curtidas,
    (SELECT COUNT(*) FROM curtidas c_user WHERE c_user.id_publicacao = p.id_publicacoes AND c_user.id_perfil_usuario = ?) AS usuario_curtiu

FROM publicacoes p
JOIN perfil_usuario u ON p.id_perfil_usuario = u.id_perfil_usuario
ORDER BY p.id_publicacoes DESC";

$stmt = $conn->prepare($sql);
$stmt->bind_param("s", $uid_logado);
$stmt->execute();
$result = $stmt->get_result();

$publicacoes = [];
while ($row = $result->fetch_assoc()) {
    if (!empty($row['foto'])) {
        $row['foto'] = "https://interword-everleigh-coordinately.ngrok-free.dev/api_php/" . $row['foto'];
    }
    if (!empty($row['autor_foto'])) {
        $row['autor_foto'] = "https://interword-everleigh-coordinately.ngrok-free.dev/api_php/" . $row['autor_foto'];
    }
    $publicacoes[] = $row;
}

echo json_encode($publicacoes);
?>
