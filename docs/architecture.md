# Arquitetura do SquarePlanner

## 1. Visão geral

O SquarePlanner é uma aplicação web desenvolvida para auxiliar na organização da rotina acadêmica, permitindo que o usuário centralize diferentes tipos de compromissos e atividades escolares em uma única aplicação.

A aplicação utiliza uma arquitetura separada em **frontend e backend**, permitindo que a interface do usuário seja desenvolvida de forma independente da lógica de negócio e da persistência dos dados.

A arquitetura atual é composta principalmente por:

* **Frontend:** Angular
* **Backend:** Java + Spring Boot
* **Persistência:** Spring Data JPA / Hibernate
* **Banco de dados:** PostgreSQL
* **Autenticação:** Spring Security + JWT

A aplicação é dividida em três camadas principais:

```text
┌─────────────────────────────┐
│          FRONTEND           │
│           Angular           │
│                             │
│ Pages / Services / Models   │
│ Guards / Interceptors       │
└──────────────┬──────────────┘
               │
               │ HTTP / REST
               ▼
┌─────────────────────────────┐
│           BACKEND           │
│         Spring Boot         │
│                             │
│ Controller                  │
│      ↓                      │
│ Service                     │
│      ↓                      │
│ Repository                  │
└──────────────┬──────────────┘
               │
               │ JPA / Hibernate
               ▼
┌─────────────────────────────┐
│         PostgreSQL          │
└─────────────────────────────┘
```

Essa separação permite que cada parte do sistema tenha responsabilidades específicas, reduzindo o acoplamento entre a interface, as regras de negócio e o armazenamento dos dados.

---

## 2. Princípios arquiteturais

A arquitetura do SquarePlanner busca seguir alguns princípios:

### Separação de responsabilidades

Cada camada possui uma função específica, evitando concentrar toda a lógica em controllers ou components.

### Baixo acoplamento

Frontend e backend são aplicações independentes que se comunicam através de uma API REST.

### Reutilização

A utilização de services permite centralizar operações que podem ser utilizadas por diferentes componentes.

### Segurança

A autenticação é tratada como uma responsabilidade própria da aplicação, utilizando Spring Security, JWT, guard e interceptor no frontend.

### Manutenibilidade

A divisão em camadas facilita a localização de responsabilidades e permite que novas funcionalidades sejam adicionadas sem modificar toda a estrutura existente.

---


## 3. Arquitetura geral

O SquarePlanner segue uma arquitetura cliente-servidor.

O **Angular** é responsável pela interface e pela interação com o usuário. Quando uma operação precisa de dados persistidos ou de uma regra de negócio do sistema, o frontend realiza uma requisição HTTP para a API REST desenvolvida em Spring Boot.

O backend recebe a requisição, processa a operação e utiliza o Spring Data JPA para acessar o PostgreSQL.

De forma simplificada:

```text
Usuário
   │
   ▼
Angular
   │
   │ HTTP
   ▼
REST API
   │
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
PostgreSQL
```

A resposta percorre o caminho inverso:

```text
PostgreSQL
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
   │ HTTP Response
   ▼
Angular
   │
   ▼
Interface
```

---

## 4. Frontend

O frontend foi desenvolvido utilizando Angular e possui uma organização baseada em páginas, serviços, modelos, guards e interceptors.

A estrutura principal da aplicação inclui:

```text
frontend/
└── src/
    └── app/
        ├── guards/
        ├── interceptors/
        ├── models/
        ├── pages/
        └── services/
```

A organização atual possui páginas para diferentes funcionalidades da aplicação, incluindo:

```text
pages/
├── ads/
├── cadastro/
├── eventos/
├── home/
├── login/
├── provas/
└── tarefas/
```

Também existem serviços responsáveis pela comunicação com a API:

```text
services/
├── ad.service.ts
├── auth.service.ts
├── evento.service.ts
├── prova.service.ts
└── tarefa.service.ts
```

Os modelos TypeScript representam as estruturas de dados utilizadas pelo frontend, incluindo:

```text
models/
├── ad.ts
├── atividade.ts
├── conteudo.ts
├── evento.ts
├── provas.ts
└── tarefa.ts
```

A autenticação também possui componentes próprios no frontend, incluindo um `AuthService`, um `auth.guard` e um `auth.interceptor`.

A responsabilidade de cada parte é separada da seguinte maneira:

### Pages

As páginas representam as telas e os fluxos de interação com o usuário.

