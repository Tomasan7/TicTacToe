package me.tomasan7.tictactoe.protocol.packet

class InvalidPacketFormatException : Exception
{
    constructor(message: String, serializedPacket: String) :
            super("Invalid packet format: $message. Serialized packet: \n$serializedPacket")

    constructor(serializedPacket: String) :
            super("Invalid packet format. Serialized packet: \n$serializedPacket")
}
