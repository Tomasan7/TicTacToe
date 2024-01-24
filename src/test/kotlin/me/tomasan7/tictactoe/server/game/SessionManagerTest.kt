package me.tomasan7.tictactoe.server.game

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import me.tomasan7.tictactoe.server.network.packet.client.ClientPacket
import me.tomasan7.tictactoe.server.network.packet.server.ServerPacket
import me.tomasan7.tictactoe.server.network.session.Session
import org.junit.jupiter.api.Test

internal class SessionManagerTest
{
    private val sessionManager = SessionManager(GameManager())

    @Test
    fun testSessionChecker() = runBlocking {
        // TODO: This test slows down tests. Make a use of coroutines-test module
        /* launchSessionChecker is called automatically after SessionManager object creation */
        val coroutineScope = CoroutineScope(Dispatchers.Default)
        val session = MockSession()
        val clientSession = sessionManager.addSession(session)
        coroutineScope.launch {
            delay(1000)
            session.close()
        }
        delay(2000)
        assert(!sessionManager.sessions.contains(clientSession))
    }

    private class MockSession : Session
    {
        override var isActive = true

        override val incomingPacketsChannel get() = Channel<ClientPacket>()
        override suspend fun sendPacket(packet: ServerPacket) = throw NotImplementedError()

        override suspend fun close(message: String)
        {
            isActive = false
        }
    }
}
