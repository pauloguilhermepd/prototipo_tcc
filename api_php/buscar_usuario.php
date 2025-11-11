<?php
include("conexao.php");
header("Content-Type: application/json; charset=UTF-8");
ini_set('display_errors', 1);
error_reporting(E_ALL);

$uid = $_GET['uid'] ?? '';

if (empty($uid)) {
    echo json_encode(["status" => "erro", "mensagem" => "ID de usuário não informado."]);
    exit;
}

$sql = "SELECT 
    pu.id_perfil_usuario,
    pu.nome_completo,
    pu.data_nascimento,
    pu.biografia,
    pu.pronomes,
    pu.foto_perfil,
    e.estilo,
    e.sub_estilo
FROM perfil_usuario pu
LEFT JOIN usu_estilo ue ON pu.id_perfil_usuario = ue.id_perfil_usuario
LEFT JOIN estilos e ON ue.id_estilo = e.id_estilo
WHERE pu.id_perfil_usuario = ?
LIMIT 1";

$stmt = $conn->prepare($sql);
$stmt->bind_param("s", $uid);
$stmt->execute();
$result = $stmt->get_result();



if ($row = $result->fetch_assoc()) {
    if (!empty($row['foto_perfil']) && file_exists($row['foto_perfil'])) {
        $row['foto_perfil'] = base64_encode(file_get_contents($row['foto_perfil']));
    }

    echo json_encode([
        "status" => "sucesso",
        "usuario" => $row
    ]);
} else {
    echo json_encode(["status" => "erro", "mensagem" => "Usuário não encontrado."]);
}
?>