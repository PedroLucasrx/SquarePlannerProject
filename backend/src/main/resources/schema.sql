CREATE TABLE IF NOT EXISTS provas (
    id BIGSERIAL PRIMARY KEY,
    materia VARCHAR(255) NOT NULL,
    data DATE NOT NULL,
    trimestre INTEGER NOT NULL,

    CONSTRAINT chk_prova_trimestre
        CHECK (trimestre BETWEEN 1 AND 3)
);


CREATE TABLE IF NOT EXISTS conteudos (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    prova_id BIGIN NOT NULL,

   CONSTRAINT fk_conteudo_prova
    FOREIGN KEY (prova_id)
    REFERENCES provas(id)
   ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS ad (
    id BIGSERIAL PRIMARY KEY,
    materia VARCHAR(255) NOT NULL,
    data DATE NOT NULL,
    trimestre INTEGER NOT NULL,
    proposta VARCHAR(255) NOT NULL

    CONSTRAINT chk_ad_trimestre
        CHECK (trimestre BETWEEN 1 AND 3)
);

CREATE TABLE IF NOT EXISTS evento (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    data DATE NOT NULL
);



CREATE TABLE IF NOT EXISTS tarefas (
    id BIGSERIAL PRIMARY KEY,
    materia VARCHAR(255) NOT NULL,
    data DATE NOT NULL,
    trimestre INTEGER NOT NULL,

    CONSTRAINT chk_tarefa_trimestre
        CHECK (trimestre BETWEEN 1 AND 3)
);


CREATE TABLE IF NOT EXISTS atividades (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    tarefa_id BIGINT NOT NULL,

    CONSTRAINT fk_atividade_tarefa
    FOREIGN KEY (tarefa_id)
    REFERENCES tarefas(id)
    ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS usuarios (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS progresso_conteudo (
    id BIGSERIAL PRIMARY KEY,

    usuario_id BIGINT NOT NULL,
    conteudo_id BIGINT NOT NULL,

    concluido BOOLEAN NOT NULL DEFAULT FALSE,

    CONSTRAINT fk_usuario_conteudo_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuarios(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_usuario_conteudo_conteudo
        FOREIGN KEY (conteudo_id)
        REFERENCES conteudos(id)
        ON DELETE CASCADE,

    CONSTRAINT uk_usuario_conteudo
        UNIQUE (usuario_id, conteudo_id)
);

CREATE TABLE IF NOT EXISTS progresso_ad (
    id BIGSERIAL PRIMARY KEY,

    usuario_id BIGINT NOT NULL,
    ad_id BIGINT NOT NULL,

    concluido BOOLEAN NOT NULL DEFAULT FALSE,

    CONSTRAINT fk_usuario_ad_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuarios(id)
    ON DELETE CASCADE,

    CONSTRAINT fk_usuario_ad_ad
        FOREIGN KEY (ad_id)
        REFERENCES ad(id)
    ON DELETE CASCADE,

    CONSTRAINT uk_usuario_ad
    UNIQUE (usuario_id, ad_id)
);

CREATE TABLE IF NOT EXISTS progresso_atividades (
    id BIGSERIAL PRIMARY KEY,

    usuario_id BIGINT NOT NULL,
    atividades_id BIGINT NOT NULL,

    concluido BOOLEAN NOT NULL DEFAULT FALSE,

    CONSTRAINT fk_usuario_atividades_usuario
    FOREIGN KEY (usuario_id)
    REFERENCES usuarios(id)
    ON DELETE CASCADE,

    CONSTRAINT fk_usuario_atividades_atividades
    FOREIGN KEY (atividades_id)
    REFERENCES atividades(id)
    ON DELETE CASCADE,

    CONSTRAINT uk_usuario_atividades
    UNIQUE (usuario_id, atividades_id)
    );
