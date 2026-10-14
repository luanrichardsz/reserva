CREATE TABLE usuario (
    id_usuario BIGSERIAL PRIMARY KEY,
    nome       VARCHAR(50) NOT NULL,
    cargo      VARCHAR(50) NOT NULL,
    setor      VARCHAR(30) NOT NULL
);

CREATE TABLE sala (
    id_sala    BIGSERIAL PRIMARY KEY,
    nome       VARCHAR(255) NOT NULL,
    descricao  VARCHAR(255) NOT NULL,
    capacidade INTEGER      NOT NULL,
    ativo      BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE reserva (
    id_reserva BIGSERIAL PRIMARY KEY,
    sala_id    BIGINT       NOT NULL REFERENCES sala(id_sala),
    usuario_id BIGINT       NOT NULL REFERENCES usuario(id_usuario),
    inicio     TIMESTAMP WITH TIME ZONE NOT NULL,
    fim        TIMESTAMP WITH TIME ZONE NOT NULL,
    status     VARCHAR(255) NOT NULL
);
