package me.tomasan7.tictactoe.server.util

/** An RGB color without an alpha channel */
class Color(val value: Int)
{
    constructor(red: UByte, green: UByte, blue: UByte):
            this((red.toInt() and 0xFF shl 16) or (green.toInt() and 0xFF shl 8) or (blue.toInt() and 0xFF))

    val valueHex = value.toString(16).removePrefix("00")
}
