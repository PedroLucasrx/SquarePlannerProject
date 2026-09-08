# API do SquarePlanner

O SquarePlanner disponibiliza uma **API REST** desenvolvida com Spring Boot para realizar a comunicação entre o frontend Angular e o backend da aplicação.

A API é responsável por receber requisições HTTP, validar os dados, executar as regras de negócio e retornar as informações em formato JSON.

---

## Base URL

Durante o desenvolvimento, o backend é executado localmente na porta `8081`.

```text
http://localhost:8081
```

Os endpoints são organizados por recurso.

---

## Padrão de comunicação

A comunicação entre frontend e backend utiliza HTTP/HTTPS e os principais métodos REST:

| Método   | Utilização           |
| -------- | -------------------- |
| `GET`    | Consulta de dados    |
| `POST`   | Criação de dados     |
| `PUT`    | Atualização de dados |
| `DELETE` | Exclusão de dados    |

As informações são enviadas e recebidas principalmente no formato:

```text
application/json
```

---

# Autenticação

Os endpoints protegidos utilizam autenticação baseada em **JWT**.

Após realizar o login, o cliente recebe um token e deve enviá-lo nas requisições autenticadas através do header:

```http
Authorization: Bearer <token>
```

O token é adicionado automaticamente pelo interceptor de autenticação do frontend.

A implementação detalhada desse mecanismo está documentada em:

* [`authentication.md`](authentication.md)

---

# Recursos da API

A API possui endpoints relacionados aos seguintes recursos:

```text
/auth
/provas
/provas/conteudos
/tarefas
/atividades
/ads
/eventos
```

---

# Autenticação

## Login

```http
POST /auth/login
```

Realiza a autenticação de um usuário.

### Request

```json
{
  "email": "usuario@email.com",
  "senha": "senha"
}
```

### Response

Em caso de autenticação bem-sucedida, a API retorna as informações necessárias para utilização do token JWT.

```json
{
  "token": "<jwt>"
}
```

O token deve ser utilizado nas próximas requisições protegidas.

---



De forma geral, a API segue uma organização baseada em recursos:

```text
                    API
                     │
       ┌─────────────┼───────────────────
       │             │             │      
    /auth         /provas       /tarefas
                    │
             /provas/conteudos
       
       ┌─────────────┼─────────────┐
       │             │             │
   /atividades      /ads        /eventos
```

Cada recurso possui um controller responsável por receber as requisições correspondentes.

---



# Respostas de erro

A API possui tratamento centralizado de exceções através de um `GlobalExceptionHandler`.

Entre os erros tratados estão situações como:

* recurso não encontrado;
* dados inválidos;
* conteúdo duplicado;
* prova duplicada;
* formato inválido;
* usuário não encontrado.

Exemplos de exceções existentes:

```text
ProvaNotFound
ConteudoNotFound
TarefaNotFound
EventoNotFound
AtividadeNotFound
AdNotFound
UsuarioNotFound
DadosInvalidosException
FormatoInvalidoException
```

Quando uma operação falha, o backend transforma a exceção em uma resposta HTTP apropriada.

---

# Todos os endpoints

## Provas
 
### Listar provas
 
**Request**
 
```http
GET /provas
```
 
**Response**
 
```json
[
  {
  "id": 1,
  "materia": "Matemática",
  "data": "2026-08-20",
  "trimestre": 1,
  "conteudos": [
    {
      "id": 1,
      "nome": "Estatistica",
      "concluido": false
    },
    {
      "id": 2,
      "nome": "Probabilidade",
      "concluido": false
    }
  ]
},
{
  "id": 2,
  "materia": "Fisica",
  "data": "2026-08-21",
  "trimestre": 1,
  "conteudos": [
    {
      "id": 1,
      "nome": "Calorimetria",
      "concluido": true
    },
    {
      "id": 2,
      "nome": "Circuitos",
      "concluido": true
    }
  ]
},
{
  "id": 3,
  "materia": "Quimica",
  "data": "2026-08-22",
  "trimestre": 1,
  "conteudos": [
    {
      "id": 1,
      "nome": "Estequiometria",
      "concluido": false
    },
    {
      "id": 2,
      "nome": "Quimica organica",
      "concluido": false
    }
  ]
}
]
```

### Criar prova
 
**Request**
 
```http
POST /provas
Content-Type: application/json
```
 
