# Autenticação do SquarePlanner

O SquarePlanner utiliza **Spring Security** para controlar o acesso à aplicação e **JWT (JSON Web Token)** para autenticar requisições.

O sistema possui três formas relacionadas à autenticação:

* cadastro de usuário;
* login com email e senha;
* login utilizando uma conta Google.

Após a autenticação, o backend gera um token JWT que é utilizado para identificar o usuário nas requisições protegidas.

---

## Tecnologias utilizadas

A autenticação utiliza os seguintes componentes:

* Spring Security;
* JWT com JJWT;
* BCrypt para criptografia de senhas;
* Google ID Token para autenticação com Google;
* `AuthenticationManager` para autenticação com email e senha;
* `JwtAuthenticationFilter` para validação dos tokens.

---

# Cadastro

O cadastro é realizado através do endpoint:

```http
POST /auth/cadastro
```

O usuário envia seu nome, email e senha.

Antes de criar o usuário, o sistema verifica se o email já está cadastrado. A senha recebida é criptografada utilizando **BCrypt** antes de ser armazenada no banco de dados.

### Fluxo

```text
Cliente
   │
   │ POST /auth/cadastro
   ▼
AuthController
   │
   ▼
AuthService
   │
   ├── Verifica se o email já existe
   │
   ├── Criptografa a senha com BCrypt
   │
   └── Salva o usuário
```

Em caso de sucesso, a API retorna:

```json
{
  "message": "Usuário criado com sucesso"
}
```

---

# Login com email e senha

O login tradicional é realizado através de:

```http
POST /auth/login
```

### Request

```json
{
  "email": "usuario@email.com",
  "senha": "senha"
}
```

O `AuthService` utiliza o `AuthenticationManager` para verificar as credenciais.

Após a autenticação, o usuário é recuperado pelo email e o `JwtService` gera um token contendo informações do usuário.

### Response

```json
{
  "token": "<jwt>"
}
```

---

# Login com Google

O sistema também permite autenticação através do Google.

O endpoint utilizado é:

```http
POST /auth/google
```

O frontend envia uma credencial fornecida pelo Google.

### Request

```json
{
  "credential": "<google-credential>"
}
```

O `GoogleTokenService` valida a credencial utilizando o `GoogleIdTokenVerifier`.

Após a validação:

1. o sistema identifica o usuário pelo `googleId`;
2. caso não encontre, procura pelo email;
3. caso o usuário ainda não exista, um novo usuário é criado;
4. o `googleId` é associado ao usuário;
5. um JWT próprio do SquarePlanner é gerado.

### Response

```json
{
  "token": "<jwt>"
}
```

O token utilizado posteriormente pela aplicação continua sendo o JWT gerado pelo próprio SquarePlanner.

---

# JWT

O `JwtService` é responsável pela geração e validação dos tokens.

O token contém:

* email do usuário como `subject`;
* nome do usuário;
* função (`role`);
* data de emissão;
* data de expiração.

A validade configurada atualmente é de **24 horas**.

A estrutura conceitual do token é:

```text
JWT
 │
 ├── subject → email
 ├── nome
 ├── role
 ├── issuedAt
 └── expiration
```

O token é assinado utilizando uma chave secreta através do algoritmo HMAC.

---

# Validação do token

As requisições autenticadas passam pelo `JwtAuthenticationFilter`.

O filtro verifica o header:

```http
Authorization: Bearer <token>
```

Quando o header não está presente ou não utiliza o formato `Bearer`, a requisição continua sem autenticação.

Quando existe um token:

```text
Requisição
    │
    ▼
JwtAuthenticationFilter
    │
    ▼
Extrai o token
    │
    ▼
Valida o JWT
    │
    ▼
Extrai o email
    │
    ▼
Busca o usuário
    │
    ▼
Obtém suas permissões
    │
    ▼
SecurityContext
```

Depois da validação, o usuário autenticado é armazenado no `SecurityContext` do Spring Security.

Caso o token seja inválido ou esteja expirado, a API retorna:

```json
{
  "status": 401,
  "erro": "UNAUTHORIZED",
  "mensagem": "Token inválido ou expirado."
}
```

