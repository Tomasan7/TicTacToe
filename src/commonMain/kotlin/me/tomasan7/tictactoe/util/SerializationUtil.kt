package me.tomasan7.tictactoe.util

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializerOrNull
import kotlin.reflect.KClass

/** Returns a [KSerializer] of a [@Serializable] annotated class, or `null` when the serializer is not found. */
@OptIn(InternalSerializationApi::class)
fun <T : Any> getSerializableClassSerializer(clazz: KClass<T> ): KSerializer<T>?
{
    return clazz.serializerOrNull()
}