```json
{
  "materia": "Matemática",
  "data": "2026-08-20",
  "trimestre": 1,
  "conteudos": [
    {
      "nome": "Estatistica"
    },
    {
      "nome": "Probabilidade"
    }
  ]
}
```
 
**Response**
 
```json
{
  "message": "Prova criada com sucesso"
}
```
 
### Buscar prova por ID
 
**Request**
 
```http
GET /provas/1
```
 
**Response**
 
```json
{
  "id": 1,
  "materia": "Matemática",
  "data": "2026-08-20",
  "trimestre": 1,
  "conteudos": [
    {
      "id": 1,
      "nome": "Estatistica",
      "concluido": false
    },
    {
      "id": 2,
      "nome": "Probabilidade",
      "concluido": false
    }
  ]
}
```
 
### Editar prova
 
**Request**
 
```http
PUT /provas/1
Content-Type: application/json
```
 
```json
{
  "materia": "Matemática",
  "data": "2026-08-20",
  "trimestre": 1,
  "conteudos": [
    {
      "nome": "Estatistica EDITADA",
      "concluido": true
    },
    {
      "nome": "Probabilidade EDITADA",
      "concluido": true
    }
  ]
}
```
 
**Response**
 
```json
{
  "message": "Prova editada com sucesso"
}
```
 
### Excluir prova
 
**Request**
 
```http
DELETE /provas/1
```
 
**Response**
 
```json
{
  "message": "Prova deletada com sucesso"
}
```
 
---
 
## Tarefas
 
### Listar tarefas
 
**Request**
 
```http
GET /tarefas
```
 
**Response**
 
```json
[
  {
		"id": 1,
		"materia": "Quimica exemplo",
		"data": "2026-09-02",
		"trimestre": 3,
		"atividades": [
			{
				"id": 1,
				"nome": "Lista 1 exemplo",
				"concluido": false
			},
			{
				"id": 2,
				"nome": "Lista 2 exemplo",
				"concluido": false
			},
			{
				"id": 3,
				"nome": "Lista 3 exemplo",
				"concluido": false
			}
		],
		"atividadesConcluidas": 0,
		"totalAtividades": 3,
		"progresso": 0.0
	},
	{
		"id": 2,
		"materia": "Matematica exemplo",
		"data": "2026-09-02",
		"trimestre": 3,
		"atividades": [
			{
				"id": 4,
				"nome": "Lista 1 exemplo",
				"concluido": false
			},
			{
				"id": 5,
				"nome": "Lista 2 exemplo",
				"concluido": false
			},
			{
				"id": 6,
				"nome": "Lista 3 exemplo",
				"concluido": false
			}
		],
		"atividadesConcluidas": 0,
		"totalAtividades": 3,
		"progresso": 0.0
	}
]
```
 
### Criar tarefa
 
**Request**
 
```http
POST /tarefas
Content-Type: application/json
```
 
```json
{
		"materia": "Matemática ",
		"data": "2026-09-15",
		"trimestre": 1,
		"atividades": [
			{
				"nome": "Efetuar polinômios "
			}
		]
}
```
 
**Response**
 
```json
{
  "message": "Tarefa criada com sucesso"
}
```
 
### Buscar tarefa por ID
 
**Request**
 
```http
GET /tarefas/1
```
 
**Response**
 
```json
{
	"id": 1,
	"materia": "Quimica exemplo",
	"data": "2026-09-02",
	"trimestre": 3,
	"atividades": [
		{
			"id": 1,
			"nome": "Lista 1 exemplo",
			"concluido": false
		},
		{
			"id": 2,
			"nome": "Lista 2 exemplo",
			"concluido": false
		},
		{
			"id": 3,
			"nome": "Lista 3 exemplo",
			"concluido": false
		}
	],
	"atividadesConcluidas": 0,
	"totalAtividades": 3,
	"progresso": 0.0
}
```
 
### Editar tarefa
 
**Request**
 
```http
PUT /tarefas/1
Content-Type: application/json
```
 
```json
{
	"materia": "Matematica",
	"data": "2026-09-15",
	"trimestre": 1,
	"atividades": [
		{
			"nome": "Efetuar polinômios EDITADO",
			"concluido": true
		},
		{
			"nome": "Nova lista CRIADO",
			"concluido": false
		}
	],
	"atividadesConcluidas": 1,
	"totalAtividades": 2,
	"progresso": 0.0
}
```
 
**Response**
 
