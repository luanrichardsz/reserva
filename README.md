# 📅 Sistema de Gestão de Reservas de Salas

API REST desenvolvida em **Java 21** e **Spring Boot** para gerenciamento de salas, usuários e reservas de horários sem conflitos.

---

## 🚀 Como Executar a Aplicação

### Pré-requisitos
* **Java 21** instalado
* **PostgreSQL** instalado e em execução (ou container Docker)
* **Maven** (o projeto já inclui o wrapper `./mvnw`)

### Passo a Passo

1. **Clone o repositório:**
   ```bash
   git clone <URL_DO_REPOSITORIO>
   cd reserva
   ```

2. **Configure as variáveis de ambiente:**
   Crie um arquivo `.env` na raiz do projeto com as credenciais do seu banco de dados:
   ```properties
   DB_URL=jdbc:postgresql://localhost:5432/nome_do_seu_banco
   DB_USERNAME=seu_usuario
   DB_PASSWORD=sua_senha
   ```

3. **Execute as migrações e inicie o projeto:**
   O Flyway criará as tabelas automaticamente no primeiro início.
   ```bash
   ./mvnw spring-boot:run
   ```
   A aplicação estará disponível em: `http://localhost:8080`.

---

## 🏛️ Explicação da Arquitetura Escolhida

O projeto adota a arquitetura clássica em **3 camadas (Layered Architecture)** com separação estrita de papéis:

1. **Controller (Apresentação / HTTP):** Recebe as requisições web, valida se o formato dos dados está correto (`@Valid`) e devolve a resposta HTTP adequada (`200`, `201`, `400`, etc.).
2. **Service (Negócio):** Onde reside o raciocínio central do sistema. Valida regras de negócio (conflitos de horário, salas interditadas, etc.) e orquestra a lógica.
3. **Repository (Persistência / Dados):** Interfaces Spring Data JPA responsáveis por conversar diretamente com o banco de dados.
4. **DTOs (Data Transfer Objects):** Records Java usados para receber dados (`RequestDTO`) e devolver apenas os campos necessários (`ResponseDTO`), evitando expor a entidade do banco diretamente para a web.
5. **GlobalExceptionHandler:** Centraliza todo o tratamento de erros em um só lugar, garantindo respostas de erro claras e amigáveis para quem consome a API.

---

## 🗺️ Mapeamento Completo de Endpoints

### 👤 Usuários (`/usuarios`)

#### 1. Cadastrar Usuário
* **Método:** `POST`
* **Rota:** `/usuarios`
* **Body (JSON):**
  ```json
  {
    "nome": "João Silva",
    "cargo": "Desenvolvedor",
    "setor": "TI"
  }
  ```
* **Retorno:** `201 Created`

#### 2. Listar Usuários
* **Método:** `GET`
* **Rota:** `/usuarios`
* **Retorno:** `200 OK` (Lista de usuários)

---

### 🏢 Salas (`/salas`)

#### 1. Cadastrar Sala
* **Método:** `POST`
* **Rota:** `/salas`
* **Body (JSON):**
  ```json
  {
    "nome": "Sala de Reunião A",
    "descricao": "Sala equipada com projetor e videoconferência",
    "capacidade": 10
  }
  ```
* **Retorno:** `201 Created` (ou `409 Conflict` se o nome já existir)

#### 2. Listar Salas
* **Método:** `GET`
* **Rota:** `/salas`
* **Retorno:** `200 OK` (Lista de salas)

#### 3. Interditar Sala
* **Método:** `PATCH`
* **Rota:** `/salas/{idSala}/interditar`
* **Retorno:** `200 OK` (Altera `ativo` para `false`)

---

### 📆 Reservas (`/reservas`)

#### 1. Criar Reserva
* **Método:** `POST`
* **Rota:** `/reservas`
* **Body (JSON):**
  ```json
  {
    "idSala": 1,
    "idUsuario": 2,
    "dataInicio": "2026-10-25T14:00:00Z",
    "dataFim": "2026-10-25T15:30:00Z"
  }
  ```