### Services

Os services concentram a comunicação com a API e as operações relacionadas a cada recurso.

### Models

Os models representam os dados utilizados pelo frontend e ajudam a manter consistência entre os dados recebidos pela API e a aplicação Angular.

### Guards

Os guards controlam o acesso às rotas que exigem autenticação.

### Interceptors

O interceptor de autenticação participa das requisições HTTP, permitindo que as informações necessárias para autenticação sejam adicionadas às requisições.

---

## 5. Backend

O backend foi desenvolvido utilizando Spring Boot e segue uma separação entre as responsabilidades de apresentação, lógica de negócio e acesso aos dados.

O fluxo principal utilizado pela aplicação é:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
```

### Controller

Os controllers são responsáveis por receber as requisições HTTP e encaminhá-las para os services correspondentes.

Exemplos de recursos existentes incluem:

* provas;
* conteúdos;
* tarefas;
* atividades diversificadas;
* eventos;
* usuários;
* autenticação.

Os controllers não devem concentrar a lógica principal das operações. Essa responsabilidade é delegada aos services.

### Service

Os services concentram as regras de negócio da aplicação.

Por exemplo, uma operação relacionada a provas pode seguir:

```text
ProvasController
       ↓
ProvasService
       ↓
ProvaRepository
```

Da mesma forma, operações relacionadas aos conteúdos de uma prova passam pelo serviço responsável pelo recurso antes de acessar o repository.

### Repository

Os repositories são responsáveis pela comunicação com a camada de persistência utilizando Spring Data JPA.

Essa camada abstrai boa parte das operações de acesso ao banco de dados e permite que os services trabalhem com os dados sem precisar executar diretamente as operações SQL em cada funcionalidade.

### DTOs

O backend utiliza DTOs para controlar os dados que entram e saem da API.

Isso evita que as entidades de persistência precisem ser utilizadas diretamente como contrato da API em todas as operações.

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
Entity / Repository
     ↓
PostgreSQL
```

E na resposta:

```text
PostgreSQL
     ↓
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

---

## 6. Persistência de dados

A persistência do SquarePlanner utiliza **PostgreSQL**, acessado pelo backend através do Spring Data JPA e Hibernate.

A aplicação possui um `schema.sql` no backend para definição da estrutura inicial do banco de dados.

O fluxo de persistência é:

```text
Spring Boot
     │
     ▼
Spring Data JPA
     │
     ▼
Hibernate
     │
     ▼
PostgreSQL
```

O Hibernate atua como camada de mapeamento objeto-relacional (ORM), permitindo que as entidades Java sejam persistidas nas estruturas correspondentes do banco.

Na configuração utilizada durante o desenvolvimento, o backend se conecta ao banco `squareplanner` através do PostgreSQL. O Hibernate identifica o banco como PostgreSQL e utiliza o schema `public`.

A modelagem detalhada das tabelas e dos relacionamentos é descrita em [`database.md`](./database.md).

---

## 7. Comunicação entre frontend e backend

A comunicação entre as duas aplicações ocorre através de uma API REST.

O Angular utiliza seus services para realizar requisições HTTP para os endpoints disponibilizados pelo Spring Boot.

Um exemplo simplificado do fluxo de uma operação de consulta é:

```text
ProvasComponent
       │
       ▼
ProvaService
       │
       │ GET /provas
       ▼
ProvasController
       │
       ▼
ProvasService
       │
       ▼
ProvaRepository
       │
       ▼
PostgreSQL
```

Depois que os dados são recuperados:

```text
PostgreSQL
       ↓
Repository
       ↓
Service
       ↓
Controller
       ↓
HTTP Response
       ↓
ProvaService
       ↓
ProvasComponent
       ↓
Interface
```

Os endpoints disponíveis e seus respectivos contratos são documentados em [`api.md`](./api.md).

---

## 8. Autenticação e autorização

O sistema possui autenticação baseada em **Spring Security e JWT**.

A autenticação está integrada tanto ao backend quanto ao frontend.

No backend, componentes específicos são responsáveis por carregar os dados do usuário, validar a autenticação e processar o token JWT.

No frontend, a autenticação possui:

```text
AuthService
     │
     ├── gerenciamento da autenticação
     │
     ▼
AuthInterceptor
     │
     └── requisições autenticadas
     
AuthGuard
     │
     └── proteção das rotas
```

O fluxo geral pode ser representado como:

```text
Login
  ↓
