package com.msomodi.imageverse.util

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.format.DateTimeFormatter

object LocalDateTimeAsStringSerializer : KSerializer<LocalDateTime> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("date", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): LocalDateTime {
        val dateString = decoder.decodeString()
        return LocalDateTime.parse(dateString.dropLast(1)+"0");
    }

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        encoder.encodeString(value.toString())
    }

}