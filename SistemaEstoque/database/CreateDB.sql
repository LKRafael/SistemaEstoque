
CREATE DATABASE SistemaEstoque;
GO

USE SistemaEstoque;
GO

CREATE TABLE produtos (
    id INT IDENTITY(1,1) PRIMARY KEY,
    codigo VARCHAR(30) NOT NULL UNIQUE,
    nome VARCHAR(100) NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    quantidade INT NOT NULL DEFAULT 0,
    preco DECIMAL(10,2) NOT NULL,
    descricao VARCHAR(500)
);
GO
