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

### Packets
- Every packet's name is prefixed with either `Client` (ServerBound, client -> server) or `Server` (ClientBound, server -> client
- Every packet has a unique `byte` id.

#### Serialization (MAY CHANGE)
Currently, they are serialized into a string of the following form:
```
{PACKET_ID}\n
{PACKET_DATA_JSON}
```
Or in case of a packet with no data:
```
{PACKET_ID}
```
Example ClientJoinGame:
```
12
{"gameCode":"123456"}
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
The server then responds with a [ServerJoinGame](#server-join-game).

| field    | type   | description              |
|----------|--------|--------------------------|
| gameCode | string | code of the game to join |

##### ClientJoinRandomGame (2)
Joins a random public game.

`NO FIELDS`

##### ServerJoinGame (3)
Tells the client that he has successfully joined a game.
Contains the game configuration, so the client can set up the game properly.

| field      | type | description                                                                  |
|------------|------|------------------------------------------------------------------------------|
| width      | int  | width of the game area                                                       |
| height     | int  | height of the game area                                                      |
| winLength  | int  | amount of symbols in row/column/diagonal needed ti win                       |
| symbolSize | int  | the size of players' symbols (it is a square, so this is its side length)    |
| maxPlayers | int  | max amount of players allowed to join the game                               |
| playerId   | int  | the receiving player's id                                                    |
| ownerId    | int  | id of the player, that is the owner of the game (most often the game create) |

#### In-Game Preparation State

##### ServerAddPlayer (4)
Sent to all players when a new player joins the game.
Should be followed by an initial [ServerSetPlayerData](#serversetplayerdata-7) packet.

| field | type | description         |
|-------|------|---------------------|
| id    | int  | the new player's id |

##### ServerRemovePlayer (5)
Sent to all players when a player leaves the game.

| field | type | description             |
|-------|------|-------------------------|
| id    | int  | the leaving player's id |

##### ClientSetPlayerData (6)
Sent to the server when a player changes his name, color or symbol.

| field  | type    | description                                           |
|--------|---------|-------------------------------------------------------|
| name   | string? | the player's new name. Missing if it hasn't changed   |
| color  | int?    | the player's new color. Missing if it hasn't changed  |
| symbol | symbol? | the player's new symbol. Missing if it hasn't changed |

##### ServerSetPlayerData (7)
Sent to all players when a player changes his name, color or symbol.

| field  | type    | description                                           |
|--------|---------|-------------------------------------------------------|
| id     | int     | the player's id                                       |
| name   | string? | the player's new name. Missing if it hasn't changed   |
| color  | int?    | the player's new color. Missing if it hasn't changed  |
| symbol | symbol? | the player's new symbol. Missing if it hasn't changed |


# WIP

Symbol = string of either 1 or 0 (1 = filled, 0 = not filled)
The length is symbolSize * symbolSize. symbol size is set in the ServerGameConfig packet.
