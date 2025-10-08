<?php
include("conexao.php");

$sql = "SELECT id_publicacoes, titulo, descricao, quantidade_curtidas, quantidade_comentarios
        FROM publicacoes";
$result = $conn->query($sql);

$dados = array();
while ($row = $result->fetch_assoc()) {
    $dados[] = $row;
}

echo json_encode($dados);
?>