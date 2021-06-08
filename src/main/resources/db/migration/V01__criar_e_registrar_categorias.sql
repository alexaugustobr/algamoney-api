CREATE TABLE categoria (
	codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO categoria (codigo, nome) values (1, 'Lazer');
INSERT INTO categoria (codigo, nome) values (2, 'Alimentação');
INSERT INTO categoria (codigo, nome) values (3, 'Supermercado');
INSERT INTO categoria (codigo, nome) values (4, 'Farmácia');
INSERT INTO categoria (codigo, nome) values (5, 'Outros');
