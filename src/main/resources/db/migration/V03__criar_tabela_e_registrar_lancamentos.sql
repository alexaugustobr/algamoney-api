CREATE TABLE lancamento (
	codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	descricao VARCHAR(50) NOT NULL,
	data_vencimento DATE NOT NULL,
	data_pagamento DATE,
	valor DECIMAL(10,2) NOT NULL,
	observacao VARCHAR(100),
	tipo VARCHAR(20) NOT NULL,
	codigo_categoria BIGINT(20) NOT NULL,
	codigo_pessoa BIGINT(20) NOT NULL,
	FOREIGN KEY (codigo_categoria) REFERENCES categoria(codigo),
	FOREIGN KEY (codigo_pessoa) REFERENCES pessoa(codigo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO lancamento (codigo, descricao, data_vencimento, data_pagamento, valor, observacao, tipo, codigo_categoria, codigo_pessoa) values (1, 'Salário mensal', '2017-06-10', null, 6500.00, 'Distribuição de lucros', 'RECEITA', 1, 1);
INSERT INTO lancamento (codigo, descricao, data_vencimento, data_pagamento, valor, observacao, tipo, codigo_categoria, codigo_pessoa) values (2, 'Supermercado', '2017-02-10', '2017-02-10', 100.32, null, 'DESPESA', 2, 2);
INSERT INTO lancamento (codigo, descricao, data_vencimento, data_pagamento, valor, observacao, tipo, codigo_categoria, codigo_pessoa) values (3, 'Academia', '2017-06-10', null, 120, null, 'DESPESA', 3, 3);
INSERT INTO lancamento (codigo, descricao, data_vencimento, data_pagamento, valor, observacao, tipo, codigo_categoria, codigo_pessoa) values (4, 'Conta de luz', '2017-02-10', '2017-02-10', 110.44, null, 'DESPESA', 3, 4);
INSERT INTO lancamento (codigo, descricao, data_vencimento, data_pagamento, valor, observacao, tipo, codigo_categoria, codigo_pessoa) values (5, 'Conta de água', '2017-06-10', null, 200.30, null, 'DESPESA', 3, 5);
INSERT INTO lancamento (codigo, descricao, data_vencimento, data_pagamento, valor, observacao, tipo, codigo_categoria, codigo_pessoa) values (6, 'Restaurante', '2017-03-10', '2017-03-10', 1010.32, null, 'DESPESA', 4, 6);
INSERT INTO lancamento (codigo, descricao, data_vencimento, data_pagamento, valor, observacao, tipo, codigo_categoria, codigo_pessoa) values (7, 'Venda vídeo game', '2017-06-10', null, 500, null, 'RECEITA', 1, 7);
INSERT INTO lancamento (codigo, descricao, data_vencimento, data_pagamento, valor, observacao, tipo, codigo_categoria, codigo_pessoa) values (8, 'Clube', '2017-03-10', '2017-03-10', 400.32, null, 'DESPESA', 4, 8);
INSERT INTO lancamento (codigo, descricao, data_vencimento, data_pagamento, valor, observacao, tipo, codigo_categoria, codigo_pessoa) values (9, 'Impostos', '2017-06-10', null, 123.64, 'Multas', 'DESPESA', 3, 9);
INSERT INTO lancamento (codigo, descricao, data_vencimento, data_pagamento, valor, observacao, tipo, codigo_categoria, codigo_pessoa) values (10, 'Multa', '2017-04-10', '2017-04-10', 665.33, null, 'DESPESA', 5, 10);
INSERT INTO lancamento (codigo, descricao, data_vencimento, data_pagamento, valor, observacao, tipo, codigo_categoria, codigo_pessoa) values (11, 'Padaria', '2017-06-10', null, 8.32, null, 'DESPESA', 1, 5);
INSERT INTO lancamento (codigo, descricao, data_vencimento, data_pagamento, valor, observacao, tipo, codigo_categoria, codigo_pessoa) values (12, 'Papelaria', '2017-04-10', '2017-04-10', 2100.32, null, 'DESPESA', 5, 4);
INSERT INTO lancamento (codigo, descricao, data_vencimento, data_pagamento, valor, observacao, tipo, codigo_categoria, codigo_pessoa) values (13, 'Almoço', '2017-06-10', null, 1040.32, null, 'DESPESA', 4, 3);
INSERT INTO lancamento (codigo, descricao, data_vencimento, data_pagamento, valor, observacao, tipo, codigo_categoria, codigo_pessoa) values (14, 'Café', '2017-04-10', '2017-04-10', 4.32, null, 'DESPESA', 4, 2);
INSERT INTO lancamento (codigo, descricao, data_vencimento, data_pagamento, valor, observacao, tipo, codigo_categoria, codigo_pessoa) values (15, 'Lanche', '2017-06-10', null, 10.20, null, 'DESPESA', 4, 1);