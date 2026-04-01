# ============================================================
# Stage 1: Build
# ============================================================
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Copia o pom.xml primeiro para aproveitar o cache de dependências
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia o código-fonte e empacota
COPY src ./src
RUN mvn clean package -DskipTests -B

# ============================================================
# Stage 2: Runtime
# ============================================================
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Cria usuário não-root por segurança (sintaxe Ubuntu/Debian)
RUN groupadd --system appgroup && useradd --system --gid appgroup appuser

# Copia apenas o jar gerado no stage de build
COPY --from=builder /app/target/token-manager-1.0.0.jar app.jar

# Define o dono dos arquivos
RUN chown appuser:appgroup app.jar

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]