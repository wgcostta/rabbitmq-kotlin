package com.exemplo.config

import org.springframework.amqp.core.*
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitMQConfig {

    companion object {
        // Nome do Exchange Fanout
        const val FANOUT_EXCHANGE = "eventos.fanout"

        // Nomes das Filas
        const val FILA_EMAIL = "fila.email"
        const val FILA_SMS = "fila.sms"
        const val FILA_PUSH = "fila.push"

        // Routing Keys (não são usadas no fanout, mas vamos definir para exemplo)
        const val ROUTING_KEY_EMAIL = "notificacao.email"
        const val ROUTING_KEY_SMS = "notificacao.sms"
        const val ROUTING_KEY_PUSH = "notificacao.push"
    }

    /**
     * Cria o Exchange do tipo Fanout
     * Fanout: envia mensagens para todas as filas vinculadas, ignorando routing keys
     */
    @Bean
    fun fanoutExchange(): FanoutExchange {
        return ExchangeBuilder
            .fanoutExchange(FANOUT_EXCHANGE)
            .durable(true)
            .build()
    }

    /**
     * Fila para processamento de emails
     */
    @Bean
    fun filaEmail(): Queue {
        return QueueBuilder
            .durable(FILA_EMAIL)
            .build()
    }

    /**
     * Fila para processamento de SMS
     */
    @Bean
    fun filaSms(): Queue {
        return QueueBuilder
            .durable(FILA_SMS)
            .build()
    }

    /**
     * Fila para notificações push
     */
    @Bean
    fun filaPush(): Queue {
        return QueueBuilder
            .durable(FILA_PUSH)
            .build()
    }

    /**
     * Bind da fila de email ao exchange fanout
     */
    @Bean
    fun bindingEmail(): Binding {
        return BindingBuilder
            .bind(filaEmail())
            .to(fanoutExchange())
    }

    /**
     * Bind da fila de SMS ao exchange fanout
     */
    @Bean
    fun bindingSms(): Binding {
        return BindingBuilder
            .bind(filaSms())
            .to(fanoutExchange())
    }

    /**
     * Bind da fila de push ao exchange fanout
     */
    @Bean
    fun bindingPush(): Binding {
        return BindingBuilder
            .bind(filaPush())
            .to(fanoutExchange())
    }

    /**
     * Configuração do RabbitTemplate com conversor JSON
     */
    @Bean
    fun rabbitTemplate(connectionFactory: ConnectionFactory): RabbitTemplate {
        val template = RabbitTemplate(connectionFactory)
        template.messageConverter = Jackson2JsonMessageConverter()
        return template
    }

    /**
     * Conversor de mensagens para JSON
     */
    @Bean
    fun messageConverter(): Jackson2JsonMessageConverter {
        return Jackson2JsonMessageConverter()
    }
}