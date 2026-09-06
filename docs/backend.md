# Backend do SquarePlanner

## 1. Visão geral

O backend do SquarePlanner foi desenvolvido em **Java utilizando Spring Boot**.

Sua principal responsabilidade é disponibilizar a API REST da aplicação, executar as regras de negócio, realizar a autenticação dos usuários e gerenciar a persistência dos dados no PostgreSQL.

A aplicação utiliza uma arquitetura em camadas:

```text
HTTP Request
     ↓
Controller
     ↓
Service
     ↓
Repository
     ↓
PostgreSQL
```

Além dessas camadas, o backend utiliza **DTOs** para definir os dados de entrada e saída da API e **entidades JPA** para representar os dados persistidos.

---

## 2. Tecnologias utilizadas

| Tecnologia      | Função                                |
| --------------- | ------------------------------------- |
| Java            | Linguagem principal                   |
| Spring Boot     | Framework principal do backend        |
| Spring Web      | Desenvolvimento da API REST           |
| Spring Data JPA | Abstração para acesso ao banco        |
| Hibernate       | ORM utilizado pelo JPA                |
| PostgreSQL      | Banco de dados relacional             |
| Spring Security | Autenticação e autorização            |
| JWT             | Autenticação baseada em tokens        |
| BCrypt          | Hash das senhas                       |
| Jackson         | Serialização e desserialização JSON   |
| Maven           | Gerenciamento de dependências e build |

Durante o desenvolvimento, o projeto utilizou Spring Boot 4.1.0, Hibernate 7.4.1.Final, PostgreSQL 18.4 e Java 26.0.2.

---

## 3. Organização do backend

O backend segue uma divisão baseada nas responsabilidades de cada camada.

A estrutura conceitual é:

```text
backend/
└── src/
    └── main/
        ├── java/
        │   └── com/example/squarePlanner/
        │       ├── controller/
        │       ├── service/
        │       ├── repository/
        │       ├── dto/
        │       ├── enity/
        │       ├── exception/
        │       └── config/
        │
        └── resources/
            ├── application.properties
            └── schema.sql
```

A divisão das responsabilidades evita que controllers, services e repositories acumulem funções que pertencem a outras camadas.

---

# 4. Controllers

Os **controllers** representam a camada de entrada da API.

Eles recebem as requisições HTTP, extraem os dados necessários e delegam o processamento para os services.

Um controller não deve ser responsável por concentrar as regras de negócio.

O fluxo esperado é:

```text
HTTP Request
     ↓
Controller
     ↓
Service
```

Entre os recursos implementados estão controllers relacionados a:

* provas;
* tarefas;
* atividades diversificadas;
* eventos;
* usuários;
* autenticação.

### Exemplo: ProvasController

O `ProvasController` disponibiliza operações relacionadas às provas.

Entre as operações implementadas estão:

```text
POST   /provas
PUT    /provas/{id}
DELETE /provas/{id}
GET    /provas
GET    /provas/{id}
```

Quando uma requisição chega ao controller, ele delega o processamento ao `ProvasService`.

Exemplo conceitual:

```text
GET /provas/{id}
       ↓
ProvasController
       ↓
ProvasService
```

O controller é responsável pela comunicação HTTP, enquanto o processamento da operação permanece no service.

---

# 5. Services

A camada de **Service** concentra as regras de negócio da aplicação.

Essa separação permite que o controller não precise conhecer os detalhes de como uma operação é executada.

Por exemplo:

```text
ProvasController
       ↓
ProvasService
       ↓
ProvaRepository
```

O `ProvasService` pode:

1. receber os dados enviados pelo controller;
2. validar as regras necessárias;
3. buscar informações no repository;
4. transformar os dados;
5. executar operações de negócio;
6. retornar o resultado para o controller.

O mesmo padrão é utilizado nos demais recursos da aplicação.

---

# 6. Repositories

Os **repositories** representam a camada responsável pelo acesso aos dados.

O projeto utiliza **Spring Data JPA**, permitindo que os repositories trabalhem com as entidades persistidas sem que cada operação precise implementar manualmente toda a comunicação com o banco.

O fluxo é:

