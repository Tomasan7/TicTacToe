package me.tomasan7.tictactoe.util

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * @constructor The value is in the format 0xAARRGGBB
 */
@Serializable(with = Color.Serializer::class)
class Color(value: UInt)
{
    constructor(red: UByte = 0u, green: UByte = 0u, blue: UByte = 0u, alpha: UByte = 0xFFu) :
            this(
                (alpha.toUInt() and 0xFFu shl 24)
                        or (red.toUInt() and 0xFFu shl 16)
                        or (green.toUInt() and 0xFFu shl 8)
                        or (blue.toUInt() and 0xFFu)
            )

    val valueHex = value.toString(16).padStart(8, '0')
    val rgbValueHex = value.toString(16).removeRange(0..1).padStart(6, '0')

    val alpha = ((value shr 24) and 0xFFu).toUByte()
    val red = ((value shr 16) and 0xFFu).toUByte()
    val green = ((value shr 8) and 0xFFu).toUByte()
    val blue = (value and 0xFFu).toUByte()

    companion object
    {
        val RED = Color(red = 0xFFu)
        val GREEN = Color(green = 0xFFu)
        val BLUE = Color(blue = 0xFFu)
        val BLACK = Color()
        val WHITE = Color(0xFFFFFFFFu)
    }

    class Serializer : KSerializer<Color>
    {
        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Color", PrimitiveKind.STRING)

        override fun serialize(encoder: Encoder, value: Color)
        {
            encoder.encodeString(value.valueHex)
        }

        override fun deserialize(decoder: Decoder): Color
        {
            return Color(decoder.decodeString().toUInt(16))
        }
    }
}
