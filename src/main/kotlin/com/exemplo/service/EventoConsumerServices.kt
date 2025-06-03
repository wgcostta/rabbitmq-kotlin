package com.exemplo.service

import com.exemplo.config.RabbitMQConfig
import com.exemplo.dto.EventoDTO
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

@Service
class EventoConsumerServices {

    private val logger = LoggerFactory.getLogger(EventoConsumerServices::class.java)

    /**
     * Consumer para processamento de emails
     * Escuta a fila de emails
     */
    @RabbitListener(queues = [RabbitMQConfig.FILA_EMAIL])
    fun processarEmail(evento: EventoDTO) {
        try {
            logger.info("📧 [EMAIL] Processando evento: {}", evento.id)
            logger.info("📧 [EMAIL] Enviando email para: {}", evento.destinatario)
            logger.info("📧 [EMAIL] Título: {}", evento.titulo)
            logger.info("📧 [EMAIL] Mensagem: {}", evento.mensagem)

            // Simula processamento
            Thread.sleep(1000)

            logger.info("📧 [EMAIL] Email enviado com sucesso para: {}", evento.destinatario)

        } catch (e: Exception) {
            logger.error("❌ [EMAIL] Erro ao processar evento: {}", e.message, e)
            // Em um cenário real, você poderia rejeitar a mensagem ou enviá-la para uma DLQ
        }
    }

    /**
     * Consumer para processamento de SMS
     * Escuta a fila de SMS
     */
    @RabbitListener(queues = [RabbitMQConfig.FILA_SMS])
    fun processarSms(evento: EventoDTO) {
        try {
            logger.info("📱 [SMS] Processando evento: {}", evento.id)
            logger.info("📱 [SMS] Enviando SMS para: {}", evento.destinatario)
            logger.info("📱 [SMS] Título: {}", evento.titulo)
            logger.info("📱 [SMS] Mensagem: {}", evento.mensagem)

            // Simula processamento
            Thread.sleep(800)

            logger.info("📱 [SMS] SMS enviado com sucesso para: {}", evento.destinatario)

        } catch (e: Exception) {
            logger.error("❌ [SMS] Erro ao processar evento: {}", e.message, e)
        }
    }

    /**
     * Consumer para processamento de notificações push
     * Escuta a fila de push notifications
     */
    @RabbitListener(queues = [RabbitMQConfig.FILA_PUSH])
    fun processarPush(evento: EventoDTO) {
        try {
            logger.info("🔔 [PUSH] Processando evento: {}", evento.id)
            logger.info("🔔 [PUSH] Enviando push para: {}", evento.destinatario)
            logger.info("🔔 [PUSH] Título: {}", evento.titulo)
            logger.info("🔔 [PUSH] Mensagem: {}", evento.mensagem)

            // Simula processamento
            Thread.sleep(500)

            logger.info("🔔 [PUSH] Push notification enviada com sucesso para: {}", evento.destinatario)

        } catch (e: Exception) {
            logger.error("❌ [PUSH] Erro ao processar evento: {}", e.message, e)
        }
    }
}