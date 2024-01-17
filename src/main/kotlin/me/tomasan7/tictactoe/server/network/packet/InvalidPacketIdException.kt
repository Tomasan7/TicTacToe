package me.tomasan7.tictactoe.server.network.packet

class InvalidPacketIdException(val id: Int) : Exception("Invalid packet id: $id")
