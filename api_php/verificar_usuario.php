<?php
header("Content-Type: application/json; charset=UTF-8");
include("conexao.php");

$uid = $_GET['uid'] ?? '';

if (empty($uid)) {
    echo json_encode(["status" => "error", "message" => "UID ausente"]);
    exit;
}
else {
    
    $sql_perfil = $conn->prepare("SELECT id_perfil_usuario FROM perfil_usuario WHERE id_perfil_usuario = ?");
    $sql_perfil->bind_param("s", $uid);
    $sql_perfil->execute();
    $result_perfil = $sql_perfil->get_result();

    if ($result_perfil->num_rows > 0) {
        $perfil = $result_perfil->fetch_assoc();
        $id_perfil_usuario = $perfil['id_perfil_usuario'];

        // Verifica se o estilo foi registrado
        $sql_estilo = $conn->prepare("SELECT id_usu_estilo FROM usu_estilo WHERE id_perfil_usuario = ?");
        $sql_estilo->bind_param("s", $id_perfil_usuario);
        $sql_estilo->execute();
        $result_estilo = $sql_estilo->get_result();

        if ($result_estilo->num_rows > 0) {
            echo json_encode(["status" => "completo", "id_perfil_usuario" => $id_perfil_usuario]);
        } else {
            echo json_encode(["status" => "sem_estilo", "id_perfil_usuario" => $id_perfil_usuario]);
        }
    } else {
        echo json_encode(["status" => "sem_perfil"]);
    }
}

$conn->close();
?>
