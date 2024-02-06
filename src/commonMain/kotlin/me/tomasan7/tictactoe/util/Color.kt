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
class Color(val value: UInt)
{
    constructor(red: UByte = 0u, green: UByte = 0u, blue: UByte = 0u, alpha: UByte = 0xFFu) :
            this(
                (alpha.toUInt() and 0xFFu shl 24)
                        or (red.toUInt() and 0xFFu shl 16)
                        or (green.toUInt() and 0xFFu shl 8)
                        or (blue.toUInt() and 0xFFu)
            )

    val valueHex by lazy { value.toString(16).padStart(8, '0') }
    val rgbValueHex by lazy { valueHex.removeRange(0..1) }

    val alpha = ((value shr 24) and 0xFFu).toUByte()
    val red = ((value shr 16) and 0xFFu).toUByte()
    val green = ((value shr 8) and 0xFFu).toUByte()
    val blue = (value and 0xFFu).toUByte()

    fun withRed(red: UByte) = Color(red, green, blue, alpha)
    fun withGreen(green: UByte) = Color(red, green, blue, alpha)
    fun withBlue(blue: UByte) = Color(red, green, blue, alpha)
    fun withAlpha(alpha: UByte) = Color(red, green, blue, alpha)
    fun copy(red: UByte = this.red, green: UByte = this.green, blue: UByte = this.blue, alpha: UByte = this.alpha) =
        Color(red, green, blue, alpha)

    override fun hashCode() = value.toInt()

    override fun equals(other: Any?): Boolean
    {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        other as Color
        return value == other.value
    }

    companion object
    {
        val RED = Color(red = 0xFFu)
        val GREEN = Color(green = 0xFFu)
        val BLUE = Color(blue = 0xFFu)
        val BLACK = Color()
        val WHITE = Color(0xFFFFFFFFu)
        val TRANSPARENT = Color(alpha = 0u)

        fun fromCssString(hex: String): Color
        {
            val hexValue = "ff" + hex.removePrefix("#") // ff for alpha channel
            return Color(hexValue.toUInt(16))
        }
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
