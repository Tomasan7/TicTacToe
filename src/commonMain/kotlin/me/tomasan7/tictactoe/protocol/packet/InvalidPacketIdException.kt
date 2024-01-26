package me.tomasan7.tictactoe.protocol.packet

class InvalidPacketIdException(val id: Int) : Exception("Invalid packet id: $id")
