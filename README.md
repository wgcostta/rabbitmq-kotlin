# Spring Boot + RabbitMQ Fanout Example (Kotlin)

Este projeto demonstra o uso do padrão **Fanout Exchange** do RabbitMQ com Spring Boot em **Kotlin** para distribuir mensagens para múltiplas filas simultaneamente.

## 🏗️ Arquitetura

### Componentes:
- **Spring Boot Application (Kotlin)**: API REST para publicar eventos
- **RabbitMQ**: Message broker com exchange fanout
- **3 Consumers**: Email, SMS e Push Notifications

### Padrão Fanout:
- Uma mensagem publicada no exchange é enviada para **TODAS** as filas vinculadas
- Routing keys são ignoradas (diferente do Direct e Topic)
- Ideal para broadcasting de eventos

## 📁 Estrutura do Projeto