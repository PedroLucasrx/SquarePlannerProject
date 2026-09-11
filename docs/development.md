# Development

Este documento apresenta as instruções para configurar, executar e desenvolver o SquarePlanner em ambiente local.

## 1. Pré-requisitos

Para executar o projeto, é necessário ter instalado:

* Java 17 ou superior compatível com o projeto;
* Maven;
* Node.js e npm;
* Angular CLI 17;
* PostgreSQL.

O backend utiliza Java 17 como versão definida no `pom.xml`.

O frontend utiliza Angular 17 e TypeScript 5.4.2.

## 2. Estrutura do projeto

O projeto é dividido em duas aplicações principais:

```text
SquarePlanner/
├── backend/
└── frontend/
```

O backend é responsável pela API REST, autenticação, regras de negócio e acesso ao banco de dados.

O frontend é responsável pela interface da aplicação e pela comunicação com a API.

## 3. Configuração do banco de dados

O SquarePlanner utiliza PostgreSQL.

É necessário criar um banco de dados para a aplicação antes de iniciar o backend.

Exemplo:

```sql
CREATE DATABASE squareplanner;
```

Depois de criar o banco, o arquivo `schema.sql` deve ser executado para criar as tabelas utilizadas pela aplicação.

Entre as principais tabelas estão:

* `provas`
* `conteudos`
* `tarefas`
* `atividades`
* `ad`
* `evento`
* `usuarios`
* `progresso_conteudo`
* `progresso_ad`
* `progresso_atividades`

O schema também define chaves primárias, chaves estrangeiras, restrições de integridade e regras de exclusão em cascata.

## 4. Configuração do backend

Entre no diretório do backend:

```bash
cd backend
```

As dependências do projeto são gerenciadas pelo Maven através do `pom.xml`.

O backend utiliza, entre outras, as seguintes dependências:

* Spring Boot WebMVC;
* Spring Data JPA;
* PostgreSQL;
* Spring Security;
* Spring Security OAuth2 Resource Server;
* JJWT;
* Google API Client;
* Lombok.

Antes de executar a aplicação, as configurações de conexão com o PostgreSQL e as demais configurações necessárias devem estar definidas nos arquivos de configuração do backend.

### Execução

O projeto pode ser executado com:

```bash
./mvnw spring-boot:run
```

ou, caso o Maven esteja instalado globalmente:

```bash
mvn spring-boot:run
```

O backend é executado na porta:

```text
8081
```

A API fica disponível em:

```text
http://localhost:8081
```

## 5. Configuração do frontend

Entre no diretório do frontend:

```bash
cd frontend
```

Instale as dependências:

```bash
npm install
```

O frontend utiliza Angular 17.

As principais dependências incluem:

* Angular Core;
* Angular Router;
* Angular Forms;
* RxJS;
* TypeScript;
* Angular CLI.

### Execução

Para iniciar o servidor de desenvolvimento:

```bash
npm start
```

O comando executa:

```bash
ng serve
```

Por padrão, a aplicação fica disponível em:

```text
http://localhost:4200
```

## 6. Executando o projeto completo

Para utilizar o SquarePlanner localmente, os dois servidores devem estar em execução.

### Terminal 1 — Backend

```bash
cd backend
./mvnw spring-boot:run
```

### Terminal 2 — Frontend

```bash
cd frontend
npm start
```

Com ambos em execução:

```text
Frontend → http://localhost:4200
Backend  → http://localhost:8081
```

O frontend realiza as requisições HTTP para a API do backend.

## 7. Desenvolvimento do frontend

Durante o desenvolvimento, o Angular pode ser executado em modo de desenvolvimento através de:

```bash
npm start
```

Também existe o comando:

```bash
npm run watch
```

que executa:

```bash
ng build --watch --configuration development
```

O projeto possui suporte a testes através de:

```bash
npm test
```

que executa:

```bash
ng test
```

## 8. Build

Para gerar uma build do frontend:

```bash
npm run build
```

Esse comando executa:

```bash
ng build
```

Para o backend, a aplicação pode ser empacotada utilizando Maven:

```bash
./mvnw package
```

ou:

```bash
mvn package
```

## 9. Fluxo de desenvolvimento

O fluxo básico para trabalhar no projeto é:

```text
1. Iniciar PostgreSQL
        ↓
2. Garantir que o banco squareplanner esteja configurado
        ↓
3. Iniciar o backend
        ↓
4. Iniciar o frontend
        ↓
5. Desenvolver e testar as alterações
        ↓
6. Executar os testes/builds necessários
        ↓
7. Versionar as alterações com Git
```

As alterações relacionadas ao backend devem ser desenvolvidas considerando a separação entre Controller, Service, Repository e demais componentes da aplicação.

As alterações relacionadas ao frontend devem respeitar a separação entre páginas, serviços, modelos, guards e interceptors.

## 10. Versionamento

O projeto utiliza Git para controle de versão.

Após realizar uma alteração, é recomendado verificar os arquivos modificados:

```bash
git status
```

Adicionar as alterações:

```bash
git add .
```

Criar um commit:

```bash
git commit -m "Descrição da alteração"
```

E enviar para o repositório remoto:

```bash
git push
```

Os commits devem representar alterações coerentes, facilitando a identificação da evolução do projeto.

## 11. Verificação antes de finalizar uma alteração

Antes de considerar uma alteração concluída, recomenda-se verificar:

* se o backend inicia corretamente;
* se o frontend inicia corretamente;
* se a aplicação consegue acessar o banco de dados;
* se as requisições da API funcionam;
* se a autenticação continua funcionando quando a alteração envolve segurança;
* se o build não apresenta erros;
* se os testes relevantes continuam funcionando;
* se não foram adicionados arquivos ou informações sensíveis ao repositório.

## 12. Documentação relacionada

Para entender outras partes do projeto:

* [Architecture](architecture.md) — arquitetura geral da aplicação.
* [Backend](backend.md) — estrutura e responsabilidades do backend.
* [Frontend](frontend.md) — estrutura e funcionamento do frontend.
* [Database](database.md) — estrutura e persistência dos dados.
* [API](api.md) — endpoints e comunicação com o backend.
* [Authentication](authentication.md) — autenticação e autorização.
* [Decisions](decisions/) — principais decisões técnicas do projeto.
