# Frontend do SquarePlanner

O frontend do SquarePlanner foi desenvolvido com **Angular 17**, utilizando componentes standalone, TypeScript, SCSS e o sistema de roteamento do Angular.

Sua responsabilidade é fornecer a interface com a qual o usuário interage, controlar o estado visual da aplicação e realizar a comunicação com a API REST do backend.

A aplicação segue uma separação entre **páginas, serviços, modelos, guards e interceptors**, mantendo a interface desacoplada das regras de negócio e da camada de persistência.

---

## Tecnologias

| Tecnologia     | Utilização                      |
| -------------- | ------------------------------- |
| Angular 17     | Framework principal             |
| TypeScript     | Linguagem do frontend           |
| SCSS           | Estilização                     |
| Angular Router | Navegação entre páginas         |
| HttpClient     | Comunicação com a API           |
| FormsModule    | Manipulação de formulários      |
| CommonModule   | Recursos comuns dos componentes |
| JWT            | Autenticação das requisições    |

---

## Estrutura

A estrutura principal do frontend está organizada da seguinte maneira:

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

Cada diretório possui uma responsabilidade específica dentro da aplicação.

---

## Pages

O diretório `pages/` contém as telas da aplicação.

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

As páginas são responsáveis principalmente pela **interface e interação com o usuário**.

Entre as principais funcionalidades estão:

* Login;
* Cadastro de usuário;
* Página inicial;
* Visualização e gerenciamento de provas;
* Visualização e gerenciamento de tarefas;
* Visualização e gerenciamento de atividades diversificadas;
* Visualização e gerenciamento de eventos.

As páginas não realizam diretamente operações no banco de dados. Quando precisam obter ou modificar informações, utilizam os serviços responsáveis pela comunicação com o backend.

---

## Services

Os serviços centralizam a comunicação do frontend com a API REST.

```text
services/
├── ad.service.ts
├── auth.service.ts
├── evento.service.ts
├── prova.service.ts
└── tarefa.service.ts
```

Essa separação evita que as páginas precisem conhecer detalhes de HTTP ou das URLs da API.

### ProvaService

O `ProvaService`, por exemplo, possui operações para trabalhar com provas:

```typescript
listarProvas(): Observable<Prova[]>

criarProva(dados: CriarProva): Observable<any>

buscarProva(id: number): Observable<ProvaDetalhes>

criarConteudo(provaId: number, nome: string): Observable<any>

deletarProva(id: number): Observable<any>
```

A comunicação é realizada através do `HttpClient` do Angular.

Exemplo:

```typescript
listarProvas(): Observable<Prova[]> {
  return this.http.get<Prova[]>(this.apiUrl);
}
```

A página chama o serviço, e o serviço realiza a requisição para o backend.

Fluxo:

```text
Página
   │
   ▼
ProvaService
   │
   ▼
HTTP Request
   │
   ▼
Spring Boot API
```

---

## Models

O diretório `models/` contém as interfaces e estruturas utilizadas para representar os dados recebidos e enviados pela aplicação.

```text
models/
├── ad.ts
├── atividade.ts
├── conteudo.ts
├── evento.ts
├── provas.ts
└── tarefa.ts
```

Esses modelos permitem que o TypeScript conheça a estrutura dos dados utilizados pela aplicação.

Por exemplo, uma prova pode possuir informações como:

```text
Prova
├── id
├── matéria
├── data
├── trimestre
└── conteúdos
```

Os modelos também ajudam a manter uma comunicação consistente entre os componentes e os serviços.

---

## Comunicação com o Backend

O frontend se comunica com o backend através de requisições HTTP para a API REST.

Durante o desenvolvimento, a API é executada em:

```text
http://localhost:8081
```

Um exemplo de comunicação é:

```text
Usuário
   │
   ▼
Página Angular
   │
   ▼
Service
   │
   │ HTTP
   ▼
Spring Boot
   │
   ▼
PostgreSQL
```

O frontend não acessa o PostgreSQL diretamente.

Toda operação de persistência passa pela API do backend.

---

## Tratamento de dados

As respostas da API são recebidas pelos serviços e disponibilizadas para as páginas através dos `Observable`s do Angular.

Exemplo:

```typescript
buscarProva(id: number): Observable<ProvaDetalhes> {
  return this.http.get<ProvaDetalhes>(
    `${this.apiUrl}/${id}`
  );
}
```

A página pode então utilizar os dados retornados para atualizar a interface.

Isso mantém a responsabilidade de comunicação HTTP dentro dos serviços, enquanto os componentes permanecem focados na apresentação e interação.

---

## Autenticação

A autenticação do frontend é integrada ao sistema de autenticação implementado no backend.

O fluxo básico é:

```text
Login
  │
  ▼
AuthService
  │
  ▼
POST /auth/login
  │
  ▼
Spring Security
  │
  ▼
JWT
  │
  ▼
Frontend armazena o token
```

Depois do login, o token JWT é utilizado nas requisições que precisam de autenticação.