```text
Service
   ↓
Repository
   ↓
JPA / Hibernate
   ↓
PostgreSQL
```

Além dos métodos fornecidos pelo Spring Data JPA, repositories podem possuir consultas e métodos específicos necessários para as regras da aplicação.

### Exemplo

O `ConteudoRepository` possui uma operação para buscar conteúdos associados a uma determinada prova:

```java
findByProvaId(Long provaId)
```

Isso permite que o service obtenha somente os conteúdos pertencentes à prova solicitada.

---

# 7. Entidades e JPA

As entidades representam os dados persistidos no banco de dados.

O backend utiliza **JPA**, com **Hibernate** como implementação ORM.

A ideia é mapear objetos Java para estruturas relacionais do PostgreSQL.

O fluxo pode ser representado como:

```text
Objeto Java
     ↓
Entity
     ↓
Hibernate / JPA
     ↓
Tabela PostgreSQL
```

Isso permite que a aplicação trabalhe com objetos Java enquanto o Hibernate realiza o mapeamento necessário para o banco de dados.

A estrutura das tabelas e seus relacionamentos é detalhada em [`database.md`](./database.md).

---

# 8. DTOs

O backend utiliza **Data Transfer Objects (DTOs)** para definir os dados que são enviados e recebidos pela API.

A utilização de DTOs separa o modelo utilizado internamente pela aplicação do contrato exposto pela API.

O fluxo pode ser representado como:

```text
HTTP Request
     ↓
Request DTO
     ↓
Controller
     ↓
Service
     ↓
Entity
```

E para respostas:

```text
Entity
     ↓
Service
     ↓
Response DTO
     ↓
Controller
     ↓
HTTP Response
```

Um exemplo é o detalhamento de uma prova.

A resposta pode conter:

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

Nesse caso, o service busca a prova e seus conteúdos e monta o `ProvaResponseDTO` utilizado pela API.

---

# 9. Exemplo de fluxo completo

Uma operação de busca de uma prova pode seguir o seguinte fluxo:

```text
GET /provas/1
       │
       ▼
ProvasController
       │
       ▼
ProvasService
       │
       ├──────────────► ProvaRepository
       │                       │
       │                       ▼
       │                  PostgreSQL
       │
       └──────────────► ConteudoRepository
                               │
                               ▼
                          PostgreSQL
       │
       ▼
ProvaResponseDTO
       │
       ▼
HTTP Response
```

O `ProvasService` busca a prova pelo seu ID e também busca os conteúdos associados a ela.

Os dados são então transformados em DTOs antes de serem devolvidos pela API.

---

# 10. Tratamento de exceções

O backend possui uma camada específica para tratamento de exceções.

A ideia é evitar que erros internos da aplicação sejam diretamente expostos ao cliente e permitir que a API retorne respostas HTTP consistentes.

O fluxo é:

```text
Erro durante a operação
        ↓
Exception
        ↓
Tratamento pelo backend
        ↓
HTTP Error Response
```

Isso permite que o frontend consiga interpretar os erros da API e apresentar uma resposta adequada ao usuário.

Os detalhes dos códigos HTTP e dos formatos de erro fazem parte da documentação da API em [`api.md`](./api.md).

---

# 11. Configuração

As configurações da aplicação ficam nos arquivos de configuração do Spring Boot.

Entre as principais configurações estão:

* conexão com PostgreSQL;
* porta do servidor;
* configurações do JPA/Hibernate;
* configurações relacionadas à segurança;
* configurações necessárias para execução da aplicação.

O banco utilizado durante o desenvolvimento é o PostgreSQL.

O backend foi configurado para executar na porta:

```text
8081
```

Dessa forma, a API pode ser acessada localmente através de:

```text
http://localhost:8081
```

---

# 12. Banco de dados

O backend utiliza PostgreSQL como sistema de gerenciamento de banco de dados.

A comunicação com o banco é realizada através da combinação:

```text
Spring Data JPA
       ↓
Hibernate
       ↓
PostgreSQL
```

O projeto também possui um `schema.sql`, utilizado para definir a estrutura inicial do banco.

A documentação detalhada da modelagem está disponível em [`database.md`](./database.md).

