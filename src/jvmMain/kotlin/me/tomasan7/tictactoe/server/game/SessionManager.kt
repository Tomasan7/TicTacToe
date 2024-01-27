package me.tomasan7.tictactoe.server.game

import io.ktor.util.collections.*
import kotlinx.coroutines.*
import me.tomasan7.tictactoe.server.network.session.Session
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class SessionManager(gameManager: GameManager)
{
    private val _sessions = ConcurrentSet<ClientSession>()
    val sessions: Set<ClientSession>
        get() = _sessions.toSet()
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val independentPacketHandler = IndependentPacketHandler(gameManager)
    private val sessionPacketHandlerJobs = ConcurrentMap<ClientSession, Job>()

    init
    {
        launchSessionChecker(500.milliseconds)
    }

    /** Periodically checks if sessions are still active and removes inactive ones. */
    fun launchSessionChecker(interval: Duration)
    {
        coroutineScope.launch {
            while (true)
            {
                val inactiveSessions = _sessions.filter { !it.isActive }
                _sessions.removeAll(inactiveSessions.toSet())
                delay(interval)
            }
        }
    }

    fun addSession(session: Session): ClientSession
    {
        val clientSession = ClientSession(session)
        _sessions.add(clientSession)
        sessionPacketHandlerJobs[clientSession] = coroutineScope.launch {
            clientSession.incomingPackets.collect { packet ->
                independentPacketHandler.handlePacket(packet, clientSession)
            }
        }
        return clientSession
    }

    fun removeSession(clientSession: ClientSession)
    {
        _sessions.remove(clientSession)
    }
}
