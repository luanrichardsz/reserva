# Estágio 1: Build da aplicação
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copia os arquivos do maven wrapper e pom para baixar dependências em cache
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw

# Copia o código-fonte e compila gerando o .jar
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Estágio 2: Imagem final enxuta para execução
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

# Render define a variável PORT dinamicamente (geralmente 10000)
ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT} -jar app.jar"]
