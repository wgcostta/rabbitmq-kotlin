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

```
src/main/kotlin/com/exemplo/
├── SpringRabbitmqFanoutApplication.kt
├── config/
│   └── RabbitMQConfig.kt
├── controller/
│   └── EventoController.kt
├── dto/
│   └── EventoDTO.kt
├── extensions/
│   └── KotlinExtensions.kt
└── service/
├── EventoProducerService.kt
└── EventoConsumerServices.kt
```

## ✨ Principais Diferenças do Kotlin

### 1. Data Classes
```kotlin
data class EventoDTO(
    var id: String? = null,
    var tipo: String? = null,
    // ...
)
```

### 2. Constructor Injection
```kotlin
@Service
class EventoProducerService(
    private val rabbitTemplate: RabbitTemplate
)
```

### 3. Null Safety
```kotlin
if (evento.id.isNullOrEmpty()) {
    evento.id = UUID.randomUUID().toString()
}
```

### 4. String Templates
```kotlin
"Não perca esta oportunidade: $descricao"
```

### 5. Extension Functions & Idiomático
```kotlin
fun main(args: Array<String>) {
    runApplication<SpringRabbitmqFanoutApplication>(*args)
}
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

## 🎯 Recursos Exclusivos do Kotlin

### 1. Extension Functions
```kotlin
fun EventoDTO.isValid(): Boolean = 
    !tipo.isNullOrBlank() && !titulo.isNullOrBlank()
```

### 2. Factory Pattern
```kotlin
val evento = EventoFactory.boasVindas("novo@usuario.com")
```

### 3. Sealed Classes (Type Safety)
```kotlin
val evento = TipoEvento.Seguranca.toEventoDTO(
    titulo = "Alerta",
    mensagem = "Login suspeito",
    destinatario = "admin@exemplo.com"
)
```

### 4. Infix Functions
```kotlin
val evento = "Compra aprovada!" notificar "cliente@exemplo.com"
```

## 🔍 Monitoramento

### Management UI
No RabbitMQ Management (http://localhost:15672):
1. **Exchanges** → Veja o `eventos.fanout`
2. **Queues** → Monitore as 3 filas
3. **Connections** → Veja as conexões ativas

### Health Checks
```bash
docker run -d \
  --name rabbitmq \
  -p 5672:5672 \
  -p 15672:15672 \
  -e RABBITMQ_DEFAULT_USER=admin \
  -e RABBITMQ_DEFAULT_PASS=admin123 \
  rabbitmq:3.12-management
  
# Aplicação
curl http://localhost:8080/actuator/health

# RabbitMQ
docker exec rabbitmq-server rabbitmq-diagnostics -q ping
```

## 🛑 Parando os Serviços

```bash
docker-compose down -v
```

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

## 🎯 Comparação: Java vs Kotlin

### Linhas de Código:
- **Java**: ~400 linhas total
- **Kotlin**: ~250 linhas total (**37% menos código**)

### Vantagens do Kotlin:
- ✅ **Null Safety** - Previne NPE em compile time
- ✅ **Data Classes** - Elimina boilerplate
- ✅ **Extension Functions** - Adiciona funcionalidades sem herança
- ✅ **Smart Casts** - Casting inteligente
- ✅ **String Templates** - Interpolação natural
- ✅ **Interoperabilidade 100%** com Java

---

**✨ Pronto!** Agora você tem um exemplo completo e funcional de Spring Boot com RabbitMQ usando o padrão Fanout em Kotlin!
```
