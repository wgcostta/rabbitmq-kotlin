package com.exemplo.extensions

import com.exemplo.dto.EventoDTO
import java.time.LocalDateTime
import java.util.*

/**
 * Extension functions para facilitar o uso da aplicação em Kotlin
 */

/**
 * Extension function para criar EventoDTO de forma mais idiomática
 */
fun eventoDTO(block: EventoDTO.() -> Unit): EventoDTO {
    return EventoDTO().apply(block)
}

/**
 * Extension function para validar se um EventoDTO está válido
 */
fun EventoDTO.isValid(): Boolean {
    return !tipo.isNullOrBlank() &&
            !titulo.isNullOrBlank() &&
            !mensagem.isNullOrBlank() &&
            !destinatario.isNullOrBlank()
}

/**
 * Extension function para obter resumo do evento
 */
fun EventoDTO.resumo(): String {
    return "[$tipo] $titulo para $destinatario"
}

/**
 * Extension function para sanitizar dados do evento
 */
fun EventoDTO.sanitize(): EventoDTO {
    return this.copy(
        id = id?.trim() ?: UUID.randomUUID().toString(),
        tipo = tipo?.trim()?.uppercase(),
        titulo = titulo?.trim(),
        mensagem = mensagem?.trim(),
        destinatario = destinatario?.trim()?.lowercase(),
        timestamp = timestamp ?: LocalDateTime.now()
    )
}

/**
 * Extension function para criar eventos pré-definidos
 */
object EventoFactory {

    fun boasVindas(destinatario: String): EventoDTO = eventoDTO {
        id = UUID.randomUUID().toString()
        tipo = "BOAS_VINDAS"
        titulo = "Bem-vindo!"
        mensagem = "Obrigado por se cadastrar em nossa plataforma!"
        this.destinatario = destinatario
    }

    fun promocao(destinatario: String, descricaoPromocao: String): EventoDTO = eventoDTO {
        id = UUID.randomUUID().toString()
        tipo = "PROMOCAO"
        titulo = "🎉 Promoção Especial!"
        mensagem = "Não perca esta oportunidade: $descricaoPromocao"
        this.destinatario = destinatario
    }

    fun manutencao(destinatario: String, dataManutencao: String): EventoDTO = eventoDTO {
        id = UUID.randomUUID().toString()
        tipo = "MANUTENCAO"
        titulo = "⚠️ Manutenção Programada"
        mensagem = "O sistema entrará em manutenção em: $dataManutencao"
        this.destinatario = destinatario
    }

    fun seguranca(destinatario: String, tipoAlerta: String): EventoDTO = eventoDTO {
        id = UUID.randomUUID().toString()
        tipo = "SEGURANCA"
        titulo = "🔒 Alerta de Segurança"
        mensagem = "Detectamos: $tipoAlerta"
        this.destinatario = destinatario
    }
}

/**
 * Sealed class para representar diferentes tipos de eventos de forma type-safe
 */
sealed class TipoEvento(val codigo: String, val descricao: String) {
    object BoasVindas : TipoEvento("BOAS_VINDAS", "Evento de boas-vindas")
    object Promocao : TipoEvento("PROMOCAO", "Evento promocional")
    object Manutencao : TipoEvento("MANUTENCAO", "Evento de manutenção")
    object Seguranca : TipoEvento("SEGURANCA", "Evento de segurança")
    object Sistema : TipoEvento("SISTEMA", "Evento do sistema")
    object Compra : TipoEvento("COMPRA", "Evento de compra")
    data class Personalizado(val codigoPersonalizado: String, val descricaoPersonalizada: String) :
        TipoEvento(codigoPersonalizado, descricaoPersonalizada)
}

/**
 * Extension function para converter TipoEvento para EventoDTO
 */
fun TipoEvento.toEventoDTO(
    titulo: String,
    mensagem: String,
    destinatario: String
): EventoDTO = eventoDTO {
    id = UUID.randomUUID().toString()
    tipo = this@toEventoDTO.codigo
    this.titulo = titulo
    this.mensagem = mensagem
    this.destinatario = destinatario
}

/**
 * Infix function para criar eventos de forma mais natural
 */
infix fun String.notificar(destinatario: String): EventoDTO = eventoDTO {
    id = UUID.randomUUID().toString()
    tipo = "NOTIFICACAO"
    titulo = "Notificação"
    mensagem = this@notificar
    this.destinatario = destinatario
}

/**
 * Extension properties para facilitar validações
 */
val EventoDTO.temDestinatarioEmail: Boolean
    get() = destinatario?.contains("@") == true

val EventoDTO.temDestinatarioTelefone: Boolean
    get() = destinatario?.matches(Regex("^\\+?[1-9]\\d{1,14}$")) == true

val EventoDTO.isUrgente: Boolean
    get() = tipo in listOf("SEGURANCA", "MANUTENCAO", "SISTEMA")

/**
 * Extension function para logging estruturado
 */
fun EventoDTO.toLogString(): String {
    return "Evento[id=$id, tipo=$tipo, destinatario=$destinatario, timestamp=$timestamp]"
}