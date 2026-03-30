# 🔐 TOKEN VAULT — Spring Boot Token Manager

Sistema de gerenciamento de tokens com geração automática, validade de 5 minutos e banco MySQL via Docker.

---

## 📋 Requisitos

- Java 17+
- Maven 3.8+
- Docker + Docker Compose

---

## 🚀 Como Executar

### 1. Subir o banco de dados MySQL com Docker

```bash
docker-compose up -d
```

Aguarde o banco inicializar (10-15 segundos). Verifique:

```bash
docker-compose ps
docker logs token-manager-db
```

### 2. Compilar e executar a aplicação

```bash
cd token-manager
mvn clean package -DskipTests
java -jar target/token-manager-1.0.0.jar
```

Ou via Maven diretamente:

```bash
mvn spring-boot:run
```

### 3. Acessar o dashboard

Abra no navegador: **http://localhost:8080**

---

## 🎯 Funcionalidades

| Funcionalidade | Descrição |
|---|---|
| ⚡ Geração automática | Token gerado a cada **30 segundos** via `@Scheduled` |
| ✅ Tokens válidos | Exibidos com barra de tempo restante |
| ❌ Tokens expirados | Exibidos com badge de expirado |
| 📊 Dashboard em tempo real | Atualização automática a cada **5 segundos** via AJAX |
| 🗃️ Persistência | Todos os tokens salvos no **MySQL** |
| 🔘 Geração manual | Botão para gerar token instantaneamente |

---

## 🌐 API REST

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/api/tokens/dashboard` | Todos os dados do dashboard |
| GET | `/api/tokens/all` | Lista todos os tokens |
| GET | `/api/tokens/valid` | Lista tokens válidos |
| GET | `/api/tokens/expired` | Lista tokens expirados |
| GET | `/api/tokens/stats` | Estatísticas (total, válidos, expirados) |
| POST | `/api/tokens/generate` | Gera um token manualmente |

---

## ⚙️ Configuração do banco (docker-compose.yml)

```yaml
MySQL:
  Host: localhost:3306
  Database: tokendb
  User: tokenuser
  Password: token123
```

---

## 🛑 Parar os serviços

```bash
# Parar o banco
docker-compose down

# Parar e remover volume (apaga todos os dados)
docker-compose down -v
```

---

## 📁 Estrutura do Projeto

```
token-manager/
├── docker-compose.yml          # Banco MySQL em Docker
├── docker/
│   └── init.sql                # Script inicial do banco
├── pom.xml
└── src/main/java/com/tokenmanager/
    ├── TokenManagerApplication.java    # Main class
    ├── controller/
    │   └── TokenController.java        # Endpoints web + REST
    ├── model/
    │   └── Token.java                  # Entidade JPA
    ├── repository/
    │   └── TokenRepository.java        # Queries no banco
    ├── service/
    │   └── TokenService.java           # Lógica de negócio
    └── scheduler/
        └── TokenScheduler.java         # Geração automática @Scheduled
```
