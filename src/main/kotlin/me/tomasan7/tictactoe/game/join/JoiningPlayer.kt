package me.tomasan7.tictactoe.game.join

import me.tomasan7.tictactoe.Color
import me.tomasan7.tictactoe.game.session.Session
import kotlin.properties.Delegates

class JoiningPlayer(private val session: Session, private val id: Int)
{
    private var joinState = JoinState.SelectName

    private lateinit var name: String
    private var symbol by Delegates.notNull<Char>()
    private lateinit var color: Color
}
