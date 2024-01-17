package me.tomasan7.tictactoe.server.game.packet

class InvalidPacketIdException(val id: Int) : Exception("Invalid packet id: $id")
