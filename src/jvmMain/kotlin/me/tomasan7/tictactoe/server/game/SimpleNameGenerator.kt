package me.tomasan7.tictactoe.server.game

import kotlin.random.Random

class SimpleNameGenerator(
    private val minLength: Int,
    private val maxLength: Int,
) : NameGenerator
{
    private val vowels = listOf("a", "e", "i", "o", "u")
    private val consonants = ('a'..'z') - vowels

    override fun generateName(): String
    {
        val length = Random.nextInt(minLength, maxLength + 1)
        val sb = StringBuilder()

        for (i in 0 until length)
            sb.append(if (i % 2 == 0) consonants.random() else vowels.random())

        return sb.toString().replaceFirstChar { it.uppercaseChar() }
    }
}