```json
{
  "message": "Tarefa editada com sucesso"
}
```
 
### Alterar estado da tarefa
 
**Request**
 
```http
PUT /tarefas/1/estado
Content-Type: application/json
```
 
```json
{
  "concluido": true
}
```
 
**Response**
 
```json
{
  "message": "Estado da tarefa atualizado com sucesso"
}
```
 
### Excluir tarefa
 
**Request**
 
```http
DELETE /tarefas/1
```
 
**Response**
 
```json
{
  "message": "Tarefa deletada com sucesso"
}
```
 
---
  
## ADs
 
### Listar ADs
 
**Request**
 
```http
GET /ads
```
 
**Response**
 
```json
{
	"ads": [
		{
			"id": 1,
			"materia": "Quimica exemplo",
			"data": "2026-09-02",
			"trimestre": 3,
			"proposta": "Proposta no laboratorio",
			"concluido": false
		},
		{
			"id": 2,
			"materia": "Biologia exemplo",
			"data": "2026-09-02",
			"trimestre": 3,
			"proposta": "mapa mental",
			"concluido": false
		},
		{
			"id": 3,
			"materia": "Educação fisica exemplo",
			"data": "2026-09-03",
			"trimestre": 3,
			"proposta": "Apresentação em sala ",
			"concluido": false
		},
		{
			"id": 4,
			"materia": "Português exemplo",
			"data": "2026-09-08",
			"trimestre": 3,
			"proposta": "Proposta exemplo ",
			"concluido": false
		}
	],
	"adsConcluidas": 0,
	"totalAds": 4,
	"progresso": 0.0
}
```
 
### Criar AD
 
**Request**
 
```http
POST /ads
Content-Type: application/json
```
 
```json
{
  "materia": "Português",
  "data": "2026-08-20",
  "trimestre": 1,
  "proposta": "Teste em sala"
}
```
 
**Response**
 
```json
{
  "message": "AD criada com sucesso"
}
```
 
### Editar AD
 
**Request**
 
```http
PUT /ads/1
Content-Type: application/json
```
 
```json
{
    "materia": "Português",
    "data": "2026-08-22",
    "trimestre": 1,
    "proposta": "Mapa mental",
    "concluido": true          
}
```
 
**Response**
 
```json
{
  "message": "AD editada com sucesso"
}
```
 
### Alterar estado da AD
 
**Request**
 
```http
PUT /ads/1/estado
Content-Type: application/json
```
 
```json
{
  "concluido": true
}
```
 
**Response**
 
```json
{
  "message": "Estado da AD atualizado com sucesso"
}
```
 
### Excluir AD
 
**Request**
 
```http
DELETE /ads/1
```
 
**Response**
 
```json
{
  "message": "AD deletada com sucesso"
}
```
 
---
 
## Eventos
 
### Listar eventos
 
**Request**
 
```http
GET /eventos
```
 
**Response**
 
```json
[
	{
		"nome": "Passeio exemplo",
		"data": "2026-09-07",
		"id": 1
	},
	{
		"nome": "trote terceirão exemplo",
		"data": "2026-09-02",
		"id": 2
	},
	{
		"nome": "Recuperarte exemplo",
		"data": "2026-09-08",
		"id": 3
	}
]
```
 
### Criar evento
 
**Request**
 
```http
POST /eventos
Content-Type: application/json
```
 
```json
{
  "nome": "Reunião",
  "data": "2026-08-20"
}
```
 
**Response**
 
```json
{
  "message": "Evento criado com sucesso"
}
```
 
### Editar evento
 
**Request**
 
```http
PUT /eventos/1
Content-Type: application/json
```
 
```json
{
  "nome": "Reunião atualizada",
  "data": "2026-08-22"
}
```
 
**Response**
 
```json
{
  "message": "Evento editado com sucesso"
}
```
 
### Excluir evento
 
**Request**
 
```http
DELETE /eventos/1
```
 
**Response**
 
```json
{
  "message": "Evento deletado com sucesso"
}
```
 

# Documentação relacionada

* [`architecture.md`](architecture.md) — arquitetura geral;
* [`backend.md`](backend.md) — implementação do backend;
* [`frontend.md`](frontend.md) — implementação do frontend;
* [`database.md`](database.md) — banco de dados e ERD;
* [`authentication.md`](authentication.md) — autenticação e JWT;
* [`development.md`](development.md) — configuração e execução.
