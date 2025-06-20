package com.exemplo.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import java.time.LocalDateTime

data class EventoDTO(
    @JsonProperty("id")
    var id: String? = null,

    @JsonProperty("tipo")
    var tipo: String? = null,

    @JsonProperty("titulo")
    var titulo: String? = null,

    @JsonProperty("mensagem")
    var mensagem: String? = null,

    @JsonProperty("destinatario")
    var destinatario: String? = null,

    @JsonProperty("timestamp")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer::class)
    @JsonDeserialize(using = LocalDateTimeDeserializer::class)
    var timestamp: LocalDateTime = LocalDateTime.now()
) {
    // Construtor secundário para facilitar a criação
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