package com.mohalim.edokan.core.model.network

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

class BooleanTypeAdapter : JsonDeserializer<Boolean>, JsonSerializer<Boolean> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Boolean {
        return json.asInt == 1
    }

    override fun serialize(src: Boolean, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return JsonPrimitive(if (src) 1 else 0)
    }
}