---

# 13. Segurança

A aplicação utiliza **Spring Security** para proteger os recursos que exigem autenticação.

O sistema utiliza:

```text
Spring Security
      +
JWT
      +
BCrypt
```

A autenticação é baseada em tokens JWT.

As senhas dos usuários não são armazenadas diretamente no banco. Antes de serem persistidas, elas passam por um `PasswordEncoder` utilizando BCrypt.

O backend também possui um `UserDetailsService`, responsável por localizar os dados do usuário durante o processo de autenticação.

A implementação completa do mecanismo de autenticação está documentada separadamente em [`authentication.md`](./authentication.md).

---

# 14. Separação de responsabilidades

A arquitetura do backend pode ser resumida da seguinte maneira:

| Componente | Responsabilidade                     |
| ---------- | ------------------------------------ |
| Controller | Receber e responder requisições HTTP |
| Service    | Executar regras de negócio           |
| Repository | Acessar os dados persistidos         |
| Entity     | Representar dados persistidos        |
| DTO        | Definir contratos de entrada e saída |
| Exception  | Representar e tratar erros           |
| Config     | Configurações da aplicação           |
| Security   | Autenticação e autorização           |

Essa separação evita que uma única classe tenha responsabilidades excessivas.

Por exemplo, uma operação relacionada a provas não precisa colocar lógica de banco diretamente no controller:

```text
❌ Controller
   ├── recebe HTTP
   ├── valida tudo
   ├── executa regra de negócio
   ├── acessa banco
   └── monta resposta
```

Em vez disso:

```text
✅ Controller
      ↓
   Service
      ↓
  Repository
      ↓
  Database
```

Cada camada possui uma responsabilidade definida.

---

# 15. Exemplo: recurso de Provas

O recurso de provas é um exemplo da arquitetura utilizada no projeto.

Sua estrutura pode ser representada por:

```text
ProvasController
       │
       ▼
ProvasService
       │
       ├──► ProvaRepository
       │
       └──► ConteudoRepository
```

Quando uma prova é consultada por ID, o service:

1. busca a prova;
2. busca os conteúdos relacionados;
3. transforma os conteúdos em `ConteudoResponseDTO`;
4. monta o `ProvaResponseDTO`;
5. devolve o resultado ao controller.

Esse fluxo demonstra como o backend combina diferentes repositories dentro de uma mesma regra de negócio sem transferir essa responsabilidade para o controller.

---

# 16. Fluxo de uma requisição

De forma geral, uma requisição ao backend segue:

```text
┌──────────────────┐
│   HTTP Request   │
└────────┬─────────┘
         ↓
┌──────────────────┐
│    Security      │
│  (quando aplic.) │
└────────┬─────────┘
         ↓
┌──────────────────┐
│    Controller    │
└────────┬─────────┘
         ↓
┌──────────────────┐
│     Service      │
└────────┬─────────┘
         ↓
┌──────────────────┐
│    Repository    │
└────────┬─────────┘
         ↓
┌──────────────────┐
│    PostgreSQL    │
└──────────────────┘
```

Depois da execução:

```text
PostgreSQL
     ↓
Repository
     ↓
Service
     ↓
DTO
     ↓
Controller
     ↓
HTTP Response
```

Quando a rota exige autenticação, a camada de segurança é executada antes que a requisição chegue ao recurso protegido.

---

# 17. Próximos níveis de documentação

Este documento apresenta a arquitetura e as responsabilidades gerais do backend.
Para aprofundar cada parte:

* [`architecture.md`](./architecture.md) — visão geral da arquitetura do sistema.
* [`frontend.md`](./frontend.md) — arquitetura do Angular.
* [`database.md`](./database.md) — estrutura e relacionamentos do PostgreSQL.
* [`api.md`](./api.md) — endpoints, requisições e respostas.
* [`authentication.md`](./authentication.md) — implementação detalhada da autenticação com Spring Security e JWT.
* [`development.md`](./development.md) — configuração e execução do ambiente de desenvolvimento.
* [`decisions/`](./decisions/) — decisões técnicas e arquiteturais tomadas durante o desenvolvimento.




