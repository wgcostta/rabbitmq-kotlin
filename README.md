# Spring Boot + RabbitMQ Fanout Example

Este projeto demonstra o uso do padrão **Fanout Exchange** do RabbitMQ com Spring Boot para distribuir mensagens para múltiplas filas simultaneamente.

## 🧪 Cenários de Teste Avançados

### Teste de Volume
```bash
# Enviar múltiplas mensagens rapidamente
for i in {1..10}; do
  curl -X POST "http://localhost:8080/api/eventos/boas-vindas?destinatario=usuario$i@exemplo.com"
  sleep 1
done
```

### Teste de Erro
```bash
# Evento com dados inválidos para testar tratamento de erro
curl -X POST http://localhost:8080/api/eventos/publicar \
  -H "Content-Type: application/json" \
  -d '{
    "tipo": "",
    "titulo": null,
    "mensagem": "",
    "destinatario": ""
  }'
```

## 📚 Conceitos do RabbitMQ Fanout

### O que é Fanout Exchange?
- **Broadcasting**: Uma mensagem é copiada para todas as filas vinculadas
- **Sem Routing**: Routing keys são completamente ignoradas
- **Desempenho**: Muito rápido, pois não precisa analisar routing keys
- **Casos de Uso**: Notificações, logs, eventos de sistema

### Comparação com Outros Exchanges

| Exchange Type | Routing | Uso |
|---------------|---------|-----|
| **Fanout** | Broadcast para todas as filas | Notificações em massa |
| **Direct** | Routing key exata | Processamento direcionado |
| **Topic** | Pattern matching | Roteamento flexível |
| **Headers** | Atributos do header | Roteamento complexo |

### Vantagens do Fanout
- ✅ Simplicidade de configuração
- ✅ Alta performance
- ✅ Garantia de entrega para todos os consumers
- ✅ Desacoplamento completo entre producer e consumers

### Desvantagens do Fanout
- ❌ Não há controle granular de roteamento
- ❌ Pode gerar tráfego desnecessário
- ❌ Todos os consumers recebem todas as mensagens

## 🔧 Personalizações Possíveis

### 1. Adicionar Dead Letter Exchange
```java
@Bean
public Queue filaEmailComDLQ() {
    return QueueBuilder
        .durable(FILA_EMAIL)
        .withArgument("x-dead-letter-exchange", "dlx.fanout")
        .withArgument("x-dead-letter-routing-key", "email.failed")
        .build();
}
```

### 2. Configurar TTL (Time To Live)
```java
@Bean
public Queue filaComTTL() {
    return QueueBuilder
        .durable("fila.com.ttl")
        .withArgument("x-message-ttl", 60000) // 60 segundos
        .build();
}
```

### 3. Limitar Tamanho da Fila
```java
@Bean
public Queue filaLimitada() {
    return QueueBuilder
        .durable("fila.limitada")
        .withArgument("x-max-length", 1000)
        .withArgument("x-overflow", "drop-head")
        .build();
}
```

## 🐛 Troubleshooting

### Problema: Aplicação não conecta ao RabbitMQ
```bash
# Verificar se o RabbitMQ está rodando
docker-compose ps

# Verificar logs do RabbitMQ
docker-compose logs rabbitmq

# Testar conexão manual
telnet localhost 5672
```

### Problema: Mensagens não são consumidas
```bash
# Verificar filas no Management UI
# Ou via CLI:
docker exec rabbitmq-server rabbitmqctl list_queues
```

### Problema: Erro de permissão
```bash
# Verificar usuários
docker exec rabbitmq-server rabbitmqctl list_users

# Resetar permissões
docker exec rabbitmq-server rabbitmqctl set_permissions -p / admin ".*" ".*" ".*"
```

## 📈 Métricas e Monitoramento

### Endpoints do Actuator
```bash
# Métricas gerais
curl http://localhost:8080/actuator/metrics

# Métricas específicas do RabbitMQ
curl http://localhost:8080/actuator/metrics/rabbitmq.connections

# Info da aplicação
curl http://localhost:8080/actuator/info
```

### Comandos RabbitMQ CLI
```bash
# Status do cluster
docker exec rabbitmq-server rabbitmqctl cluster_status

# Lista de exchanges
docker exec rabbitmq-server rabbitmqctl list_exchanges

# Lista de bindings
docker exec rabbitmq-server rabbitmqctl list_bindings

# Estatísticas das filas
docker exec rabbitmq-server rabbitmqctl list_queues name messages_ready messages_unacknowledged
```

## 🚀 Próximos Passos

### Melhorias Sugeridas
1. **Implementar retry policies** mais sofisticadas
2. **Adicionar métricas customizadas** com Micrometer
3. **Configurar clustering** do RabbitMQ
4. **Implementar circuit breakers** com Resilience4j
5. **Adicionar testes de integração** com Testcontainers

### Outros Padrões para Explorar
- **Direct Exchange**: Roteamento por routing key exata
- **Topic Exchange**: Roteamento por padrões
- **Request/Reply**: Comunicação síncrona via RabbitMQ
- **Competing Consumers**: Load balancing entre consumers

## 📝 Logs Importantes

