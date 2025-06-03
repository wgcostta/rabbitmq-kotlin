package com.exemplo.service

import com.exemplo.config.RabbitMQConfig
import com.exemplo.dto.EventoDTO
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service
import java.util.*

@Service
class EventoProducerService(
    private val rabbitTemplate: RabbitTemplate
) {

    private val logger = LoggerFactory.getLogger(EventoProducerService::class.java)

    /**
     * Publica um evento no exchange fanout
     * O evento será enviado para todas as filas vinculadas ao exchange
     */
    fun publicarEvento(evento: EventoDTO) {
        try {
            // Gera ID único se não existir
            if (evento.id.isNullOrEmpty()) {
                evento.id = UUID.randomUUID().toString()
            }

            logger.info("Publicando evento: {}", evento)

            // Envia para o exchange fanout
            // No fanout, a routing key é ignorada, mas vamos passar uma mesmo assim
            rabbitTemplate.convertAndSend(
                RabbitMQConfig.FANOUT_EXCHANGE,
                "", // routing key vazia para fanout
                evento
            )

            logger.info("Evento publicado com sucesso. ID: {}", evento.id)

        } catch (e: Exception) {
            logger.error("Erro ao publicar evento: {}", e.message, e)
            throw RuntimeException("Falha ao publicar evento", e)
        }
    }

    /**
     * Método helper para criar e publicar eventos rapidamente
     */
    fun publicarNotificacao(tipo: String, titulo: String, mensagem: String, destinatario: String) {
        val evento = EventoDTO(
            id = UUID.randomUUID().toString(),
            tipo = tipo,
            titulo = titulo,
            mensagem = mensagem,
            destinatario = destinatario
        )

        publicarEvento(evento)
    }
}