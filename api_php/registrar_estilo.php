<?php 
include("conexao.php");


$estilo = $_POST['estilo'] ?? '';
$sub_estilo = $_POST['sub_estilo'] ?? '';

if(empty($estilo) || empty($sub_estilo)) {
    echo json_encode(["status" => "erro", "mensagem" => "Dados inclompletos."]);
}

$stmt = $conn->prepare("INSERT INTO estilos (estilo, sub_estilo) VALUES (?, ?)");
$stmt->bind_param("ss", $estilo, $sub_estilo);

if ($stmt->execute()) {
    echo json_encode(["status" => "ok", "mensagem" => "Estilo e subestilo inserido."]);
} else {
    echo json_encode(["status" => "erro", "mensagem" => $conn->error]);
}

$stmt->close();
$conn->close();





?>