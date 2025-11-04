-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Tempo de geração: 04/11/2025 às 19:10
-- Versão do servidor: 10.4.32-MariaDB
-- Versão do PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Banco de dados: `fitmatch`
--

-- --------------------------------------------------------

--
-- Estrutura para tabela `comentario`
--

CREATE TABLE `comentario` (
  `id_comentario` int(11) NOT NULL,
  `comentarios` text DEFAULT NULL,
  `data_comentario` datetime DEFAULT NULL,
  `id_publicacoes` int(11) NOT NULL,
  `id_usuario` varchar(28) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estrutura para tabela `curtidas`
--

CREATE TABLE `curtidas` (
  `id_curtida` int(11) NOT NULL,
  `id_publicacao` int(11) NOT NULL,
  `id_perfil_usuario` varchar(28) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estrutura para tabela `estilos`
--

CREATE TABLE `estilos` (
  `id_estilo` int(11) NOT NULL,
  `estilo` char(4) DEFAULT NULL,
  `sub_estilo` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `estilos`
--

INSERT INTO `estilos` (`id_estilo`, `estilo`, `sub_estilo`) VALUES
(7, 'Punk', 'super teste legal 1');

-- --------------------------------------------------------

--
-- Estrutura para tabela `login`
--

CREATE TABLE `login` (
  `id_usuario` varchar(28) NOT NULL,
  `senha` varchar(20) NOT NULL,
  `email` varchar(84) NOT NULL,
  `tipo` enum('usuario','moderador') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estrutura para tabela `perfil_usuario`
--

CREATE TABLE `perfil_usuario` (
  `id_perfil_usuario` varchar(28) NOT NULL,
  `nome_completo` varchar(150) DEFAULT NULL,
  `data_nascimento` date DEFAULT NULL,
  `biografia` varchar(150) DEFAULT NULL,
  `pronomes` char(1) DEFAULT NULL,
  `foto_perfil` mediumblob DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `perfil_usuario`
--

INSERT INTO `perfil_usuario` (`id_perfil_usuario`, `nome_completo`, `data_nascimento`, `biografia`, `pronomes`, `foto_perfil`) VALUES
('IPHZy0rvw3QVrjRdnAFPhf5jwN82', 'testeteste', '2006-05-05', 'kakakajsiisjwjx', 'O', 0x75706c6f6164732f363930613133363262363264355f313030303135393833382e6a7067);

-- --------------------------------------------------------

--
-- Estrutura para tabela `publicacoes`
--

CREATE TABLE `publicacoes` (
  `id_publicacoes` int(11) NOT NULL,
  `descricao` varchar(200) DEFAULT NULL,
  `foto` mediumblob DEFAULT NULL,
  `titulo` varchar(50) DEFAULT NULL,
  `id_perfil_usuario` varchar(28) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `publicacoes`
--

INSERT INTO `publicacoes` (`id_publicacoes`, `descricao`, `foto`, `titulo`, `id_perfil_usuario`) VALUES
(3, 'teste supr sigma 1', 0x75706c6f6164732f363930613332316636313030315f313030303135393933372e6a7067, 'kayke legal', 'IPHZy0rvw3QVrjRdnAFPhf5jwN82'),
(5, 'kakskakaka', 0x75706c6f6164732f363930613361343039383037655f313030303135393438302e6a7067, 'ajksjjs', 'IPHZy0rvw3QVrjRdnAFPhf5jwN82'),
(6, 'jakakaka', 0x75706c6f6164732f363930613363373635626139385f313030303135393833382e6a7067, 'testets', 'IPHZy0rvw3QVrjRdnAFPhf5jwN82'),
(7, 'jqjajaja', 0x75706c6f6164732f363930613364303237636435315f313030303135393438302e6a7067, 'jajajaka', 'IPHZy0rvw3QVrjRdnAFPhf5jwN82');

-- --------------------------------------------------------

--
-- Estrutura para tabela `usu_estilo`
--

CREATE TABLE `usu_estilo` (
  `id_usu_estilo` int(11) NOT NULL,
  `id_estilo` int(11) NOT NULL,
  `id_perfil_usuario` varchar(28) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `usu_estilo`
--

INSERT INTO `usu_estilo` (`id_usu_estilo`, `id_estilo`, `id_perfil_usuario`) VALUES
(3, 7, 'IPHZy0rvw3QVrjRdnAFPhf5jwN82');

--
-- Índices para tabelas despejadas
--

--
-- Índices de tabela `comentario`
--
ALTER TABLE `comentario`
  ADD PRIMARY KEY (`id_comentario`),
  ADD KEY `fk_publicacao_comentario` (`id_publicacoes`);

--
-- Índices de tabela `curtidas`
--
ALTER TABLE `curtidas`
  ADD PRIMARY KEY (`id_curtida`),
  ADD KEY `fk_publicacao_curtida` (`id_publicacao`);

--
-- Índices de tabela `estilos`
--
ALTER TABLE `estilos`
  ADD PRIMARY KEY (`id_estilo`);

--
-- Índices de tabela `login`
--
ALTER TABLE `login`
  ADD PRIMARY KEY (`id_usuario`);

--
-- Índices de tabela `perfil_usuario`
--
ALTER TABLE `perfil_usuario`
  ADD PRIMARY KEY (`id_perfil_usuario`);

--
-- Índices de tabela `publicacoes`
--
ALTER TABLE `publicacoes`
  ADD PRIMARY KEY (`id_publicacoes`),
  ADD KEY `fk_perfil_usuario_publicacoes` (`id_perfil_usuario`);

--
-- Índices de tabela `usu_estilo`
--
ALTER TABLE `usu_estilo`
  ADD PRIMARY KEY (`id_usu_estilo`),
  ADD KEY `fk_usuestilo_estilo` (`id_estilo`),
  ADD KEY `fk_usuestilo_perfilusuario` (`id_perfil_usuario`);

--
-- AUTO_INCREMENT para tabelas despejadas
--

--
-- AUTO_INCREMENT de tabela `comentario`
--
ALTER TABLE `comentario`
  MODIFY `id_comentario` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de tabela `curtidas`
--
ALTER TABLE `curtidas`
  MODIFY `id_curtida` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de tabela `estilos`
--
ALTER TABLE `estilos`
  MODIFY `id_estilo` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT de tabela `publicacoes`
--
ALTER TABLE `publicacoes`
  MODIFY `id_publicacoes` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT de tabela `usu_estilo`
--
ALTER TABLE `usu_estilo`
  MODIFY `id_usu_estilo` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Restrições para tabelas despejadas
--

--
-- Restrições para tabelas `comentario`
--
ALTER TABLE `comentario`
  ADD CONSTRAINT `fk_publicacao_comentario` FOREIGN KEY (`id_publicacoes`) REFERENCES `publicacoes` (`id_publicacoes`);

--
-- Restrições para tabelas `curtidas`
--
ALTER TABLE `curtidas`
  ADD CONSTRAINT `fk_publicacao_curtida` FOREIGN KEY (`id_publicacao`) REFERENCES `publicacoes` (`id_publicacoes`);

--
-- Restrições para tabelas `publicacoes`
--
ALTER TABLE `publicacoes`
  ADD CONSTRAINT `fk_perfil_usuario_publicacoes` FOREIGN KEY (`id_perfil_usuario`) REFERENCES `perfil_usuario` (`id_perfil_usuario`);

--
-- Restrições para tabelas `usu_estilo`
--
ALTER TABLE `usu_estilo`
  ADD CONSTRAINT `fk_usuestilo_estilo` FOREIGN KEY (`id_estilo`) REFERENCES `estilos` (`id_estilo`),
  ADD CONSTRAINT `fk_usuestilo_perfilusuario` FOREIGN KEY (`id_perfil_usuario`) REFERENCES `perfil_usuario` (`id_perfil_usuario`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
