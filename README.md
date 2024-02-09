# TicTacToe

## Structure

### Main page
- Button to create new game
- Field to enter a game code and join an exising game
- Button to join a random public game

### New game
After clicking the create new game button on the main page.
- Settings:
   - Game area size (width, height)
   - Win streak length (how many symbols in a row/column/diagonal are needed to win)
   - Symbol size (the symbol is always a square, so the size is its side length)
   - Max amount of players allowed to join
   - Public/private, whether people without a code can join
- Button to create the game, the user automatically joins the game

### Game

#### Preparation State
- Each player can change their name, color and symbol
- Each player can see all the players (including himself) in a left sidebar
- Each player can toggle their ready state
- Once all players are ready, a countdown starts, when no one unreadyes in its period, the game starts (move to the Game State)

#### Game State
- Every player can see all the players (including himself) in the left sidebar
- After the game starts, the players get shuffled, so the order is not the same in which they joined
- The sidebar shows which player is on turn
- All players take turns placing their symbol on the game area
- If a player leaves, his symbols remain on the game area, but he is not going to be on turn anymore. If only one player remains, he is automatically declared the winner

#### End state
- The game ends when
   - a player wins
      - he normally wins (placing a winStreak length of his symbols in a row/column/diagonal)
      - or all the other players have left
   - the game area is full (a draw)


## Protocol
Implementation independent protocol (web, desktop, mobile...)