Essa comunicação é automatizada pelo interceptor de autenticação.

A implementação detalhada da autenticação e do JWT está documentada em:

* [`authentication.md`](authentication.md)

---

## Auth Guard

O frontend possui um `auth.guard`, responsável por proteger rotas que exigem autenticação.

De forma simplificada:

```text
Usuário acessa rota protegida
          │
          ▼
       Auth Guard
       /       \
   autenticado  não autenticado
       │              │
       ▼              ▼
    Página          Login
```

Isso impede que usuários não autenticados acessem diretamente determinadas áreas da aplicação.

O guard trabalha em conjunto com o sistema de autenticação do frontend.

---

## Auth Interceptor

O `auth.interceptor` atua nas requisições HTTP realizadas pelo frontend.

Seu objetivo é adicionar o token JWT à requisição quando necessário.

O fluxo é:

```text
Página
   │
   ▼
Service
   │
   ▼
HttpClient
   │
   ▼
Auth Interceptor
   │
   ├── adiciona Authorization: Bearer <token>
   │
   ▼
Backend
```

Dessa forma, as páginas e serviços não precisam adicionar manualmente o token em cada requisição.

---

## Roteamento

O Angular Router é utilizado para controlar a navegação entre as diferentes páginas da aplicação.

Entre as áreas existentes estão:

```text
/login
/cadastro
/home
/provas
/tarefas
/ads
/eventos
```

As rotas que precisam de autenticação podem utilizar o `auth.guard`.

Isso permite separar as áreas públicas das áreas destinadas aos usuários autenticados.

---

## Exemplo de fluxo: visualização de uma prova

Um exemplo completo do funcionamento do frontend ocorre quando o usuário abre uma prova.

```text
1. Usuário acessa a página de provas
            │
            ▼
2. Componente solicita as provas
            │
            ▼
3. ProvaService realiza GET /provas
            │
            ▼
4. AuthInterceptor adiciona o JWT
            │
            ▼
5. Backend processa a requisição
            │
            ▼
6. API retorna os dados
            │
            ▼
7. ProvaService entrega os dados
            │
            ▼
8. Componente atualiza a interface
```

Ao acessar os detalhes de uma prova, o frontend pode realizar uma requisição como:

```http
GET /provas/{id}
```

O backend retorna a prova juntamente com seus conteúdos, permitindo que a página apresente as informações ao usuário.

---

## Separação de responsabilidades

A organização do frontend segue uma divisão de responsabilidades:

| Camada       | Responsabilidade                             |
| ------------ | -------------------------------------------- |
| Pages        | Interface e interação com o usuário          |
| Services     | Comunicação com a API                        |
| Models       | Estrutura dos dados                          |
| Guards       | Proteção das rotas                           |
| Interceptors | Interceptação e configuração das requisições |

Essa separação reduz o acoplamento entre as partes da aplicação e facilita futuras alterações.

Por exemplo, uma alteração na URL de uma API pode ser realizada no serviço correspondente sem exigir alterações em todos os componentes que utilizam aquela funcionalidade.

---

## Princípios utilizados

A estrutura do frontend foi organizada buscando:

* **Separação de responsabilidades** — cada parte possui uma função específica;
* **Reutilização** — serviços centralizam operações que podem ser utilizadas por diferentes páginas;
* **Baixo acoplamento** — páginas não acessam diretamente o banco ou implementam regras de persistência;
* **Tipagem** — TypeScript e models ajudam a manter estruturas de dados consistentes;
* **Segurança** — autenticação, guards e interceptor trabalham em conjunto com o backend;
* **Manutenibilidade** — a organização facilita localizar e modificar funcionalidades específicas.

---

## Relação com o Backend

O frontend e o backend possuem responsabilidades complementares:

```text
┌──────────────────────┐
│      Angular         │
│                      │
│ Pages                │
│ Services             │
│ Models               │
│ Guards               │
│ Interceptors         │
└──────────┬───────────┘
           │
           │ HTTP / REST
           ▼
┌──────────────────────┐
│    Spring Boot       │
│                      │
│ Controllers          │
│ Services             │
│ Repositories         │
│ DTOs                 │
└──────────┬───────────┘
           │
           ▼
┌──────────────────────┐
│     PostgreSQL       │
└──────────────────────┘
```

O Angular é responsável pela experiência do usuário e pela comunicação com a API.

O Spring Boot concentra as regras de negócio, autenticação e persistência.

O PostgreSQL é responsável pelo armazenamento dos dados.

---

## Documentação relacionada

* [`architecture.md`](architecture.md) — visão geral da arquitetura;
* [`backend.md`](backend.md) — estrutura e funcionamento do backend;
* [`authentication.md`](authentication.md) — autenticação, Spring Security e JWT;
* [`database.md`](database.md) — banco de dados e relacionamentos;
* [`api.md`](api.md) — endpoints disponíveis;
* [`development.md`](development.md) — configuração e execução do projeto.
