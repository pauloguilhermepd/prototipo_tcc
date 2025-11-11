<?php
header("Content-Type: application/json; charset=UTF-8");
include("conexao.php");

$id_usuario = $_POST['id_usuario'] ?? ''; 

if (empty($id_usuario)) {
    http_response_code(400); 
    echo json_encode(["status" => "erro", "msg" => "Id usuario vazio"]);
    exit;
}

$conn->begin_transaction();
$id_estilo_para_excluir = null;

try {
    $sql_find_estilo = "SELECT id_estilo FROM usu_estilo WHERE id_perfil_usuario = ?";
    $stmt_find = $conn->prepare($sql_find_estilo);
    $stmt_find->bind_param("s", $id_usuario);
    if ($stmt_find->execute()) {
        $result_estilo = $stmt_find->get_result();
        if ($row = $result_estilo->fetch_assoc()) {
            $id_estilo_para_excluir = $row['id_estilo'];
        }
    }
    $stmt_find->close();

    $sql_estilo = "DELETE FROM usu_estilo WHERE id_perfil_usuario = ?";
    $stmt_estilo = $conn->prepare($sql_estilo);
    $stmt_estilo->bind_param("s", $id_usuario);
    if (!$stmt_estilo->execute()) {
        throw new Exception("Falha ao excluir ligação de estilo: " . $stmt_estilo->error);
    }
    $stmt_estilo->close();

    $sql_publi = "DELETE FROM publicacoes WHERE id_perfil_usuario = ?";
    $stmt_publi = $conn->prepare($sql_publi);
    $stmt_publi->bind_param("s", $id_usuario);
    if (!$stmt_publi->execute()) {
        throw new Exception("Falha ao excluir publicações: " . $stmt_publi->error);
    }
    $stmt_publi->close();

    $sql_perfil = "DELETE FROM perfil_usuario WHERE id_perfil_usuario = ?";
    $stmt_perfil = $conn->prepare($sql_perfil);
    $stmt_perfil->bind_param("s", $id_usuario);
    if (!$stmt_perfil->execute()) {
        throw new Exception("Falha ao excluir perfil: " . $stmt_perfil->error);
    }
    $affected_rows = $stmt_perfil->affected_rows;
    $stmt_perfil->close();

    if ($id_estilo_para_excluir !== null) {
        $sql_del_estilo = "DELETE FROM estilos WHERE id_estilo = ?";
        $stmt_del_estilo = $conn->prepare($sql_del_estilo);
        $stmt_del_estilo->bind_param("i", $id_estilo_para_excluir);
        if (!$stmt_del_estilo->execute()) {

        }
        $stmt_del_estilo->close();
    }

    $conn->commit();

    if ($affected_rows > 0) {
        http_response_code(200);
        echo json_encode(["status" => "sucesso", "msg" => "Perfil e dados relacionados excluídos com sucesso."]);
    } else {
        http_response_code(404); 
        echo json_encode(["status" => "erro", "msg" => "Usuário não encontrado."]);
    }

} catch (Exception $e) {
    $conn->rollback();
    http_response_code(500); 
    echo json_encode(["status" => "erro", "msg" => "Falha no banco de dados. " . $e->getMessage()]);
}

$conn->close();
?>