* [Packets](#packets)
  * [Serialization (MAY CHANGE)](#serialization-may-change)
  * [Types](#types)
    * [Symbol](#symbol)
* [All packets](#all-packets)
  * [Independent](#independent)
    * [ClientCreateGame (0)](#clientcreategame-0)
    * [ClientJoinGame (1)](#clientjoingame-1)
    * [ClientJoinRandomGame (2)](#clientjoinrandomgame-2)
    * [ServerJoinGame (3)](#serverjoingame-3)
  * [In-Game Preparation State](#in-game-preparation-state)
    * [ServerAddPlayer (4)](#serveraddplayer-4)
    * [ServerRemovePlayer (5)](#serverremoveplayer-5)
    * [ClientSetPlayerData (6)](#clientsetplayerdata-6)
    * [ServerSetPlayerData (7)](#serversetplayerdata-7)
    * [ClientReady (8)](#clientready-8)
    * [ServerClientReadyAck (9)](#serverclientreadyack-9)
    * [ServerPlayerReady (10)](#serverplayerready-10)
  * [In-Game State](#in-game-state)
    * [ServerStartGame (11)](#serverstartgame-11)
    * [ServerPlayerTurn (12)](#serverplayerturn-12)
    * [ClientPlaceSymbol (13)](#clientplacesymbol-13)
    * [ServerPlaceSymbol (14)](#serverplacesymbol-14)
    * [ServerGameEnd (15)](#servergameend-15)
    * [ServerGameClose (16)](#servergameclose-16)

### Packets
- Every packet's name is prefixed with either `Client` (ServerBound, client -> server) or `Server` (ClientBound, server -> client
- Every packet has a unique `byte` id.

#### Serialization (MAY CHANGE)
Currently, they are serialized into a string of the following form:
```
{PACKET_ID}={PACKET_DATA_JSON}
```
Or in case of a packet with no data:
```
{PACKET_ID}
```
Example ClientJoinGame:
```
12={"gameCode":"123456"}
```

### Types

#### Symbol
Is a string of 1s and 0s representing either a colored or not colored pixel.
It should be as long as the game's symbolSize squared.
Example of an `X` symbol in a game with symbolSize 3:
```
101010101
```
Same thing spaced out for visualisation: (not in protocol)
```
1 0 1
0 1 0
1 0 1
```

### All packets

#### Independent
All packets here don't require the client to be in any specific state.
They are sent to the server, without any previous preconditions.

##### ClientCreateGame (0)
Creates a new game with the specified settings.
The server then responds with a [ServerJoinGame](#serverjoingame-3) packet to the client.

| field      | type | description                                                               |
|------------|------|---------------------------------------------------------------------------|
| width      | int  | width of the game area                                                    |
| height     | int  | height of the game area                                                   |
| winLength  | int  | amount of symbols in row/column/diagonal needed ti win                    |
| symbolSize | int  | the size of players' symbols (it is a square, so this is its side length) |
| maxPlayers | int  | max amount of players allowed to join the game                            |
| public     | bool | whether random players can join without a code                            |

##### ClientJoinGame (1)
Joins an existing game with the specified game code.
The server then responds with a [ServerJoinGame](#serverjoingame-3).

| field    | type   | description              |
|----------|--------|--------------------------|
| gameCode | string | code of the game to join |

##### ClientJoinRandomGame (2)
Joins a random public game.

`NO FIELDS`

##### ServerJoinGame (3)
Tells the client that he has successfully joined a game.
Contains the game configuration, so the client can set up the game properly.

| field      | type   | description                                                                  |
|------------|--------|------------------------------------------------------------------------------|
| gameCode   | string | the game's code                                                              |
| width      | int    | width of the game area                                                       |
| height     | int    | height of the game area                                                      |
| winLength  | int    | amount of symbols in row/column/diagonal needed ti win                       |
| symbolSize | int    | the size of players' symbols (it is a square, so this is its side length)    |
| maxPlayers | int    | max amount of players allowed to join the game                               |
| public     | bool   | whether random players can join without a code                               |
| playerId   | int    | the receiving player's new id                                                |
| ownerId    | int    | id of the player, that is the owner of the game (most often the game create) |

#### In-Game Preparation State

##### ServerAddPlayer (4)
Sent to all players when a new player joins the game.

| field    | type | description         |
|----------|------|---------------------|
| playerId | int  | the new player's id |

##### ServerRemovePlayer (5)
Sent to all players when a player leaves the game.

| field    | type | description             |
|----------|------|-------------------------|
| playerId | int  | the leaving player's id |

##### ClientSetPlayerData (6)
Sent to the server when a player changes his name, color or symbol.

| field  | type    | description                                                        |
|--------|---------|--------------------------------------------------------------------|
| name   | string? | the player's new name. Missing if it hasn't changed                |
| color  | int?    | the player's new color as an RGB int. Missing if it hasn't changed |
| symbol | symbol? | the player's new symbol. Missing if it hasn't changed              |

##### ServerSetPlayerData (7)
Sent to all other players when a player changes his name, color or symbol.

| field    | type    | description                                           |
|----------|---------|-------------------------------------------------------|
| playerId | int     | the player's id                                       |
| name     | string? | the player's new name. Missing if it hasn't changed   |
| color    | int?    | the player's new color. Missing if it hasn't changed  |
| symbol   | symbol? | the player's new symbol. Missing if it hasn't changed |

##### ClientReady (8)
Sent when the player is (un)ready. 
When player is ready, [ClientSetPlayerData](#clientsetplayerdata-6) from them are ignored.
Must be confirmed by a [ServerClientReadyAck](#serverclientreadyack-9) packet.

| field | type | description                                                  |
|-------|------|--------------------------------------------------------------|
| value | bool | true, when they are ready, false when they are unready again |

##### ServerClientReadyAck (9)
Sent to a player after receiving their [ClientReady](#clientready-8) packet.
Tells them, whether they are actually ready or not.
If they are not ready, the reason is specified.
That can happen, for example when they have a name, color or symbol same as another ready player.

| field  | type    | description                            |
|--------|---------|----------------------------------------|
| value  | bool    | whether they are actually ready or not |
| reason | string? | only present, when value is false      |

##### ServerPlayerReady (10)
Sent to all players when a player is (un)ready.

| field    | type | description                                                  |
|----------|------|--------------------------------------------------------------|
| playerId | int  | the player's id                                              |
| value    | bool | true, when they are ready, false when they are unready again |

#### In-Game State

##### ServerStartGame (11)
Sent to all players when the game starts.
The player order is shuffled, so the order is not the same in which they joined.
Contains the turn player order. 
After this packet, all packets from the [In-Game Preparation State](#in-game-preparation-state) are ignored.
After this packet, clients wait for the [ServerPlayerTurn](#serverplayerturn-12) packet.

| field       | type  | description              |
|-------------|-------|--------------------------|
| playerOrder | int[] | the order of the players |

##### ServerPlayerTurn (12)
Sent to all players when a player is on turn.

| field    | type | description                     |
|----------|------|---------------------------------|
| playerId | int  | id of the player who is on turn |

##### ClientPlaceSymbol (13)
Sent to the server when a player places their symbol on the game area.
Is confirmed by a [ServerPlaceSymbol](#serverplacesymbol-14) packet

| field | type | description                       |
|-------|------|-----------------------------------|
| x     | int  | x coordinate of the placed symbol |
| y     | int  | y coordinate of the placed symbol |

##### ServerPlaceSymbol (14)
Sent to all clients (including the placer) after receiving a [ClientPlaceSymbol](#clientplacesymbol-13) packet.
Is not sent, when the symbol placement is invalid (e.g. out of bounds, already occupied, not on turn...)
Is also sent to the placer, so they know their placement is valid and acknowledged by the server.

| field    | type | description                           |
|----------|------|---------------------------------------|
| playerId | int  | id of the placer                      |
| x        | int  | the x coordinate of the placed symbol |
| y        | int  | the y coordinate of the placed symbol |

##### ServerGameEnd (15)
Sent to all players when the game ends. Either by a player winning, or by a draw.

| field    | type | description                                                    |
|----------|------|----------------------------------------------------------------|
| winnerId | int? | id of the winning player. Missing when the game ends in a draw |

##### ServerGameClose (16)
Sent to all players when the game has closed, so the clients should transition from the game.

| field  | type    | description             |
|--------|---------|-------------------------|
| reason | string? | why is the game closing |
