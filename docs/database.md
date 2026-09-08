# Banco de Dados do SquarePlanner

O SquarePlanner utiliza **PostgreSQL** como sistema de gerenciamento de banco de dados.

A persistência dos dados é realizada pelo backend através do **Spring Data JPA**, utilizando o **Hibernate** como implementação de JPA.

O frontend não possui acesso direto ao banco. Todas as operações passam pela API REST desenvolvida com Spring Boot.

---


## ERD
![alt text](<User Progress Measurement-2026-09-08-185945.png>)

---

## Tecnologias

| Tecnologia      | Utilização                               |
| --------------- | ---------------------------------------- |
| PostgreSQL      | Banco de dados relacional                |
| Spring Data JPA | Abstração para acesso aos dados          |
| Hibernate       | Implementação do JPA e ORM               |
| SQL             | Definição da estrutura do banco          |
| JDBC            | Comunicação entre aplicação e PostgreSQL |

---

## Arquitetura de persistência

A comunicação com o banco segue a seguinte estrutura:

```text
Frontend Angular
       │
       │ HTTP
       ▼
Spring Boot
       │
       ▼
Services
       │
       ▼
Repositories
       │
       ▼
Hibernate / JPA
       │
       ▼
PostgreSQL
```

Cada camada possui uma responsabilidade diferente.

* **Services** — aplicam as regras de negócio;
* **Repositories** — realizam operações de persistência;
* **JPA/Hibernate** — fazem o mapeamento entre objetos Java e tabelas relacionais;
* **PostgreSQL** — armazena os dados permanentemente.

---

## Banco utilizado

Durante o desenvolvimento, o projeto utiliza o banco:

```text
squareplanner
```

A aplicação Spring Boot se conecta ao PostgreSQL através de uma configuração semelhante a:

```text
jdbc:postgresql://localhost:5432/squareplanner
```

O schema utilizado é o padrão:

```text
public
```

---

## Schema

A estrutura inicial do banco é definida através do arquivo:

```text
src/main/resources/schema.sql
```

Esse arquivo contém os comandos SQL necessários para criação das estruturas utilizadas pela aplicação.

A utilização de um arquivo SQL permite que a estrutura do banco seja documentada e reproduzida de forma controlada.

---

## Entidades

O banco armazena as informações utilizadas pelas principais funcionalidades do SquarePlanner.

Entre os principais recursos do sistema estão:

* Usuários;
* Provas;
* Conteúdos;
* Tarefas;
* Atividades Diversificadas;
* Eventos.

Cada recurso possui sua representação na aplicação e sua respectiva estrutura de persistência.

---

## Provas

As provas são armazenadas em uma tabela própria.

A estrutura utilizada para uma prova possui informações relacionadas à disciplina, data e trimestre.

Exemplo da estrutura SQL utilizada durante o desenvolvimento:

```sql
CREATE TABLE provas (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    materia VARCHAR(255) NOT NULL,
    data DATE NOT NULL,
    trimestre INTEGER NOT NULL,

    CONSTRAINT chk_prova_trimestre
        CHECK (trimestre BETWEEN 1 AND 3)
);
```

O campo `id` é utilizado como identificador único da prova.

O campo `trimestre` possui uma restrição que permite somente valores entre `1` e `3`.

> A estrutura apresentada deve ser mantida de acordo com a versão atual do `schema.sql` do projeto.

---

## Conteúdos

Os conteúdos representam os assuntos associados a uma prova.

Uma prova pode possuir diversos conteúdos.

O relacionamento é utilizado pelo backend para buscar os conteúdos pertencentes a uma determinada prova.

No repositório, essa consulta é representada por:

```java
findByProvaId(Long provaId)
```

O fluxo de consulta é:

```text
Prova
  │
  └── Conteúdos
        ├── Conteúdo 1
        ├── Conteúdo 2
        └── Conteúdo 3
```

Quando os detalhes de uma prova são solicitados, o backend busca os conteúdos relacionados e os inclui na resposta enviada ao frontend.

---

## Relacionamento entre Provas e Conteúdos

O relacionamento entre provas e conteúdos possui uma característica de **um para muitos**:

```text
1 Prova
   │
   ├── N Conteúdo
   ├── N Conteúdo
   └── N Conteúdo
```

Uma prova pode possuir vários conteúdos, enquanto cada conteúdo pertence a uma prova.

O backend utiliza o identificador da prova para realizar essa associação durante as consultas e operações de conteúdo.

---

## Repositories

O acesso aos dados é organizado através dos repositories do Spring Data JPA.

Os repositories são responsáveis por executar operações como:

* Buscar registros;
* Inserir registros;
* Atualizar registros;
* Excluir registros;
* Verificar a existência de registros.

Um exemplo é o `ConteudoRepository`, que possui uma consulta para buscar conteúdos associados a uma prova:

```java
findByProvaId(Long provaId)
```

Essa abordagem permite utilizar as abstrações do Spring Data JPA sem precisar escrever manualmente uma consulta SQL para cada operação.

---

## JPA e Hibernate

As entidades Java são utilizadas pelo JPA para representar os dados persistidos no banco.

O Hibernate realiza o mapeamento entre:

```text
Objeto Java
     ↕
Tabela PostgreSQL
```

Por exemplo:

```text
Prova.java
     │
     ▼
Tabela de provas
```

