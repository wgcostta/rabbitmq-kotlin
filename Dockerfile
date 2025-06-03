FROM openjdk:17-jdk-slim

WORKDIR /app

# Copiar o arquivo JAR da aplicação
COPY target/*.jar app.jar

# Expor a porta da aplicação
EXPOSE 8080

# Comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]