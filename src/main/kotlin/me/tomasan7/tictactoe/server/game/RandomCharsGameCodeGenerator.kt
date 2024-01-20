package me.tomasan7.tictactoe.server.game

import kotlin.math.pow

class RandomCharsGameCodeGenerator(private val length: Int) : GameCodeGenerator
{
    override fun generateCode() = buildString {
        for (i in 0 until this@RandomCharsGameCodeGenerator.length)
            append(CHARS.random())
    }

    override fun generateCode(usedCodes: Set<String>): String
    {
        if (usedCodes.size >= CHARS.length.toDouble().pow(length))
            throw IllegalStateException("All possible codes have been used.")

        var result = generateCode()

        while (usedCodes.contains(result))
            result = generateCode()

        return result
    }

    companion object
    {
        const val CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
    }
}
