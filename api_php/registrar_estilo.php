<?php
include("conexao.php");
header("Content-Type: application/json; charset=UTF-8");

$estilo = $_POST['estilo'] ?? '';
$sub_estilo = $_POST['sub_estilo'] ?? '';
$id_perfil_usuario = $_POST['id_perfil_usuario'] ?? '';



if (empty($estilo) || empty($sub_estilo) || empty($id_perfil_usuario)) {
    echo json_encode(["status" => "erro", "mensagem" => "Dados incompletos."]);
    exit;
}


$stmt = $conn->prepare("INSERT INTO estilos (estilo, sub_estilo) VALUES (?, ?)");
$stmt->bind_param("ss", $estilo, $sub_estilo);

if ($stmt->execute()) {
    $id_estilo = $conn->insert_id;

    $stmt2 = $conn->prepare("INSERT INTO usu_estilo (id_estilo, id_perfil_usuario) VALUES (?, ?)");
    $stmt2->bind_param("is", $id_estilo, $id_perfil_usuario);

    if ($stmt2->execute()) {
        echo json_encode([
            "status" => "ok",
            "mensagem" => "Estilo e vínculo com usuário inseridos com sucesso.",
            "id_estilo" => $id_estilo
        ]);
    } else {
        echo json_encode([
            "status" => "erro",
            "mensagem" => "Falha ao vincular estilo ao usuário: " . $stmt2->error
        ]);
    }

    $stmt2->close();
} else {
    echo json_encode([
        "status" => "erro",
        "mensagem" => "Falha ao inserir estilo: " . $stmt->error
    ]);
}

$stmt->close();
$conn->close();
?>