### Sucesso na Publicação
```
INFO  [EventoProducerService] - Publicando evento: EventoDTO{id='123', tipo='TESTE'...}
INFO  [EventoProducerService] - Evento publicado com sucesso. ID: 123
```

### Processamento pelos Consumers
```
INFO  [EventoConsumerServices] - 📧 [EMAIL] Processando evento: 123
INFO  [EventoConsumerServices] - 📱 [SMS] Processando evento: 123
INFO  [EventoConsumerServices] - 🔔 [PUSH] Processando evento: 123
```

### Erro de Conexão
```
ERROR [o.s.a.r.l.SimpleMessageListenerContainer] - Failed to check/redeclare auto-delete destination(s)
```

## 🎓 Recursos de Aprendizado

### Documentação Oficial
- [Spring AMQP Reference](https://docs.spring.io/spring-amqp/docs/current/reference/html/)
- [RabbitMQ Tutorials](https://www.rabbitmq.com/getstarted.html)
- [Docker Compose Reference](https://docs.docker.com/compose/)

### Comandos Úteis Docker
```bash
# Ver logs em tempo real
docker-compose logs -f

# Reiniciar apenas um serviço
docker-compose restart app

# Rebuild da aplicação
docker-compose up --build app

# Limpeza completa
docker-compose down -v --rmi all
```

---

**✨ Pronto!** Agora você tem um exemplo completo e funcional de Spring Boot com RabbitMQ usando o padrão Fanout. Experimente os diferentes endpoints e observe como as mensagens são distribuídas para todos os consumers simultaneamente!🏗️ Arquitetura

### Componentes:
- **Spring Boot Application**: API REST para publicar eventos
- **RabbitMQ**: Message broker com exchange fanout
- **3 Consumers**: Email, SMS e Push Notifications

### Padrão Fanout:
- Uma mensagem publicada no exchange é enviada para **TODAS** as filas vinculadas
- Routing keys são ignoradas (diferente do Direct e Topic)
- Ideal para broadcasting de eventos

## 📁 Estrutura do Projeto

```
src/main/java/com/exemplo/
├── SpringRabbitmqFanoutApplication.java
├── config/
│   └── RabbitMQConfig.java
├── controller/
│   └── EventoController.java
├── dto/
│   └── EventoDTO.java
└── service/
    ├── EventoProducerService.java
    └── EventoConsumerServices.java
```

## 🚀 Como Executar

### 1. Pré-requisitos
- Java 17+
- Maven 3.6+
- Docker e Docker Compose

### 2. Build da Aplicação
```bash
mvn clean package -DskipTests
```

### 3. Subir os Serviços
```bash
docker-compose up -d
```

### 4. Verificar os Logs
```bash
# Logs da aplicação
docker-compose logs -f app

# Logs do RabbitMQ
docker-compose logs -f rabbitmq
```

## 🎯 Testando a Aplicação

### Interface Web do RabbitMQ
- URL: http://localhost:15672
- Usuário: `admin`
- Senha: `admin123`

### Endpoints da API

#### 1. Teste Rápido
```bash
curl http://localhost:8080/api/eventos/teste
```

#### 2. Boas-vindas
```bash
curl -X POST "http://localhost:8080/api/eventos/boas-vindas?destinatario=usuario@exemplo.com"
```

#### 3. Promoção
```bash
curl -X POST "http://localhost:8080/api/eventos/promocao?destinatario=cliente@exemplo.com&descricao=50%25%20de%20desconto"
```

#### 4. Evento Customizado
```bash
curl -X POST http://localhost:8080/api/eventos/publicar \
  -H "Content-Type: application/json" \
  -d '{
    "tipo": "PERSONALIZADO",
    "titulo": "Evento Teste",
    "mensagem": "Esta é uma mensagem personalizada",
    "destinatario": "test@exemplo.com"
  }'
```

#### 5. Health Check
```bash
curl http://localhost:8080/api/eventos/health
```

## 📊 Observando o Comportamento

Quando você enviar uma mensagem, verá nos logs que **todos os 3 consumers** processam a mesma mensagem:

```
📧 [EMAIL] Processando evento: abc-123
📱 [SMS] Processando evento: abc-123  
🔔 [PUSH] Processando evento: abc-123
```

## 🔧 Configurações Importantes

### RabbitMQ Config
- **Exchange**: `eventos.fanout` (tipo Fanout)
- **Filas**: `fila.email`, `fila.sms`, `fila.push`
- **Bindings**: Todas as filas conectadas ao exchange

### Properties
- Conexão: `spring.rabbitmq.host=rabbitmq`
- Credenciais: `admin/admin123`
- Retry: 3 tentativas com intervalo de 2s

## 🔍 Monitoramento

### Management UI
No RabbitMQ Management (http://localhost:15672):
1. **Exchanges** → Veja o `eventos.fanout`
2. **Queues** → Monitore as 3 filas
3. **Connections** → Veja as conexões ativas

### Health Checks
```bash
# Aplicação
curl http://localhost:8080/actuator/health

# RabbitMQ
docker exec rabbitmq-server rabbitmq-diagnostics -q ping
```

## 🛑 Parando os Serviços

```bash
docker-compose down -v
```

##