package me.tomasan7.tictactoe.server.game

interface GameCodeGenerator
{
    fun generateCode(): String

    /** Generates a code that is not in the [usedCodes] set. */
    fun generateCode(usedCodes: Set<String>): String
}
