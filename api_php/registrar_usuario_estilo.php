<?php
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");

include_once("conexao.php");

// Verifica se todos os campos esperados foram enviados
if (
    isset($_POST['uid']) &&
    isset($_POST['nome']) &&
    isset($_POST['data_nascimento']) &&
    isset($_POST['bio']) &&
    isset($_POST['pronome'])
) {
    $uid = $_POST['uid'];
    $nome = $_POST['nome'];
    $data_nascimento = $_POST['data_nascimento'];
    $bio = $_POST['bio'];
    $pronome = $_POST['pronome'];

    $foto_nome = null;

    // Caso tenha sido enviada uma imagem
    if (isset($_FILES['foto_perfil']) && $_FILES['foto_perfil']['error'] === 0) {
        $pasta = "uploads/"; // certifique-se de que essa pasta exista e tenha permissão de escrita
        if (!is_dir($pasta)) {
            mkdir($pasta, 0777, true);
        }

        $foto_nome = uniqid() . "_" . basename($_FILES['foto_perfil']['name']);
        $caminho = $pasta . $foto_nome;

        if (!move_uploaded_file($_FILES['foto_perfil']['tmp_name'], $caminho)) {
            echo json_encode(["status" => "error", "message" => "Falha ao salvar a foto"]);
            exit;
        }
    }

    // Insere o usuário no banco
    $sql = "INSERT INTO perfil_usuario (uid, nome, data_nascimento, bio, pronome, foto_perfil)
            VALUES (?, ?, ?, ?, ?, ?)";

    $stmt = $conn->prepare($sql);
    $stmt->bind_param("ssssss", $uid, $nome, $data_nascimento, $bio, $pronome, $foto_nome);

    if ($stmt->execute()) {
        echo json_encode([
            "status" => "success",
            "id_perfil_usuario" => $conn->insert_id
        ]);
    } else {
        echo json_encode([
            "status" => "error",
            "message" => $stmt->error
        ]);
    }

    $stmt->close();
    $conn->close();
} else {
    echo json_encode([
        "status" => "error",
        "message" => "Campos obrigatórios ausentes"
    ]);
}
?>
