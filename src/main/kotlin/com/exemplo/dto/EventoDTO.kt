package com.exemplo.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class EventoDTO(
    var id: String? = null,
    var tipo: String? = null,
    var titulo: String? = null,
    var mensagem: String? = null,
    var destinatario: String? = null,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    var timestamp: LocalDateTime = LocalDateTime.now()
) {

    // Construtor com parâmetros para facilitar a criação
    constructor(
        id: String,
        tipo: String,
        titulo: String,
        mensagem: String,
        destinatario: String
    ) : this(id, tipo, titulo, mensagem, destinatario, LocalDateTime.now())

    override fun toString(): String {
        return "EventoDTO(id='$id', tipo='$tipo', titulo='$titulo', " +
                "mensagem='$mensagem', destinatario='$destinatario', timestamp=$timestamp)"
    }
}