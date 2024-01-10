package me.tomasan7.tictactoe

data class Color(val red: Int, val green: Int, val blue: Int)
{
    init
    {
        require(red in 0..255) { "Red value must be between 0 and 255" }
        require(green in 0..255) { "Green value must be between 0 and 255" }
        require(blue in 0..255) { "Blue value must be between 0 and 255" }
    }

    val value = blue or (green shl 8) or (red shl 16)
    val valueHex = value.toString(16).removePrefix("00")
}
