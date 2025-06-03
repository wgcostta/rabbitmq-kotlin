package com.exemplo.controller

import com.exemplo.dto.EventoDTO
import com.exemplo.service.EventoProducerService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/eventos")
class EventoController(
    private val eventoProducerService: EventoProducerService
) {

    /**
     * Endpoint para publicar eventos customizados
     */
    @PostMapping("/publicar")
    fun publicarEvento(@RequestBody evento: EventoDTO): ResponseEntity<Map<String, String>> {
        return try {
            eventoProducerService.publicarEvento(evento)

            ResponseEntity.ok(mapOf(
                "status" to "sucesso",
                "mensagem" to "Evento publicado com sucesso",
                "eventoId" to (evento.id ?: "")
            ))

        } catch (e: Exception) {
            ResponseEntity.badRequest().body(mapOf(
                "status" to "erro",
                "mensagem" to "Erro ao publicar evento: ${e.message}"
            ))
        }
    }

    /**
     * Endpoint para publicar notificação de boas-vindas
     */
    @PostMapping("/boas-vindas")
    fun publicarBoasVindas(@RequestParam destinatario: String): ResponseEntity<Map<String, String>> {
        return try {
            eventoProducerService.publicarNotificacao(
                tipo = "BOAS_VINDAS",
                titulo = "Bem-vindo!",
                mensagem = "Obrigado por se cadastrar em nossa plataforma!",
                destinatario = destinatario
            )

            ResponseEntity.ok(mapOf(
                "status" to "sucesso",
                "mensagem" to "Notificação de boas-vindas enviada",
                "destinatario" to destinatario
            ))

        } catch (e: Exception) {
            ResponseEntity.badRequest().body(mapOf(
                "status" to "erro",
                "mensagem" to "Erro ao enviar boas-vindas: ${e.message}"
            ))
        }
    }

    /**
     * Endpoint para publicar notificação de promoção
     */
    @PostMapping("/promocao")
    fun publicarPromocao(
        @RequestParam destinatario: String,
        @RequestParam descricao: String
    ): ResponseEntity<Map<String, String>> {
        return try {
            eventoProducerService.publicarNotificacao(
                tipo = "PROMOCAO",
                titulo = "🎉 Promoção Especial!",
                mensagem = "Não perca esta oportunidade: $descricao",
                destinatario = destinatario
            )

            ResponseEntity.ok(mapOf(
                "status" to "sucesso",
                "mensagem" to "Notificação de promoção enviada",
                "destinatario" to destinatario
            ))

        } catch (e: Exception) {
            ResponseEntity.badRequest().body(mapOf(
                "status" to "erro",
                "mensagem" to "Erro ao enviar promoção: ${e.message}"
            ))
        }
    }

    /**
     * Endpoint para teste rápido
     */
    @GetMapping("/teste")
    fun testeRapido(): ResponseEntity<Map<String, String>> {
        return try {
            eventoProducerService.publicarNotificacao(
                tipo = "TESTE",
                titulo = "Teste do Sistema",
                mensagem = "Esta é uma mensagem de teste do sistema de notificações",
                destinatario = "usuario.teste@exemplo.com"
            )

            ResponseEntity.ok(mapOf(
                "status" to "sucesso",
                "mensagem" to "Evento de teste publicado com sucesso"
            ))

        } catch (e: Exception) {
            ResponseEntity.badRequest().body(mapOf(
                "status" to "erro",
                "mensagem" to "Erro no teste: ${e.message}"
            ))
        }
    }

    /**
     * Endpoint de health check
     */
    @GetMapping("/health")
    fun health(): ResponseEntity<Map<String, String>> {
        return ResponseEntity.ok(mapOf(
            "status" to "UP",
            "servico" to "EventoController",
            "timestamp" to System.currentTimeMillis().toString()
        ))
    }
}