Esse mecanismo é conhecido como **Object-Relational Mapping (ORM)**.

O ORM reduz a necessidade de manipulação direta de SQL durante as operações comuns da aplicação.

---

## Tipos de dados

Existe uma correspondência entre os tipos utilizados no Java e os tipos armazenados no PostgreSQL.

Alguns exemplos utilizados pelo projeto:

| Java        | PostgreSQL             |
| ----------- | ---------------------- |
| `Long`      | `BIGINT` / `BIGSERIAL` |
| `String`    | `VARCHAR`              |
| `LocalDate` | `DATE`                 |
| `Integer`   | `INTEGER`              |
| `Boolean`   | `BOOLEAN`              |

Essa conversão é realizada pelo JPA/Hibernate durante a persistência e recuperação dos dados.

---

## Integridade dos dados

Além das regras implementadas no código Java, algumas restrições são definidas diretamente no banco de dados.

Por exemplo:

```sql
CONSTRAINT chk_prova_trimestre
CHECK (trimestre BETWEEN 1 AND 3)
```

Essa restrição impede que sejam armazenados valores inválidos para o trimestre.

O banco também utiliza chaves primárias para identificar registros de maneira única.

---

## Fluxo de persistência

Quando um usuário cria uma nova informação através da interface, o fluxo completo é:

```text
Usuário
   │
   ▼
Angular
   │
   │ POST / PUT / DELETE
   ▼
Controller
   │
   ▼
Service
   │
   ▼
Repository
   │
   ▼
Hibernate / JPA
   │
   ▼
PostgreSQL
```

Na leitura de dados, o fluxo ocorre no sentido inverso:

```text
PostgreSQL
   │
   ▼
Hibernate / JPA
   │
   ▼
Repository
   │
   ▼
Service
   │
   ▼
Controller
   │
   ▼
JSON
   │
   ▼
Angular
```

---

## Exemplo: busca de uma prova

Quando o frontend solicita:

```http
GET /provas/{id}
```

o backend realiza aproximadamente o seguinte fluxo:

```text
ProvasController
       │
       ▼
ProvasService
       │
       ├── busca a prova
       │
       └── busca os conteúdos
               │
               ▼
       ConteudoRepository
               │
               ▼
          PostgreSQL
```

Os dados recuperados são então transformados em DTOs e enviados para o frontend.

Um exemplo de resposta é:

```json
{
  "id": 1,
  "materia": "Matemática",
  "data": "2026-08-20",
  "trimestre": 1,
  "conteudos": [
    {
      "id": 2,
      "nome": "Estatistica",
      "concluido": true
    },
    {
      "id": 1,
      "nome": "Probabilidade",
      "concluido": true
    }
  ]
}
```

Dessa forma, o banco mantém os dados de forma relacional, enquanto o backend é responsável por organizar esses dados no formato necessário para a API.

---

## Banco de dados e usuários

O sistema possui autenticação de usuários e, portanto, os dados relacionados aos usuários são tratados pelo backend.

A autenticação e autorização não são realizadas diretamente pelo PostgreSQL.

O fluxo é:

```text
Angular
   │
   ▼
Spring Security
   │
   ▼
JWT
   │
   ▼
Services / Repositories
   │
   ▼
PostgreSQL
```

A documentação específica sobre autenticação está em:

* [`authentication.md`](authentication.md)

---

## Evolução do modelo de dados

Durante o desenvolvimento, o modelo de dados passou por alterações conforme novas necessidades do sistema foram identificadas.

Um exemplo foi a necessidade de diferenciar dados pertencentes a diferentes usuários.

Essa evolução é importante porque determinadas informações inicialmente poderiam parecer globais, mas, em uma aplicação multiusuário, podem precisar estar associadas ao usuário responsável por aquele dado.

Alterações futuras no modelo devem preservar a separação entre:

* dados globais;
* dados pertencentes ao usuário;
* relações entre usuários e recursos;
* informações de progresso ou estado.

Decisões arquiteturais desse tipo devem ser registradas na pasta:

```text
docs/decisions/
```

---

## Responsabilidades

| Componente   | Responsabilidade                      |
| ------------ | ------------------------------------- |
| PostgreSQL   | Armazenamento persistente             |
| `schema.sql` | Definição da estrutura SQL            |
| Entity       | Representação dos dados no Java       |
| Repository   | Acesso aos dados                      |
| Service      | Regras de negócio                     |
| DTO          | Estrutura dos dados expostos pela API |

Essa divisão evita que a lógica de negócio fique diretamente acoplada ao banco de dados.

---

## Resumo

O banco de dados do SquarePlanner utiliza uma arquitetura relacional baseada em PostgreSQL.

O acesso é realizado pelo backend através do Spring Data JPA e Hibernate, seguindo o fluxo:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
JPA / Hibernate
    ↓
PostgreSQL
```

Essa arquitetura permite separar a persistência das demais partes da aplicação, facilitando manutenção, evolução e organização do sistema.

---

## Documentação relacionada

* [`architecture.md`](architecture.md) — arquitetura geral do sistema;
* [`backend.md`](backend.md) — implementação do backend;
* [`frontend.md`](frontend.md) — implementação do frontend;
* [`api.md`](api.md) — endpoints da API;
* [`authentication.md`](authentication.md) — autenticação e segurança;
* [`development.md`](development.md) — configuração e execução do projeto.