---

# Controle de acesso

O `SecurityConfig` define quais endpoints podem ser acessados por usuários autenticados e quais exigem a função `ADMIN`.

### Endpoints públicos

Os seguintes endpoints não exigem autenticação:

```text
/auth/**
/health
```

Isso permite que operações de autenticação sejam realizadas antes da existência de um token.

### Endpoints protegidos

As operações de leitura exigem autenticação:

```text
GET /provas/**
GET /tarefas/**
GET /ads/**
GET /eventos/**
```

Alterações de estado também exigem autenticação:

```text
PUT /provas/conteudos/{id}/estado
PUT /tarefas/atividades/{id}/estado
PUT /ads/{id}/estado
```

### Operações administrativas

Operações de criação, edição e exclusão exigem a função `ADMIN`.

```text
POST   /provas/**
PUT    /provas/**
DELETE /provas/**

POST   /tarefas/**
PUT    /tarefas/**
DELETE /tarefas/**

POST   /ads/**
PUT    /ads/**
DELETE /ads/**

POST   /eventos/**
PUT    /eventos/**
DELETE /eventos/**
```

O controle utiliza:

```java
.hasRole("ADMIN")
```

---

# Senhas

As senhas dos usuários não são armazenadas diretamente.

Durante o cadastro, a senha é processada pelo:

```java
BCryptPasswordEncoder
```

O fluxo é:

```text
Senha informada
      │
      ▼
BCrypt
      │
      ▼
Senha criptografada
      │
      ▼
Banco de dados
```

Durante o login, o Spring Security compara a senha fornecida com o valor armazenado.

---

# CORS

O backend possui uma configuração de CORS para permitir a comunicação com o frontend.

As origens configuradas atualmente são:

```text
http://localhost:4200
https://squareplannerproject.pedrolucasrxsantoss.workers.dev
```

Os métodos HTTP permitidos são:

```text
GET
POST
PUT
DELETE
OPTIONS
```

Os headers são permitidos através da configuração:

```text
*
```

---

# Fluxo completo de autenticação

De forma geral, o processo de autenticação funciona da seguinte maneira:

```text
                    FRONTEND
                       │
                       │
             ┌─────────┴─────────┐
             │                   │
          Login              Login Google
             │                   │
             ▼                   ▼
       /auth/login         /auth/google
             │                   │
             └─────────┬─────────┘
                       ▼
                  AuthService
                       │
                       ▼
                  Validação
                       │
                       ▼
                  JwtService
                       │
                       ▼
                    JWT
                       │
                       ▼
                  FRONTEND
                       │
                       │ Authorization: Bearer <token>
                       ▼
             JwtAuthenticationFilter
                       │
                       ▼
                Validação do JWT
                       │
                       ▼
                Usuário autenticado
                       │
                       ▼
                Spring Security
                       │
                       ▼
                Endpoint protegido
```

Dessa forma, o JWT funciona como o mecanismo utilizado para transportar a identidade e a função do usuário entre o frontend e o backend durante as requisições autenticadas.

---

# Componentes principais

| Componente                | Responsabilidade                                         |
| ------------------------- | -------------------------------------------------------- |
| `SecurityConfig`          | Configura o Spring Security, CORS e permissões das rotas |
| `AuthController`          | Disponibiliza os endpoints de cadastro e login           |
| `AuthService`             | Executa as regras de autenticação                        |
| `JwtService`              | Gera e valida tokens JWT                                 |
| `JwtAuthenticationFilter` | Intercepta e autentica requisições com JWT               |
| `UsuarioDetailsService`   | Recupera usuários e suas permissões                      |
| `GoogleTokenService`      | Valida credenciais do Google                             |
| `BCryptPasswordEncoder`   | Criptografa e verifica senhas                            |

---

# Documentação relacionada

* [`architecture.md`](architecture.md) — arquitetura geral;
* [`backend.md`](backend.md) — implementação do backend;
* [`frontend.md`](frontend.md) — implementação do frontend;
* [`database.md`](database.md) — banco de dados e ERD;
* [`api.md`](api.md) — endpoints da API;
* [`development.md`](development.md) — configuração e execução.
