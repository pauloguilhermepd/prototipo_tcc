<?php
header('Content-Type: application/json; charset=utf-8');
require_once 'conexao.php';

$uid = $_GET['uid'] ?? '';

if (empty($uid)) {
    echo json_encode(['erro' => 'UID não fornecido']);
    exit;
}

$query = $conn->prepare("SELECT nome, pronome, bio, foto_perfil FROM usuario WHERE uid = ?");
$query->bind_param("s", $uid);
$query->execute();
$result = $query->get_result();

if ($result->num_rows > 0) {
    $usuario = $result->fetch_assoc();

    // ✅ Adapte o caminho da imagem se necessário
    $usuario['foto_perfil'] = 'http://SEU_IP/fitmatch/uploads/' . $usuario['foto_perfil'];

    echo json_encode($usuario);
} else {
    echo json_encode(['erro' => 'Usuário não encontrado']);
}

$query->close();
$conn->close();
