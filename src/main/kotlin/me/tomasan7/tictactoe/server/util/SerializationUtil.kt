package me.tomasan7.tictactoe.server.util

import kotlinx.serialization.KSerializer
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.functions

/** Returns a [KSerializer] of a [@Serializable] annotated class, or `null` when the serializer is not found. */
fun <T : Any> getSerializableClassSerializer(clazz: KClass<out T>): KSerializer<T>?
{
    return clazz.companionObject?.functions?.find { it.name == "serializer" }?.call(clazz.companionObjectInstance) as? KSerializer<T>
}