Backend
  ↓
Validação das credenciais
  ↓
JWT
  ↓
Frontend
  ↓
Requisição autenticada
  ↓
Backend
  ↓
Validação do JWT
  ↓
Recurso protegido
```

A implementação da autenticação e o funcionamento de cada componente de segurança são detalhados em [`authentication.md`](./authentication.md).

---

## 9. Fluxo completo de uma operação

Para visualizar como as diferentes partes da arquitetura trabalham juntas, considere uma operação de consulta de provas.

### 1. Usuário acessa a página

O usuário acessa a página de provas através do Angular.

```text
Usuário
  ↓
ProvasComponent
```

### 2. Component solicita os dados

O component utiliza o service responsável pelas provas.

```text
ProvasComponent
  ↓
ProvaService
```

### 3. Service realiza a requisição

O service realiza uma requisição HTTP para a API.

```text
ProvaService
  ↓
GET /provas
```

Caso a rota exija autenticação, o mecanismo de autenticação do frontend adiciona as informações necessárias à requisição.

### 4. Backend recebe a requisição

```text
HTTP Request
  ↓
ProvasController
```

O controller direciona a operação para o service.

### 5. Regra de negócio

```text
ProvasController
  ↓
ProvasService
```

O service executa a lógica necessária para a operação.

### 6. Acesso ao banco

```text
ProvasService
  ↓
ProvaRepository
  ↓
Hibernate
  ↓
PostgreSQL
```

### 7. Retorno

Os dados retornam pelo mesmo fluxo:

```text
PostgreSQL
  ↓
Repository
  ↓
Service
  ↓
Controller
  ↓
HTTP Response
  ↓
ProvaService
  ↓
ProvasComponent
```

Finalmente, o Angular atualiza a interface com os dados recebidos.

---

## 10. Organização das responsabilidades

Uma das principais características da arquitetura do SquarePlanner é a separação de responsabilidades.

| Camada               | Responsabilidade                               |
| -------------------- | ---------------------------------------------- |
| Angular Pages        | Interface e interação com o usuário            |
| Angular Services     | Comunicação com a API e operações do frontend  |
| Angular Models       | Representação dos dados no frontend            |
| Angular Guards       | Controle de acesso às rotas                    |
| Angular Interceptors | Interceptação e preparação de requisições HTTP |
| Controllers          | Entrada e saída das requisições HTTP           |
| Services             | Regras de negócio                              |
| Repositories         | Acesso aos dados                               |
| Entities             | Representação dos dados persistidos            |
| DTOs                 | Contratos de entrada e saída da API            |
| Spring Security      | Autenticação e autorização                     |
| PostgreSQL           | Persistência dos dados                         |

Essa divisão facilita a manutenção do sistema porque alterações em uma camada podem ser realizadas sem necessariamente modificar todas as outras.

---

## 11. Estrutura de alto nível do projeto

A estrutura geral do projeto pode ser representada da seguinte forma:

```text
SquarePlanner/
│
├── backend/
│   └── src/
│       ├── main/
│       │   ├── java/
│       │   │   └── com/example/squarePlanner/
│       │   │       ├── controller/
│       │   │       ├── service/
│       │   │       ├── repository/
│       │   │       ├── dto/
│       │   │       └── ...
│       │   │
│       │   └── resources/
│       │       ├── application.properties
│       │       └── schema.sql
│       │
│       └── test/
│
├── frontend/
│   └── src/
│       └── app/
│           ├── guards/
│           ├── interceptors/
│           ├── models/
│           ├── pages/
│           └── services/
│
├── docs/
│
├── screenshots/
│
└── README.md
```

A estrutura do frontend acima corresponde à organização presente no projeto, incluindo `pages`, `services`, `models`, `guards` e `interceptors`.

---

## 12. Documentação relacionada

Para detalhes específicos de cada parte da arquitetura:

* [`backend.md`](./backend.md) — estrutura e funcionamento do backend.
* [`frontend.md`](./frontend.md) — estrutura e funcionamento do frontend.
* [`database.md`](./database.md) — modelo e relacionamentos do banco de dados.
* [`api.md`](./api.md) — endpoints e contratos da API.
* [`authentication.md`](./authentication.md) — autenticação e autorização utilizando Spring Security e JWT.
* [`development.md`](./development.md) — configuração e execução do projeto.
* [`decisions/`](./decisions/) — decisões arquiteturais e técnicas do projeto.
