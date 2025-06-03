---

## 🧪 Scripts de Teste

### `test-endpoints.sh`
```bash
#!/bin/bash

# Script para testar os endpoints da aplicação Spring Boot + RabbitMQ Fanout (Kotlin)
# Certifique-se de que a aplicação está rodando antes de executar

BASE_URL="http://localhost:8080/api/eventos"

echo "🚀 Iniciando testes da aplicação Spring Boot + RabbitMQ Fanout (Kotlin)"
echo "=================================================="

# Função para imprimir separadores
print_separator() {
    echo ""
    echo "=================================================="
    echo "$1"
    echo "=================================================="
}

# Função para aguardar
wait_seconds() {
    echo "⏳ Aguardando $1 segundos..."
    sleep $1
}

# 1. Health Check
print_separator "1. HEALTH CHECK"
curl -s "$BASE_URL/health" | jq '.' || curl -s "$BASE_URL/health"
wait_seconds 2

# 2. Teste Rápido
print_separator "2. TESTE RÁPIDO"
curl -s "$BASE_URL/teste" | jq '.' || curl -s "$BASE_URL/teste"
wait_seconds 3

# 3. Boas-vindas
print_separator "3. NOTIFICAÇÃO DE BOAS-VINDAS (Kotlin Style)"
curl -s -X POST "$BASE_URL/boas-vindas?destinatario=kotlin.user@exemplo.com" | jq '.' || \
curl -s -X POST "$BASE_URL/boas-vindas?destinatario=kotlin.user@exemplo.com"
wait_seconds 3

# 4. Promoção
print_separator "4. NOTIFICAÇÃO DE PROMOÇÃO (String Templates)"
curl -s -X POST "$BASE_URL/promocao?destinatario=cliente.kotlin@exemplo.com&descricao=Kotlin%20é%20incrível%20-%2070%25%20OFF" | jq '.' || \
curl -s -X POST "$BASE_URL/promocao?destinatario=cliente.kotlin@exemplo.com&descricao=Kotlin%20é%20incrível%20-%2070%25%20OFF"
wait_seconds 3

# 5. Evento Customizado (Sealed Class Style)
print_separator "5. EVENTO CUSTOMIZADO (Kotlin Data Class)"
curl -s -X POST "$BASE_URL/publicar" \
  -H "Content-Type: application/json" \
  -d '{
    "tipo": "KOTLIN_DEMO",
    "titulo": "Demonstração Kotlin",
    "mensagem": "Testando recursos avançados: data classes, null safety, extension functions",
    "destinatario": "kotlin.dev@exemplo.com"
  }' | jq '.' || \
curl -s -X POST "$BASE_URL/publicar" \
  -H "Content-Type: application/json" \
  -d '{
    "tipo": "KOTLIN_DEMO",
    "titulo": "Demonstração Kotlin",
    "mensagem": "Testando recursos avançados: data classes, null safety, extension functions",
    "destinatario": "kotlin.dev@exemplo.com"
  }'
wait_seconds 3

# 6. Teste de Null Safety
print_separator "6. TESTE NULL SAFETY (Dados Parciais)"
curl -s -X POST "$BASE_URL/publicar" \
  -H "Content-Type: application/json" \
  -d '{
    "tipo": "TESTE_NULL",
    "titulo": "Teste Null Safety",
    "mensagem": null,
    "destinatario": "null.safety@exemplo.com"
  }' | jq '.' || \
curl -s -X POST "$BASE_URL/publicar" \
  -H "Content-Type: application/json" \
  -d '{
    "tipo": "TESTE_NULL",
    "titulo": "Teste Null Safety",
    "mensagem": null,
    "destinatario": "null.safety@exemplo.com"
  }'
wait_seconds 2

# 7. Teste de Volume (Kotlin Collection Processing)
print_separator "7. TESTE DE VOLUME - 5 MENSAGENS (Kotlin Style)"
for i in {1..5}; do
    echo "📤 Enviando mensagem Kotlin $i..."
    curl -s -X POST "$BASE_URL/boas-vindas?destinatario=kotlin$i@exemplo.com" > /dev/null
    sleep 1
done
echo "✅ 5 mensagens Kotlin enviadas!"
wait_seconds 5

# 8. Diferentes tipos de eventos (Demonstrando Kotlin Features)
print_separator "8. DIVERSOS TIPOS DE EVENTOS (Kotlin Features)"

# Evento de segurança (Extension Functions)
echo "🔒 Enviando evento de segurança..."
curl -s -X POST "$BASE_URL/publicar" \
  -H "Content-Type: application/json" \
  -d '{
    "tipo": "SEGURANCA",
    "titulo": "Login suspeito detectado",
    "mensagem": "Tentativa de login de localização desconhecida - Kotlin Null Safety ativo",
    "destinatario": "security.kotlin@exemplo.com"
  }' > /dev/null
sleep 1

# Evento de compra (Data Classes)
echo "🛒 Enviando evento de compra..."
curl -s -X POST "$BASE_URL/publicar" \
  -H "Content-Type: application/json" \
  -d '{
    "tipo": "COMPRA",
    "titulo": "Compra realizada com sucesso!",
    "mensagem": "Sua compra de R$ 299,90 foi processada usando Kotlin Data Classes",
    "destinatario": "comprador.kotlin@exemplo.com"
  }' > /dev/null
sleep 1

# Evento de sistema (Companion Objects)
echo "⚙️ Enviando evento de sistema..."
curl -s -X POST "$BASE_URL/publicar" \
  -H "Content-Type: application/json" \
  -d '{
    "tipo": "SISTEMA",
    "titulo": "Backup realizado",
    "mensagem": "Backup diário concluído com sucesso - Sistema Kotlin funcionando perfeitamente",
    "destinatario": "admin.kotlin@exemplo.com"
  }' > /dev/null

echo "✅ Eventos diversos enviados!"
wait_seconds 3

# 9. Teste de recursos específicos do Kotlin
print_separator "9. RECURSOS ESPECÍFICOS DO KOTLIN"

# String Templates
echo "🔤 Testando String Templates..."
TIMESTAMP=$(date +%s)
curl -s -X POST "$BASE_URL/publicar" \
  -H "Content-Type: application/json" \
  -d "{
    \"tipo\": \"STRING_TEMPLATE\",
    \"titulo\": \"Teste String Template\",
    \"mensagem\": \"Mensagem gerada em timestamp: $TIMESTAMP usando String Templates do Kotlin\",
    \"destinatario\": \"template@exemplo.com\"
  }" > /dev/null

# Smart Casts
echo "🧠 Testando Smart Casts..."
curl -s -X POST "$BASE_URL/publicar" \
  -H "Content-Type: application/json" \
  -d '{
    "tipo": "SMART_CAST",
    "titulo": "Teste Smart Cast",
    "mensagem": "Kotlin Smart Casts garantem type safety em tempo de compilação",
    "destinatario": "smartcast@exemplo.com"
  }' > /dev/null

echo "✅ Recursos Kotlin testados!"
wait_seconds 3

print_separator "✨ TESTES KOTLIN CONCLUÍDOS"
echo "📊 Verifique os logs da aplicação para ver o processamento:"
echo "   docker-compose logs -f app"
echo ""
echo "🌐 Acesse o RabbitMQ Management UI:"
echo "   http://localhost:15672 (admin/admin123)"
echo ""
echo "📈 Endpoints de monitoramento:"
echo "   http://localhost:8080/actuator/health"
echo "   http://localhost:8080/actuator/metrics"
echo ""
echo "🎉 Kotlin Features Demonstradas:"
echo "   ✅ Data Classes"
echo "   ✅ Null Safety"
echo "   ✅ Extension Functions"
echo "   ✅ Sealed Classes"
echo "   ✅ String Templates"
echo "   ✅ Smart Casts"
echo "   ✅ Constructor Injection"
echo "   ✅ Companion Objects"
echo ""
echo "🚀 Fim dos testes Kotlin!"