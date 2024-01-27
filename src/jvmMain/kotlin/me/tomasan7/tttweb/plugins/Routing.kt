package me.tomasan7.tttweb.plugins

import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import me.tomasan7.tictactoe.protocol.packet.client.ClientPacket
import org.reflections.Reflections
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.declaredMemberProperties

fun Application.configureRouting()
{
    routing {
        staticResources("/static/", "/pages/")
        staticResources("/frontend/", "/frontend/")

        protocolDebugger()

        get("/") {
            call.respondTemplate("index.ftlh")
        }
    }
}

private fun Route.protocolDebugger()
{
    val clientPacketsInPackage = getClientPacketsInPackage("me.tomasan7.tictactoe.protocol.packet.client.packet")
    val packetViews = composePackets(clientPacketsInPackage)

    get("/protocol-debugger") {
        call.respondTemplate("protocol-debugger.ftlh", mapOf("packets" to packetViews))
    }
}

data class PacketView(val name: String, val id: Int, val fields: List<FieldView>)
{
    data class FieldView(val name: String, val type: String)
}

private fun composePackets(clientPackets: Iterable<KClass<out ClientPacket>>): List<PacketView>
{
    return clientPackets
        .filter { it.simpleName != "ClientJoinRandomGamePacket" }
        .map { composePacket(it) }
}

private fun composePacket(clientPacketClass: KClass<out ClientPacket>): PacketView
{
    val packetId = getClientPacketId(clientPacketClass)!!
    val packetFieldNames = getClientPacketFields(clientPacketClass).filter { it.first != "id" }

    return PacketView(
        clientPacketClass.simpleName!!,
        packetId,
        packetFieldNames.map { PacketView.FieldView(it.first, it.second.toString()) }
    )
}

private fun getClientPacketFields(clientPacketClass: KClass<out ClientPacket>): Iterable<Pair<String, KType>>
{
    return clientPacketClass.declaredMemberProperties.map { it.name to it.returnType }
}

private fun getClientPacketId(clientPacketClass: KClass<out ClientPacket>): Int?
{
    return clientPacketClass.companionObject?.declaredMemberProperties?.find { it.name == "PACKET_ID" }?.call(clientPacketClass.companionObjectInstance) as? Int
}

private fun getClientPacketsInPackage(packageName: String): Iterable<KClass<out ClientPacket>>
{
    val reflections = Reflections(packageName)
    return reflections.getSubTypesOf(ClientPacket::class.java).map { it.kotlin }
}