* **Retorno:** `201 Created` (Valida sala ativa, existência do usuário/sala e conflitos de horários)

#### 2. Listar Todas as Reservas
* **Método:** `GET`
* **Rota:** `/reservas`
* **Retorno:** `200 OK`

#### 3. Listar Reservas por Data (Dia Civil)
* **Método:** `GET`
* **Rota:** `/reservas/data?data=2026-10-25`
* **Retorno:** `200 OK`

#### 4. Listar Reservas por Status
* **Método:** `GET`
* **Rota:** `/reservas/status?status=CONFIRMADO` (ou `CANCELADO`)
* **Retorno:** `200 OK`

#### 5. Listar Reservas de um Usuário
* **Método:** `GET`
* **Rota:** `/reservas/usuario/{idUsuario}`
* **Retorno:** `200 OK`

#### 6. Listar Reservas de uma Sala
* **Método:** `GET`
* **Rota:** `/reservas/sala/{idSala}`
* **Retorno:** `200 OK`

#### 7. Buscar Reserva por ID
* **Método:** `GET`
* **Rota:** `/reservas/{idReserva}`
* **Retorno:** `200 OK` (ou `404 Not Found`)

#### 8. Cancelar Reserva
* **Método:** `PATCH`
* **Rota:** `/reservas/{idReserva}/cancelar`
* **Retorno:** `200 OK` (ou `409 Conflict` se já cancelada)

---

## ❓ Perguntas e Respostas sobre o Projeto

### 1. Por que você organizou a aplicação dessa maneira?
Para manter o código **organizado, fácil de entender e simples de manter**.  
Quando cada parte do código tem uma única responsabilidade:
* Se precisamos mudar a URL ou receber um parâmetro novo, mexemos apenas no **Controller**.
* Se a regra de cancelamento ou choque de horário mudar, alteramos apenas o **Service**.
* Se precisarmos otimizar uma consulta SQL, alteramos apenas o **Repository**.  

---

### 2. Onde estão as regras de negócio?
As regras de negócio estão centralizadas na camada de **`Service`** (principalmente no `ReservaService` e `SalaService`).  

---

### 3. O que aconteceria se amanhã precisássemos trocar o banco de dados?
Praticamente **nada mudaria na lógica do sistema (Services e Controllers)**.  
Como usamos o **Spring Data JPA** e o **Hibernate**, o Java abstrai a comunicação com o banco:
1. Bastaria trocar a dependência do driver no `pom.xml` (ex: trocar Postgres por MySQL ou Oracle).
2. Atualizar as credenciais e a URL de conexão no arquivo `.env` / `application.properties`.
3. Ajustar os scripts do Flyway (`V1__criar-tabelas.sql`) apenas se houver algum tipo de dado muito específico do PostgreSQL.

O restante do código Java funcionaria da mesma forma sem nenhuma alteração.

---

### 4. Como você adicionaria uma regra impedindo reservas fora do horário comercial?
Adicionaria essa validação dentro do método `cadastrar` no **`ReservaService`**, logo antes de salvar a reserva:

1. **Converter o `Instant` para o fuso horário local:** Como os horários chegam em UTC (`Instant`), converteria para o horário de Brasília (`America/Sao_Paulo`) usando `ZonedDateTime` ou `LocalTime`.
2. **Validar o dia da semana:** Checar se o dia não é sábado ou domingo (`DayOfWeek.SATURDAY` ou `SUNDAY`).
3. **Validar a faixa de horários:** Checar se a hora de início é maior ou igual às 08:00 e o término é menor ou igual às 18:00.
4. **Disparar exceção:** Se estiver fora do horário, lançar uma exceção de validação (ex: `IllegalArgumentException("Reservas só são permitidas de segunda a sexta, das 08:00 às 18:00!")`), que o `GlobalExceptionHandler` já intercepta e devolve como um erro `400 Bad Request` claro para o usuário.
