package com.example.service

import com.exemplo.config.RabbitMQConfig
import com.exemplo.dto.EventoDTO
import com.exemplo.extensions.EventoFactory
import com.exemplo.extensions.isValid
import com.exemplo.extensions.notificar
import com.exemplo.extensions.TipoEvento
import com.exemplo.extensions.toEventoDTO
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.mockito.kotlin.*
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource

@SpringBootTest
@TestPropertySource(properties = [
    "spring.rabbitmq.host=localhost",
    "spring.rabbitmq.port=5672",
    "spring.rabbitmq.username=guest",
    "spring.rabbitmq.password=guest"
])
class EventoServiceTest {

    private lateinit var rabbitTemplate: RabbitTemplate
    private lateinit var eventoProducerService: EventoProducerService

    @BeforeEach
    fun setup() {
        rabbitTemplate = mock()
        eventoProducerService = EventoProducerService(rabbitTemplate)
    }

    @Test
    fun `deve publicar evento com sucesso`() {
        // Given
        val evento = EventoDTO(
            tipo = "TESTE",
            titulo = "Evento de Teste",
            mensagem = "Esta é uma mensagem de teste",
            destinatario = "teste@exemplo.com"
        )

        // When
        eventoProducerService.publicarEvento(evento)

        // Then
        verify(rabbitTemplate).convertAndSend(
            eq(RabbitMQConfig.FANOUT_EXCHANGE),
            eq(""),
            eq(evento)
        )
        assertNotNull(evento.id)
    }

    @Test
    fun `deve gerar ID automaticamente quando nao fornecido`() {
        // Given
        val evento = EventoDTO(
            tipo = "TESTE",
            titulo = "Evento sem ID",
            mensagem = "Teste de geração automática de ID",
            destinatario = "teste@exemplo.com"
        )

        // When
        eventoProducerService.publicarEvento(evento)

        // Then
        assertNotNull(evento.id)
        assertTrue(evento.id!!.isNotEmpty())
    }

    @Test
    fun `deve publicar notificacao usando helper method`() {
        // When
        eventoProducerService.publicarNotificacao(
            tipo = "PROMOCAO",
            titulo = "Oferta Especial",
            mensagem = "50% de desconto",
            destinatario = "cliente@exemplo.com"
        )

        // Then
        verify(rabbitTemplate).convertAndSend(
            eq(RabbitMQConfig.FANOUT_EXCHANGE),
            eq(""),
            any<EventoDTO>()
        )
    }

    @Test
    fun `deve validar evento usando extension function`() {
        // Given
        val eventoValido = EventoDTO(
            tipo = "TESTE",
            titulo = "Título válido",
            mensagem = "Mensagem válida",
            destinatario = "destino@exemplo.com"
        )

        val eventoInvalido = EventoDTO()

        // Then
        assertTrue(eventoValido.isValid())
        assertFalse(eventoInvalido.isValid())
    }

    @Test
    fun `deve criar evento usando factory`() {
        // When
        val eventoBoasVindas = EventoFactory.boasVindas("novo@usuario.com")
        val eventoPromocao = EventoFactory.promocao("cliente@exemplo.com", "Black Friday 70% OFF")

        // Then
        assertEquals("BOAS_VINDAS", eventoBoasVindas.tipo)
        assertEquals("novo@usuario.com", eventoBoasVindas.destinatario)
        assertEquals("PROMOCAO", eventoPromocao.tipo)
        assertTrue(eventoPromocao.mensagem!!.contains("Black Friday"))
    }

    @Test
    fun `deve criar evento usando sealed class`() {
        // When
        val eventoSeguranca = TipoEvento.Seguranca.toEventoDTO(
            titulo = "Login Suspeito",
            mensagem = "Tentativa de acesso não autorizada",
            destinatario = "admin@exemplo.com"
        )

        // Then
        assertEquals("SEGURANCA", eventoSeguranca.tipo)
        assertEquals("Login Suspeito", eventoSeguranca.titulo)
        assertEquals("admin@exemplo.com", eventoSeguranca.destinatario)
    }

    @Test
    fun `deve criar evento usando infix function`() {
        // When
        val evento = "Sua compra foi aprovada!" notificar "comprador@exemplo.com"

        // Then
        assertEquals("NOTIFICACAO", evento.tipo)
        assertEquals("Sua compra foi aprovada!", evento.mensagem)
        assertEquals("comprador@exemplo.com", evento.destinatario)
    }

    @Test
    fun `deve lancar excecao quando rabbitmq falhar`() {
        // Given
        val evento = EventoDTO(
            tipo = "TESTE",
            titulo = "Teste de erro",
            mensagem = "Mensagem de teste",
            destinatario = "teste@exemplo.com"
        )

        doThrow(RuntimeException("Conexão falhou"))
            .whenever(rabbitTemplate).convertAndSend(any<String>(), any<String>(), any<EventoDTO>())

        // When & Then
        assertThrows(RuntimeException::class.java) {
            eventoProducerService.publicarEvento(evento)
        }
    }

    @Test
    fun `deve criar multiplos eventos usando diferentes abordagens`() {
        // Different ways to create events in Kotlin
        val eventos = listOf(
            // Traditional constructor
            EventoDTO(
                id = "1",
                tipo = "TESTE1",
                titulo = "Teste 1",
                mensagem = "Mensagem 1",
                destinatario = "user1@test.com"
            ),

            // Factory method
            EventoFactory.boasVindas("user2@test.com"),

            // Sealed class
            TipoEvento.Promocao.toEventoDTO(
                titulo = "Promoção Relâmpago",
                mensagem = "Apenas hoje!",
                destinatario = "user3@test.com"
            ),

            // Infix function
            "Mensagem importante!" notificar "user4@test.com"
        )

        // Verify all events are valid
        eventos.forEach { evento ->
            assertTrue(evento.isValid(), "Evento deve ser válido: $evento")
            assertNotNull(evento.id, "ID não deve ser nulo")
        }

        assertEquals(4, eventos.size)
    }
}