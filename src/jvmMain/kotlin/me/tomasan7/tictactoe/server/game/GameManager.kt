package me.tomasan7.tictactoe.server.game

class GameManager
{
    private val games = mutableMapOf<String, Game>()
    private val sessionGames = mutableMapOf<ClientSession, Game>()
    private val gameCodeGenerator: GameCodeGenerator = RandomCharsGameCodeGenerator(4)

    fun createGame(options: GameOptions): Game
    {
        val code = gameCodeGenerator.generateCode(games.keys)
        val game = Game(code, options)
        games[game.code] = game
        return game
    }

    fun joinToGame(session: ClientSession, code: String): Boolean
    {
        val game = games[code] ?: return false
        return joinToGame(session, game)
    }

    fun joinToGame(session: ClientSession, game: Game): Boolean
    {
        /* Player already in-game */
        if (sessionGames.containsKey(session))
            return false

        val inGame = game.addNewPlayer(session)
        if (inGame)
            sessionGames[session] = game
        return inGame
    }

    fun joinToRandomPublicGame(session: ClientSession): Boolean
    {
        val gameCode = games.entries.firstOrNull { it.value.options.public }?.key

        if (gameCode == null)
            return false

        return joinToGame(session, gameCode)
    }
}
