package me.tomasan7.tictactoe.util

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = Color.Serializer::class)
class Color(
    val red: UByte = 0u,
    val green: UByte = 0u,
    val blue: UByte = 0u,
    val alpha: UByte = 0xFFu
)
{
    constructor(rgbaValue: UInt) :
            this(
                red = ((rgbaValue shr 24) and 0xFFu).toUByte(),
                green = ((rgbaValue shr 16) and 0xFFu).toUByte(),
                blue = ((rgbaValue shr 8) and 0xFFu).toUByte(),
                alpha = (rgbaValue and 0xFFu).toUByte()
            )

    val rgbaValue by lazy { (red.toUInt() shl 24) or (green.toUInt() shl 16) or (blue.toUInt() shl 8) or alpha.toUInt() }

    val rgbaValueHex by lazy { rgbaValue.toString(16).padStart(8, '0')}
    val rgbValueHex by lazy { rgbaValueHex.removeRange(0..1) }

    fun withRed(red: UByte) = Color(red, green, blue, alpha)
    fun withGreen(green: UByte) = Color(red, green, blue, alpha)
    fun withBlue(blue: UByte) = Color(red, green, blue, alpha)
    fun withAlpha(alpha: UByte) = Color(red, green, blue, alpha)

    fun copy(red: UByte = this.red, green: UByte = this.green, blue: UByte = this.blue, alpha: UByte = this.alpha) =
        Color(red, green, blue, alpha)

    override fun hashCode() = rgbaValue.toInt()

    override fun equals(other: Any?): Boolean
    {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        other as Color
        return rgbaValue == other.rgbaValue
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
            encoder.encodeString(value.rgbaValueHex)
        }

        override fun deserialize(decoder: Decoder): Color
        {
            return Color(decoder.decodeString().toUInt(16))
        }
    }
}
